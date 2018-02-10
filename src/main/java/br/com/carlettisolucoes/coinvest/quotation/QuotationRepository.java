package br.com.carlettisolucoes.coinvest.quotation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepository extends CrudRepository<Quotation, Long> {
	
}