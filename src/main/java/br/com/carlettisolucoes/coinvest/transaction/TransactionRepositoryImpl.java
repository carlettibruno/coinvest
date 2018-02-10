//package br.com.carlettisolucoes.coinvest.transaction;
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class TransactionRepositoryImpl {
//
//	@PersistenceContext
//	private EntityManager entityManager;
//	
//	@SuppressWarnings("unchecked")
//	public List<Transaction> getLatest(Integer amount, Integer walletId) {
//		Query query = entityManager.createQuery("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId ORDER BY t.id DESC");
//		query.setParameter("walletId", walletId);
//		query.setMaxResults(amount);
//		List<Transaction> transactions = query.getResultList();
//		return transactions;
//	}
//	
//}