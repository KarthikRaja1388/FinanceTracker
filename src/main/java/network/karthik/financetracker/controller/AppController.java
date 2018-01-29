package network.karthik.financetracker.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import network.karthik.financetracker.entity.User;
import network.karthik.financetracker.services.ExpenseService;
import network.karthik.financetracker.services.IncomeService;

@Controller
public class AppController {
	
	@Autowired
	private ExpenseService expenseService;
	
	@Autowired
	private IncomeService incomeService;
	

	@RequestMapping(value = {"/","/home"}, method = RequestMethod.GET)
	public String homePage(@ModelAttribute("user") User user, Model model) {
		model.addAttribute(user);
		return "index";
	}
	
	@RequestMapping(value = "/Dashboard", method = RequestMethod.GET)	
	public String dashboard(Model model,@SessionAttribute("sessionUser")User user) throws ParseException {
		model.addAttribute("expensesFromDb",expenseService.getExpense(user.getUserId()));
		model.addAttribute("incomeFromDb",incomeService.getIncome(user.getUserId()));
		model.addAttribute("foodExpense",expenseService.getExpenseByCategory("food",user.getUserId()));
		model.addAttribute("transportExpense",expenseService.getExpenseByCategory("transport",user.getUserId()));
		model.addAttribute("rent",expenseService.getExpenseByCategory("rent",user.getUserId()));
		model.addAttribute("bills",expenseService.getExpenseByCategory("bills",user.getUserId()));
		model.addAttribute("loan",expenseService.getExpenseByCategory("loan",user.getUserId()));
		model.addAttribute("groceries",expenseService.getExpenseByCategory("groceries",user.getUserId()));
		model.addAttribute("misc",expenseService.getExpenseByCategory("misc",user.getUserId()));
		model.addAttribute("loggedinUser",user);
		model.addAttribute("currentDate",formatDate());
		return "dashboard";
	}
	

	
	public String formatDate() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Date today = new Date();
		String result = formatter.format(today);
		return result;
	}
}
