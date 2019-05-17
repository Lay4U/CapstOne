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
public class Transfer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6801266945842265722L;
	@Id
	private long id;
	private String startline;
	private String endline;
	private String name;
}
