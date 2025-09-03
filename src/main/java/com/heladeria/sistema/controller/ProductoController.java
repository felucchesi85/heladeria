package com.heladeria.sistema.controller;

import com.heladeria.sistema.model.Producto;
import com.heladeria.sistema.service.ProductoService;
import com.heladeria.sistema.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.findAllConCategoria());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.findAll());
        return "productos/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return productoService.findById(id)
                .map(p -> {
                    model.addAttribute("producto", p);
                    model.addAttribute("categorias", categoriaService.findAll());
                    return "productos/form";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Producto no encontrado");
                    return "redirect:/productos";
                });
    }

    @PostMapping
    public String guardar(@ModelAttribute Producto producto, RedirectAttributes ra) {
        productoService.save(producto);
        ra.addFlashAttribute("ok", "Producto guardado");
        return "redirect:/productos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        productoService.deleteById(id);
        ra.addFlashAttribute("ok", "Producto eliminado");
        return "redirect:/productos";
    }
}