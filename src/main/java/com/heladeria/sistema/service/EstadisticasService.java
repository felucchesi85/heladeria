package com.heladeria.sistema.service;

import com.heladeria.sistema.model.Cliente;
import com.heladeria.sistema.model.DetalleFactura;
import com.heladeria.sistema.model.Factura;
import com.heladeria.sistema.model.Producto;
import com.heladeria.sistema.repository.ClienteRepository;
import com.heladeria.sistema.repository.DetalleFacturaRepository;
import com.heladeria.sistema.repository.FacturaRepository;
import com.heladeria.sistema.repository.ProductoRepository;
import com.heladeria.sistema.service.dto.StatsResumen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {

    private final FacturaRepository facturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    @Value("${heladeria.stock.minimo:5}")
    private int stockMinimo;

    public EstadisticasService(FacturaRepository facturaRepository,
                               DetalleFacturaRepository detalleFacturaRepository,
                               ProductoRepository productoRepository,
                               ClienteRepository clienteRepository) {
        this.facturaRepository = facturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public StatsResumen obtenerResumen() {
        // Ventas por período
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.with(DayOfWeek.MONDAY);
        LocalDate inicioMes = hoy.withDayOfMonth(1);

        BigDecimal ventasDia = BigDecimal.ZERO;
        BigDecimal ventasSemana = BigDecimal.ZERO;
        BigDecimal ventasMes = BigDecimal.ZERO;

        List<Factura> facturas = facturaRepository.findAll();
        for (Factura f : facturas) {
            if (f.getFecha() == null) continue;
            BigDecimal total = f.getTotal() != null ? f.getTotal() : BigDecimal.ZERO;
            if (f.getFecha().isEqual(hoy)) ventasDia = ventasDia.add(total);
            if (!f.getFecha().isBefore(inicioSemana) && !f.getFecha().isAfter(hoy)) ventasSemana = ventasSemana.add(total);
            if (!f.getFecha().isBefore(inicioMes) && !f.getFecha().isAfter(hoy)) ventasMes = ventasMes.add(total);
        }

        // Producto más vendido (usar IDs para evitar inicializar proxies)
        Producto productoMasVendido = null;
        long cantidadProductoMasVendido = 0L;
        Map<Long, Long> cantPorProductoId = new HashMap<>();

        for (DetalleFactura d : detalleFacturaRepository.findAll()) {
            if (d.getProducto() == null || d.getCantidad() == null) continue;
            Long prodId = d.getProducto().getId(); // no inicializa proxy
            if (prodId == null) continue;
            cantPorProductoId.merge(prodId, d.getCantidad().longValue(), Long::sum);
        }
        Long prodIdMax = null;
        for (Map.Entry<Long, Long> e : cantPorProductoId.entrySet()) {
            if (e.getValue() > cantidadProductoMasVendido) {
                prodIdMax = e.getKey();
                cantidadProductoMasVendido = e.getValue();
            }
        }
        if (prodIdMax != null) {
            productoMasVendido = productoRepository.findById(prodIdMax).orElse(null);
        }

        // Mejor cliente (también por ID)
        Cliente mejorCliente = null;
        BigDecimal totalMejorCliente = BigDecimal.ZERO;
        Map<Long, BigDecimal> totalPorClienteId = new HashMap<>();

        for (Factura f : facturas) {
            if (f.getCliente() == null) continue;
            Long cliId = f.getCliente().getId();
            if (cliId == null) continue;
            BigDecimal t = f.getTotal() != null ? f.getTotal() : BigDecimal.ZERO;
            totalPorClienteId.merge(cliId, t, BigDecimal::add);
        }
        Long cliIdMax = null;
        for (Map.Entry<Long, BigDecimal> e : totalPorClienteId.entrySet()) {
            if (e.getValue().compareTo(totalMejorCliente) > 0) {
                cliIdMax = e.getKey();
                totalMejorCliente = e.getValue();
            }
        }
        if (cliIdMax != null) {
            mejorCliente = clienteRepository.findById(cliIdMax).orElse(null);
        }

        // Stock bajo
        List<Producto> productosStockBajo = productoRepository.findAll().stream()
                .filter(p -> p.getStock() != null && p.getStock() <= stockMinimo)
                .sorted(Comparator.comparing(Producto::getStock))
                .collect(Collectors.toList());

        return new StatsResumen(
                productoMasVendido, cantidadProductoMasVendido,
                mejorCliente, totalMejorCliente,
                ventasDia, ventasSemana, ventasMes,
                productosStockBajo
        );
    }
}