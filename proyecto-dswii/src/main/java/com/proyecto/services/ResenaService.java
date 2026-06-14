package com.proyecto.services;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyecto.dto.ResenaDTO;
import com.proyecto.dto.ResenaResumenDTO;
import com.proyecto.model.Cliente;
import com.proyecto.model.Producto;
import com.proyecto.model.Resena;
import com.proyecto.repository.IClienteRepository;
import com.proyecto.repository.IProductoRepository;
import com.proyecto.repository.IResenaRepository;
 
@Service
public class ResenaService {
 
    @Autowired
    private IResenaRepository repo;
 
    @Autowired
    private IProductoRepository productoRepo;
 
    @Autowired
    private IClienteRepository clienteRepo;
 
    private ResenaDTO toDTO(Resena r) {
        ResenaDTO dto = new ResenaDTO();
        dto.setResenaId(r.getResenaId());
        dto.setCalificacion(r.getCalificacion());
        dto.setComentario(r.getComentario());
        dto.setClienteNombre(r.getClienteNombre());
        dto.setFechaResena(r.getFechaResena());
        if (r.getProducto() != null) {
            dto.setProductoId(r.getProducto().getProductoId());
            dto.setProductoNombre(r.getProducto().getNombre());
        }
        if (r.getCliente() != null) {
            dto.setClienteId(r.getCliente().getClienteId());
        }
        return dto;
    }
 
    public List<ResenaDTO> listarResenas() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public List<ResenaDTO> listarResenasPorProducto(Integer productoId) {
        return repo.findByProductoProductoIdOrderByFechaResenaDesc(productoId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public ResenaResumenDTO obtenerResumen(Integer productoId) {
        Long total = repo.countByProductoProductoId(productoId);
        Double promedio = repo.promedioCalificacion(productoId);
        return new ResenaResumenDTO(productoId, total, promedio != null ? promedio : 0.0);
    }
 
    public ResenaDTO agregarResena(Integer productoId, Integer clienteId, Resena resena) {
        // Evitar reseñas duplicadas del mismo cliente al mismo producto
        if (repo.existsByProductoProductoIdAndClienteClienteId(productoId, clienteId))
            return null;
 
        Producto producto = productoRepo.findById(productoId).orElse(null);
        Cliente cliente = clienteRepo.findById(clienteId).orElse(null);
        if (producto == null || cliente == null) return null;
 
        resena.setProducto(producto);
        resena.setCliente(cliente);
        resena.setClienteNombre(cliente.getNombre());
        resena.setFechaResena(LocalDateTime.now());
        return toDTO(repo.save(resena));
    }
}