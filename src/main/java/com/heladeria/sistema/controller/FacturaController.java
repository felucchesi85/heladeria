package com.heladeria.sistema.controller;

import com.heladeria.sistema.model.Cliente;
import com.heladeria.sistema.model.DetalleFactura;
import com.heladeria.sistema.model.Factura;
import com.heladeria.sistema.service.ClienteService;
import com.heladeria.sistema.service.FacturaService;
import com.heladeria.sistema.service.ProductoService;
import com.heladeria.sistema.service.PdfFacturaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/facturas")
public class FacturaController {
    private static final Logger log = LoggerFactory.getLogger(FacturaController.class);

    private final FacturaService facturaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final PdfFacturaService pdfFacturaService;

    public FacturaController(FacturaService facturaService,
                             ClienteService clienteService,
                             ProductoService productoService,
                             PdfFacturaService pdfFacturaService) {
        this.facturaService = facturaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.pdfFacturaService = pdfFacturaService;
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        Factura factura = new Factura();
        factura.getDetalles().add(new DetalleFactura());
        model.addAttribute("factura", factura);
        model.addAttribute("clientes", clienteService.findAll(null));
        model.addAttribute("productos", productoService.findAllConCategoria());
        model.addAttribute("ultimas", facturaService.findUltimas10());
        // Etiqueta de botón en la vista
        model.addAttribute("accionBoton", "Generar factura");
        return "facturas/form";
    }

    @PostMapping
    public String guardar(@ModelAttribute Factura factura, RedirectAttributes ra, Model model) {
        Cliente cli = factura.getCliente();
        if (cli == null || cli.getId() == null) {
            model.addAttribute("error", "Seleccione un cliente.");
            model.addAttribute("clientes", clienteService.findAll(null));
            model.addAttribute("productos", productoService.findAllConCategoria());
            model.addAttribute("ultimas", facturaService.findUltimas10());
            model.addAttribute("accionBoton", "Generar factura");
            model.addAttribute("factura", factura);
            return "facturas/form";
        }

        // Validación: fecha obligatoria
        try {
            Object fecha = factura.getClass().getMethod("getFecha").invoke(factura);
            if (fecha == null) {
                model.addAttribute("error", "Debe ingresar la fecha de la factura.");
                model.addAttribute("clientes", clienteService.findAll(null));
                model.addAttribute("productos", productoService.findAllConCategoria());
                model.addAttribute("ultimas", facturaService.findUltimas10());
                model.addAttribute("accionBoton", "Generar factura");
                model.addAttribute("factura", factura);
                return "facturas/form";
            }
        } catch (Exception ignore) {
            // Si no existe getFecha, no forzar (pero recomendado tenerlo)
        }

        // Generar número si viene vacío (único simple)
        if (factura.getNumero() == null || factura.getNumero().isBlank()) {
            factura.setNumero("A-" + System.currentTimeMillis());
        }

        // Normalizar detalles y calcular totales
        BigDecimal total = BigDecimal.ZERO;
        List<DetalleFactura> detalles = factura.getDetalles();
        if (detalles != null) {
            for (Iterator<DetalleFactura> it = detalles.iterator(); it.hasNext();) {
                DetalleFactura d = it.next();
                // remover líneas vacías
                if (d == null || d.getProducto() == null || d.getProducto().getId() == null) {
                    it.remove();
                    continue;
                }
                d.setFactura(factura);

                // Si no vino el precio o es 0, obtenerlo desde la DB usando el producto
                if (d.getPrecioUnitario() == null || d.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                    try {
                        productoService.findById(d.getProducto().getId()).ifPresent(prod -> {
                            BigDecimal precioDb = null;
                            try {
                                // Intentar getPrecioVenta()
                                precioDb = (BigDecimal) prod.getClass().getMethod("getPrecioVenta").invoke(prod);
                            } catch (Exception ignore) { }
                            if (precioDb == null) {
                                try {
                                    // Fallback: getPrecio()
                                    precioDb = (BigDecimal) prod.getClass().getMethod("getPrecio").invoke(prod);
                                } catch (Exception ignore) { }
                            }
                            if (precioDb != null) {
                                d.setPrecioUnitario(precioDb);
                                log.debug("Precio tomado de DB para producto {}: {}", d.getProducto().getId(), precioDb);
                            }
                        });
                    } catch (Exception e) {
                        log.warn("No se pudo resolver el precio desde DB para producto {}", d.getProducto().getId(), e);
                    }
                }

                BigDecimal cant = d.getCantidad() == null ? BigDecimal.ZERO : new BigDecimal(d.getCantidad());
                BigDecimal pu = d.getPrecioUnitario() == null ? BigDecimal.ZERO : d.getPrecioUnitario();
                BigDecimal sub = pu.multiply(cant);
                d.setSubtotal(sub);
                total = total.add(sub);
            }
        }
        factura.setTotal(total);

        Factura guardada = facturaService.save(factura);
        // Importante: no modificar saldoPendiente aquí (se mantiene separado de facturación)
        ra.addFlashAttribute("ok", "Factura generada");
        return "redirect:/facturas/" + guardada.getId();
    }

