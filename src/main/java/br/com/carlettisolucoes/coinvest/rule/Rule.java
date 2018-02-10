package br.com.carlettisolucoes.coinvest.rule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="rule")
@SequenceGenerator(name="seq", sequenceName="rule_seq")
public class Rule implements Serializable {

	private static final long serialVersionUID = -1522605621248359498L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name="id")
	private Integer id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="uri", nullable=false)
	private String uri;
	
	@Column(name="percentComposition", nullable=false)
	private Integer percentComposition;

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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Integer getPercentComposition() {
		return percentComposition;
	}

	public void setPercentComposition(Integer percentComposition) {
		this.percentComposition = percentComposition;
	}

	@Override
	public String toString() {
		return "Rule [id=" + id + ", name=" + name + ", uri=" + uri + ", percentComposition=" + percentComposition
				+ "]";
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
		Rule other = (Rule) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}