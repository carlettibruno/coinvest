package br.com.carlettisolucoes.coinvest.runner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import br.com.carlettisolucoes.coinvest.cexio.CexioApi;
import br.com.carlettisolucoes.coinvest.cexio.OrderBook;
import br.com.carlettisolucoes.coinvest.coin.Coin;
import br.com.carlettisolucoes.coinvest.coin.CoinRepository;
import br.com.carlettisolucoes.coinvest.fakeorder.FakeOrder;
import br.com.carlettisolucoes.coinvest.fakeorder.FakeOrderRepository;
import br.com.carlettisolucoes.coinvest.orderbook.OrderBookHistory;
import br.com.carlettisolucoes.coinvest.orderbook.OrderBookHistoryRepositoryImpl;

@Component
public class RunFakeOrder {
	
	private static final Logger log = LoggerFactory.getLogger(RunFakeOrder.class);
	
	@Autowired
	private CexioApi cexioApi;
	
	@Autowired
	private CoinRepository coinRepo;
	
	@Autowired
	private OrderBookHistoryRepositoryImpl obhRepo;
	
	@Autowired
	private FakeOrderRepository fakeOrderRepo;
	
	public void process() {
		log.info("Starting process RunFakeOrder...");
		
		List<OrderBookHistory> obhs = obhRepo.getGoodHistories(1, new BigDecimal(8));
		if(obhs.isEmpty()) {
			return;
		}
		BigDecimal hundred = new BigDecimal(100);
		BigDecimal diffLimit = new BigDecimal(20);
		int diffTotal = 20;
		Iterable<Coin> coins = coinRepo.findAll();
		Gson gson = new Gson();
		float lessPercent = 100f;
		OrderBookHistory obhReference = null;
		Coin coinReference = null;
		for (Coin coin : coins) {
			OrderBook orderBook = cexioApi.getOrderBook(coin.getPath());
			for (OrderBookHistory obh : obhs) {
				int amountOk = 0;
				int totalAmount = 0;
				OrderBook goodOrderBook = gson.fromJson(obh.getOrdersJson(), OrderBook.class);
				for (int i = 0; (i < orderBook.getAsks().length - 1 && i < goodOrderBook.getAsks().length - 1); i++) {
					BigDecimal diff = orderBook.getAsks()[i][0].divide(orderBook.getAsks()[i][1], 6, RoundingMode.HALF_UP);
					BigDecimal diffNext = orderBook.getAsks()[i + 1][0].divide(orderBook.getAsks()[i + 1][1], 6, RoundingMode.HALF_UP);
					BigDecimal percent = diffNext.multiply(hundred).divide(diff, 6, RoundingMode.HALF_UP);
					
					BigDecimal goodDiff = goodOrderBook.getAsks()[i][0].divide(goodOrderBook.getAsks()[i][1], 6, RoundingMode.HALF_UP);
					BigDecimal goodDiffNext = goodOrderBook.getAsks()[i + 1][0].divide(goodOrderBook.getAsks()[i + 1][1], 6, RoundingMode.HALF_UP);
					BigDecimal goodPercent = goodDiffNext.multiply(hundred).divide(goodDiff, 6, RoundingMode.HALF_UP);
					
					if(goodPercent.subtract(percent).abs().compareTo(diffLimit) < 0) {
						amountOk++;
					}
					totalAmount++;
				}
				
				for (int i = 0; (i < orderBook.getBids().length - 1 && i < goodOrderBook.getBids().length - 1); i++) {
					BigDecimal diff = orderBook.getBids()[i][0].divide(orderBook.getBids()[i][1], 6, RoundingMode.HALF_UP);
					BigDecimal diffNext = orderBook.getBids()[i + 1][0].divide(orderBook.getBids()[i + 1][1], 6, RoundingMode.HALF_UP);
					BigDecimal percent = diffNext.multiply(hundred).divide(diff, 6, RoundingMode.HALF_UP);
					
					BigDecimal goodDiff = goodOrderBook.getBids()[i][0].divide(goodOrderBook.getBids()[i][1], 6, RoundingMode.HALF_UP);
					BigDecimal goodDiffNext = goodOrderBook.getBids()[i + 1][0].divide(goodOrderBook.getBids()[i + 1][1], 6, RoundingMode.HALF_UP);
					BigDecimal goodPercent = goodDiffNext.multiply(hundred).divide(goodDiff, 6, RoundingMode.HALF_UP);
					
					if(goodPercent.subtract(percent).abs().compareTo(diffLimit) < 0) {
						amountOk++;
					}
					totalAmount++;
				}
				
				float percentFinal = ((float)amountOk / (float)totalAmount) * 100;
				if(percentFinal < diffTotal && lessPercent > percentFinal) {
					lessPercent = percentFinal;
					obhReference = obh;
					coinReference = coin;
				}
			}
			
		}
		
		if(obhReference != null) {
			FakeOrder fakeOrder = new FakeOrder();
			fakeOrder.setAcceptedPercent(lessPercent);
			fakeOrder.setDate(new Date());
			fakeOrder.setOrderBookHistory(obhReference);
			fakeOrder.setCoin(coinReference);
			fakeOrderRepo.save(fakeOrder);
		}
		
	}

}
