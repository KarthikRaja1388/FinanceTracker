package network.karthik.financetracker.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import network.karthik.financetracker.entity.Income;
import network.karthik.financetracker.entity.User;
import network.karthik.financetracker.services.IncomeService;

@Controller
@RequestMapping(value="/Income")
public class IncomeController {
	
	@Autowired
	private IncomeService incomeService;
	

	public IncomeController(IncomeService incomeService) {
		this.incomeService = incomeService;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String income(@ModelAttribute Income income, Model model) {
		model.addAttribute("income", income);
		return "income";
	}
	
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
	
	@RequestMapping(value="/View", method = RequestMethod.GET)
	public String viewIncome(Model model,@SessionAttribute("sessionUser")User user) {
		model.addAttribute("incomeFromDb",incomeService.findIncomeByMonth(user.getUserId()));
		return "viewIncome";
	}
	
	@RequestMapping(value= "/Export", method=RequestMethod.GET )
	public void getExpenseReport(@SessionAttribute("sessionUser")User user,HttpServletRequest request,HttpServletResponse response) throws IOException {
		incomeService.buildExcelDocument(user.getUserId());
		incomeService.downloadFile(request, response, user.getUserId());
	}
}
