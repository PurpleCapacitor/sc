package root.demo.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MagazineDTO implements Serializable {

	private String name;

	public MagazineDTO() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
