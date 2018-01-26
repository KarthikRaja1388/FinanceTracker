package network.karthik.financetracker.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.assertj.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import network.karthik.financetracker.entity.Expense;
import network.karthik.financetracker.entity.User;
import network.karthik.financetracker.repository.IExpenseRepository;
import network.karthik.financetracker.utility.DateUtility;

@Service
public class ExpenseService {
	
	@Autowired
	private IExpenseRepository expenseRepository;
	
	
	@Autowired
	private DateUtility dateUtility;

	public ExpenseService(IExpenseRepository expenseRepository) {
		this.expenseRepository = expenseRepository;
	}
	
	
	public void add(Expense expense) {
		expenseRepository.save(expense);
	}
	
	public void update(Expense expense,User user) {
		Expense expenseById = expenseRepository.findOne(expense.getExpenseId());
		
		expenseById.setAmountSpent(expense.getAmountSpent());
		expenseById.setDateSpent(expense.getDateSpent());
		expenseById.setPlaceSpent(expense.getPlaceSpent());
		expenseById.setUser(user);
		
		expenseRepository.save(expenseById);
	}
	
	public void delete(Long expenseId) {
		expenseRepository.delete(expenseId);
	}
	
	public float getExpense(Long id) {
		List<Expense> expenses = expenseRepository.findByUserUserId(id);
		float totalExpense = 0;
		for (Expense expense : expenses) {
			if(dateUtility.getMonthOfDate(expense.getDateSpent())) {
				totalExpense = totalExpense + expense.getAmountSpent();
			}
		}
		return totalExpense;
	}
	
	public float getExpenseByCategory(String category,Long id) {
		float foodExpense = 0;
		List<Expense> foodExpenseList = expenseRepository.findByCategoryAndUserUserId(category, id);
		
		for (Expense expense : foodExpenseList) {
			foodExpense = foodExpense + expense.getAmountSpent();
		}
		return foodExpense;
	}
	
	public List<Expense> findExpenseByMonth(Long id){
		List<Expense> expenseList = new ArrayList<>();
		Iterator<Expense> iterator = expenseRepository.findByUserUserId(id).iterator();
		
		while(iterator.hasNext()) {
			expenseList.add(iterator.next());
		}
		return expenseList;
		
	}
	
}
