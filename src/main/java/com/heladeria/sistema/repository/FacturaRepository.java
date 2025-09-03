package com.heladeria.sistema.repository;

import com.heladeria.sistema.model.Factura;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long>, JpaSpecificationExecutor<Factura> {
	// Lista paginada cargando cliente para evitar lazy en la vista
	@EntityGraph(attributePaths = { "cliente" })
	Page<Factura> findAll(Pageable pageable);

	// Lista con filtros + cliente cargado
	@EntityGraph(attributePaths = { "cliente" })
	Page<Factura> findAll(Specification<Factura> spec, Pageable pageable);

	// Detalle completo (cliente, detalles y producto)
	@EntityGraph(attributePaths = { "cliente", "detalles", "detalles.producto" })
	Optional<Factura> findWithDetailsById(Long id);

	@EntityGraph(attributePaths = { "cliente" })
	List<Factura> findAllByOrderByFechaDescIdDesc();

	// Cargar cliente para evitar LazyInitialization en facturas/form.html (tabla Últimas ventas)
	@EntityGraph(attributePaths = { "cliente" })
	List<Factura> findTop10ByOrderByFechaDescIdDesc();
}