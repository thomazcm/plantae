package com.thomazcm.plantae.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

//    @Autowired private UsuarioRepository repository;
//    @Autowired private BCryptPasswordEncoder encoder;
    
    @Value("${plantae.endpoint.apiendpoint}")
    private String apiEndpoint;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("apiEndpoint", apiEndpoint);
        return "login";
    }
    
    @GetMapping("/logoutStateless")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("logging out.. or is it??");
        Cookie jwtCookie = new Cookie("JWT-TOKEN", null);
        // set max age to zero
        jwtCookie.setMaxAge(0);
        // set http only to true
        jwtCookie.setHttpOnly(true);
        // set the path
        jwtCookie.setPath("/");
        // add cookie to response
        response.addCookie(jwtCookie);

        // invalidate the session if exists
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        //context holder
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
    
//    @PostMapping("/cadastro")
//    public String cadastrarUsuario(Model model, @RequestBody UsuarioForm usuarioForm) {
//        var usuario = usuarioForm.toUser(encoder);
//        repository.save(usuario);
//        
//        return "home";
//    }

}
