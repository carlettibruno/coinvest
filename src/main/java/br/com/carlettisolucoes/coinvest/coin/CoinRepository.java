package br.com.carlettisolucoes.coinvest.coin;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends CrudRepository<Coin, Integer> {

}