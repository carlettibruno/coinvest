package br.com.carlettisolucoes.coinvest.rule;

import java.util.List;

import br.com.carlettisolucoes.coinvest.account.Account;
import br.com.carlettisolucoes.coinvest.cexio.PlaceOrder;
import br.com.carlettisolucoes.coinvest.wallet.Wallet;

public interface RuleExecution {

	List<PlaceOrder> process(Iterable<Wallet> wallets, Account account);
	
}
