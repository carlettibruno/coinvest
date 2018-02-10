package br.com.carlettisolucoes.coinvest.runner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.carlettisolucoes.coinvest.account.Account;
import br.com.carlettisolucoes.coinvest.account.AccountRepository;
import br.com.carlettisolucoes.coinvest.business.AccountWalletUpdater;
import br.com.carlettisolucoes.coinvest.cexio.PlaceOrder;
import br.com.carlettisolucoes.coinvest.quotation.QuotationService;
import br.com.carlettisolucoes.coinvest.rule.ByOrderBook;
import br.com.carlettisolucoes.coinvest.transaction.TransactionService;
import br.com.carlettisolucoes.coinvest.wallet.Wallet;
import br.com.carlettisolucoes.coinvest.wallet.WalletRepository;

@Component
public class RunCoinvest {
	
	private static final Logger log = LoggerFactory.getLogger(RunCoinvest.class);
	
	@Autowired
	private AccountWalletUpdater accountWalletUpdater;
	
//	@Autowired
//	private QuotationService quotationService;
	
	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private TransactionService transactionService;
//	
//	@Autowired
//	private ByUpDown byUpDown;
//	
//	@Autowired
//	private ByTicker byTicker;
	
	@Autowired
	private ByOrderBook byOrderBook;
	
//	
//	@Autowired
//	private WalletRepository walletRepo;
	
	private Iterable<Wallet> wallets;
	
	private Account account;
//	
//	@Autowired
//	private BigValueRuleSecurity bigValue;	
	
//	@Scheduled(fixedDelay=60 * 1000, initialDelay=60 * 1000)
//	public void processQuotation() {
//		log.info("Getting wallets...");
//		Iterable<Wallet> wallets = walletRepo.findAll();
//		
//		log.info("Updating quotations...");
//		quotationService.updater(wallets);
//
//		log.info("Process quotation finished.");
//	}
	
//	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void process() {
		updateAccountWalletInfo();
		try {
			List<PlaceOrder> placeOrders = byOrderBook.process(wallets, account);
			
			for (PlaceOrder placeOrder : placeOrders) {
				transactionService.placeOrder(placeOrder, account);
			}
			if(!placeOrders.isEmpty()) {
				updateAccountWalletInfo();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private void updateAccountWalletInfo() {
		String username = "up112960956";
		log.info("Processing orders to username: {}", username);
		account = accountRepo.findByUsername(username);
		log.info("Account {}", account);
		boolean ok = false;
		do {
			try {
				wallets = accountWalletUpdater.update(account);
				log.info("Wallets updated.");
				ok = true;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while(!ok);
	}
	
}
