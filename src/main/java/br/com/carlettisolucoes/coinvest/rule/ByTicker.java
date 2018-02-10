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
import br.com.carlettisolucoes.coinvest.cexio.BestTickerUtil;
import br.com.carlettisolucoes.coinvest.cexio.CexioApi;
import br.com.carlettisolucoes.coinvest.cexio.PlaceOrder;
import br.com.carlettisolucoes.coinvest.cexio.Ticker;
import br.com.carlettisolucoes.coinvest.transaction.TransactionType;
import br.com.carlettisolucoes.coinvest.wallet.Wallet;

@Component
public class ByTicker implements RuleExecution {

	private static final Logger log = LoggerFactory.getLogger(ByUpDown.class);
	
	@Autowired
	private CexioApi cexioApi;
	
	public List<PlaceOrder> process(Iterable<Wallet> wallets, Account account) {
		log.info("Processing orders ByTicker...");
		BigDecimal buyAt = new BigDecimal("99.95");
		BigDecimal sellAt = new BigDecimal("99.94");
//		BigDecimal factorChangeValue = new BigDecimal("0.00015");
		BigDecimal factorChangeValue = BigDecimal.ZERO;
		BigDecimal factorDevaludLimit = new BigDecimal("0.002");
		BigDecimal factorAppreciatedLimit = new BigDecimal("0.0055");
		
		List<Ticker> tickers = cexioApi.getTickers("USD");
		List<Ticker> tickersTarget = new ArrayList<Ticker>();
		for (Ticker ticker : tickers) {
			Wallet wallet = getWalletByTicker(wallets, ticker);
			if(!wallet.isActive()) {
				continue;
			}
			if(wallet.getValue().compareTo(wallet.getCoin().getAmountMinimumBuy()) == -1 
					&& ticker.getDiffBidAsk().compareTo(buyAt) >= 0) {
				
				BestTickerUtil.add(ticker.getPair());
				tickersTarget.add(ticker);
				log.info("Adding ticker: {}", ticker);
			}
		}
		Ticker bestTicker = BestTickerUtil.getBestTicker(tickersTarget);
		
		List<PlaceOrder> orders = new ArrayList<PlaceOrder>();
		
		//build sell
		for (Wallet wallet : wallets) {
			if(!wallet.isActive() || wallet.getValue().signum() == 0) {
				continue;
			}
			if(wallet.getLastPaidUsd() == null) {
				log.warn("Wallet last paid usd is null for sell: {}", wallet);
				continue;
			}
			if(wallet.getLastOrderId() != null) {
				ActiveOrderStatusResponse response = cexioApi.getActiveOrderStatus(Arrays.asList(wallet.getLastOrderId()), account);
				if(response == null || !response.isCancelledOrExecuted(0)) {
					continue;
				}
			}			
			Ticker ticker = getTickerByWallet(tickers, wallet);
			if(ticker == null) {
				log.warn("Ticker not found for wallet: {}", wallet);
				continue;
			}
			log.debug("Wallet to sell: {}", wallet);
			log.debug("Ticker: {}", ticker);
			BigDecimal devaluedLimit = wallet.getLastPaidUsd().subtract(wallet.getLastPaidUsd().multiply(factorDevaludLimit));
			BigDecimal appreciatedLimit = wallet.getLastPaidUsd().add(wallet.getLastPaidUsd().multiply(factorAppreciatedLimit));
			boolean sellByDiffBidAsk = ticker.getDiffBidAsk().compareTo(sellAt) <= 0;
//			boolean stopLoss = sellByDiffBidAsk && ticker.getAsk().compareTo(devaluedLimit) < 0;
			boolean stopLoss = false;
			boolean timeToSell = sellByDiffBidAsk && ticker.getBid().compareTo(appreciatedLimit) >= 0;
			log.info("DevaluedLimit: {}, AppreciatedLimit: {}, sellByDiffBidAsk: {}, stopLoss: {}, timeToSell: {}", devaluedLimit, appreciatedLimit, sellByDiffBidAsk, stopLoss, timeToSell);
			if(stopLoss || timeToSell) {
				PlaceOrder placeOrder = new PlaceOrder();
				placeOrder.setType(TransactionType.SELL.toString().toLowerCase());
				placeOrder.setWallet(wallet);
				placeOrder.setAmount(wallet.getValue());
				placeOrder.setPrice(ticker.getBid().subtract(ticker.getBid().multiply(factorChangeValue)));
				orders.add(placeOrder);
			}
		}
		
		if(bestTicker != null) {
			boolean exec = true;
			Wallet wallet = getWalletByTicker(wallets, bestTicker);
			if(wallet.getLastOrderId() != null) {
				ActiveOrderStatusResponse response = cexioApi.getActiveOrderStatus(Arrays.asList(wallet.getLastOrderId()), account);
				if(response == null || !response.isCancelledOrExecuted(0)) {
					exec = false;
				}
			}			
			log.debug("Wallet to buy: {}", wallet);
			if(wallet.getValue().compareTo(wallet.getCoin().getAmountMinimumBuy()) == -1 && exec) {
				PlaceOrder placeOrder = new PlaceOrder();
				placeOrder.setType(TransactionType.BUY.toString().toLowerCase());
				placeOrder.setWallet(wallet);
				
				BigDecimal usdToUse = account.getValue();
				if(usdToUse.compareTo(wallet.getUsdLimit()) == 1) {
					usdToUse = wallet.getUsdLimit();
				}
				
				BigDecimal valueToPay = bestTicker.getAsk().add(bestTicker.getAsk().multiply(factorChangeValue));
				placeOrder.setAmount(usdToUse.divide(valueToPay, 6, RoundingMode.HALF_UP));
				placeOrder.setPrice(valueToPay);
				orders.add(placeOrder);					
			}
		}

		return orders;
	}
	
	private Ticker getTickerByWallet(List<Ticker> tickers, Wallet wallet) {
		String regex = "[:\\/]";
		String replacement = "";
		for (Ticker ticker : tickers) {
			String pair = ticker.getPair().replaceAll(regex, replacement);
			String path = wallet.getCoin().getPath().replaceAll(regex, replacement);
			log.debug("Wallet and Ticker to compare: Pair is {} Path is {}", pair, path);
			if(pair.equals(path)) {
				return ticker;
			}
		}
		return null;
	}
	
	private Wallet getWalletByTicker(Iterable<Wallet> wallets, Ticker ticker) {
		String regex = "[:\\/]";
		String replacement = "";
		for (Wallet wallet : wallets) {
			String pair = ticker.getPair().replaceAll(regex, replacement);
			String path = wallet.getCoin().getPath().replaceAll(regex, replacement);
			log.debug("Wallet and Ticker to compare: Pair is {} Path is {}", pair, path);			
			if(pair.equals(path)) {
				return wallet;
			}
		}
		return null;
	}	
	
}