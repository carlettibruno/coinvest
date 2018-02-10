package br.com.carlettisolucoes.coinvest.bitfinex;

import java.io.Serializable;
import java.math.BigDecimal;

public class Order implements Serializable {

	private static final long serialVersionUID = 6222130959699754715L;
	
	private BigDecimal price;
	
	private BigDecimal amount;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
