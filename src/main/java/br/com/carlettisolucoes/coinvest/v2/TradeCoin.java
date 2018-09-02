package br.com.carlettisolucoes.coinvest.v2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradeCoin implements Serializable {

	private static final long serialVersionUID = -8861505073165167052L;

	private Exchange exchangeBuy;
	
	private Exchange exchangeReference;
	
	private InfoCoin buyInfo;
	
	private InfoCoin sellInfo;
	
	private Date date;
	
	private BigDecimal percent;

	public Exchange getExchangeBuy() {
		return exchangeBuy;
	}

	public void setExchangeBuy(Exchange exchangeBuy) {
		this.exchangeBuy = exchangeBuy;
	}

	public Exchange getExchangeReference() {
		return exchangeReference;
	}

	public void setExchangeReference(Exchange exchangeReference) {
		this.exchangeReference = exchangeReference;
	}

	public InfoCoin getBuyInfo() {
		return buyInfo;
	}

	public void setBuyInfo(InfoCoin buyInfo) {
		this.buyInfo = buyInfo;
	}

	public InfoCoin getSellInfo() {
		return sellInfo;
	}

	public void setSellInfo(InfoCoin sellInfo) {
		this.sellInfo = sellInfo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	@Override
	public String toString() {
		return "TradeCoin [exchangeBuy=" + exchangeBuy + ", exchangeReference=" + exchangeReference + ", buyInfo="
				+ buyInfo + ", sellInfo=" + sellInfo + ", date=" + date + ", percent=" + percent + "]";
	}

}