package blacksmith.sullivanway.model;

public class Transfermap {
	private long id;
	private int image;
	private String startnext;
	private String endnext;
	private int floor;
	private int time;
	private String startline;
	private String endline;
	private String name;

    public Transfermap() {
    }

    public Transfermap(long id, int image, String startnext, String endnext, int floor, int time, String startline, String endline, String name) {
		this.id = id;
		this.image = image;
		this.startnext = startnext;
		this.endnext = endnext;
		this.floor = floor;
		this.time = time;
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

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getStartnext() {
		return startnext;
	}

	public void setStartnext(String startnext) {
		this.startnext = startnext;
	}

	public String getEndnext() {
		return endnext;
	}

	public void setEndnext(String endnext) {
		this.endnext = endnext;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
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
