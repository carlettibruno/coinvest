package br.com.carlettisolucoes.coinvest.cexio;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderBook implements Serializable {

	private static final long serialVersionUID = 7493311809924994263L;

	private Long id;
	
	private String timestamp;
	
	private BigDecimal[][] bids;
	
	private BigDecimal[][] asks;
	
	private String pair;
	
	private BigDecimal sell_total;
	
	private BigDecimal buy_total;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public BigDecimal[][] getBids() {
		return bids;
	}

	public void setBids(BigDecimal[][] bids) {
		this.bids = bids;
	}

	public void setAsks(BigDecimal[][] asks) {
		this.asks = asks;
	}

	public String getPair() {
		return pair;
	}

	public void setPair(String pair) {
		this.pair = pair;
	}

	public BigDecimal getSell_total() {
		return sell_total;
	}

	public void setSell_total(BigDecimal sell_total) {
		this.sell_total = sell_total;
	}

	public BigDecimal getBuy_total() {
		return buy_total;
	}

	public void setBuy_total(BigDecimal buy_total) {
		this.buy_total = buy_total;
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
		OrderBook other = (OrderBook) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderBook [id=" + id + ", timestamp=" + timestamp + ", pair=" + pair + ", sell_total=" + sell_total
				+ ", buy_total=" + buy_total + "]";
	}

	public BigDecimal[][] getAsks() {
		return asks;
	}
	
}
