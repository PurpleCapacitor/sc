package root.demo.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class scAreaDTO implements Serializable {

	private String name;
	private boolean checked = false;

	public scAreaDTO() {

	}

	public scAreaDTO(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	

}
