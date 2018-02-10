package br.com.carlettisolucoes.coinvest.distanceorderbook;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.carlettisolucoes.coinvest.bitfinex.BitfinexApi;
import br.com.carlettisolucoes.coinvest.cexio.CexioApi;
import br.com.carlettisolucoes.coinvest.cexio.OrderBook;
import br.com.carlettisolucoes.coinvest.cexio.Ticker;
import br.com.carlettisolucoes.coinvest.coin.Coin;
import br.com.carlettisolucoes.coinvest.coin.CoinRepository;
import br.com.carlettisolucoes.coinvest.exchange.Exchange;
import br.com.carlettisolucoes.coinvest.exchange.ExchangeRepository;

@Service
public class DistanceOrderBookService {

//	private static final Logger log = LoggerFactory.getLogger(DistanceOrderBookService.class);
	
	@Autowired
	private DistanceOrderBookRepositoryImpl repoImpl;
	
	@Autowired
	private DistanceOrderBookRepository repo;	
	
	@Autowired
	private CexioApi cexioApi;
	
	@Autowired
	private BitfinexApi bitfinexApi;	
	
	@Autowired
	private CoinRepository coinRepo;
	
	@Autowired
	private ExchangeRepository exchangeRepo;
	
	public boolean execOrder(Coin coin) {
		BigDecimal percentLimit = new BigDecimal("0.15");
		int TIMES_TO_EXEC = 4;
		List<DistanceOrderBook> distances = repoImpl.getLastest(TIMES_TO_EXEC, coin.getId());
		int times = 0;
		for (DistanceOrderBook distanceOrderBook : distances) {
			if(distanceOrderBook.getPercent().compareTo(percentLimit) <= 0) {
				times++;
			}
		}
		return times == TIMES_TO_EXEC;
	}
	
	public void update() {
		BigDecimal marginPercent = new BigDecimal("0.01");
		
		Iterable<Coin> coins = coinRepo.findAll();
//		List<Ticker> tickers = cexioApi.getTickers("USD");
		for (Coin coin : coins) {
//			Ticker ticker = null;
//			for (Ticker t : tickers) {
//				if(t.getPair().toUpperCase().contains(coin.getName().toUpperCase())) {
//					ticker = t;
//					break;
//				}
//			}
//			if(ticker == null) {
//				continue;
//			}
			
			
			BigDecimal usdToUse = new BigDecimal("600.00"); //TODO
			
			OrderBook orderBookCexio = cexioApi.getOrderBook(coin.getPath());
			OrderBook orderBookBitfinex =  bitfinexApi.getOrderBook(coin.getPath());
			OrderBook[] orderBooks = new OrderBook[] {orderBookCexio, orderBookBitfinex};
			
			for (int i = 0; i < orderBooks.length; i++) {
				if(orderBooks[i] == null || orderBooks[i].getAsks() == null || orderBooks[i].getBids() == null) {
					continue;
				}
				
				BigDecimal targetValueToSell = orderBooks[i].getAsks()[0][0].multiply(marginPercent).add(orderBooks[i].getAsks()[0][0]);
				BigDecimal valueToPay = orderBooks[i].getBids()[0][0]; //TODO build a rule for get this valueToPay from OrderBook rule
				BigDecimal amountToBuy = usdToUse.divide(valueToPay, 6, RoundingMode.HALF_UP);
				
//			StringBuilder analyze = new StringBuilder(); se precisar utilizar o debug logger.
				BigDecimal[][] asks = orderBooks[i].getAsks();
				BigDecimal valueAsk = BigDecimal.ZERO;
				BigDecimal sumAmountAsk = BigDecimal.ZERO;
//			analyze.append(String.format("Analyze %s at target %s with amount %s \n", coin.getName(), targetValueToSell.toString(), amountToBuy.toString()));
				int countAsk = 0;
				for (BigDecimal[] ask : asks) {
					if(targetValueToSell.subtract(ask[0]).signum() == -1) {
//					analyze.append(String.format("askM%d ; %s ; %s ; %s \n", countAsk, targetValueToSell.toString(), targetValueToSell.multiply(amountToBuy), amountToBuy));
						sumAmountAsk = sumAmountAsk.add(amountToBuy); //adicionando o meu montante q vou comprar.. computar para poder vender ele tbm.
						valueAsk = valueAsk.add(targetValueToSell.multiply(amountToBuy)); //idem com a linha de cima
//					analyze.append(String.format("ask result: %s ; %s \n", valueAsk.toString(), sumAmountAsk.toString()));
						break;
					}
					countAsk++;
//				analyze.append(String.format("ask%d ; %s ; %s ; %s \n", countAsk, ask[0].toString(), ask[0].multiply(ask[1]), ask[1].toString()));
					valueAsk = valueAsk.add(ask[0].multiply(ask[1]));
					sumAmountAsk = sumAmountAsk.add(ask[1]);
				}
				BigDecimal[][] bids = orderBooks[i].getBids();
				BigDecimal valueBid = BigDecimal.ZERO;
				BigDecimal sumAmountBid = BigDecimal.ZERO;
				int countBid = 0;
				for (BigDecimal[] bid : bids) {
					countBid++;
					
					if(sumAmountBid.add(bid[1]).compareTo(sumAmountAsk) >= 0) { //pegar o restante para chegar na mesma quantidade do ask
						BigDecimal remaining = sumAmountAsk.subtract(sumAmountBid);
						BigDecimal thisBidValue = bid[0].multiply(bid[1]);
						//regra de 3 para saber o valor proporcinal restante....
						BigDecimal finalValue = thisBidValue.multiply(remaining).divide(bid[1], 6, RoundingMode.HALF_UP);
//					analyze.append(String.format("bidM%d ; %s ; %s ; %s \n", countBid, bid[0].toString(), finalValue, remaining));
						sumAmountBid = sumAmountBid.add(remaining);
						valueBid = valueBid.add(finalValue);
//					analyze.append(String.format("bid result: %s ; %s \n", valueBid.toString(), sumAmountBid.toString()));
						break;
					}
//				analyze.append(String.format("bid%d ; %s ; %s ; %s \n", countBid, bid[0].toString(), bid[0].multiply(bid[1]), bid[1].toString()));
					valueBid = valueBid.add(bid[0].multiply(bid[1]));
					sumAmountBid = sumAmountBid.add(bid[1]);
				}
				BigDecimal hundred = new BigDecimal("100");
				BigDecimal percent = valueAsk.divide(valueBid, 4, RoundingMode.HALF_UP).multiply(hundred).subtract(hundred);
				
				DistanceOrderBook distance = new DistanceOrderBook();
				distance.setAmount(sumAmountAsk);
				distance.setCoin(coin);
				distance.setDistanceAsk(countAsk);
				distance.setDistanceBid(countBid);
				distance.setPercent(percent);
				distance.setValueAsk(valueAsk);
				distance.setValueBid(valueBid);
				distance.setTargetValue(targetValueToSell);
				
				distance.setActualAsk(orderBooks[i].getAsks()[0][0]);
				distance.setActualBid(orderBooks[i].getBids()[0][0]);
				distance.setDate(new Date());

				Exchange exchange = exchangeRepo.findOne(i + 1);
				distance.setExchange(exchange);
				
				repo.save(distance);
			}
			
					
		}
	}
	
}