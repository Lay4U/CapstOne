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
public class Downline implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7009277800319228480L;
	@Id
	private long id;
	private String line;
	private String startname;
	private String endname;
}
