package com.proyecto.repository;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto.model.Producto;
 
@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer> {
 
    // Búsqueda por nombre con paginación
    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
 
    // Búsqueda por categoría con paginación
    Page<Producto> findByCategoriaCategoriaId(Integer categoriaId, Pageable pageable);
 
    // Todos con paginación (para el listado general)
    Page<Producto> findAll(Pageable pageable);
 
    // Buscar productos con stock bajo (para el dashboard)
    @Query("SELECT p FROM Producto p WHERE p.stock <= :limite ORDER BY p.stock ASC")
    java.util.List<Producto> findProductosStockBajo(@Param("limite") Integer limite);
 
    // Descontar stock
    @Modifying
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad " +
           "WHERE p.productoId = :productoId AND p.stock >= :cantidad")
    int descontarStock(@Param("productoId") Integer productoId,
                       @Param("cantidad") Integer cantidad);
}