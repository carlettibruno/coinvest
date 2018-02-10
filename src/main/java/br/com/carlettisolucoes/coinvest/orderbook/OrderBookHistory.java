package br.com.carlettisolucoes.coinvest.orderbook;

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
@Table(name="order_book_history")
@SequenceGenerator(name="seq", sequenceName="order_book_history_seq")
public class OrderBookHistory implements Serializable {

	private static final long serialVersionUID = -2676757111173045362L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name="id")
	private Long id;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="orders_json")
	private String ordersJson;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="exchange_id", referencedColumnName="id")
	private Exchange exchange;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="coin_id", referencedColumnName="id")
	private Coin coin;
	
	@Column(name="percent")
	private BigDecimal percent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getOrdersJson() {
		return ordersJson;
	}

	public void setOrdersJson(String ordersJson) {
		this.ordersJson = ordersJson;
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
		OrderBookHistory other = (OrderBookHistory) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderBookHistory [id=" + id + ", date=" + date + ", ordersJson=" + ordersJson + ", exchange=" + exchange
				+ "]";
	}

	public Exchange getExchange() {
		return exchange;
	}

	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}

	public Coin getCoin() {
		return coin;
	}

	public void setCoin(Coin coin) {
		this.coin = coin;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}
	
}
