package com.heladeria.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.heladeria.sistema.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	// ...existing code...
}