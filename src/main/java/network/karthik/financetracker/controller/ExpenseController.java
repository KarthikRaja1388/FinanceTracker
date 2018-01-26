package network.karthik.financetracker.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import network.karthik.financetracker.entity.Expense;
import network.karthik.financetracker.entity.User;
import network.karthik.financetracker.services.ExpenseService;

@Controller
@RequestMapping(value="/Expense")
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;

	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	} 
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String expense(@ModelAttribute Expense expense, Model model) {
		model.addAttribute("expense",expense);
		return "expense";
	}
	
	@RequestMapping(value="/Add", method = RequestMethod.POST)
	public String addExpense(@ModelAttribute @Valid Expense expense,BindingResult result , Model model,@SessionAttribute("sessionUser")User user) {
		model.addAttribute("expense", expense);
		if(result.hasErrors()) {
			return "expense";
		}else {
			expense.setUser(user);
			expenseService.add(expense);
			return "redirect:/Dashboard?e_action=add";
		}
	}
	
	@RequestMapping(value="/View", method= RequestMethod.GET)
	public String viewExpense(Model model,@SessionAttribute("sessionUser")User user) {
		model.addAttribute("expenseFromDb",expenseService.findExpenseByMonth(user.getUserId()));
		return "viewExpense";
	}
	
	@RequestMapping(value="/Delete/{expenseId}")
	public String deleteExpense(@PathVariable("expenseId") Long expenseId) {
		expenseService.delete(expenseId);
		return "redirect:/Expense/View?e_action=del";
		
	}
	
}
