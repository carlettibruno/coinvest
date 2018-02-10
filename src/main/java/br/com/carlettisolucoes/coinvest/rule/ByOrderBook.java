package br.com.carlettisolucoes.coinvest.rule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.carlettisolucoes.coinvest.account.Account;
import br.com.carlettisolucoes.coinvest.cexio.ActiveOrderStatusResponse;
import br.com.carlettisolucoes.coinvest.cexio.CexioApi;
import br.com.carlettisolucoes.coinvest.cexio.PlaceOrder;
import br.com.carlettisolucoes.coinvest.cexio.Ticker;
import br.com.carlettisolucoes.coinvest.distanceorderbook.DistanceOrderBookService;
import br.com.carlettisolucoes.coinvest.transaction.TransactionType;
import br.com.carlettisolucoes.coinvest.wallet.Wallet;
import br.com.carlettisolucoes.coinvest.wallet.WalletRepository;

@Component
public class ByOrderBook implements RuleExecution {
	
	private static final Logger log = LoggerFactory.getLogger(ByOrderBook.class);

	@Autowired
	private CexioApi cexioApi;
	
	@Autowired
	private WalletRepository walletRepo;
	
	@Autowired
	private DistanceOrderBookService distanceService;
	
	public List<PlaceOrder> process(Iterable<Wallet> wallets, Account account) {
		List<PlaceOrder> orders = new ArrayList<PlaceOrder>();
		BigDecimal marginPercent = new BigDecimal("0.006");
		BigDecimal valueMakerLoss = new BigDecimal("0.01");
		
		for (Wallet wallet : wallets) {
			if(!wallet.isActive()) {
				continue;
			}

			if(wallet.getLastOrderId() != null) {
				ActiveOrderStatusResponse response = cexioApi.getActiveOrderStatus(Arrays.asList(wallet.getLastOrderId()), account);
				if(response == null || !response.isCancelledOrExecuted(0)) {
					continue;
				}
				wallet.setLastOrderId(null);
				walletRepo.save(wallet);
			}
			
			TransactionType type = wallet.getValue().compareTo(wallet.getCoin().getAmountMinimumBuy()) >= 0 ? TransactionType.SELL : TransactionType.BUY;
			
			Ticker ticker = cexioApi.getTicker(wallet.getCoin().getPath());
			
			PlaceOrder placeOrder = new PlaceOrder();
			placeOrder.setWallet(wallet);
			
			if(type.equals(TransactionType.BUY)) {
				BigDecimal usdToUse = account.getValue();
				if(usdToUse.compareTo(wallet.getUsdLimit()) == 1) {
					usdToUse = wallet.getUsdLimit();
				}
				BigDecimal valueToPay = wallet.getLastSellUsd().subtract(wallet.getLastSellUsd().multiply(marginPercent)); //TODO build a rule for get this valueToPay from OrderBook rule
				BigDecimal amountToGet = usdToUse.divide(valueToPay, 6, RoundingMode.HALF_UP);
				if(amountToGet.compareTo(wallet.getCoin().getAmountMinimumBuy()) == -1) {
					continue;
				}
				boolean execDistance = distanceService.execOrder(wallet.getCoin());
//				if(!execDistance && valueToPay.compareTo(ticker.getAsk()) < 0) {
				if(!execDistance) {
					log.info("Waiting price to buy: {} exec distance: {} for wallet: {}", valueToPay, execDistance, wallet);
					continue;
				}				
				placeOrder.setAmount(amountToGet);
				placeOrder.setPrice(ticker.getAsk().add(valueMakerLoss));
				placeOrder.setType(TransactionType.BUY.toString().toLowerCase());
				orders.add(placeOrder);
			} else {
				BigDecimal valueToSell = wallet.getLastPaidUsd().add(wallet.getLastPaidUsd().multiply(marginPercent)); //TODO build a rule for get this valueToPay from OrderBook rule
				if(valueToSell.compareTo(ticker.getBid()) > 0) {
					log.info("Waiting price to sell: {} for wallet: {}", valueToSell, wallet);
					continue;
				}				
				BigDecimal amountToSell = wallet.getValue();
				if(amountToSell.compareTo(wallet.getCoin().getAmountMinimumBuy()) == -1) {
					continue;
				}				
				placeOrder.setAmount(amountToSell);
				placeOrder.setPrice(ticker.getBid().subtract(valueMakerLoss));
				placeOrder.setType(TransactionType.SELL.toString().toLowerCase());
				orders.add(placeOrder);				
			}
			
		}
		return orders;
	}
	
}
