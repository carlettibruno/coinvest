package br.com.carlettisolucoes.coinvest.cexio;

import java.io.Serializable;

public abstract class PrivateObject implements Serializable {

	private static final long serialVersionUID = 2582195055219347091L;
	
	private String key;
	
	private String signature;
	
	private Long nonce;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Long getNonce() {
		return nonce;
	}

	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

	@Override
	public String toString() {
		return "PrivateObject [key=" + key + ", signature=" + signature + ", nonce=" + nonce + "]";
	}

}
