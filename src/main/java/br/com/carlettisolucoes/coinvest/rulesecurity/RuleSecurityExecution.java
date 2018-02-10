package br.com.carlettisolucoes.coinvest.rulesecurity;

import java.util.List;

import br.com.carlettisolucoes.coinvest.cexio.PlaceOrder;

public interface RuleSecurityExecution {

	void checkOrders(List<PlaceOrder> placeOrders);
	
}
