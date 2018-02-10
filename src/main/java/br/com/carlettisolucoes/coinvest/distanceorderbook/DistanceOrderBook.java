package br.com.carlettisolucoes.coinvest.distanceorderbook;

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
import br.com.carlettisolucoes.coinvest.exchange.Exchange;

@Entity
@Table(name="distance_order_book")
@SequenceGenerator(name="seq", sequenceName="distance_order_book_seq")
public class DistanceOrderBook implements Serializable {

	private static final long serialVersionUID = -7485308141056835891L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name="id")
	private Long id;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="distance_ask")
	private Integer distanceAsk;
	
	@Column(name="distance_bid")
	private Integer distanceBid;
	
	@Column(name="actual_ask", precision=16, scale=6)
	private BigDecimal actualAsk;
	
	@Column(name="actual_bid", precision=16, scale=6)
	private BigDecimal actualBid;
	
	@Column(name="percent", precision=16, scale=6)
	private BigDecimal percent;
	
	@Column(name="value_ask", precision=16, scale=6)
	private BigDecimal valueAsk;
	
	@Column(name="value_bid", precision=16, scale=6)
	private BigDecimal valueBid;
	
	@Column(name="amount", precision=16, scale=6)
	private BigDecimal amount;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="coin_id", referencedColumnName="id")
	private Coin coin;
	
	@Column(name="target_value", precision=16, scale=6)
	private BigDecimal targetValue;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="exchange_id", referencedColumnName="id")
	private Exchange exchange;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDistanceAsk() {
		return distanceAsk;
	}

	public void setDistanceAsk(Integer distanceAsk) {
		this.distanceAsk = distanceAsk;
	}

	public Integer getDistanceBid() {
		return distanceBid;
	}

	public void setDistanceBid(Integer distanceBid) {
		this.distanceBid = distanceBid;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public BigDecimal getValueAsk() {
		return valueAsk;
	}

	public void setValueAsk(BigDecimal valueAsk) {
		this.valueAsk = valueAsk;
	}

	public BigDecimal getValueBid() {
		return valueBid;
	}

	public void setValueBid(BigDecimal valueBid) {
		this.valueBid = valueBid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Coin getCoin() {
		return coin;
	}

	public void setCoin(Coin coin) {
		this.coin = coin;
	}

	@Override
	public String toString() {
		return "DistanceOrderBook [id=" + id + ", distanceAsk=" + distanceAsk + ", distanceBid=" + distanceBid
				+ ", actualAsk=" + actualAsk + ", actualBid=" + actualBid + ", percent=" + percent + ", valueAsk="
				+ valueAsk + ", valueBid=" + valueBid + ", amount=" + amount + ", coin=" + coin + ", targetValue="
				+ targetValue + "]";
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
		DistanceOrderBook other = (DistanceOrderBook) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public BigDecimal getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(BigDecimal targetValue) {
		this.targetValue = targetValue;
	}

	public BigDecimal getActualAsk() {
		return actualAsk;
	}

	public void setActualAsk(BigDecimal actualAsk) {
		this.actualAsk = actualAsk;
	}

	public BigDecimal getActualBid() {
		return actualBid;
	}

	public void setActualBid(BigDecimal actualBid) {
		this.actualBid = actualBid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Exchange getExchange() {
		return exchange;
	}

	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}
	
}
