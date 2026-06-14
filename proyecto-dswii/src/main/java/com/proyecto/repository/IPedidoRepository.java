package com.proyecto.repository;
 
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.proyecto.model.Pedido;
 
@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Integer> {
 
    List<Pedido> findByClienteClienteIdOrderByFechaPedidoDesc(Integer clienteId);
 
    Long countByEstado(String estado);
 
    // Total de ingresos solo de pedidos completados
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.estado = 'Completado'")
    BigDecimal sumTotalIngresos();
 
    // Top 5 clientes con más pedidos
    @Query("SELECT p.cliente.clienteId, p.cliente.nombre, COUNT(p), SUM(p.total) " +
           "FROM Pedido p GROUP BY p.cliente.clienteId, p.cliente.nombre " +
           "ORDER BY COUNT(p) DESC")
    List<Object[]> findTopClientes(org.springframework.data.domain.Pageable pageable);
}