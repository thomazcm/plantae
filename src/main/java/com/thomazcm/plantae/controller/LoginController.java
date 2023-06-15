package com.thomazcm.plantae.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.thomazcm.plantae.config.TokenService;
import com.thomazcm.plantae.model.config.Usuario;
import com.thomazcm.plantae.repository.UsuarioRepository;

@Controller
public class LoginController {

//    @Autowired private UsuarioRepository repository;
//    @Autowired private BCryptPasswordEncoder encoder;
    
    @Value("${plantae.endpoint.apiendpoint}")
    private String apiEndpoint;
    
    private UsuarioRepository repository;
    private TokenService service;
    

    public LoginController(UsuarioRepository repository, TokenService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request) {
        model.addAttribute("apiEndpoint", apiEndpoint);
        String token = getToken(request);
        if (token == null) {
            return "login";
        }
        return "home";
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
    
    private String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT-TOKEN")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }
    
//    @PostMapping("/cadastro")
//    public String cadastrarUsuario(Model model, @RequestBody UsuarioForm usuarioForm) {
//        var usuario = usuarioForm.toUser(encoder);
//        repository.save(usuario);
//        
//        return "home";
//    }

}
