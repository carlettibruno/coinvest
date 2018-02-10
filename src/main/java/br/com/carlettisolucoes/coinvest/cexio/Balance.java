package br.com.carlettisolucoes.coinvest.cexio;

import java.io.Serializable;
import java.math.BigDecimal;

public class Balance implements Serializable {

	private static final long serialVersionUID = -6339301570269021553L;
	
	private BigDecimal available;
	
	private BigDecimal orders;

	public BigDecimal getAvailable() {
		return available;
	}

	public void setAvailable(BigDecimal available) {
		this.available = available;
	}

	public BigDecimal getOrders() {
		return orders;
	}

	public void setOrders(BigDecimal orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "Balance [available=" + available + ", orders=" + orders + "]";
	}

}
