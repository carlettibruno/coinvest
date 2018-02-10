package br.com.carlettisolucoes.coinvest.cexio;

import java.util.List;

public class ActiveOrderStatusRequest extends PrivateObject {

	private static final long serialVersionUID = -5929710443322390524L;

	private List<Long> orders_list;

	public List<Long> getOrders_list() {
		return orders_list;
	}

	public void setOrders_list(List<Long> orders_list) {
		this.orders_list = orders_list;
	}
	
}
