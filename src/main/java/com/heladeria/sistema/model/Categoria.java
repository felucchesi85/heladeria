// filepath: c:\Users\flucchesi\Repo\heladeria_app\heladeria\src\main\java\com\heladeria\sistema\model\Categoria.java
package com.heladeria.sistema.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
    name = "cat_categoria",
    uniqueConstraints = @UniqueConstraint(name = "uk_cat_nombre", columnNames = "nombre"),
    indexes = @Index(name = "ix_cat_nombre", columnList = "nombre")
)
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.PERSIST)
    private List<Producto> productos = new ArrayList<>();

    public Categoria() {}

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public void agregarProducto(Producto p) {
        if (p == null) return;
        productos.add(p);
        p.setCategoria(this);
    }

    public void quitarProducto(Producto p) {
        if (p == null) return;
        productos.remove(p);
        if (p.getCategoria() == this) p.setCategoria(null);
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Producto> getProductos() { return productos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return 31; }

    @Override
    public String toString() {
        return "Categoria{id=%d, nombre='%s'}".formatted(id, nombre);
    }
}