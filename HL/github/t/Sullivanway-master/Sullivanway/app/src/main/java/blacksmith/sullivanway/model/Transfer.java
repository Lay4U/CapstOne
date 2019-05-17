package blacksmith.sullivanway.model;

public class Transfer {
	private long id;
	private String startline;
	private String endline;
	private String name;

	public Transfer() {
	}

	public Transfer(long id, String startline, String endline, String name) {
		this.id = id;
		this.startline = startline;
		this.endline = endline;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStartline() {
		return startline;
	}

	public void setStartline(String startline) {
		this.startline = startline;
	}

	public String getEndline() {
		return endline;
	}

	public void setEndline(String endline) {
		this.endline = endline;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
