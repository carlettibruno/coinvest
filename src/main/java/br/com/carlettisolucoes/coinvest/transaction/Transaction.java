package br.com.carlettisolucoes.coinvest.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.carlettisolucoes.coinvest.quotation.Quotation;
import br.com.carlettisolucoes.coinvest.wallet.Wallet;

@Entity
@Table(name="transaction")
@SequenceGenerator(name="seq", sequenceName="transaction_seq")
public class Transaction implements Serializable {

	private static final long serialVersionUID = 3644505536806685568L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name="id")
	private Long id;
	
	@Column(name="value", nullable=false, precision=16, scale=6)
	private BigDecimal value;
	
	@Column(name="usd", nullable=false, precision=16, scale=6)
	private BigDecimal usd;	
	
	@Column(name="datetime", nullable=false)
	private Date datetime;
	
	@Column(name="type", nullable=false)
	private TransactionType type;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="wallet_id", referencedColumnName="id")
	private Wallet wallet;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="quotation_id", referencedColumnName="id")
	private Quotation quotation;
	
	@Column(name="order_id")
	private Long orderId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", value=" + value + ", datetime=" + datetime + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public BigDecimal getUsd() {
		return usd;
	}

	public void setUsd(BigDecimal usd) {
		this.usd = usd;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

}
