package com.proyecto.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.model.Categoria;
import com.proyecto.repository.ICategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private ICategoriaRepository repo;

    public List<Categoria> listarCategorias() {
        return repo.findAll();
    }

    public Categoria obtenerCategoriaPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public Categoria agregarCategoria(Categoria nueva) {
        return repo.save(nueva);
    }

    public Categoria actualizarCategoria(Integer id, Categoria categoria) {

        Categoria existente = repo.findById(id).orElse(null);

        if(existente != null) {
            existente.setDescripcion(categoria.getDescripcion());
            return repo.save(existente);
        }

        return null;
    }

    public void eliminarCategoria(Integer id) {
        repo.deleteById(id);
    }
}