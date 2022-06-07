package goa.systems.empman;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import goa.systems.commons.io.InputOutput;

@Component
class MyCommandLineRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(MyCommandLineRunner.class);

	@Override
	public void run(String... args) {

		SysProps sp = SysProps.getInstance();

		// Check data directory
		sp.setDatadir(new File(System.getProperty("user.home"), ".goasys"));
		sp.setAppdatadir(new File(sp.getDatadir(), "empman"));
		sp.setOdtdir(new File(sp.getAppdatadir(), "odt"));
		sp.setPdfdir(new File(sp.getAppdatadir(), "pdf"));

		createifnotexists(sp.getDatadir());
		createifnotexists(sp.getAppdatadir());
		if (createifnotexists(sp.getOdtdir())) {
			copy("/odt/registration.odt", new File(sp.getOdtdir(), "registration.odt"));
		}
		if (createifnotexists(sp.getPdfdir())) {
			copy("/pdf/registration.pdf", new File(sp.getPdfdir(), "registration.pdf"));
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
