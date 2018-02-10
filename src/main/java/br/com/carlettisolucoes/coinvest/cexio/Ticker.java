package br.com.carlettisolucoes.coinvest.cexio;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Ticker extends ResponseObject {

	private static final long serialVersionUID = -8923015248032875613L;
	
	private BigDecimal bid;
	
	private BigDecimal ask;
	
	private BigDecimal low;
	
	private BigDecimal high;
	
	private BigDecimal last;
	
	private BigDecimal volume;
	
	private BigDecimal volume30d;
	
	private String pair;

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

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLast() {
		return last;
	}

	public void setLast(BigDecimal last) {
		this.last = last;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getVolume30d() {
		return volume30d;
	}

	public void setVolume30d(BigDecimal volume30d) {
		this.volume30d = volume30d;
	}

	public String getPair() {
		return pair;
	}

	public void setPair(String pair) {
		this.pair = pair;
	}

	@Override
	public String toString() {
		return "Ticker [bid=" + bid + ", ask=" + ask + ", low=" + low + ", high=" + high + ", last=" + last
				+ ", volume=" + volume + ", volume30d=" + volume30d + ", pair=" + pair + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pair == null) ? 0 : pair.hashCode());
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
		Ticker other = (Ticker) obj;
		if (pair == null) {
			if (other.pair != null)
				return false;
		} else if (!pair.equals(other.pair))
			return false;
		return true;
	}
	
	public BigDecimal getDiffBidAsk() {
		return bid.divide(ask, 6, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
	}

}
