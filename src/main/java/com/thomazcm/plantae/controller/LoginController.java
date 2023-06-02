package com.thomazcm.plantae.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(("/login"))
public class LoginController {

//    @Autowired private UsuarioRepository repository;
//    @Autowired private BCryptPasswordEncoder encoder;

    @GetMapping
    public String loginPage(Model model) {
//    	System.out.println("tentando logar...");
        return "login";
    }
    
//    @PostMapping("/cadastro")
//    public String cadastrarUsuario(Model model, @RequestBody UsuarioForm usuarioForm) {
//        var usuario = usuarioForm.toUser(encoder);
//        repository.save(usuario);
//        
//        return "home";
//    }

}
