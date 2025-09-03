package com.heladeria.sistema.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cli_cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    // Nuevo: apellido
    @Column(name = "apellido", length = 150)
    private String apellido;

    // Nuevo: email
    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "direccion", length = 250)
    private String direccion;

    @Column(name = "telefono", length = 50)
    private String telefono;

    @Column(name = "saldo_pendiente", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoPendiente = BigDecimal.ZERO;

    // Cuenta corriente: movimientos (fecha, monto, observacion)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cli_movimiento", joinColumns = @JoinColumn(name = "id_cliente"))
    @OrderBy("fecha DESC")
    private List<MovimientoCuenta> movimientos = new ArrayList<>();

    public Cliente() {}

    public Cliente(String nombre, String direccion, String telefono, BigDecimal saldoPendiente) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.saldoPendiente = saldoPendiente;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Nuevo: getters/setters apellido
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    // Nuevo: getters/setters email
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }

    public List<MovimientoCuenta> getMovimientos() { return movimientos; }
    public void setMovimientos(List<MovimientoCuenta> movimientos) { this.movimientos = movimientos; }

    // Embeddable para los movimientos de cuenta
    @Embeddable
    public static class MovimientoCuenta {
        @Column(name = "fecha", nullable = false)
        private LocalDate fecha;

        // Permitir saldo/movimientos negativos
        @Column(name = "monto", nullable = false, precision = 15, scale = 2)
        private BigDecimal monto;

        @Column(name = "observacion", length = 250)
        private String observacion;

        public MovimientoCuenta() {}
        public MovimientoCuenta(LocalDate fecha, BigDecimal monto, String observacion) {
            this.fecha = fecha;
            this.monto = monto;
            this.observacion = observacion;
        }

        public LocalDate getFecha() { return fecha; }
        public void setFecha(LocalDate fecha) { this.fecha = fecha; }
        public BigDecimal getMonto() { return monto; }
        public void setMonto(BigDecimal monto) { this.monto = monto; }
        public String getObservacion() { return observacion; }
        public void setObservacion(String observacion) { this.observacion = observacion; }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return 31; }

    @Override
    public String toString() {
        // Antes: "Cliente{id=%d, nombre='%s'}"
        String ap = apellido != null ? apellido : "";
        String em = email != null ? email : "";
        return "Cliente{id=%d, nombre='%s', apellido='%s', email='%s'}".formatted(id, nombre, ap, em);
    }

}