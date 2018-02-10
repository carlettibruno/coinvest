package br.com.carlettisolucoes.coinvest.cexio;

import java.io.Serializable;
import java.math.BigDecimal;

public class PlaceOrderResponse implements Serializable {

	private static final long serialVersionUID = -815306242937753715L;
	
	private Long id;
	
	private Boolean complete;
	
	private Long time;
	
	private BigDecimal pending;
	
	private BigDecimal amount;
	
	private String type;
	
	private BigDecimal price;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getComplete() {
		return complete;
	}

	public void setComplete(Boolean complete) {
		this.complete = complete;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public BigDecimal getPending() {
		return pending;
	}

	public void setPending(BigDecimal pending) {
		this.pending = pending;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlaceOrderResponse other = (PlaceOrderResponse) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PlaceOrderResponse [id=" + id + ", complete=" + complete + ", time=" + time + ", pending=" + pending
				+ ", amount=" + amount + ", type=" + type + ", price=" + price + "]";
	}

}