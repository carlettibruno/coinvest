package br.com.carlettisolucoes.coinvest.v2;

import java.io.Serializable;
import java.math.BigDecimal;

public class KeyPair implements Serializable {
	
	private static final long serialVersionUID = -655759295192793015L;

	private String keyPair;
	
	private BigDecimal value;

	public String getKeyPair() {
		return keyPair;
	}

	public void setKeyPair(String keyPair) {
		this.keyPair = keyPair;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "KeyPair [keyPair=" + keyPair + ", value=" + value + "]";
	}

}