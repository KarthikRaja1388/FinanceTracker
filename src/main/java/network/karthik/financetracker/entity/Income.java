package network.karthik.financetracker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "income")
public class Income {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "incomeId")
	private Long incomeId;
	
	@Column(name = "amountReceived")
	@NotNull
	private float amountReceived;
	
	@Column(name = "source")
	@NotNull
	private String source;
	
	@Column (name = "dateReceived")
	@NotNull
	private String dateReceived;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	public Income() {	}

	public Income(Long incomeId, float amountReceived, String source, String dateReceived, User user) {
		super();
		this.incomeId = incomeId;
		this.amountReceived = amountReceived;
		this.source = source;
		this.dateReceived = dateReceived;
		this.user = user;
	}

	public Long getIncomeId() {
		return incomeId;
	}

	public void setIncomeId(Long incomeId) {
		this.incomeId = incomeId;
	}

	public float getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(float amountReceived) {
		this.amountReceived = amountReceived;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(String dateReceived) {
		this.dateReceived = dateReceived;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(amountReceived);
		result = prime * result + ((dateReceived == null) ? 0 : dateReceived.hashCode());
		result = prime * result + ((incomeId == null) ? 0 : incomeId.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Income other = (Income) obj;
		if (Float.floatToIntBits(amountReceived) != Float.floatToIntBits(other.amountReceived))
			return false;
		if (dateReceived == null) {
			if (other.dateReceived != null)
				return false;
		} else if (!dateReceived.equals(other.dateReceived))
			return false;
		if (incomeId == null) {
			if (other.incomeId != null)
				return false;
		} else if (!incomeId.equals(other.incomeId))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	
	
}
