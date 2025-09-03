package com.heladeria.sistema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador principal para las páginas de inicio de la heladería.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("titulo", "Sistema de Facturación - Heladería");
        model.addAttribute("mensaje", "¡Bienvenido al sistema de gestión para tu heladería!");
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("titulo", "Panel de Administración");
        return "admin/dashboard";
    }
}