    @GetMapping
    public String listar(Model model) {
        List<Factura> todas = facturaService.findAll();
        model.addAttribute("facturas", todas);
        return "facturas/lista";
    }

    // Endpoint JSON para verificar rápidamente si el service retorna datos
    @GetMapping("/api")
    @ResponseBody
    public List<Factura> listarJson() {
        List<Factura> todas = facturaService.findAll();
        return todas;
    }

    // ====== Gestión mínima de clientes (búsqueda y alta rápida) ======

    // Buscar clientes (para autocomplete). Si no viene 'q', devuelve hasta 20.
    @GetMapping("/clientes/api")
    @ResponseBody
    public List<Cliente> buscarClientes(@RequestParam(value = "q", required = false) String q) {
        List<Cliente> all = clienteService.findAll(null);
        if (q == null || q.isBlank()) {
            return all.stream().limit(20).collect(Collectors.toList());
        }
        final String term = q.toLowerCase();
        return all.stream()
                .filter(c -> {
                    try {
                        String nombre = c.getNombre() != null ? c.getNombre().toLowerCase() : "";
                        String apellido = c.getApellido() != null ? c.getApellido().toLowerCase() : "";
                        String telefono = c.getTelefono() != null ? c.getTelefono().toLowerCase() : "";
                        String email = c.getEmail() != null ? c.getEmail().toLowerCase() : "";
                        return nombre.contains(term) || apellido.contains(term) || telefono.contains(term) || email.contains(term);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .limit(20)
                .collect(Collectors.toList());
    }

    // Alta/actualización rápida de cliente desde el formulario de factura (JSON)
    @PostMapping("/clientes")
    @ResponseBody
    public Cliente crearOActualizarCliente(@RequestBody Cliente cliente) {
        try {
            // Validaciones mínimas
            if (cliente == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente requerido");
            }
            String nombre = cliente.getNombre();
            if (nombre == null || nombre.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre requerido");
            }
            Cliente saved = clienteService.save(cliente);
            log.info("Cliente {} guardado con ID {}", saved.getNombre(), saved.getId());
            return saved;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error guardando cliente", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo guardar el cliente");
        }
    }

    // ====== Fin gestión mínima de clientes ======

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return facturaService.findWithDetailsById(id)
                .map(f -> { model.addAttribute("factura", f); return "facturas/detalle"; })
                .orElseGet(() -> { ra.addFlashAttribute("error", "Factura no encontrada"); return "redirect:/facturas"; });
    }

    @GetMapping("/{id}/pdf")
    @ResponseBody
    public byte[] pdf(@PathVariable Long id) throws Exception {
        return pdfFacturaService.generarFacturaPdf(id);
    }

    // ====== Cuenta corriente de clientes (separada de facturación) ======

    // Agregar movimiento (fecha, monto, observacion). Monto puede ser negativo (pago) o positivo (cargo).
    @PostMapping("/clientes/{id}/movimientos")
    @ResponseBody
    public Cliente agregarMovimiento(
            @PathVariable Long id,
            @RequestParam String fecha,
            @RequestParam String monto,
            @RequestParam(required = false) String observacion
    ) {
        try {
            if (fecha == null || fecha.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fecha requerida");
            if (monto == null || monto.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Monto requerido");
            LocalDate f = LocalDate.parse(fecha); // ISO yyyy-MM-dd
            BigDecimal m = new BigDecimal(monto); // permite negativos
            return clienteService.registrarMovimiento(id, f, m, observacion);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos inválidos para movimiento");
        }
    }

    // Listar últimos movimientos del cliente
    @GetMapping("/clientes/{id}/movimientos")
    @ResponseBody
    public List<Cliente.MovimientoCuenta> ultimosMovimientos(
            @PathVariable Long id,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        return clienteService.findUltimosMovimientos(id, limit);
    }

    // Vista: selector de cliente para ver cuenta corriente
    @GetMapping("/clientes/cuenta")
    public String seleccionarClienteCuenta(Model model) {
        model.addAttribute("clientes", clienteService.findAll(null));
        // Sin cliente seleccionado aún
        return "clientes/cuenta";
    }

    // Vista: detalle de cuenta corriente del cliente
    @GetMapping("/clientes/{id}/cuenta")
    public String verCuentaCliente(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return clienteService.findById(id)
                .map(c -> {
                    model.addAttribute("clientes", clienteService.findAll(null));
                    model.addAttribute("cliente", c);
                    model.addAttribute("saldo", c.getSaldoPendiente());
                    model.addAttribute("movimientos", clienteService.findUltimosMovimientos(id, 20));
                    return "clientes/cuenta";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Cliente no encontrado");
                    return "redirect:/facturas/clientes/cuenta";
                });
    }

    // ====== Fin cuenta corriente ======
}