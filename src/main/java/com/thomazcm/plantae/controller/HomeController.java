package com.thomazcm.plantae.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thomazcm.plantae.controller.dto.ClienteDto;

@Controller
@RequestMapping("/home")
public class HomeController {

	@GetMapping
	public String home(Model model, ClienteDto clienteDto) {
		return "home";
	}
	
}
