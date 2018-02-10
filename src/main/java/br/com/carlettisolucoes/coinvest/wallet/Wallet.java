package br.com.carlettisolucoes.coinvest.wallet;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name="wallet")
@SequenceGenerator(name="seq", sequenceName="wallet_seq")
public class Wallet implements Serializable {

	private static final long serialVersionUID = 7729087007864229934L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name="id")
	private Integer id;
	
	@Column(name="description", nullable=false)
	private String description;
	
	@Column(name="value", nullable=false, precision=16, scale=6)
	private BigDecimal value;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="coin_id", referencedColumnName="id")
	private Coin coin;
	
	@Column(name="active")
	private Boolean active;
	
	@Column(name="priority")
	private Integer priority;
	
	@Column(name="usd_limit", precision=16, scale=6)
	private BigDecimal usdLimit;
	
	@Column(name="last_order_id")
	private Long lastOrderId;
	
	@Column(name="last_paid_usd", precision=16, scale=6)
	private BigDecimal lastPaidUsd;
	
	@Column(name="last_sell_usd", precision=16, scale=6)
	private BigDecimal lastSellUsd;	
	
//	@Column(name="amount_minimum_buy", precision=16, scale=6)
//	private BigDecimal amountMinimumBuy;	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
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
		Wallet other = (Wallet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Wallet [id=" + id + ", description=" + description + ", value=" + value + "]";
	}

	public Coin getCoin() {
		return coin;
	}

	public void setCoin(Coin coin) {
		this.coin = coin;
	}

	public Boolean getActive() {
		return active;
	}
	
	public boolean isActive() {
		return active != null && active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public BigDecimal getUsdLimit() {
		return usdLimit;
	}

	public void setUsdLimit(BigDecimal usdLimit) {
		this.usdLimit = usdLimit;
	}

	public Long getLastOrderId() {
		return lastOrderId;
	}

	public void setLastOrderId(Long lastOrderId) {
		this.lastOrderId = lastOrderId;
	}

	public BigDecimal getLastPaidUsd() {
		return lastPaidUsd;
	}

	public void setLastPaidUsd(BigDecimal lastPaidUsd) {
		this.lastPaidUsd = lastPaidUsd;
	}

	public BigDecimal getLastSellUsd() {
		return lastSellUsd;
	}

	public void setLastSellUsd(BigDecimal lastSellUsd) {
		this.lastSellUsd = lastSellUsd;
	}

//	public BigDecimal getAmountMinimumBuy() {
//		return amountMinimumBuy;
//	}
//
//	public void setAmountMinimumBuy(BigDecimal amountMinimumBuy) {
//		this.amountMinimumBuy = amountMinimumBuy;
//	}

}
