package com.proyecto.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.model.Cliente;
import com.proyecto.repository.IClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private IClienteRepository repo;

    public List<Cliente> listarClientes() {
        return repo.findAll();
    }

    public Cliente obtenerClientePorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public Cliente agregarCliente(Cliente nuevo) {
        return repo.save(nuevo);
    }

    public Cliente actualizarCliente(Integer id, Cliente cliente) {

        Cliente existente = repo.findById(id).orElse(null);

        if(existente != null) {

            existente.setNombre(cliente.getNombre());
            existente.setEmail(cliente.getEmail());
            existente.setTelefono(cliente.getTelefono());

            return repo.save(existente);
        }

        return null;
    }

    public void eliminarCliente(Integer id) {
        repo.deleteById(id);
    }
}