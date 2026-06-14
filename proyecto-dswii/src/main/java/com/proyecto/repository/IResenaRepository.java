package com.proyecto.repository;
 
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto.model.Resena;
 
@Repository
public interface IResenaRepository extends JpaRepository<Resena, Integer> {
 
    List<Resena> findByProductoProductoIdOrderByFechaResenaDesc(Integer productoId);
    Long countByProductoProductoId(Integer productoId);
    boolean existsByProductoProductoIdAndClienteClienteId(Integer productoId, Integer clienteId);
 
    @Query("SELECT AVG(r.calificacion) FROM Resena r WHERE r.producto.productoId = :productoId")
    Double promedioCalificacion(@Param("productoId") Integer productoId);
 
    // Promedio global de todas las reseñas (para el dashboard)
    @Query("SELECT AVG(r.calificacion) FROM Resena r")
    Double promedioGlobal();
}