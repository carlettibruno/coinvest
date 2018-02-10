package br.com.carlettisolucoes.coinvest.fakeorder;

import java.io.Serializable;
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
import br.com.carlettisolucoes.coinvest.orderbook.OrderBookHistory;

@Entity
@Table(name="fake_order")
@SequenceGenerator(name="seq",sequenceName="fake_order_seq")
public class FakeOrder implements Serializable {

	private static final long serialVersionUID = -8196540149950910821L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name="id")
	private Integer id;
	
	@Column(name="date", nullable=false)
	private Date date;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="order_book_history_id", referencedColumnName="id")
	private OrderBookHistory orderBookHistory;
	
	@Column(name="accepted_percent", nullable=false)
	private Float acceptedPercent;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="coin_id", referencedColumnName="id")
	private Coin coin;	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public OrderBookHistory getOrderBookHistory() {
		return orderBookHistory;
	}

	public Coin getCoin() {
		return coin;
	}

	public void setCoin(Coin coin) {
		this.coin = coin;
	}

	public void setOrderBookHistory(OrderBookHistory orderBookHistory) {
		this.orderBookHistory = orderBookHistory;
	}

	public Float getAcceptedPercent() {
		return acceptedPercent;
	}

	public void setAcceptedPercent(Float acceptedPercent) {
		this.acceptedPercent = acceptedPercent;
	}

	@Override
	public String toString() {
		return "FakeOrder [id=" + id + ", date=" + date + ", acceptedPercent=" + acceptedPercent + "]";
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
		FakeOrder other = (FakeOrder) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
