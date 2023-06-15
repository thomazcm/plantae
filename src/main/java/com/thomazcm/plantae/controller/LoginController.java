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
        Cookie jwtCookie = new Cookie("JWT-TOKEN", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
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
