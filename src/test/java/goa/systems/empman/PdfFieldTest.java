package goa.systems.empman;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.junit.jupiter.api.Test;

class PdfFieldTest {

	@Test
	void testFields() {

		assertDoesNotThrow(() -> {
			PDDocument pddoc = PDDocument.load(PdfFieldTest.class.getResourceAsStream("/filled_registration.pdf"));
			PDDocumentCatalog docat = pddoc.getDocumentCatalog();
			PDAcroForm aform = docat.getAcroForm();
			assertEquals("Andreas", aform.getField("prename_field").getValueAsString());
			assertNotEquals("NotAndreas", aform.getField("prename_field").getValueAsString());
		});
	}

}
