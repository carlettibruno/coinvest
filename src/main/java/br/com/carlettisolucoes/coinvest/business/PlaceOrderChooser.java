package br.com.carlettisolucoes.coinvest.business;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.carlettisolucoes.coinvest.cexio.PlaceOrder;

@Component
public class PlaceOrderChooser {

	public PlaceOrder make(List<PlaceOrder> placeOrders) {
		return placeOrders.get(0);
	}
	
}
