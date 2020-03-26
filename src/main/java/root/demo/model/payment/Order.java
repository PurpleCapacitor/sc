package root.demo.model.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.netty.util.internal.ThreadLocalRandom;
import root.demo.model.enums.OrderStatus;
import root.demo.model.enums.OrderType;

@Entity
@Table(name = "ORDER_TABLE")
public class Order {

	@Id
	private Long merchantOrderId;
	
	@Column
	private String merchantId; // magazine name
	
	@Column
	private OrderType type;
	
	@Column
	private String buyerUsername;
	
	@Column
	private LocalDateTime timestamp;
	
	@Column
	private String fileName;
	
	@Column
	private String edition;
	
	@Column
	private BigDecimal amount;
	
	@Column
	private OrderStatus status;
	
	public long getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(long merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public OrderType getType() {
		return type;
	}
	public void setType(OrderType type) {
		this.type = type;
	}
	public String getBuyerUsername() {
		return buyerUsername;
	}
	public void setBuyerUsername(String buyerUsername) {
		this.buyerUsername = buyerUsername;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	
	public Order() {
		
	}
	
	@SuppressWarnings("unused")
	public long generateId() {
		return ThreadLocalRandom.current().nextLong(1000000000, 9999999999L);
	}

}
