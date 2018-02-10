package br.com.carlettisolucoes.coinvest.coin;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="COIN")
@SequenceGenerator(name="seq",sequenceName="coin_seq")
public class Coin implements Serializable {

	private static final long serialVersionUID = -3356623047817474686L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name="id")
	private Integer id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="path", nullable=false)
	private String path;
	
	@Column(name="max_usd")
	private BigDecimal maxUsd;
	
	@Column(name="min_usd")
	private BigDecimal minUsd;
	
	@Column(name="max_up")
	private BigDecimal maxUp;
	
	@Column(name="max_down")
	private BigDecimal maxDown;
	
	@Column(name="max_up_history")
	private BigDecimal maxUpHistory;
	
	@Column(name="max_down_history")
	private BigDecimal maxDownHistory;
	
	@Column(name="amount_minimum_buy", precision=16, scale=6)
	private BigDecimal amountMinimumBuy;
	
	@Column(name="price_precision")
	private Integer pricePrecision;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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
		Coin other = (Coin) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Coin [id=" + id + ", name=" + name + ", path=" + path + "]";
	}

	public BigDecimal getMaxUsd() {
		return maxUsd;
	}

	public void setMaxUsd(BigDecimal maxUsd) {
		this.maxUsd = maxUsd;
	}

	public BigDecimal getMinUsd() {
		return minUsd;
	}

	public void setMinUsd(BigDecimal minUsd) {
		this.minUsd = minUsd;
	}

	public BigDecimal getMaxUp() {
		return maxUp;
	}

	public void setMaxUp(BigDecimal maxUp) {
		this.maxUp = maxUp;
	}

	public BigDecimal getMaxDown() {
		return maxDown;
	}

	public void setMaxDown(BigDecimal maxDown) {
		this.maxDown = maxDown;
	}

	public BigDecimal getMaxUpHistory() {
		return maxUpHistory;
	}

	public void setMaxUpHistory(BigDecimal maxUpHistory) {
		this.maxUpHistory = maxUpHistory;
	}

	public BigDecimal getMaxDownHistory() {
		return maxDownHistory;
	}

	public void setMaxDownHistory(BigDecimal maxDownHistory) {
		this.maxDownHistory = maxDownHistory;
	}

	public BigDecimal getAmountMinimumBuy() {
		return amountMinimumBuy;
	}

	public void setAmountMinimumBuy(BigDecimal amountMinimumBuy) {
		this.amountMinimumBuy = amountMinimumBuy;
	}

	public Integer getPricePrecision() {
		return pricePrecision;
	}

	public void setPricePrecision(Integer pricePrecision) {
		this.pricePrecision = pricePrecision;
	}
	
}