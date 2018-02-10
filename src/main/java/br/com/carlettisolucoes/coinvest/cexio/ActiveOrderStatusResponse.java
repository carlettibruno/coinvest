package br.com.carlettisolucoes.coinvest.cexio;

import java.math.BigDecimal;
import java.util.List;

public class ActiveOrderStatusResponse extends ResponseObject {

	private static final long serialVersionUID = 7320697371154594769L;
	
	private List<String[]> data;

	public List<String[]> getData() {
		return data;
	}

	public void setData(List<String[]> data) {
		this.data = data;
	}

	public boolean isCancelledOrExecuted(int indexOf) {
		return new BigDecimal(data.get(indexOf)[1]).compareTo(BigDecimal.ZERO) == 0 && new BigDecimal(data.get(indexOf)[2]).compareTo(BigDecimal.ZERO) == 0; 
	}
	
}
