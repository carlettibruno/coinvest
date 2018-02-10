package br.com.carlettisolucoes.coinvest.bitfinex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class BitfinexApi {

	private static final Logger log = LoggerFactory.getLogger(BitfinexApi.class);

	private static final String URL_BASE = "https://api.bitfinex.com/v1";
	
	public String getOrderBookJson(String path) {
		HttpClient http = getHttpClient();
		HttpGet getRequest = new HttpGet(URL_BASE + "/book/" + path.replaceAll("\\/", "").toLowerCase());
		try {
			HttpResponse response = http.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("Failed {} : HTTP error code : {}", path, response.getStatusLine().getStatusCode());
				return null;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output = br.readLine();
			br.close();
			return output;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public br.com.carlettisolucoes.coinvest.cexio.OrderBook getOrderBook(String path) {
		HttpClient http = getHttpClient();
		HttpGet getRequest = new HttpGet(URL_BASE + "/book/" + path.replaceAll("\\/", "").toLowerCase());
		try {
			HttpResponse response = http.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("Failed {} : HTTP error code : {}", path, response.getStatusLine().getStatusCode());
				return null;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output = br.readLine();
			Gson gson = new Gson();
			OrderBook orderBook = gson.fromJson(output, OrderBook.class);
			return toOrderBookDefault(orderBook);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static HttpClient getHttpClient() {
		int timeout = 3;
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000)
				.setSocketTimeout(timeout * 1000).build();
		return HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}
	
	public br.com.carlettisolucoes.coinvest.cexio.OrderBook toOrderBookDefault(OrderBook orderBook) {
		br.com.carlettisolucoes.coinvest.cexio.OrderBook orderBookDefault = new br.com.carlettisolucoes.coinvest.cexio.OrderBook();
		orderBookDefault.setAsks(new BigDecimal[orderBook.getAsks().size()][]);
		orderBookDefault.setBids(new BigDecimal[orderBook.getBids().size()][]);
		for (int i = 0; i < orderBook.getAsks().size(); i++) {
			orderBookDefault.getAsks()[i] = new BigDecimal[] {orderBook.getAsks().get(i).getPrice(), orderBook.getAsks().get(i).getAmount()};
		}
		for (int i = 0; i < orderBook.getBids().size(); i++) {
			orderBookDefault.getBids()[i] = new BigDecimal[] {orderBook.getBids().get(i).getPrice(), orderBook.getBids().get(i).getAmount()};
		}
		return orderBookDefault;
	}
	
}