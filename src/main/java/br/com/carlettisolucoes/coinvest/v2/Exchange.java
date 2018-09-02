package br.com.carlettisolucoes.coinvest.v2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.spi.JsonException;

import br.com.carlettisolucoes.coinvest.rule.ByOrderBook;

public abstract class Exchange {
	
	private static final Logger log = LoggerFactory.getLogger(ByOrderBook.class);
	
	private static HttpClient getHttpClient() {
		int timeout = 3;
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000)
				.setSocketTimeout(timeout * 1000).build();
		return HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}
	
	public void recordMarket(Long timemillis) {
		try {
			File dir = new File(String.format("/coins/%s/", getName()));
			dir.mkdirs();
			OutputStream os = new FileOutputStream(dir.getPath() + File.separator + timemillis.toString() + ".txt");
			recordMarket(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<TradeCoin> getTradeElegible(Map<Exchange, List<InfoCoin>> exchangesInfos) {
		List<TradeCoin> trades = new ArrayList<>();
		Date datenow = new Date();
		List<InfoCoin> infos = exchangesInfos.get(this);
		exchangesInfos.forEach((exchangeCompare,exchangeInfos)->{
			if(this.equals(exchangeCompare)) {
				return;
			}
			infos.stream().filter(info->info.getLastPrice().compareTo(getMinValueToTrade()) >= 0).forEach(info->{
				exchangeInfos.stream()
				.filter(
						infoAux->infoAux.getCoin().equals(info.getCoin())
						).forEach(
								infoAux->{
									BigDecimal res = (infoAux.getLastPrice().divide(info.getLastPrice(), RoundingMode.HALF_UP)).multiply(new BigDecimal("100"));
									res = res.subtract(new BigDecimal("100"));
									if(res.compareTo(BigDecimal.ONE) == 1 
											&& res.compareTo(new BigDecimal("7")) == -1
											&& exchangeCompare.getVolume(infoAux).compareTo(this.getVolume(info)) == 1
											&& exchangeCompare.getVolume(infoAux).compareTo(new BigDecimal("500")) == 1
											&& this.getVolume(info).compareTo(new BigDecimal("100")) == 1 
											&& BigDecimal.ONE.subtract(infoAux.getBid().divide(infoAux.getAsk(), RoundingMode.HALF_UP)).compareTo(new BigDecimal("0.005")) == -1
											&& BigDecimal.ONE.subtract(info.getBid().divide(info.getAsk(), RoundingMode.HALF_UP)).compareTo(new BigDecimal("0.005")) == -1 ) {
										TradeCoin tc = new TradeCoin();
										tc.setBuyInfo(info);
										tc.setSellInfo(infoAux);
										tc.setDate(datenow);
										tc.setExchangeBuy(this);
										tc.setExchangeReference(exchangeCompare);
										tc.setPercent(res);
										trades.add(tc);
									}
								});
			});
		});
		return trades;
	}
	
	private BigDecimal getMinValueToTrade() {
		return new BigDecimal("0.000001");
	}
	
	public void recordMarket(OutputStream os) throws IOException {
		try {
			HttpClient client = getHttpClient();
			String url = getApi() + getPathMarket();
			HttpGet getRequest = new HttpGet(url);
//			log.info("Recording market {}", url);
			HttpResponse response = client.execute(getRequest);
			boolean error = response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299;
			InputStream is = response.getEntity().getContent();
			if(!error) {
				IOUtils.copy(is, os);
			}
			is.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	protected String getJustCoin(String keypair) {
		Pattern pattern = Pattern.compile("[A-Z]{2,5}");
		Matcher matcher = pattern.matcher(keypair.toUpperCase().replace("BTC", ""));
		if(matcher.find())
			return matcher.group();
		return "";
	}
	
	protected BigDecimal getBigDecimal(String val) {
		if(null == val || "null".equals(val) || val.isEmpty()) {
			return BigDecimal.ZERO;
		}
		try {
			return new BigDecimal(val);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return BigDecimal.ZERO;
		}
	}
	
	public BigDecimal getVolume(InfoCoin infoCoin) {
		return infoCoin.getVolume().multiply(infoCoin.getLastPrice());
	}
	
	public List<InfoCoin> getInfo(InputStream is) throws IOException, JsonException {
		List<InfoCoin> infoCoins = new ArrayList<>();
		Any any = JsonIterator.deserialize(IOUtils.toByteArray(is));
		any.get(getPathToReachList()).asList().forEach(coin->{
			String coinStr = coin.get(getTokenSymbol()).toString();
			if(!coinStr.toUpperCase().contains("BTC")) {
				return;
			}
			String token = getJustCoin(coinStr);
			if(null == token || token.isEmpty()) {
				return;
			}
			BigDecimal lastPrice = getBigDecimal(coin.get(getTokenLastPrice()).toString());
			BigDecimal askPrice = getBigDecimal(coin.get(getTokenAskPrice()).toString());
			BigDecimal bidPrice = getBigDecimal(coin.get(getTokenBidPrice()).toString());
			BigDecimal volume = getBigDecimal(coin.get(getTokenVolume()).toString());
			infoCoins.add(new InfoCoin(token, lastPrice, volume, bidPrice, askPrice));
		});
		return infoCoins;
	}
	
	abstract String getApi();
	
	abstract String getPathMarket();
	
	abstract String getName();
	
	abstract String getTokenSymbol();
	
	abstract String getTokenLastPrice();
	
	abstract String getTokenAskPrice();
	
	abstract String getTokenBidPrice();
	
	abstract String getTokenVolume();
	
	abstract Object[] getPathToReachList();
	
	abstract boolean readyToTrade();
	
	public File getMarketFile(String name) {
		return new File(String.format("/coins/%s/%s", getName(), name));
	}
	
	public File getDirectory() {
		return new File(String.format("/coins/%s/", getName()));
	}
	
	abstract List<String> getCoinsPairBTC();
	
//	abstract List<InfoCoin> getInfo(InputStream is) throws IOException;

}