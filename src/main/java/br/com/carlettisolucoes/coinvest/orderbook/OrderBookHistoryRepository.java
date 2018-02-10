package br.com.carlettisolucoes.coinvest.orderbook;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderBookHistoryRepository extends CrudRepository<OrderBookHistory, Long> {

}
