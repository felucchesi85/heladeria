package com.heladeria.sistema.service.dto;

import com.heladeria.sistema.model.Cliente;
import com.heladeria.sistema.model.Producto;

import java.math.BigDecimal;
import java.util.List;

public class StatsResumen {
    private final Producto productoMasVendido;
    private final long cantidadProductoMasVendido;
    private final Cliente mejorCliente;
    private final BigDecimal totalMejorCliente;
    private final BigDecimal ventasDia;
    private final BigDecimal ventasSemana;
    private final BigDecimal ventasMes;
    private final List<Producto> productosStockBajo;

    public StatsResumen(Producto productoMasVendido, long cantidadProductoMasVendido,
                        Cliente mejorCliente, BigDecimal totalMejorCliente,
                        BigDecimal ventasDia, BigDecimal ventasSemana, BigDecimal ventasMes,
                        List<Producto> productosStockBajo) {
        this.productoMasVendido = productoMasVendido;
        this.cantidadProductoMasVendido = cantidadProductoMasVendido;
        this.mejorCliente = mejorCliente;
        this.totalMejorCliente = totalMejorCliente;
        this.ventasDia = ventasDia;
        this.ventasSemana = ventasSemana;
        this.ventasMes = ventasMes;
        this.productosStockBajo = productosStockBajo;
    }

    public Producto getProductoMasVendido() { return productoMasVendido; }
    public long getCantidadProductoMasVendido() { return cantidadProductoMasVendido; }
    public Cliente getMejorCliente() { return mejorCliente; }
    public BigDecimal getTotalMejorCliente() { return totalMejorCliente; }
    public BigDecimal getVentasDia() { return ventasDia; }
    public BigDecimal getVentasSemana() { return ventasSemana; }
    public BigDecimal getVentasMes() { return ventasMes; }
    public List<Producto> getProductosStockBajo() { return productosStockBajo; }
}