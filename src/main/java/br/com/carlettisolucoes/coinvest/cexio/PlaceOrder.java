package br.com.carlettisolucoes.coinvest.cexio;

import java.math.BigDecimal;

import br.com.carlettisolucoes.coinvest.wallet.Wallet;

public class PlaceOrder extends PrivateObject {

	private static final long serialVersionUID = 1239520464246594045L;
	
	private String type;
	
	private BigDecimal amount;
	
	private BigDecimal price;
	
	private Double trust;
	
	private Double security;
	
	private Wallet wallet;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "PlaceOrder [type=" + type + ", amount=" + amount + ", price=" + price + "]";
	}

	public Double getTrust() {
		return trust;
	}

	public void setTrust(Double trust) {
		this.trust = trust;
	}

	public Double getSecurity() {
		return security;
	}

	public void setSecurity(Double security) {
		this.security = security;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

}
