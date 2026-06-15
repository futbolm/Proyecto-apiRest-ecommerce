package com.proyecto.services;
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.dto.CarritoItemRequest;
import com.proyecto.dto.ConfirmarPedidoRequest;
import com.proyecto.dto.DetallePedidoDTO;
import com.proyecto.dto.PedidoDTO;
import com.proyecto.model.Cliente;
import com.proyecto.model.DetallePedido;
import com.proyecto.model.Pedido;
import com.proyecto.model.Producto;
import com.proyecto.repository.IClienteRepository;
import com.proyecto.repository.IDetallePedidoRepository;
import com.proyecto.repository.IPedidoRepository;
import com.proyecto.repository.IProductoRepository;
 
@Service
public class PedidoService {
 
    @Autowired
    private IPedidoRepository pedidoRepo;
 
    @Autowired
    private IDetallePedidoRepository detalleRepo;
 
    @Autowired
    private IClienteRepository clienteRepo;
 
    @Autowired
    private IProductoRepository productoRepo;
    
    //esto es para el gmail la notificacion
    @Autowired
    private EmailService emailService;
 
    // Pedido entidad → DTO
    private PedidoDTO toDTO(Pedido p) {
        PedidoDTO dto = new PedidoDTO();
        dto.setPedidoId(p.getPedidoId());
        dto.setFechaPedido(p.getFechaPedido());
        dto.setTotal(p.getTotal());
        dto.setEstado(p.getEstado());
        if (p.getCliente() != null) {
            dto.setClienteId(p.getCliente().getClienteId());
            dto.setClienteNombre(p.getCliente().getNombre());
        }
        return dto;
    }
 
    // DetallePedido entidad → DTO
    private DetallePedidoDTO toDetalleDTO(DetallePedido d) {
        DetallePedidoDTO dto = new DetallePedidoDTO();
        dto.setDetallePedidoId(d.getDetallePedidoId());
        dto.setCantidad(d.getCantidad());
        dto.setPrecioUnitario(d.getPrecioUnitario());
        if (d.getPedido() != null)
            dto.setPedidoId(d.getPedido().getPedidoId());
        if (d.getProducto() != null) {
            dto.setProductoId(d.getProducto().getProductoId());
            dto.setProductoNombre(d.getProducto().getNombre());
            // Calculamos subtotal
            if (d.getPrecioUnitario() != null && d.getCantidad() != null)
                dto.setSubtotal(d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())));
        }
        return dto;
    }
 
    public List<PedidoDTO> listarPedidos() {
        return pedidoRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public List<PedidoDTO> listarPedidosPorCliente(Integer clienteId) {
        return pedidoRepo.findByClienteClienteIdOrderByFechaPedidoDesc(clienteId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public PedidoDTO obtenerPedidoPorId(Integer id) {
        Pedido p = pedidoRepo.findById(id).orElse(null);
        if (p == null) return null;
        return toDTO(p);
    }
 
    public List<DetallePedidoDTO> listarDetallePorPedido(Integer pedidoId) {
        return detalleRepo.findByPedidoPedidoId(pedidoId)
                .stream().map(this::toDetalleDTO).collect(Collectors.toList());
    }
 
    // Confirmar pedido completo: crea pedido + detalle + descuenta stock
    // Equivalente al método Confirmar() del CarritoController que realice en el proyecto mvc 
    @Transactional
    public PedidoDTO confirmarPedido(ConfirmarPedidoRequest request) {
        Cliente cliente = clienteRepo.findById(request.getClienteId()).orElse(null);
        if (cliente == null) return null;
 
        // Calcular total
        BigDecimal total = request.getItems().stream()
                .map(i -> i.getPrecioUnitario().multiply(BigDecimal.valueOf(i.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
 
        // Creamos el pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setTotal(total);
        pedido.setEstado("Pendiente");
        Pedido pedidoGuardado = pedidoRepo.save(pedido);
 
        // Guardar cada ítem del carrito como detalle + descontar stock
        for (CarritoItemRequest item : request.getItems()) {
            Producto producto = productoRepo.findById(item.getProductoId()).orElse(null);
            if (producto == null) continue;
 
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedidoGuardado);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalleRepo.save(detalle);
 
            // Descontar stock
            productoRepo.descontarStock(item.getProductoId(), item.getCantidad());
        }
        
        
        // Enviar email de confirmación (en segundo plano con @Async)
        try {
            // Obtener el email del cliente
            String emailCliente = cliente.getEmail();
            String nombreCliente = cliente.getNombre();
         
            // Obtener detalle para el email
            List<DetallePedidoDTO> detalleEmail = listarDetallePorPedido(pedidoGuardado.getPedidoId());
         
            emailService.enviarConfirmacionPedido(
                emailCliente,
                nombreCliente,
                pedidoGuardado.getPedidoId(),
                pedidoGuardado.getTotal(),
                detalleEmail
            );
        } catch (Exception e) {
            // Si falla el email, el pedido igual queda confirmado
            System.err.println("Error al enviar email de confirmación: " + e.getMessage());
        }
 
        return toDTO(pedidoGuardado);
    }
 
    public PedidoDTO actualizarEstado(Integer pedidoId, String nuevoEstado) {
        Pedido pedido = pedidoRepo.findById(pedidoId).orElse(null);
        if (pedido == null) return null;
        pedido.setEstado(nuevoEstado);
        return toDTO(pedidoRepo.save(pedido));
    }
 
    public void eliminarPedido(Integer id) {
        pedidoRepo.deleteById(id);
    }
}