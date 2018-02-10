package br.com.carlettisolucoes.coinvest.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.carlettisolucoes.coinvest.distanceorderbook.DistanceOrderBookService;

@Component
public class RunDistanceOrderBook {

	@Autowired
	private DistanceOrderBookService service;
	
//	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void process() {
		service.update();
	}	
	
}
