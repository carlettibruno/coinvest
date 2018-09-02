package br.com.carlettisolucoes.coinvest.v2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinvestApp {

	static List<Exchange> exchanges = new ArrayList<Exchange>();

	static {
		exchanges.add(new Poloniex());
		exchanges.add(new Bittrex());
		exchanges.add(new Binance());
		exchanges.add(new KuCoin());
		exchanges.add(new Hitbtc());
		exchanges.add(new Okex());
		exchanges.add(new Huobi());
	}

	public static void main(String[] args) {
		while(true) {
			Map<Exchange, List<InfoCoin>> exchangesInfos = readExchangesInfos();
	//		exchanges.stream().filter(exchange->exchange.readyToTrade()).forEach(exchange->{
			exchanges.stream().forEach(exchange->{
				try {
					List<TradeCoin> trades = exchange.getTradeElegible(exchangesInfos);
					
					Collections.sort(trades, new Comparator<TradeCoin>() {
						@Override
						public int compare(TradeCoin o1, TradeCoin o2) {
							return o1.getExchangeReference().getVolume(o1.getSellInfo()).compareTo(o2.getExchangeReference().getVolume(o2.getSellInfo()));
						}
					});
					TradeCoin trade = getMostImportant(trades);
					if(trade != null) {
						System.out.println(trade);
					}
	//				Order order = exchange.getActiveOrder();
	//				if((order.isBuy() && isOutOfValue(trade, order)) 
	//						|| order.isSell() && hasRisk(exchangesInfos, order)) {
	//					
	//					cancelOrder(order);
	//				}
	//				
	//				if(order == null) {
	//					exchange.makeTrade(trade);
	//				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Map<Exchange, List<InfoCoin>> readExchangesInfos() {
		Map<Exchange, List<InfoCoin>> exchangesInfos = new HashMap<>();
		for (Exchange exchange : exchanges) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				exchange.recordMarket(baos);
				List<InfoCoin> infoCoins = exchange.getInfo(new ByteArrayInputStream(baos.toByteArray()));
				exchangesInfos.put(exchange, infoCoins);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return exchangesInfos;
	}
	
	private static TradeCoin getMostImportant(List<TradeCoin> trades) {
		TradeCoin bestTrade = null;
		for (TradeCoin t : trades) {
			if(bestTrade == null || t.getExchangeReference().getVolume(t.getSellInfo()).compareTo(t.getExchangeReference().getVolume(bestTrade.getSellInfo())) == 1) {
				bestTrade = t;
			}
		}
		return bestTrade;
	}

//	private static void compareMarket() throws IOException {
//		int total = 0;
//		int error = 0;
//		Exchange exchangeBase = exchanges.get(0);
//		File[] files = exchangeBase.getDirectory().listFiles();
//		List<TradeCoin> possibleTrades = new ArrayList<>();
//		for (File filebase : files) {
//			String filename = filebase.getName();
//			Map<Exchange, List<InfoCoin>> exchangesInfos = new HashMap<>();
//			for (Exchange exchange : exchanges) {
//				File f = exchange.getMarketFile(filename);
//				if(null != f && f.exists()) {
//					FileInputStream fis = new FileInputStream(f);
//					try {
//						total++;
//						List<InfoCoin> infoCoins = exchange.getInfo(fis);
//						exchangesInfos.put(exchange, infoCoins);
//					} catch (JsonException e) {
//						error++;
//						e.printStackTrace();
//					} catch (Exception e) {
//						error++;
//						e.printStackTrace();
//					}
//					IOUtils.closeQuietly(fis);
//				}
//			}
//			
//			List<TradeCoin> tradesDate = new ArrayList<>();
//			exchangesInfos.forEach((ex,infos)->{
//				exchangesInfos.forEach((exother,infosother)->{
//					if(exother.equals(ex)) {
//						return;
//					}
//					Calendar calendar = Calendar.getInstance();
//					calendar.setTimeInMillis(Long.valueOf(filename.replace(".txt", "")));
////					System.out.println(String.format("Comparing at %s exchanges: %s and %s", calendar.getTime(), ex.getName(), exother.getName()));
//					infos.stream().filter(info->info.getLastPrice().compareTo(BigDecimal.ZERO) == 1).forEach(info->{
//						infosother.stream()
//						.filter(
//								infoAux->infoAux.getCoin().equals(info.getCoin())
//								).forEach(
//										infoAux->{
//											BigDecimal res = (infoAux.getLastPrice().divide(info.getLastPrice(), RoundingMode.HALF_UP)).multiply(new BigDecimal("100"));
//											res = res.subtract(new BigDecimal("100"));
////											res = res.signum() == -1 ? res.multiply(new BigDecimal("-1")) : res;
//											if(res.compareTo(BigDecimal.ONE) == 1 
//													&& res.compareTo(new BigDecimal("7")) == -1
//													&& infoAux.getLastPrice().compareTo(new BigDecimal("0.000001")) >= 0
//													&& exother.getVolume(infoAux).compareTo(ex.getVolume(info)) == 1
//													
//													&& ex.getName().equals("bittrex")
//													) {
////												System.out.println(String.format("DIFF is %s - Comparing %s to %s", res.toString(), info.toString(), infoAux.toString()));
//												TradeCoin tc = new TradeCoin();
//												tc.setBuyInfo(info);
//												tc.setSellInfo(infoAux);
//												tc.setDate(calendar.getTime());
//												tc.setExchangeBuy(ex);
//												tc.setExchangeReference(exother);
//												tc.setPercent(res);
//												tradesDate.add(tc);
//											}
//										});
//					});
//				});
//			});
//			
//			TradeCoin bestTrade = null;
//			for (TradeCoin t : tradesDate) {
//				if(bestTrade == null || t.getExchangeReference().getVolume(t.getSellInfo()).compareTo(t.getExchangeReference().getVolume(bestTrade.getSellInfo())) == 1) {
//					bestTrade = t;
//				}
//			}
//			possibleTrades.add(bestTrade);
//		}
////		System.out.println(possibleTrades);
//		
//		Collections.sort(possibleTrades, new Comparator<TradeCoin>() {
//			@Override
//			public int compare(TradeCoin o1, TradeCoin o2) {
//				if(o1 == null) {
//					return 1;
//				}
//				if(o2 == null) {
//					return -1;
//				}
//				return o1.getDate().compareTo(o2.getDate());
//			}
//		});
//		for (TradeCoin tradeCoin : possibleTrades) {
//			System.out.println(tradeCoin);
//		}
//		
//		System.out.println(String.format("Total %d de %d com erro", total, error));
//	}
//
//	private static void readMarket() {
//		while(true) {
//			long timemillis = System.currentTimeMillis();
//			for (Exchange exchange : exchanges) {
//				exchange.recordMarket(timemillis);
//			}
//
//			try {
//				Thread.sleep(300000l);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}

}
