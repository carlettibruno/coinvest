package br.com.carlettisolucoes.coinvest.v2;

import java.math.BigDecimal;
import java.util.List;

public class Huobi extends Exchange {

	@Override
	String getApi() {
		return "https://api.huobi.pro";
	}

	@Override
	String getPathMarket() {
		return "/market/tickers";
	}

	@Override
	String getName() {
		return "huobi";
	}

	@Override
	String getTokenSymbol() {
		return "symbol";
	}

	@Override
	String getTokenLastPrice() {
		return "low";
	}

	@Override
	String getTokenAskPrice() {
		return "high";
	}

	@Override
	String getTokenBidPrice() {
		return "low";
	}

	@Override
	String getTokenVolume() {
		return "vol";
	}

	@Override
	Object[] getPathToReachList() {
		return new Object[] {"data"};
	}

	@Override
	public BigDecimal getVolume(InfoCoin infoCoin) {
		return infoCoin.getVolume();
	}
	
	@Override
	boolean readyToTrade() {
		return false;
	}

	@Override
	List<String> getCoinsPairBTC() {
		return null;
	}

}
