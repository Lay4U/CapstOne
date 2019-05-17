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
public class Congestion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2777336340680285415L;
	@Id
	private long id;
	private String date;
	private String line;
	private String name;
	private int time;
	private int popularity;
}
