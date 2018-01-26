package network.karthik.financetracker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "expenseId")
	private Long expenseId;
	
	@Column(name = "amountSpent")
	@NotNull
	private float amountSpent;
	
	@Column(name = "dateSpent")
	@NotNull
	private String dateSpent;
	
	@Column(name = "placeSpent")
	@NotNull
	private String placeSpent;
	
	@Column(name = "category")
	@NotNull
	private String category;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	
	public Expense() {	}

	public Expense(Long expenseId, float amountSpent, String dateSpent, String placeSpent, String category, User user) {
		super();
		this.expenseId = expenseId;
		this.amountSpent = amountSpent;
		this.dateSpent = dateSpent;
		this.placeSpent = placeSpent;
		this.category = category;
		this.user = user;
	}


	public Long getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Long expenseId) {
		this.expenseId = expenseId;
	}

	public float getAmountSpent() {
		return amountSpent;
	}

	public void setAmountSpent(float amountSpent) {
		this.amountSpent = amountSpent;
	}

	public String getDateSpent() {
		return dateSpent;
	}

	public void setDateSpent(String dateSpent) {
		this.dateSpent = dateSpent;
	}

	public String getPlaceSpent() {
		return placeSpent;
	}

	public void setPlaceSpent(String placeSpent) {
		this.placeSpent = placeSpent;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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
		result = prime * result + Float.floatToIntBits(amountSpent);
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((dateSpent == null) ? 0 : dateSpent.hashCode());
		result = prime * result + ((expenseId == null) ? 0 : expenseId.hashCode());
		result = prime * result + ((placeSpent == null) ? 0 : placeSpent.hashCode());
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
		Expense other = (Expense) obj;
		if (Float.floatToIntBits(amountSpent) != Float.floatToIntBits(other.amountSpent))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (dateSpent == null) {
			if (other.dateSpent != null)
				return false;
		} else if (!dateSpent.equals(other.dateSpent))
			return false;
		if (expenseId == null) {
			if (other.expenseId != null)
				return false;
		} else if (!expenseId.equals(other.expenseId))
			return false;
		if (placeSpent == null) {
			if (other.placeSpent != null)
				return false;
		} else if (!placeSpent.equals(other.placeSpent))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	
}
