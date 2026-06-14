package com.proyecto.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.model.DetallePedido;
import com.proyecto.repository.IDetallePedidoRepository;

@Service
public class DetallePedidoService {

    @Autowired
    private IDetallePedidoRepository repo;

    public List<DetallePedido> listarDetalles() {
        return repo.findAll();
    }

    public DetallePedido obtenerDetallePorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public DetallePedido agregarDetalle(DetallePedido detalle) {
        return repo.save(detalle);
    }
}