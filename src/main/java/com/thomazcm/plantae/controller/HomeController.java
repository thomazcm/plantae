package com.thomazcm.plantae.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

	@Value("${plantae.endpoint.apiEndpoint}")
	private String apiEndpoint;
	
	@GetMapping
	public String home(Model model) {
		model.addAttribute("apiEndpoint", apiEndpoint);
		return "home";
	}
	
}
