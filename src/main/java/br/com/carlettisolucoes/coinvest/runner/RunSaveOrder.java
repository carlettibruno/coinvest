package br.com.carlettisolucoes.coinvest.runner;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.carlettisolucoes.coinvest.cexio.CexioApi;
import br.com.carlettisolucoes.coinvest.coin.Coin;
import br.com.carlettisolucoes.coinvest.coin.CoinRepository;
import br.com.carlettisolucoes.coinvest.exchange.Exchange;
import br.com.carlettisolucoes.coinvest.exchange.ExchangeRepository;
import br.com.carlettisolucoes.coinvest.orderbook.OrderBookHistory;
import br.com.carlettisolucoes.coinvest.orderbook.OrderBookHistoryRepository;

@Component
public class RunSaveOrder {
	
	private static final Logger log = LoggerFactory.getLogger(RunSaveOrder.class);

	@Autowired
	private OrderBookHistoryRepository repo;
	
	@Autowired
	private CexioApi cexioApi;
	
//	@Autowired
//	private BitfinexApi bitfinexApi;
	
	@Autowired
	private CoinRepository coinRepo;
	
	@Autowired
	private ExchangeRepository exchangeRepo;
	
	public void process() {
		log.info("Starting process RunSaveOrder");
//		Iterator<Exchange> exchanges = exchangeRepo.findAll().iterator();
		Exchange cexio = exchangeRepo.findOne(1);
//		Exchange bitfinex = exchanges.next();
		Iterable<Coin> coins = coinRepo.findAll();
		for (Coin coin : coins) {
//			log.info("Getting orderbookhistory of coin: {}", coin);
			String jsonCexioApi = cexioApi.getOrderBookJson(coin.getPath());
//			String jsonBitfinexApi = bitfinexApi.getOrderBookJson(coin.getPath());
			OrderBookHistory obhCexio = new OrderBookHistory();
			obhCexio.setDate(new Date());
			obhCexio.setExchange(cexio);
			obhCexio.setOrdersJson(jsonCexioApi);
			obhCexio.setCoin(coin);
			repo.save(obhCexio);
			
//			OrderBookHistory obhBitfinex = new OrderBookHistory();
//			obhBitfinex.setDate(new Date());
//			obhBitfinex.setExchange(bitfinex);
//			obhBitfinex.setOrdersJson(jsonBitfinexApi);
//			obhBitfinex.setCoin(coin);
//			repo.save(obhBitfinex);
		}
	}
	
}
