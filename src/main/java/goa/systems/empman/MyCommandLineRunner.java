package goa.systems.empman;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import goa.systems.commons.io.InputOutput;
import goa.systems.empman.model.Metadata;

@Component
class MyCommandLineRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(MyCommandLineRunner.class);

	@Override
	public void run(String... args) {

		SysProps sp = SysProps.getInstance();

		// Check data directory
		sp.setDatadir(new File(System.getProperty("user.home"), ".goasys"));
		sp.setAppdatadir(new File(sp.getDatadir(), "empman"));
		sp.setFormsdir(new File(sp.getAppdatadir(), "forms"));

		createifnotexists(sp.getDatadir());
		createifnotexists(sp.getAppdatadir());
		if (createifnotexists(sp.getFormsdir())) {
			File registrationdir = new File(sp.getFormsdir(), UUID.randomUUID().toString());
			createifnotexists(registrationdir);
			copy("/forms/registration.odt", new File(registrationdir, ".odt"));
			copy("/forms/registration.pdf", new File(registrationdir, ".pdf"));
			Metadata md = new Metadata();
			md.setName("registration");
			md.setDescription("A employee registration form");
			InputOutput.writeString(new Gson().toJson(md), new File(registrationdir, "metadata.json"));
		}

	}

	private boolean createifnotexists(File dir) {
		if (dir.exists()) {
			logger.info("Directory {} exists.", dir);
			return false;
		} else {
			logger.info("Directory {} does not exist. Creating it.", dir);
			dir.mkdirs();
			return true;
		}
	}

	private void copy(String classPath, File destination) {
		try (FileOutputStream fos = new FileOutputStream(destination)) {
			InputOutput.copy(MyCommandLineRunner.class.getResourceAsStream(classPath), fos);
		} catch (IOException e) {
			logger.error("Error copying odt file.");
		}
	}
}
