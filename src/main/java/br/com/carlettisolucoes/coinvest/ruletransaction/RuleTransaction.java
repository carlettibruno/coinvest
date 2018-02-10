package br.com.carlettisolucoes.coinvest.ruletransaction;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.carlettisolucoes.coinvest.rule.Rule;
import br.com.carlettisolucoes.coinvest.transaction.Transaction;

@Entity
@Table(name="rule_transaction")
public class RuleTransaction implements Serializable {

	private static final long serialVersionUID = -4899908581924596765L;
	
	@EmbeddedId
	private RuleTransactionId id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="rule_id", referencedColumnName="id", updatable=false, insertable=false)
	private Rule rule;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="transaction_id", referencedColumnName="id", updatable=false, insertable=false)
	private Transaction transcation;

	public RuleTransactionId getId() {
		return id;
	}

	public void setId(RuleTransactionId id) {
		this.id = id;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public Transaction getTranscation() {
		return transcation;
	}

	public void setTranscation(Transaction transcation) {
		this.transcation = transcation;
	}

	@Override
	public String toString() {
		return "RuleTransaction [id=" + id + ", rule=" + rule + ", transcation=" + transcation + "]";
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
		RuleTransaction other = (RuleTransaction) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
