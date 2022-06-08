package goa.systems.empman;

import java.io.File;

public class SysProps {

	private static SysProps mthis = null;

	private File datadir;
	private File appdatadir;
	private File formsdir;

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

	public File getFormsdir() {
		return formsdir;
	}

	public void setFormsdir(File formsdir) {
		this.formsdir = formsdir;
	}
}
