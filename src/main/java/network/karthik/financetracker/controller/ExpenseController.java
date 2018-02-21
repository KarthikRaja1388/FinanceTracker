package network.karthik.financetracker.controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import network.karthik.financetracker.entity.Expense;
import network.karthik.financetracker.entity.User;
import network.karthik.financetracker.services.ExpenseService;
import network.karthik.financetracker.utility.DateUtility;
import network.karthik.financetracker.utility.ExpenseUtility;

@Controller
@RequestMapping(value="/Expense")
@SessionAttributes("sessionUser")
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private DateUtility dateUtility;
	@Autowired
	private ExpenseUtility expenseUtility;
	
	public ExpenseController() {
		super();
	}

	/**
	 * 
	 * @param expense
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String expense(@ModelAttribute Expense expense, Model model) {
		model.addAttribute("expense",expense);
		return "expense";
	}
	
	/**
	 * 
	 * @param expense
	 * @param result
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/Add", method = RequestMethod.POST)
	public String addExpense(@ModelAttribute @Valid Expense expense,BindingResult result , Model model,@SessionAttribute("sessionUser")User user) {
		model.addAttribute("expense", expense);
		if(result.hasErrors()) {
			return "expense";
		}else {
			if(expenseUtility.isExpenseLessThanIncome(expense.getAmountSpent(), user.getUserId())){
				expense.setUser(user);
				expenseService.add(expense);
				return "redirect:/Dashboard?e_action=add";
			}else{
				return "redirect:/Dashboard?e_action=expense_error";
			}
			}
	}
	
	/**
	 * 
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/View", method= RequestMethod.GET)
	public String viewExpense(Model model,@SessionAttribute("sessionUser")User user) {
		List<Expense> expenseList = expenseService.findExpenseForCurrentMonth(user.getUserId());

		if(expenseList.isEmpty()) {
			model.addAttribute("noResult","No Expense Found for this month");
		}else {
			model.addAttribute("expenseFromDb",expenseList);
			model.addAttribute("chosenMonth",dateUtility.returnMonthInAlphabet().toUpperCase());
		}
		return "viewExpense";
	}
	
	/**
	 * 
	 * @param model
	 * @param user
	 * @param month
	 * @return
	 */
	@RequestMapping(value="View/{month}")
	public String viewExpenseBasedOnMonth(Model model,@SessionAttribute("sessionUser")User user,
										@PathVariable("month")String month) {
		List<Expense> expenseList = expenseService.findExpenseForChosenMonth(user.getUserId(), month);

		if(expenseList.isEmpty()) {
			model.addAttribute("noResult","No Expense Found for this month");
			model.addAttribute("chosenMonth",month.toUpperCase());	
		}else {
			model.addAttribute("expenseFromDb",expenseList);
			model.addAttribute("chosenMonth",month.toUpperCase());
		}
		return "viewExpense";

	}
	
	@RequestMapping(value="/Edit/{expenseId}")	
	public String editExpense(Model model,@PathVariable("expenseId")Long expenseId) {
		Expense expenseById = expenseService.getExpenseById(expenseId);
		model.addAttribute("expense",expenseById);
		return "expense";
		
	}
	
	/**
	 * 
	 * @param expenseId
	 * @return
	 */
	@RequestMapping(value="/Delete/{expenseId}")
	public String deleteExpense(@PathVariable("expenseId") Long expenseId) {
		expenseService.delete(expenseId);
		return "redirect:/Expense/View?e_action=del";
		
	}

	/**
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value= "/Export/{month}", method=RequestMethod.GET )
	public void getExpenseReport(@SessionAttribute("sessionUser")User user,HttpServletRequest request,HttpServletResponse response,
			@PathVariable("month")String month) throws IOException {
		List<Expense> expenseList = expenseService.findExpenseForChosenMonth(user.getUserId(), month);

		if(!expenseList.isEmpty()) {
			expenseService.buildExcelDocument(user.getUserId());
			expenseService.downloadFile(request, response, user.getUserId());
		}else {
			System.err.println("Can't be downloaded");
		}
	}

}
