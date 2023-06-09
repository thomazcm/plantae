package com.thomazcm.plantae.controller;

import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.thomazcm.plantae.controller.helper.ControllerHelper;
import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.repository.IngressoRepository;

@Controller
@RequestMapping("/")
public class HomeController {

    @Value("${plantae.endpoint.apiEndpoint}")
    private String apiEndpoint;
    private IngressoRepository ingressoRepository;
    private ControllerHelper helper;

    public HomeController(IngressoRepository ingressoRepository,
            ControllerHelper ControllerHelper) {
        this.ingressoRepository = ingressoRepository;
        this.helper = ControllerHelper;
    }

    @GetMapping("/")
    public String redirect() {
        return "forward:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("apiEndpoint", apiEndpoint);
        model.addAttribute("maxTickets", helper.getMaxTickets());
        return "home";
    }

    @GetMapping("/verificar")
    public String verificarQrCode(@RequestParam(required = false) String id,
            @RequestParam(required = false) String senha, Model model) {
        try {
            Ingresso ingresso = ingressoRepository.findById(id).get();
            model.addAttribute("nomeValidado", ingresso.getCliente());
            if (!ingresso.validar(Integer.parseInt(senha))) {
                model.addAttribute("expirado", true);
                return "forward:/";
            }
            ingressoRepository.save(ingresso);
            model.addAttribute("validado", true);
            return "forward:/";
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            model.addAttribute("invalido", true);
            return "forward:/";
        }
    }

}
