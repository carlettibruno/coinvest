package br.com.carlettisolucoes.coinvest.rulesecurity;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.carlettisolucoes.coinvest.cexio.PlaceOrder;

@Component
public class BigValueRuleSecurity implements RuleSecurityExecution {

	public void checkOrders(List<PlaceOrder> placeOrders) {
		
	}

}
