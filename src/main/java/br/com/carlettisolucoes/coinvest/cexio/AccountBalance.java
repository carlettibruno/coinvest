package br.com.carlettisolucoes.coinvest.cexio;

import java.io.Serializable;

public class AccountBalance implements Serializable {

	private static final long serialVersionUID = -3413231425397926387L;
	
	private Long timestamp;
	
	private String username;
	
	private Balance BTC;
	
	private Balance USD;
	
	private Balance XRP;
	
	private Balance ETH;
	
	private Balance BCH;
	
	private Balance BTG;
	
	private Balance DASH;
	
	private Balance ZEC;
	
	@Override
	public String toString() {
		return "AccountBalance [timestamp=" + timestamp + ", username=" + username + ", BTC=" + BTC + ", USD=" + USD
				+ ", XRP=" + XRP + ", ETH=" + ETH + ", BCH=" + BCH + ", BTG=" + BTG + ", DASH=" + DASH + ", ZEC=" + ZEC
				+ "]";
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Balance getBTC() {
		return BTC;
	}

	public void setBTC(Balance bTC) {
		BTC = bTC;
	}

	public Balance getUSD() {
		return USD;
	}

	public void setUSD(Balance uSD) {
		USD = uSD;
	}

	public Balance getXRP() {
		return XRP;
	}

	public void setXRP(Balance xRP) {
		XRP = xRP;
	}

	public Balance getETH() {
		return ETH;
	}

	public void setETH(Balance eTH) {
		ETH = eTH;
	}

	public Balance getBCH() {
		return BCH;
	}

	public void setBCH(Balance bCH) {
		BCH = bCH;
	}

	public Balance getBTG() {
		return BTG;
	}

	public void setBTG(Balance bTG) {
		BTG = bTG;
	}

	public Balance getDASH() {
		return DASH;
	}

	public void setDASH(Balance dASH) {
		DASH = dASH;
	}

	public Balance getZEC() {
		return ZEC;
	}

	public void setZEC(Balance zEC) {
		ZEC = zEC;
	}

}