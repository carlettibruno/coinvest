package br.com.carlettisolucoes.coinvest.v2;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.spi.JsonException;

public class Poloniex extends Exchange {

	public String getApi() {
		return "https://poloniex.com/public";
	}
	
	@Override
	String getPathMarket() {
		return "?command=returnTicker";
	}

	@Override
	String getName() {
		return "poloniex";
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
		return "lowestAsk";
	}

	@Override
	String getTokenBidPrice() {
		return "highestBid";
	}

	@Override
	String getTokenVolume() {
		return "quoteVolume";
	}
	
	@Override
	Object[] getPathToReachList() {
		return new Object[0];
	}
	
	@Override
	public List<InfoCoin> getInfo(InputStream is) throws IOException, JsonException {
		List<InfoCoin> infoCoins = new ArrayList<>();
		Any any = JsonIterator.deserialize(IOUtils.toByteArray(is));
		any.asMap().forEach((k,v)->{
			if(!k.contains("BTC")) {
				return;
			}	
			String token = getJustCoin(k);
			BigDecimal lastPrice = getBigDecimal(v.get(getTokenLastPrice()).toString());
			BigDecimal askPrice = getBigDecimal(v.get(getTokenAskPrice()).toString());
			BigDecimal bidPrice = getBigDecimal(v.get(getTokenBidPrice()).toString());
			BigDecimal volume = getBigDecimal(v.get(getTokenVolume()).toString());
			infoCoins.add(new InfoCoin(token, lastPrice, volume, bidPrice, askPrice));			
		});
		return infoCoins;
	}

	@Override
	boolean readyToTrade() {
		return false;
	}
	
}