package goa.systems.empman;

import java.io.File;

public class SysProps {

	private static SysProps mthis = null;

	private File datadir;
	private File appdatadir;
	private File odtdir;
	private File pdfdir;

	private SysProps() {

	}

	public static SysProps getInstance() {
		if (mthis == null) {
			mthis = new SysProps();
		}
		return mthis;
	}

	public File getDatadir() {
		return datadir;
	}

	public void setDatadir(File datadir) {
		this.datadir = datadir;
	}

	public File getAppdatadir() {
		return appdatadir;
	}

	public void setAppdatadir(File appdatadir) {
		this.appdatadir = appdatadir;
	}

	public File getOdtdir() {
		return odtdir;
	}

	public void setOdtdir(File odtdir) {
		this.odtdir = odtdir;
	}

	public File getPdfdir() {
		return pdfdir;
	}

	public void setPdfdir(File pdfdir) {
		this.pdfdir = pdfdir;
	}

}
