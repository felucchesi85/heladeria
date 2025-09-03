package com.heladeria.sistema.service;

import com.heladeria.sistema.model.Factura;
import com.heladeria.sistema.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FacturaService {

    private final FacturaRepository repository;

    public FacturaService(FacturaRepository repository) {
        this.repository = repository;
    }

    public List<Factura> findAll() { return repository.findAllByOrderByFechaDescIdDesc(); }

    public Optional<Factura> findById(Long id) { return repository.findById(id); }

    @Transactional
    public Factura save(Factura factura) { return repository.save(factura); }

    public boolean existsById(Long id) { return repository.existsById(id); }

    @Transactional
    public void deleteById(Long id) { repository.deleteById(id); }

    public long count() { return repository.count(); }

    public List<Factura> findUltimas10() { return repository.findTop10ByOrderByFechaDescIdDesc(); }

    public Optional<Factura> findWithDetailsById(Long id) { return repository.findWithDetailsById(id); }
}