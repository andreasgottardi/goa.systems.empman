package goa.systems.empman.model;

public class Form {

	private String name;
	private boolean pdf;
	private boolean odt;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPdf() {
		return pdf;
	}

	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}

	public boolean isOdt() {
		return odt;
	}

	public void setOdt(boolean odt) {
		this.odt = odt;
	}

}
