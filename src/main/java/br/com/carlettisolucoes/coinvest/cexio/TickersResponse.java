package br.com.carlettisolucoes.coinvest.cexio;

import java.util.List;

public class TickersResponse extends ResponseObject {

	private static final long serialVersionUID = 690142572793389000L;

	private List<Ticker> data;

	public List<Ticker> getData() {
		return data;
	}

	public void setData(List<Ticker> data) {
		this.data = data;
	}
	
}
