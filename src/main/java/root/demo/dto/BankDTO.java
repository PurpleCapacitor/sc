package root.demo.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BankDTO implements Serializable {
	
	private String paymentUrl;
	private String paymentId;
	
	public BankDTO() {
		
	}

	public String getPaymentUrl() {
		return paymentUrl;
	}

	public void setPaymentUrl(String paymentUrl) {
		this.paymentUrl = paymentUrl;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	
	
}
