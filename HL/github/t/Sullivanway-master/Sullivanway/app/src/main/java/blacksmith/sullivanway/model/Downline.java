package blacksmith.sullivanway.model;

public class Downline {
	private long id;
	private String line;
	private String startname;
	private String endname;

	public Downline() {
	}

	public Downline(long id, String line, String startname, String endname) {
		this.id = id;
		this.line = line;
		this.startname = startname;
		this.endname = endname;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getStartname() {
		return startname;
	}

	public void setStartname(String startname) {
		this.startname = startname;
	}

	public String getEndname() {
		return endname;
	}

	public void setEndname(String endname) {
		this.endname = endname;
	}
}
