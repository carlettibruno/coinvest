package br.com.carlettisolucoes.coinvest.v2;

import java.util.List;

public class Bittrex extends Exchange {

	public String getApi() {
		return "https://bittrex.com/api/v1.1";
	}
	
	@Override
	String getPathMarket() {
		return "/public/getmarketsummaries";
	}

	@Override
	String getName() {
		return "bittrex";
	}

	@Override
	List<String> getCoinsPairBTC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String getTokenSymbol() {
		return "MarketName";
	}

	@Override
	String getTokenLastPrice() {
		return "Last";
	}

	@Override
	String getTokenAskPrice() {
		return "Ask";
	}

	@Override
	String getTokenBidPrice() {
		return "Bid";
	}

	@Override
	String getTokenVolume() {
		return "Volume";
	}
	
	@Override
	Object[] getPathToReachList() {
		return new Object[] {"result"};
	}

	@Override
	boolean readyToTrade() {
		return false;
	}	
	
}