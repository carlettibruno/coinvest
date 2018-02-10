package br.com.carlettisolucoes.coinvest.business;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.carlettisolucoes.coinvest.account.Account;
import br.com.carlettisolucoes.coinvest.account.AccountRepository;
import br.com.carlettisolucoes.coinvest.cexio.AccountBalance;
import br.com.carlettisolucoes.coinvest.cexio.Balance;
import br.com.carlettisolucoes.coinvest.cexio.CexioApi;
import br.com.carlettisolucoes.coinvest.wallet.Wallet;
import br.com.carlettisolucoes.coinvest.wallet.WalletRepository;

@Component
public class AccountWalletUpdater {

	@Autowired
	private CexioApi cexioApi;

	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private WalletRepository walletRepo;
	
	public Iterable<Wallet> update(Account account) throws Exception {
		AccountBalance accountBalance = cexioApi.getAccountBalance(account);
		Iterable<Wallet> wallets = walletRepo.findAllByOrderByPriority();
		for (Wallet wallet : wallets) {
			String coinName = wallet.getCoin().getName();
			try {
				Method method = AccountBalance.class.getMethod(String.format("get%s", coinName));
				Balance balance = (Balance) method.invoke(accountBalance);
				BigDecimal newValue = balance.getAvailable().add(balance.getOrders());
				wallet.setValue(newValue);
				walletRepo.save(wallet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		account.setValue(accountBalance.getUSD().getAvailable());
		account.setValueOrders(accountBalance.getUSD().getOrders());
		accountRepo.save(account);
		return wallets;
	}
	
}
