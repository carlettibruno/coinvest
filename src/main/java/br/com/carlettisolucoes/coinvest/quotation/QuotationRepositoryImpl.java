package br.com.carlettisolucoes.coinvest.quotation;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class QuotationRepositoryImpl {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Quotation> getLatest(Integer amount, Integer coinId) {
		Query query = entityManager.createQuery("SELECT q FROM Quotation q WHERE q.coin.id = :coinId ORDER BY q.id DESC");
		query.setParameter("coinId", coinId);
		query.setMaxResults(amount);
		List<Quotation> quotations = query.getResultList();
		return quotations;
	}
	
	@SuppressWarnings("unchecked")
	public BigDecimal[][] getHistory(Integer precision, Integer amount, Integer coinId) {
		StringBuilder hql = new StringBuilder("SELECT q1.value ");
		for (int i = 2; i <= precision; i++) {
			hql.append(String.format(", q%d.value ", i));
		}
		hql.append("FROM Quotation q1 ");
		for (int i = 2; i <= precision; i++) {
			hql.append(String.format("JOIN q%d.previousQuotation q%d ", i - 1, i));
		}
		hql.append("WHERE q1.coin.id = :coinId ");
		hql.append("ORDER BY 1 DESC ");
		Query query = entityManager.createQuery(hql.toString());
		query.setParameter("coinId", coinId);
		query.setMaxResults(amount);
		List<Object[]> objects = query.getResultList();
		BigDecimal[][] numbers = new BigDecimal[objects.size()][];
		for (int i = 0; i < objects.size(); i++) {
			Object[] objsLine = objects.get(i);
			numbers[i] = new BigDecimal[objsLine.length];
			for (int j = 0; j < objsLine.length; j++) {
				numbers[i][j] = (BigDecimal) objsLine[j];
			}
		}
		return numbers;
	}

}
