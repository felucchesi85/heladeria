package com.heladeria.sistema.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
    name = "fac_factura",
    uniqueConstraints = @UniqueConstraint(name = "uk_fac_numero", columnNames = "numero"),
    indexes = @Index(name = "ix_fac_fecha", columnList = "fecha")
)
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Long id;

    @Column(name = "numero", nullable = false, length = 30)
    private String numero;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_cliente",
        nullable = true, // antes false: permitir facturas sin cliente
        foreignKey = @ForeignKey(name = "fk_factura_cliente")
    )
    private Cliente cliente;

    @Column(name = "total", nullable = false, precision = 15, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(
        mappedBy = "factura",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<DetalleFactura> detalles = new ArrayList<>();

    public Factura() {}

    public Factura(String numero, LocalDate fecha, Cliente cliente) {
        this.numero = numero;
        this.fecha = fecha;
        this.cliente = cliente;
    }

    public void addDetalle(DetalleFactura d) {
        if (d == null) return;
        detalles.add(d);
        d.setFactura(this);
    }

    public void removeDetalle(DetalleFactura d) {
        if (d == null) return;
        detalles.remove(d);
        if (d.getFactura() == this) d.setFactura(null);
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public List<DetalleFactura> getDetalles() { return detalles; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Factura that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return 31; }

    @Override
    public String toString() {
        return "Factura{id=%d, numero='%s'}".formatted(id, numero);
    }
}