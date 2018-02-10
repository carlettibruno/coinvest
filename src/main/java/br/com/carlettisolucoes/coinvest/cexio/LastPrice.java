package br.com.carlettisolucoes.coinvest.cexio;

import java.io.Serializable;

public class LastPrice implements Serializable {

	private static final long serialVersionUID = 5248376986547895646L;
	
	private String lprice;

	public String getLprice() {
		return lprice;
	}

	public void setLprice(String lprice) {
		this.lprice = lprice;
	}

}
