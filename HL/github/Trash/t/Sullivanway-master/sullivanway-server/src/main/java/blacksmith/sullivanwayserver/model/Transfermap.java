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
public class Transfermap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -270065513674483669L;
	@Id
	private long id;
	private int image;
	private String startnext;
	private String endnext;
	private int floor;
	private int time;
	private String startline;
	private String endline;
	private String name;
}
