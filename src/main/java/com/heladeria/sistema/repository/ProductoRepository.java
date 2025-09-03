package com.heladeria.sistema.repository;

import com.heladeria.sistema.model.Producto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @EntityGraph(attributePaths = "categoria")
    @Query("select p from Producto p")
    List<Producto> findAllWithCategoria();
}