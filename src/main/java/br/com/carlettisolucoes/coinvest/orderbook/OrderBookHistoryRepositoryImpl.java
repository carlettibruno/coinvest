package br.com.carlettisolucoes.coinvest.orderbook;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import br.com.carlettisolucoes.coinvest.bitfinex.BitfinexApi;
import br.com.carlettisolucoes.coinvest.cexio.OrderBook;

@Repository
public class OrderBookHistoryRepositoryImpl {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private BitfinexApi bitfinexApi;
	
	public OrderBook getOrderBook(String exchange, String coin, Date date) {
		Query query = this.entityManager.createQuery("SELECT obh FROM OrderBookHistory obh JOIN obh.coin c JOIN obh.exchange e WHERE UPPER(c.path) like :path AND e.name = :name AND obh.date >= :date ORDER BY obh.id");
		query.setParameter("path", "%"+coin.toUpperCase()+"%");
		query.setParameter("name", exchange);
		query.setParameter("date", date);
		query.setMaxResults(1);
		OrderBookHistory obh = (OrderBookHistory) query.getSingleResult();
		OrderBook ob = null;
		Gson gson = new Gson();
		if(obh.getExchange().getName().equals("cexio")) {
			ob = gson.fromJson(obh.getOrdersJson(), OrderBook.class);			
		} else {
			br.com.carlettisolucoes.coinvest.bitfinex.OrderBook obBitfinex = gson.fromJson(obh.getOrdersJson(), br.com.carlettisolucoes.coinvest.bitfinex.OrderBook.class);			
			ob = bitfinexApi.toOrderBookDefault(obBitfinex);
		}
		return ob;
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderBookHistory> getOrderBookPending(Integer exchangeId, Integer coinId, Integer amount) {
		Query query = this.entityManager.createQuery("SELECT obh FROM OrderBookHistory obh WHERE obh.exchange.id = :exchangeId AND obh.coin.id = :coinId AND obh.percent IS NULL ORDER BY obh.id");
		query.setParameter("exchangeId", exchangeId);
		query.setParameter("coinId", coinId);
		query.setMaxResults(amount);
		List<OrderBookHistory> orders = query.getResultList();
		return orders;
	}
	
	@Transactional
	public void updateOrderBookPending(Long id, BigDecimal percent) {
		Query query = this.entityManager.createQuery("UPDATE OrderBookHistory obh SET obh.percent = :percent WHERE obh.id = :id");
		query.setParameter("percent", percent);
		query.setParameter("id", id);
		query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderBookHistory> getGoodHistories(Integer exchangeId, BigDecimal minPercent) {
		Query query = this.entityManager.createQuery("SELECT obh FROM OrderBookHistory obh WHERE obh.exchange.id = :exchangeId AND obh.percent >= :percent ORDER BY obh.percent DESC");
		query.setParameter("exchangeId", exchangeId);
		query.setParameter("percent", minPercent);
		List<OrderBookHistory> obhs = query.getResultList();
		return obhs;
	}

}