package com.heladeria.sistema.service;

import com.heladeria.sistema.model.Cliente;
import com.heladeria.sistema.repository.ClienteRepository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> findAll(Specification<Cliente> spec) {
        return (spec != null) ? repository.findAll(spec) : repository.findAll();
    }

    public Optional<Cliente> findById(Long id) { return repository.findById(id); }

    @Transactional
    public Cliente save(Cliente cliente) { return repository.save(cliente); }

    public boolean existsById(Long id) { return repository.existsById(id); }

    @Transactional
    public void deleteById(Long id) { repository.deleteById(id); }

    public long count() { return repository.count(); }

    @Transactional
    public Cliente registrarMovimiento(Long idCliente, LocalDate fecha, BigDecimal monto, String observacion) {
        Cliente c = repository.findById(idCliente).orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        if (fecha == null) throw new IllegalArgumentException("Fecha requerida");
        if (monto == null) throw new IllegalArgumentException("Monto requerido");
        if (c.getMovimientos() == null) c.setMovimientos(new ArrayList<>());
        c.getMovimientos().add(new Cliente.MovimientoCuenta(fecha, monto, observacion));
        BigDecimal saldo = c.getSaldoPendiente() == null ? BigDecimal.ZERO : c.getSaldoPendiente();
        c.setSaldoPendiente(saldo.add(monto)); // permite negativos
        return repository.save(c);
    }

    @Transactional(readOnly = true)
    public List<Cliente.MovimientoCuenta> findUltimosMovimientos(Long idCliente, int limit) {
        Cliente c = repository.findById(idCliente).orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        List<Cliente.MovimientoCuenta> movs = c.getMovimientos() == null ? List.of() : c.getMovimientos();
        return movs.stream()
                .sorted(Comparator.comparing(Cliente.MovimientoCuenta::getFecha).reversed())
                .limit(Math.max(1, limit))
                .collect(Collectors.toList());
    }
}