package blacksmith.sullivanway.model;

public class Station {
    private long id;
	private String line;
	private int serial;
	private String name;
	private double km;
	private double wgsx;
	private double wgsy;
	private int door;
	private String contact;
	private int toilet;
	private int elevator;
	private int escalator;
	private int wheellift;
	private int pointx;
	private int pointy;

	public Station() {
	}

    public Station(long id, String line, int serial, String name, double km, double wgsx, double wgsy, int door, String contact, int toilet, int elevator, int escalator, int wheellift, int pointx, int pointy) {
        this.id = id;
        this.line = line;
        this.serial = serial;
        this.name = name;
        this.km = km;
        this.wgsx = wgsx;
        this.wgsy = wgsy;
        this.door = door;
        this.contact = contact;
        this.toilet = toilet;
        this.elevator = elevator;
        this.escalator = escalator;
        this.wheellift = wheellift;
        this.pointx = pointx;
        this.pointy = pointy;
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

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public double getWgsx() {
        return wgsx;
    }

    public void setWgsx(double wgsx) {
        this.wgsx = wgsx;
    }

    public double getWgsy() {
        return wgsy;
    }

    public void setWgsy(double wgsy) {
        this.wgsy = wgsy;
    }

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getToilet() {
        return toilet;
    }

    public void setToilet(int toilet) {
        this.toilet = toilet;
    }

    public int getElevator() {
        return elevator;
    }

    public void setElevator(int elevator) {
        this.elevator = elevator;
    }

    public int getEscalator() {
        return escalator;
    }

    public void setEscalator(int escalator) {
        this.escalator = escalator;
    }

    public int getWheellift() {
        return wheellift;
    }

    public void setWheellift(int wheellift) {
        this.wheellift = wheellift;
    }

    public int getPointx() {
        return pointx;
    }

    public void setPointx(int pointx) {
        this.pointx = pointx;
    }

    public int getPointy() {
        return pointy;
    }

    public void setPointy(int pointy) {
        this.pointy = pointy;
    }
}
