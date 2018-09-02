package br.com.carlettisolucoes.coinvest.v2;

import java.util.List;

public class Binance extends Exchange {

	public String getApi() {
		return "https://api.binance.com/api/v1";
	}
	
	@Override
	String getPathMarket() {
		return "/ticker/24hr";
	}

	@Override
	String getName() {
		return "binance";
	}

	@Override
	List<String> getCoinsPairBTC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String getTokenSymbol() {
		return "symbol";
	}

	@Override
	String getTokenLastPrice() {
		return "lastPrice";
	}

	@Override
	String getTokenAskPrice() {
		return "askPrice";
	}

	@Override
	String getTokenBidPrice() {
		return "bidPrice";
	}

	@Override
	String getTokenVolume() {
		return "volume";
	}

	@Override
	Object[] getPathToReachList() {
		return new Object[0];
	}

	@Override
	boolean readyToTrade() {
		return false;
	}
	
}