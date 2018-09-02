package br.com.carlettisolucoes.coinvest.v2;

import java.math.BigDecimal;
import java.util.List;

public class KuCoin extends Exchange {

	public String getApi() {
		return "https://api.kucoin.com/v1";
	}
	
	@Override
	String getPathMarket() {
		return "/market/open/symbols";
	}

	@Override
	String getName() {
		return "kucoin";
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
		return "lastDealPrice";
	}

	@Override
	String getTokenAskPrice() {
		return "sell";
	}

	@Override
	String getTokenBidPrice() {
		return "buy";
	}

	@Override
	String getTokenVolume() {
		return "volValue";
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
	
}