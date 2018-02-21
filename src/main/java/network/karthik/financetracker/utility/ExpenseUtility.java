package network.karthik.financetracker.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import network.karthik.financetracker.services.ExpenseService;
import network.karthik.financetracker.services.IncomeService;

@Component
public class ExpenseUtility {
	
	@Autowired
	private IncomeService incomeServie;
	@Autowired
	private ExpenseService expenseService;

	public boolean isExpenseLessThanIncome(float expenseToBeAdded, Long id){
		
		float currentIncome = incomeServie.getIncome(id);
		float currentExpense = expenseService.getExpense(id);
		
		float newExpense = currentExpense + expenseToBeAdded;
		
		if(currentIncome >= newExpense){
			return true;
		}else{
			return false;
		}
	}
}
