package com.heladeria.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.heladeria.sistema.model.DetalleFactura;

public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {
	// ...existing code...
}