package com.heladeria.sistema.controller;

import com.heladeria.sistema.model.Cliente;
import com.heladeria.sistema.service.ClienteService;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model,
                         @RequestParam(required = false) String nombre,
                         @RequestParam(required = false) BigDecimal saldoMin,
                         @RequestParam(required = false) BigDecimal saldoMax) {
        Specification<Cliente> spec = Specification.where(null);

        if (nombre != null && !nombre.isBlank()) {
            String like = "%" + nombre.trim().toLowerCase() + "%";
            spec = spec.and((root, cq, cb) -> cb.like(cb.lower(root.get("nombre")), like));
        }
        if (saldoMin != null) {
            spec = spec.and((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("saldoPendiente"), saldoMin));
        }
        if (saldoMax != null) {
            spec = spec.and((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("saldoPendiente"), saldoMax));
        }

        model.addAttribute("clientes", clienteService.findAll(spec));
        model.addAttribute("nombre", nombre);
        model.addAttribute("saldoMin", saldoMin);
        model.addAttribute("saldoMax", saldoMax);
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return clienteService.findById(id)
                .map(c -> {
                    model.addAttribute("cliente", c);
                    return "clientes/form";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Cliente no encontrado");
                    return "redirect:/clientes";
                });
    }

    @PostMapping
    public String guardar(@ModelAttribute Cliente cliente, RedirectAttributes ra) {
        clienteService.save(cliente);
        ra.addFlashAttribute("ok", "Cliente guardado");
        return "redirect:/clientes";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        clienteService.deleteById(id);
        ra.addFlashAttribute("ok", "Cliente eliminado");
        return "redirect:/clientes";
    }
}