package root.demo.model.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VerificationToken {
	
	@Id
	@Column
	private String email;
	@Column
	private String token;
	
	public VerificationToken() {
		
	}

	public VerificationToken(String email, String token) {
		super();
		this.email = email;
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
