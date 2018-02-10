package br.com.carlettisolucoes.coinvest.runner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import br.com.carlettisolucoes.coinvest.cexio.OrderBook;
import br.com.carlettisolucoes.coinvest.coin.Coin;
import br.com.carlettisolucoes.coinvest.coin.CoinRepository;
import br.com.carlettisolucoes.coinvest.exchange.Exchange;
import br.com.carlettisolucoes.coinvest.exchange.ExchangeRepository;
import br.com.carlettisolucoes.coinvest.orderbook.OrderBookHistory;
import br.com.carlettisolucoes.coinvest.orderbook.OrderBookHistoryRepositoryImpl;

@Component
public class RunStatistics {

	private static final Logger log = LoggerFactory.getLogger(RunStatistics.class);
	
	@Autowired
	private OrderBookHistoryRepositoryImpl repoImpl;
	
	@Autowired
	private ExchangeRepository exchangeRepo;
	
	@Autowired
	private CoinRepository coinRepo;
	
//	@Autowired
//	private BitfinexApi bitfinexApi;
	
	public void process() {
		BigDecimal hundred = new BigDecimal("100");
		log.info("Starting process RunStatistics");
		int amountVerify = 360;
		int amount = amountVerify * 3;
		Exchange exchange = exchangeRepo.findOne(1);
		Iterable<Coin> coins = coinRepo.findAll();
//		for (Exchange exchange : exchanges) {
			for (Coin coin : coins) {
//				log.info("Getting orders history for coin: {} exchange: {}", coin, exchange);
				List<OrderBookHistory> orders = repoImpl.getOrderBookPending(exchange.getId(), coin.getId(), amount);
				for (int i = 0; i < orders.size(); i++) {
					int limit = i + 1 + amountVerify;
					if(limit >= orders.size()) {
						break;
					}
					OrderBookHistory obhActual = orders.get(i);
					OrderBook obActual = null;
					Gson gson = new Gson();
//					if(exchange.getName().equalsIgnoreCase("bitfinex")) {
//						br.com.carlettisolucoes.coinvest.bitfinex.OrderBook ob = gson.fromJson(obhActual.getOrdersJson(), br.com.carlettisolucoes.coinvest.bitfinex.OrderBook.class);
//						if(ob.getAsks() == null || ob.getBids() == null) {
//							continue;
//						}
//						obActual = bitfinexApi.toOrderBookDefault(ob);
//					} else {
						obActual = gson.fromJson(obhActual.getOrdersJson(), OrderBook.class);
//					}
					if(obActual == null || obActual.getBids() == null) {
						continue;
					}					
					OrderBookHistory obhCompare = orders.get(limit);
					OrderBook obLast = null;
//					if(exchange.getName().equalsIgnoreCase("bitfinex")) {
//						br.com.carlettisolucoes.coinvest.bitfinex.OrderBook ob = gson.fromJson(obhCompare.getOrdersJson(), br.com.carlettisolucoes.coinvest.bitfinex.OrderBook.class);
//						obLast = bitfinexApi.toOrderBookDefault(ob);
//					} else {
						obLast = gson.fromJson(obhCompare.getOrdersJson(), OrderBook.class);	
//					}
					if(obLast == null || obLast.getBids() == null) {
						continue;
					}
					BigDecimal diff = obLast.getBids()[0][0].multiply(hundred.divide(obActual.getBids()[0][0], 6, RoundingMode.HALF_UP));
					repoImpl.updateOrderBookPending(obhActual.getId(), diff.subtract(hundred));
				}
			}
//		}
	}
	
}
