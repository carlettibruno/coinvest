package br.com.carlettisolucoes.coinvest.bitfinex;

import java.io.Serializable;
import java.util.List;

public class OrderBook implements Serializable {

	private static final long serialVersionUID = 83165102464909735L;
	
	private List<Order> bids;
	
	private List<Order> asks;

	public List<Order> getBids() {
		return bids;
	}

	public void setBids(List<Order> bids) {
		this.bids = bids;
	}

	public List<Order> getAsks() {
		return asks;
	}

	public void setAsks(List<Order> asks) {
		this.asks = asks;
	}
	
}
