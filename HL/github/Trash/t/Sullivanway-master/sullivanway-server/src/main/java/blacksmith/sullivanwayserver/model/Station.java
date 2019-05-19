package blacksmith.sullivanwayserver.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Station implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1525040077313508699L;
	@Id
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
}
