package network.karthik.financetracker.controller;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import network.karthik.financetracker.entity.User;
import network.karthik.financetracker.services.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;


	public UserController() {
		super();
	}

	public UserController(UserService userService) {
		super();
		this.userService = userService;

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String userLogin(@ModelAttribute("user") User user, Model model,HttpSession session) {
		
		if( userService.login(user.getEmail(),user.getPassword()) != null) {
			User loggedInUser = userService.login(user.getEmail(), user.getPassword());
			resgisterUserToSession(loggedInUser, session);
			return "redirect:/Dashboard";
		}else {
			model.addAttribute("loginError","* Username or password is incorrect");
			return "index";
		}

	}
	
	@RequestMapping(value="/Register", method = RequestMethod.GET)
	public String register(Model model,@ModelAttribute User user) {
		model.addAttribute("user",user);
		return "register";
	}
	
	@RequestMapping(value="/Register", method = RequestMethod.POST)
	public String signUp(@ModelAttribute  @Valid  User user,BindingResult result,
			Model model) {
		
		model.addAttribute("user",user);
		if(result.hasErrors()) {
			return "register";
		}else {
			userService.add(user);
			return "redirect:/home?register=success";
		}
		
	}
	
	@RequestMapping(value = "/Logout")
	public String userLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/home?logout=y";
	}
	
	public void resgisterUserToSession(User user,HttpSession session) {
		session.setAttribute("sessionUser", user);
		session.setAttribute("sessionUserFirstName", user.getFirstName());
	}
	
}
