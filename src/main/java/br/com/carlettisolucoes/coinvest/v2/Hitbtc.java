package br.com.carlettisolucoes.coinvest.v2;

import java.util.List;

public class Hitbtc extends Exchange {

	public String getApi() {
		return "https://api.hitbtc.com/api/2";
	}
	
	@Override
	String getPathMarket() {
		return "/public/ticker";
	}

	@Override
	String getName() {
		return "hitbtc";
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
		return "last";
	}

	@Override
	String getTokenAskPrice() {
		return "ask";
	}

	@Override
	String getTokenBidPrice() {
		return "bid";
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