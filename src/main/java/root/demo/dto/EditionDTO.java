package root.demo.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EditionDTO implements Serializable {

	private String name;
	private String price;
	private String magazineName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMagazineName() {
		return magazineName;
	}

	public void setMagazineName(String magazineName) {
		this.magazineName = magazineName;
	}

	public EditionDTO() {
		super();
	}

	public EditionDTO(String name, String price, String magazineName) {
		super();
		this.name = name;
		this.price = price;
		this.magazineName = magazineName;
	}

	

}
