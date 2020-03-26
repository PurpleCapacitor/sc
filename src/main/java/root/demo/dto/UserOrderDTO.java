package root.demo.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserOrderDTO implements Serializable {
	
	private String type;
	private String amount;
	private String fileName;
	private String edition;
	
	public UserOrderDTO() {
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}
	
	

}
