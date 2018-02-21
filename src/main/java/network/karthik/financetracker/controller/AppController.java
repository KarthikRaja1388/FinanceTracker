package network.karthik.financetracker.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import network.karthik.financetracker.entity.Expense;
import network.karthik.financetracker.entity.User;
import network.karthik.financetracker.services.ExpenseService;
import network.karthik.financetracker.services.IncomeService;
import network.karthik.financetracker.utility.DateUtility;
import network.karthik.financetracker.utility.ExpenseUtility;

@Controller
public class AppController {
	
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private IncomeService incomeService;
	@Autowired
	private DateUtility dateUtility;
	@Autowired
	private ExpenseUtility expenseUtility;

	
	public AppController() {
		super();
	}
	/*
	public AppController(ExpenseService expenseService) {
		super();
		this.expenseService = expenseService;
	}
	public AppController(IncomeService incomeService) {
		super();
		this.incomeService = incomeService;
	}
	public AppController(DateUtility dateUtility) {
		super();
		this.dateUtility = dateUtility;
	}*/
	@RequestMapping(value = {"/","/home"}, method = RequestMethod.GET)
	public String homePage(@ModelAttribute("user") User user, Model model) {
		model.addAttribute(user);
		return "index";
	}
	
	@RequestMapping(value = "/Dashboard", method = RequestMethod.GET)	
	public String dashboard(Model model,@SessionAttribute("sessionUser")User user) throws ParseException {
		model.addAttribute("expensesFromDb",expenseService.getExpense(user.getUserId()));
		model.addAttribute("incomeFromDb",incomeService.getIncome(user.getUserId()));
		model.addAttribute("currentSavings",returnSavings(user));
		model.addAttribute("foodExpense",expenseService.getExpenseByCategory("food",user.getUserId()));
		model.addAttribute("transportExpense",expenseService.getExpenseByCategory("transport",user.getUserId()));
		model.addAttribute("rent",expenseService.getExpenseByCategory("rent",user.getUserId()));
		model.addAttribute("bills",expenseService.getExpenseByCategory("bills",user.getUserId()));
		model.addAttribute("loan",expenseService.getExpenseByCategory("loan",user.getUserId()));
		model.addAttribute("groceries",expenseService.getExpenseByCategory("groceries",user.getUserId()));
		model.addAttribute("misc",expenseService.getExpenseByCategory("misc",user.getUserId()));
		model.addAttribute("loggedinUser",user);
		model.addAttribute("currentDate",dateUtility.returnCurrentDate());
		return "dashboard";
	}
	
	@RequestMapping(value="/Register", method = RequestMethod.GET)
	public String register(Model model,@ModelAttribute User user) {
		model.addAttribute("user",user);
		return "register";
	}
	
	@RequestMapping(value="/ForgotPassword", method=RequestMethod.GET)
	public String forgotPassword() {
		return "forgotPassword";
	}
	
	public float returnSavings(User user) {
		float savings = incomeService.getAllIncome(user.getUserId())-expenseService.getAllExpense(user.getUserId());
		return savings;
	}

}
