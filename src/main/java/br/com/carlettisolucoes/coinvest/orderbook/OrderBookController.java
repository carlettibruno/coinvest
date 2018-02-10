package br.com.carlettisolucoes.coinvest.orderbook;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlettisolucoes.coinvest.cexio.OrderBook;

@RestController
@RequestMapping("/orderbook")
public class OrderBookController {

//	@Autowired
//	private ExchangeRepository exchangeRepo;
//	
//	@Autowired
//	private CoinRepository coinRepo;
	
	@Autowired
	private OrderBookHistoryRepositoryImpl orderBookHistoryRepoImpl;
	
	@GetMapping("/chart/{exchange}/{coin}/{date}")
	public ResponseEntity<OrderBook> getOrderBook(@PathVariable("exchange") String exchange, @PathVariable("coin") String coin, @PathVariable("date") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")  Date date) {
		return new ResponseEntity<OrderBook>(orderBookHistoryRepoImpl.getOrderBook(exchange, coin, date), HttpStatus.OK);
	}
	
}
