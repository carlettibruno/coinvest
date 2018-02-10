package br.com.carlettisolucoes.coinvest.distanceorderbook;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class DistanceOrderBookRepositoryImpl {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<DistanceOrderBook> getLastest(Integer amount, Integer coinId) {
		Query query = this.entityManager.createQuery("SELECT d FROM DistanceOrderBook d WHERE d.coin.id = :coinId ORDER BY d.id DESC");
		query.setParameter("coinId", coinId);
		query.setMaxResults(amount);
		List<DistanceOrderBook> distances = query.getResultList();
		return distances;
	}

}
