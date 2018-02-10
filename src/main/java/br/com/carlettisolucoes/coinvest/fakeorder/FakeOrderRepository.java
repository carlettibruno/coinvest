package br.com.carlettisolucoes.coinvest.fakeorder;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FakeOrderRepository extends CrudRepository<FakeOrder, Integer> {

}
