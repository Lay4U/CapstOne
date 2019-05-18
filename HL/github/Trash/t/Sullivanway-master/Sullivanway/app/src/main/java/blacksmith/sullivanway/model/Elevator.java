package blacksmith.sullivanway.model;

public class Elevator {
	private long id;
	private String line;
	private String name;
	private int in_gate;
	private String floor;
	private String location;

	public Elevator() {
	}

	public Elevator(long id, String line, String name, int in_gate, String floor, String location) {
		this.id = id;
		this.line = line;
		this.name = name;
		this.in_gate = in_gate;
		this.floor = floor;
		this.location = location;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIn_gate() {
		return in_gate;
	}

	public void setIn_gate(int in_gate) {
		this.in_gate = in_gate;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
