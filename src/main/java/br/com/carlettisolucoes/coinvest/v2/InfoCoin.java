package br.com.carlettisolucoes.coinvest.v2;

import java.io.Serializable;
import java.math.BigDecimal;

public class InfoCoin implements Serializable {
	
	private static final long serialVersionUID = 3135546207204121424L;

	private String coin;
	
	private BigDecimal lastPrice;
	
	private BigDecimal volume;
	
	private BigDecimal bid;
	
	private BigDecimal ask;

	public InfoCoin(String coin) {
		super();
		this.coin = coin;
	}

	public InfoCoin(String coin, BigDecimal lastPrice, BigDecimal volume, BigDecimal bid, BigDecimal ask) {
		super();
		this.coin = coin;
		this.lastPrice = lastPrice;
		this.volume = volume;
		this.bid = bid;
		this.ask = ask;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public BigDecimal getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	public BigDecimal getAsk() {
		return ask;
	}

	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coin == null) ? 0 : coin.hashCode());
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
		InfoCoin other = (InfoCoin) obj;
		if (coin == null) {
			if (other.coin != null)
				return false;
		} else if (!coin.equals(other.coin))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InfoCoin [coin=" + coin + ", lastPrice=" + lastPrice + ", volume=" + volume + ", bid=" + bid + ", ask="
				+ ask + "]";
	}

}
