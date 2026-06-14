package com.proyecto.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.model.Proveedor;
import com.proyecto.repository.IProveedorRepository;

@Service
public class ProveedorService {

    @Autowired
    private IProveedorRepository repo;

    public List<Proveedor> listarProveedores() {
        return repo.findAll();
    }

    public Proveedor obtenerProveedorPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public Proveedor agregarProveedor(Proveedor nuevo) {
        return repo.save(nuevo);
    }

    public Proveedor actualizarProveedor(Integer id, Proveedor proveedor) {

        Proveedor existente = repo.findById(id).orElse(null);

        if(existente != null) {

            existente.setNombre(proveedor.getNombre());
            existente.setEmail(proveedor.getEmail());
            existente.setTelefono(proveedor.getTelefono());
            existente.setRuc(proveedor.getRuc());

            return repo.save(existente);
        }

        return null;
    }

    public void eliminarProveedor(Integer id) {
        repo.deleteById(id);
    }
}