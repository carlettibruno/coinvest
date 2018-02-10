package br.com.carlettisolucoes.coinvest.cexio;

import java.io.Serializable;

public class ResponseObject implements Serializable {

	private static final long serialVersionUID = -7619722064615553589L;
	
	private String e;
	
	private String ok;

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}

	@Override
	public String toString() {
		return "ResponseObject [e=" + e + ", ok=" + ok + "]";
	}

}
