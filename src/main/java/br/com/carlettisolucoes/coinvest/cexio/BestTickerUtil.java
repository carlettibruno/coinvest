package br.com.carlettisolucoes.coinvest.cexio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BestTickerUtil {

	private static Map<String, Integer> bestTickerTimes = new HashMap<String, Integer>();
	
	private static final Logger log = LoggerFactory.getLogger(BestTickerUtil.class);
	
	private static final int TIMES_TO_EXECUTE = 6;
	
	public static void eraseAll() {
		eraseAll("");
	}
	
	public static void eraseAll(String exceptKey) {
		Set<String> keys = bestTickerTimes.keySet();
		for (String key : keys) {
			if(!key.equals(exceptKey)) {
				bestTickerTimes.put(key, 0);
			}
		}
	}
	
	public static void add(String key) {
		if(bestTickerTimes.get(key) == null) {
			bestTickerTimes.put(key, 0);
		}
		bestTickerTimes.put(key, bestTickerTimes.get(key) + 1);
	}
	
	public static Ticker getBestTicker(List<Ticker> tickers) {
		Set<String> keys = bestTickerTimes.keySet();
		for (String key : keys) {
			boolean findTicker = false;
			for (Ticker ticker : tickers) {
				if(ticker.getPair().equals(key)) {
					findTicker = true;
				}
			}
			if(!findTicker) {
				bestTickerTimes.put(key, 0);
			}
		}		
		
		for (Ticker ticker : tickers) {
			if(bestTickerTimes.get(ticker.getPair()) != null && bestTickerTimes.get(ticker.getPair()) >= TIMES_TO_EXECUTE) {
				log.info("bestTicker: {}", ticker);
				return ticker;
			}
		}
		return null;
	}
	
}
