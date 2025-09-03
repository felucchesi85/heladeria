package com.heladeria.sistema.service;

import com.heladeria.sistema.model.Categoria;
import com.heladeria.sistema.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> findAll() { return repository.findAll(); }

    public Optional<Categoria> findById(Long id) { return repository.findById(id); }

    @Transactional
    public Categoria save(Categoria categoria) { return repository.save(categoria); }

    public boolean existsById(Long id) { return repository.existsById(id); }

    @Transactional
    public void deleteById(Long id) { repository.deleteById(id); }

    public long count() { return repository.count(); }
}