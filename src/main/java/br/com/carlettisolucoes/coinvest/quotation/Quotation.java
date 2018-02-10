package br.com.carlettisolucoes.coinvest.quotation;

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

import br.com.carlettisolucoes.coinvest.coin.Coin;

@Entity
@Table(name="quotation")
@SequenceGenerator(name="seq", sequenceName="quotation_seq", allocationSize=1, initialValue=1)
public class Quotation implements Serializable {

	private static final long serialVersionUID = -2926201893632049805L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name="id")
	private Long id;
	
	@Column(name="datetime", nullable=false)
	private Date datetime;
	
	@Column(name="value", nullable=false, precision=16, scale=6)
	private BigDecimal value;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="coin_id", referencedColumnName="id")
	private Coin coin;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="previous_quotation_id", referencedColumnName="id")	
	private Quotation previousQuotation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Cotation [id=" + id + ", datetime=" + datetime + ", value=" + value + "]";
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
		Quotation other = (Quotation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Coin getCoin() {
		return coin;
	}

	public void setCoin(Coin coin) {
		this.coin = coin;
	}

	public Quotation getPreviousQuotation() {
		return previousQuotation;
	}

	public void setPreviousQuotation(Quotation previousQuotation) {
		this.previousQuotation = previousQuotation;
	}
	
}
