package goa.systems.empman;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import goa.systems.commons.io.InputOutput;
import goa.systems.commons.xml.XmlFramework;
import goa.systems.empman.model.Form;
import goa.systems.empman.model.Metadata;

@RestController
public class MainEndpointController {

	private static final Logger logger = LoggerFactory.getLogger(MainEndpointController.class);

	@PostMapping(value = "/upload", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		try {
			PDDocument pddoc = PDDocument.load(file.getInputStream());
			PDDocumentCatalog docat = pddoc.getDocumentCatalog();
			PDAcroForm aform = docat.getAcroForm();

			StringBuilder page = new StringBuilder();
			StringBuilder fields = new StringBuilder();
			page.append(
					"<html><title>Welcome</title><body><p>Is this information correct?</p><form method=\"POST\" action=\"/confirmed\">");

			for (PDField field : aform.getFields()) {
				page.append(field.getPartialName());
				page.append(String.format(": <input name=\"%s\" value=\"%s\"><br>", field.getPartialName(),
						field.getValueAsString()));
				fields.append(field.getPartialName());
				fields.append(",");
			}
			page.append(String.format("<input name=\"fieldlist\" type=\"hidden\" value=\"%s\">",
					fields.toString().replaceAll(".$", "")));
			page.append("<input type=\"submit\" value=\"Yes\">");
			page.append("</form></body></html>");

			pddoc.close();

			return ResponseEntity.ok().body(page.toString());

		} catch (IOException e) {
			logger.error("Error uploading and parsing file.", e);
			return ResponseEntity.badRequest().body("Error uploading file");
		}
	}

	@PostMapping(value = "/confirmed", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> confirmed(HttpServletRequest request) {

		String template = InputOutput
				.readString(MainEndpointController.class.getResourceAsStream("/templates/confirmed.html"));

		String[] fields = request.getParameter("fieldlist").split(",");
		Map<String, String> content = new HashMap<>();

		for (String field : fields) {
			content.put(field.substring("field_".length()), request.getParameter(field));
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String c = String.format(template, gson.toJson(content), HtmlUtils.htmlEscape(toXml(content)), toSql(content));
		return ResponseEntity.ok().body(c);
	}

	@GetMapping(value = "/form/{uuid}/{format}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileSystemResource getFile(@PathVariable String uuid, @PathVariable String format,
			HttpServletResponse response) {
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s.%s", uuid, format));
		File directory = new File(SysProps.getInstance().getFormsdir(), uuid);
		return new FileSystemResource(new File(directory, "." + format));
	}

	/**
	 * 
	 * @return JSON object with the available forms.
	 */
	@GetMapping(value = "/forms", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getForms() {
		List<Form> names = new ArrayList<>();
		for (File f : SysProps.getInstance().getFormsdir().listFiles()) {

			Form form = new Form();
			form.setUuid(f.getName());
			form.setPdf(new File(f, ".pdf").exists());
			form.setOdt(new File(f, ".odt").exists());

			try (FileReader fr = new FileReader(new File(f, "metadata.json"))) {
				form.setMetadata(new Gson().fromJson(new JsonReader(fr), Metadata.class));
			} catch (JsonIOException | JsonSyntaxException | IOException e) {
				logger.error("Error reading metadata.", e);
			}

			names.add(form);
		}
		return ResponseEntity.ok().body(new Gson().toJson(names));
	}

	private String toXml(Map<String, String> data) {

		StringWriter sw = new StringWriter();

		try {
			Document d = XmlFramework.getDocumentBuilder().newDocument();
			Element elem = d.createElement("data");

			for (Entry<String, String> entry : data.entrySet()) {
				Element e = d.createElement(entry.getKey());
				e.setTextContent(entry.getValue());
				elem.appendChild(e);
			}
			d.appendChild(elem);
			Transformer t = XmlFramework.getTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			t.transform(new DOMSource(d), new StreamResult(sw));
		} catch (ParserConfigurationException | TransformerException e) {
			logger.error("Error", e);
		}
		return sw.toString();
	}

	private String toSql(Map<String, String> data) {

		StringBuilder c = new StringBuilder();
		StringBuilder d = new StringBuilder();

		for (Entry<String, String> entry : data.entrySet()) {
			c.append(entry.getKey());
			c.append(",");
			d.append("'");
			d.append(entry.getValue());
			d.append("',");
		}
		return String.format("INSERT INTO USERS (%s) VALUES (%s);", c.toString().replaceAll(".$", ""),
				d.toString().replaceAll(".$", ""));
	}
}