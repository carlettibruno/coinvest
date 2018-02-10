package br.com.carlettisolucoes.coinvest.distanceorderbook;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistanceOrderBookRepository extends CrudRepository<DistanceOrderBook, Long>  {

}