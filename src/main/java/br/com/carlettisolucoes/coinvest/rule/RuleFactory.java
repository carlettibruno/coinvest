package br.com.carlettisolucoes.coinvest.rule;

public class RuleFactory {
	
	public static RuleExecution getInstance(String uri) {
		return new ByUpDown();
	}

}
