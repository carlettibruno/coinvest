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
import br.com.carlettisolucoes.coinvest.quotation.Quotation;
import br.com.carlettisolucoes.coinvest.quotation.QuotationRepositoryImpl;
import br.com.carlettisolucoes.coinvest.transaction.TransactionType;
import br.com.carlettisolucoes.coinvest.wallet.Wallet;

@Component
public class ByUpDown implements RuleExecution {

	private static final Logger log = LoggerFactory.getLogger(ByUpDown.class);

	@Autowired
	private QuotationRepositoryImpl quotationImplRepo;

//	@Autowired	
//	private TransactionRepositoryImpl transactionRepoImpl;
	
	@Autowired
	private CexioApi cexioApi;

	public List<PlaceOrder> process(Iterable<Wallet> wallets, Account account) {
		BigDecimal tradePercent = new BigDecimal("0.65"); //TODO pensar em variar esse numero... de acordo com o historico.. com a media de variacao de pico do momento.fazer uma media do percentual das ultimas transacoes talvez..
		/*
		 * 
		 * 
select c.name, avg((q.value * 100) / qa.value - 100)--, *
from quotation q 
join quotation qa on(q.previous_quotation_id = qa.id)
join coin c on(q.coin_id = c.id)
where ((q.value * 100) / qa.value - 100) > 0.60
and ((q.value * 100) / qa.value - 100) < 1
group by c.name
--order by q.coin_id, q.id


select c.name, avg((q.value * 100) / qa.value - 100)--, *
from quotation q 
join quotation qa on(q.previous_quotation_id = qa.id)
join coin c on(q.coin_id = c.id)
where ((q.value * 100) / qa.value - 100) < -0.60
and ((q.value * 100) / qa.value - 100) > -1
group by c.name
--order by q.coin_id, q.id 
		 * 
		 * 
		 */
		
		
		List<PlaceOrder> orders = new ArrayList<PlaceOrder>();

		BigDecimal valueRemaining = account.getValue();
		for (Wallet wallet : wallets) {
			if(!wallet.isActive()) {
				continue;
			}
//			Iterable<Transaction> transactions = transactionRepoImpl.getLatest(1, wallet.getId());
			TransactionType transactionType = TransactionType.BUY;
			if(wallet.getValue().signum() > 0) {
				transactionType = TransactionType.SELL;
			}
			
			if(wallet.getLastOrderId() != null) {
				ActiveOrderStatusResponse response = cexioApi.getActiveOrderStatus(Arrays.asList(wallet.getLastOrderId()), account);
				if(response == null || !response.isCancelledOrExecuted(0)) {
					continue;
				}
			}
			
			List<Quotation> quotations = quotationImplRepo.getLatest(1, wallet.getCoin().getId());
			Quotation quotation = quotations.get(0);
			BigDecimal valueOrder = calculateValue(wallet, quotation, transactionType, tradePercent);
			BigDecimal coins = null;
			if(transactionType.equals(TransactionType.BUY)) {
				BigDecimal usdToUse = valueRemaining;
				if(valueRemaining.compareTo(wallet.getUsdLimit()) == 1) {
					usdToUse = wallet.getUsdLimit();
				}
				coins = usdToUse.divide(valueOrder, 6, RoundingMode.HALF_UP);
				if(coins.compareTo(wallet.getCoin().getAmountMinimumBuy()) == -1) {
					continue;
				}
				valueRemaining = valueRemaining.subtract(usdToUse);
			} else {
				coins = wallet.getValue();
			}
			
			log.info("Transaction Type: {}, Quotation Value: {}, ValueOrder: {}", transactionType, quotation.getValue(), valueOrder);
			if(transactionType.equals(TransactionType.BUY)
					|| (transactionType.equals(TransactionType.SELL) && quotation.getValue().compareTo(valueOrder) >= 0) ) {
				
				PlaceOrder placeOrder = new PlaceOrder();
				placeOrder.setType(transactionType.toString().toLowerCase());
				placeOrder.setWallet(wallet);
				placeOrder.setAmount(coins);
				if(transactionType.equals(TransactionType.BUY)) {
					placeOrder.setPrice(valueOrder);
				} else {
					placeOrder.setPrice(quotation.getValue().subtract(valueOrder).multiply(new BigDecimal("0.5")).add(valueOrder)); //subtraindo da cotação atual
				}
				orders.add(placeOrder);
			}
		}

		return orders;
	}

	private BigDecimal calculateValue(Wallet wallet, Quotation quotation, TransactionType type, BigDecimal tradePercent) {
		BigDecimal hundred = new BigDecimal("100");
		BigDecimal refValue = quotation.getValue();
		if(wallet.getLastPaidUsd() != null && !type.equals(TransactionType.BUY)) {
			refValue = wallet.getLastPaidUsd();
		}
//		log.info("refValue: {}", refValue);
		BigDecimal factorPercent = hundred.add(tradePercent).divide(hundred, 6, RoundingMode.HALF_UP); //TODO fazer dinamico depois, pegar da base uma media... ver.. o minimo eh 1.005 por conta da taxa
		if(type.equals(TransactionType.BUY)) {
			factorPercent = hundred.subtract(tradePercent).divide(hundred, 6, RoundingMode.HALF_UP);
		}

		BigDecimal valueOrder = refValue.multiply(factorPercent);
		//		log.info("valueCompare: {}", valueCompare);
		//		Calendar calendar = Calendar.getInstance();
		//		calendar.setTime(transaction.getDatetime());
		//		calendar.add(Calendar.MINUTE, 5);
		//		Date datetimeLimit = calendar.getTime();
		//		log.info("timeLimite to force execute: {}", datetimeLimit);
		//		log.info("using quotation: {}", quotation);
		////		if(type.equals(TransactionType.SELL) && (valueCompare.compareTo(quotation.getValue()) == -1 || datetimeLimit.before(new Date()))) {
		//		if(type.equals(TransactionType.SELL) && valueCompare.compareTo(quotation.getValue()) == -1) {
		//			return 100.0; //TODO pensar em uma regra para definir um percentual aqui
		////		} else if(type.equals(TransactionType.BUY) && (valueCompare.compareTo(quotation.getValue()) == 1 || datetimeLimit.before(new Date()))) { //TODO melhorar a comparaçao desse tempo com algum historico na base
		//		} else if(type.equals(TransactionType.BUY) && valueCompare.compareTo(quotation.getValue()) == 1) { //TODO melhorar a comparaçao desse tempo com algum historico na base
		//			return 100.0; //TODO pensar em uma regra para definir um percentual aqui
		//		}

//		return quotation.getValue().multiply(new BigDecimal(100)).divide(valueCompare, 6, RoundingMode.HALF_UP).subtract(new BigDecimal(100));
		return valueOrder;
	}

}
