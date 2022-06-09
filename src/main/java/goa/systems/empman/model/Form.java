package goa.systems.empman.model;

public class Form {

	private String uuid;
	private String name;
	private String description;
	private boolean pdf;
	private boolean odt;

	private Metadata metadata;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

}
