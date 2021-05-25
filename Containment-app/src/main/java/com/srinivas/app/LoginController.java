package com.srinivas.app;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;





@Controller
public class LoginController {
	
	

	@GetMapping("/")
	public String listUsers(Model model) {
	    return "dashboard";
	}
	@GetMapping("/dashboard")
	public String listUsers1(Model model) {
	    return "dashboard";
	}
	@GetMapping("/loc_search")
	public String joc(Model model,@RequestParam("address") String add) {
		model.addAttribute(add);
		return "dashboard";
	}
}


	

	


