package com.proyecto.repository;
 
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.model.DetallePedido;
 
@Repository
public interface IDetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
 
    // Obtener detalle de un pedido específico
    List<DetallePedido> findByPedidoPedidoId(Integer pedidoId);
}