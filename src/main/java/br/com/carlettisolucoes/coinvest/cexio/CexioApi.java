package br.com.carlettisolucoes.coinvest.cexio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import br.com.carlettisolucoes.coinvest.account.Account;

@Component
public class CexioApi {

	private static final Logger log = LoggerFactory.getLogger(CexioApi.class);

	private static final String URL_BASE = "https://cex.io/api";

	public List<Ticker> getTickers(String symbol1) {
		HttpClient http = getHttpClient();
		HttpGet getRequest = new HttpGet(URL_BASE + "/tickers/" + symbol1);
		try {
			HttpResponse response = http.execute(getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("Failed Tickers {} : HTTP error code : {}", symbol1, response.getStatusLine().getStatusCode());
				return null;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output = br.readLine();
			Gson gson = new Gson();
			TickersResponse tickersResponse = gson.fromJson(output, TickersResponse.class);
			return tickersResponse.getData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<Ticker>();
	}
	
	public Ticker getTicker(String path) {
		HttpClient http = getHttpClient();
		HttpGet getRequest = new HttpGet(URL_BASE + "/ticker" + path);
		try {
			HttpResponse response = http.execute(getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("Failed Tickers {} : HTTP error code : {}", path, response.getStatusLine().getStatusCode());
				return null;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output = br.readLine();
			Gson gson = new Gson();
			Ticker ticker = gson.fromJson(output, Ticker.class);
			return ticker;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public OrderBook getOrderBook(String path) {
		HttpClient http = getHttpClient();
		HttpGet getRequest = new HttpGet(URL_BASE + "/order_book" + path);
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
			return orderBook;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getOrderBookJson(String path) {
		HttpClient http = getHttpClient();
		HttpGet getRequest = new HttpGet(URL_BASE + "/order_book" + path);
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

	public LastPrice getLastPrice(String path) {
		HttpClient http = getHttpClient();
		HttpGet getRequest = new HttpGet(URL_BASE + "/last_price" + path);
		try {
			HttpResponse response = http.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("Failed {} : HTTP error code : {}", path, response.getStatusLine().getStatusCode());
				return null;
			}

			// Get-Capture Complete application/xml body response
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			//			System.out.println("============Output:============");

			// Simply iterate through XML response and show on console.
			output = br.readLine();

			Gson gson = new Gson();
			LastPrice lastPrice = gson.fromJson(output, LastPrice.class);
			return lastPrice;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public AccountBalance getAccountBalance(Account account) throws Exception {
		Long nonce = System.currentTimeMillis();
		HttpClient http = getHttpClient();
		HttpPost postRequest = new HttpPost(URL_BASE + "/balance/");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("key", account.getApiKey()));
		params.add(new BasicNameValuePair("signature", getSignature(nonce, account)));
		params.add(new BasicNameValuePair("nonce", nonce.toString()));
		try {
			postRequest.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			HttpResponse response = http.execute(postRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("Failed {} : HTTP error code : {}", account, response.getStatusLine().getStatusCode());
				return null;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			output = br.readLine();
			Gson gson = new Gson();
			AccountBalance accountBalance = gson.fromJson(output, AccountBalance.class);
			if(accountBalance == null || accountBalance.getUsername() == null) {
				log.warn("Error getting accountBalance: {}", output);
				throw new Exception("ERROR_GETTING_ACCOUNT_BALANCE");
			}
			return accountBalance;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}

	public PlaceOrderResponse placeOrder(PlaceOrder placeOrder, Account account) {
		Long nonce = System.currentTimeMillis();
		HttpClient http = getHttpClient();
		HttpPost post = new HttpPost(URL_BASE + "/place_order" + placeOrder.getWallet().getCoin().getPath());

		placeOrder.setKey(account.getApiKey());
		placeOrder.setSignature(getSignature(nonce, account));
		placeOrder.setNonce(nonce);
		try {
			post.setEntity(new StringEntity(new Gson().toJson(placeOrder)));
			post.setHeader("Content-type", "application/json");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			HttpResponse response = http.execute(post);

			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("Failed {} : HTTP error code : {}", placeOrder, response.getStatusLine().getStatusCode());
				return null;				
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			//			System.out.println("============Output:============");
			output = br.readLine();

			Gson gson = new Gson();
			PlaceOrderResponse placeOrderResponse = gson.fromJson(output, PlaceOrderResponse.class);
			if(placeOrderResponse.getId() == null) {
				placeOrderResponse = null;
				log.error(output);
			}
			return placeOrderResponse;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}

	public ActiveOrderStatusResponse getActiveOrderStatus(List<Long> ordersId, Account account) {
		Long nonce = System.currentTimeMillis();
		HttpClient http = getHttpClient();
		HttpPost post = new HttpPost(URL_BASE + "/active_orders_status");

		ActiveOrderStatusRequest request = new ActiveOrderStatusRequest();
		request.setOrders_list(ordersId);
		request.setKey(account.getApiKey());
		request.setSignature(getSignature(nonce, account));
		request.setNonce(nonce);
		try {
			post.setEntity(new StringEntity(new Gson().toJson(request)));
			post.setHeader("Content-type", "application/json");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			HttpResponse response = http.execute(post);

			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("Failed {} : HTTP error code : {}", account, response.getStatusLine().getStatusCode());
				return null;		
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			output = br.readLine();

			Gson gson = new Gson();
			ActiveOrderStatusResponse activeOrderStatusResponse = gson.fromJson(output, ActiveOrderStatusResponse.class);
			return activeOrderStatusResponse;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}

	private String getSignature(Long nonce, Account account) {
		try {
			String message = nonce + account.getUsername() + account.getApiKey();
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(account.getSecretKey().getBytes("UTF-8"), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			String signature = Hex.encodeHexString(sha256_HMAC.doFinal(message.getBytes("UTF-8")));
			return signature;
		} catch (Exception e) {
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

}