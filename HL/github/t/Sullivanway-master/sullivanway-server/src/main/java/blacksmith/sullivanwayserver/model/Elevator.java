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
public class Elevator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1701832761349741616L;
	@Id
	private long id;
	private String line;
	private String name;
	private int in_gate;
	private String floor;
	private String location;
}
