package com.heladeria.sistema.service;

import com.heladeria.sistema.model.Producto;
import com.heladeria.sistema.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> findAll() { return repository.findAll(); }

    // Nuevo: productos con categoría cargada para evitar LazyInitialization en la vista
    public List<Producto> findAllConCategoria() { return repository.findAllWithCategoria(); }

    public Optional<Producto> findById(Long id) { return repository.findById(id); }

    @Transactional
    public Producto save(Producto producto) { return repository.save(producto); }

    public boolean existsById(Long id) { return repository.existsById(id); }

    @Transactional
    public void deleteById(Long id) { repository.deleteById(id); }

    public long count() { return repository.count(); }
}