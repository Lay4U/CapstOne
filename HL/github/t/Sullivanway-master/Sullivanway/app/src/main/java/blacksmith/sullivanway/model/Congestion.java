package blacksmith.sullivanway.model;

public class Congestion {
	private long id;
	private String date;
	private String line;
	private String name;
	private int time;
	private int popularity;

	public Congestion() {
	}

	public Congestion(long id, String date, String line, String name, int time, int popularity) {
		this.id = id;
		this.date = date;
		this.line = line;
		this.name = name;
		this.time = time;
		this.popularity = popularity;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}
}
