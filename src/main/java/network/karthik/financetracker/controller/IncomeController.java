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

import network.karthik.financetracker.entity.Income;
import network.karthik.financetracker.entity.User;
import network.karthik.financetracker.services.IncomeService;
import network.karthik.financetracker.utility.DateUtility;

@Controller
@RequestMapping(value="/Income")
public class IncomeController {
	
	@Autowired
	private IncomeService incomeService;
	@Autowired
	private DateUtility dateUtility;
	
	public IncomeController() {
		super();
	}

	/**
	 * Method to display Income Page
	 * @param income
	 * @param model
	 * @return income view
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String income(@ModelAttribute Income income, Model model) {
		model.addAttribute("income", income);
		return "income";
	}
	
	/**
	 * Method to add income to database
	 * 
	 * @param income valid income object
	 * @param result value of Binding result
	 * @param model
	 * @param user User of the Session
	 * @return Dashboard with action parameter if no errors else return income page
	 */
	
	@RequestMapping(value = "/Add", method = RequestMethod.POST)
	public String addIncome(@ModelAttribute @Valid Income income,BindingResult result , Model model,@SessionAttribute("sessionUser")User user) {
		model.addAttribute("income",income);
		if(result.hasErrors()) {
			return "income";
		}else {
			income.setUser(user);
			incomeService.add(income);
			return "redirect:/Dashboard?i_action=suxs";
		}
	}
	/**
	 * Method to 
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/View", method = RequestMethod.GET)
	public String viewIncome(Model model,@SessionAttribute("sessionUser")User user) {
		List<Income>incomeByMonth = incomeService.findIncomeByMonth(user.getUserId());
		if( incomeByMonth.isEmpty()) {
			model.addAttribute("noResult","No Income Found for this month");
			model.addAttribute("chosenMonth",dateUtility.returnMonthInAlphabet().toUpperCase());			
		}else {
			model.addAttribute("incomeFromDb",incomeByMonth);
			model.addAttribute("chosenMonth",dateUtility.returnMonthInAlphabet().toUpperCase());
		}
		return "viewIncome";		
	}
	
	/**
	 * 
	 * @param model
	 * @param user
	 * @param month
	 * @return
	 */
	@RequestMapping(value="/View/{month}",method=RequestMethod.GET)
	public String viewExpenseBasedOnMonth(Model model,@SessionAttribute("sessionUser")User user,@PathVariable("month")String month) {
		List<Income> incomeList = incomeService.findIncomeForChosenMonth(user.getUserId(), month);
		if(incomeList.isEmpty()) {
			model.addAttribute("chosenMonth",month.toUpperCase());
			model.addAttribute("noResult","No Income Found for this month");
		}else {
			model.addAttribute("incomeFromDb",incomeList);
			model.addAttribute("chosenMonth",month.toUpperCase());
		}
		return "viewIncome";		
	}
	
	@RequestMapping(value="/Edit/{incomeId}", method=RequestMethod.GET)
	public String editIncome(Model model,@PathVariable("incomeId")Long incomeId) {
		Income incomeById = incomeService.getIncomeById(incomeId);
		model.addAttribute("income",incomeById);
		return "income";
	}
	
	/**
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value= "/Export", method=RequestMethod.GET )
	public void getExpenseReport(@SessionAttribute("sessionUser")User user,HttpServletRequest request,HttpServletResponse response) throws IOException {
		incomeService.buildExcelDocument(user.getUserId());
		incomeService.downloadFile(request, response, user.getUserId());
	}
}
