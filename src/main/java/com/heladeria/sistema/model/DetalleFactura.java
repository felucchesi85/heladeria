package com.heladeria.sistema.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "det_detalle_factura")
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_factura",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_detalle_factura")
    )
    private Factura factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_producto",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_detalle_producto")
    )
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad = 0;

    @Column(name = "precio_unitario", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioUnitario = BigDecimal.ZERO;

    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    public DetalleFactura() {}

    public DetalleFactura(Producto producto, Integer cantidad, BigDecimal precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = calcularSubtotal();
    }

    private BigDecimal calcularSubtotal() {
        return precioUnitario != null && cantidad != null
            ? precioUnitario.multiply(BigDecimal.valueOf(cantidad))
            : BigDecimal.ZERO;
    }

    public void actualizarSubtotal() {
        this.subtotal = calcularSubtotal();
    }

    public Long getId() { return id; }
    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; actualizarSubtotal(); }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; actualizarSubtotal(); }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleFactura that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return 31; }

    @Override
    public String toString() {
        return "DetalleFactura{id=%d, producto=%s, cantidad=%d}".formatted(
            id, producto != null ? producto.getNombre() : "-", cantidad
        );
    }
}