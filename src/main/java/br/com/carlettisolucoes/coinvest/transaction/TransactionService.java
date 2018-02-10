package br.com.carlettisolucoes.coinvest.transaction;

import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.carlettisolucoes.coinvest.account.Account;
import br.com.carlettisolucoes.coinvest.cexio.CexioApi;
import br.com.carlettisolucoes.coinvest.cexio.PlaceOrder;
import br.com.carlettisolucoes.coinvest.cexio.PlaceOrderResponse;
import br.com.carlettisolucoes.coinvest.wallet.Wallet;
import br.com.carlettisolucoes.coinvest.wallet.WalletRepository;

@Service
public class TransactionService {
	
	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

	@Autowired
	private WalletRepository walletRepo;
	
	@Autowired
	private CexioApi cexioApi;

	public void placeOrder(PlaceOrder placeOrder, Account account) {
		
//		BigDecimal finalAmount = placeOrder.getAmount();
//		if(placeOrder.getType().equalsIgnoreCase(TransactionType.BUY.toString())) {
//			BigDecimal feePercent = new BigDecimal("0.003");
//			BigDecimal feeValue = placeOrder.getAmount().multiply(feePercent);
//			finalAmount = finalAmount.subtract(feeValue).setScale(6, RoundingMode.CEILING);
//		}
//		placeOrder.setAmount(finalAmount);
		//
		
		
		placeOrder.setPrice(placeOrder.getPrice().setScale(placeOrder.getWallet().getCoin().getPricePrecision(), RoundingMode.CEILING));
		placeOrder.setAmount(placeOrder.getAmount().setScale(6, RoundingMode.CEILING));
		log.info("Do order: {}", placeOrder);
		PlaceOrderResponse placeOrderResponse = cexioApi.placeOrder(placeOrder, account);
		log.info("PlaceOrderResponse: {}", placeOrderResponse);
		
		if(placeOrderResponse != null) {
			Wallet wallet = placeOrder.getWallet();
			wallet.setLastOrderId(placeOrderResponse.getId());
			if(placeOrder.getType().equalsIgnoreCase(TransactionType.BUY.toString())) {
				wallet.setLastPaidUsd(placeOrder.getPrice());
			} else {
				wallet.setLastSellUsd(placeOrder.getPrice());
			}
			walletRepo.save(wallet);
		}
		
		
		//
//		if(placeOrderResponse != null) {
//			Transaction transaction = new Transaction();
//			transaction.setDatetime(new Date());
//			transaction.setOrderId(placeOrderResponse.getId());
//			transaction.setQuotation(null);
//			transaction.setType(TransactionType.valueOf(placeOrderResponse.getType().toUpperCase()));
//			transaction.setUsd(placeOrderResponse.getPrice());
//			transaction.setValue(placeOrderResponse.getAmount());
//			transaction.setWallet(placeOrder.getWallet());
//			repo.save(transaction);
//		}
	}
	
}
