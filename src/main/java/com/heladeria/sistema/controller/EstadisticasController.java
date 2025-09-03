package com.heladeria.sistema.controller;

import com.heladeria.sistema.service.EstadisticasService;
import com.heladeria.sistema.service.dto.StatsResumen;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @GetMapping("/estadisticas")
    public String index(Model model) {
        StatsResumen stats = estadisticasService.obtenerResumen();
        model.addAttribute("stats", stats);
        return "estadisticas/index";
    }
}