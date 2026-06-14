package com.proyecto.services;
 
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.dto.PaginaDTO;
import com.proyecto.dto.ProductoDTO;
import com.proyecto.model.Categoria;
import com.proyecto.model.Producto;
import com.proyecto.model.Proveedor;
import com.proyecto.repository.ICategoriaRepository;
import com.proyecto.repository.IProductoRepository;
import com.proyecto.repository.IProveedorRepository;
 
@Service
public class ProductoService {
 
    @Autowired private IProductoRepository repo;
    @Autowired private ICategoriaRepository categoriaRepo;
    @Autowired private IProveedorRepository proveedorRepo;
 
    private ProductoDTO toDTO(Producto p) {
        ProductoDTO dto = new ProductoDTO();
        dto.setProductoId(p.getProductoId());
        dto.setNombre(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setImagen(p.getImagen());
        if (p.getCategoria() != null) {
            dto.setCategoriaId(p.getCategoria().getCategoriaId());
            dto.setCategoriaNombre(p.getCategoria().getDescripcion());
        }
        if (p.getProveedor() != null) {
            dto.setProveedorId(p.getProveedor().getProveedorId());
            dto.setProveedorNombre(p.getProveedor().getNombre());
        }
        return dto;
    }
 
    // Lista simple (sin paginación — para el chatbot y compatibilidad)
    public List<ProductoDTO> listarProductos() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    // Lista CON paginación
    // página=0 es la primera, tamanio=10 devuelve 10 por página
    public PaginaDTO<ProductoDTO> listarPaginado(int pagina, int tamanio) {
        Pageable pageable = PageRequest.of(pagina, tamanio, Sort.by("nombre").ascending());
        Page<Producto> page = repo.findAll(pageable);
        List<ProductoDTO> contenido = page.getContent().stream()
                .map(this::toDTO).collect(Collectors.toList());
        return new PaginaDTO<>(contenido, page.getNumber(), page.getTotalPages(),
                page.getTotalElements(), page.getSize());
    }
 
    // Búsqueda por nombre CON paginación
    public PaginaDTO<ProductoDTO> buscarPorNombre(String nombre, int pagina, int tamanio) {
        Pageable pageable = PageRequest.of(pagina, tamanio, Sort.by("nombre").ascending());
        Page<Producto> page = repo.findByNombreContainingIgnoreCase(nombre, pageable);
        List<ProductoDTO> contenido = page.getContent().stream()
                .map(this::toDTO).collect(Collectors.toList());
        return new PaginaDTO<>(contenido, page.getNumber(), page.getTotalPages(),
                page.getTotalElements(), page.getSize());
    }
 
    // Búsqueda por categoría CON paginación
    public PaginaDTO<ProductoDTO> buscarPorCategoria(Integer categoriaId, int pagina, int tamanio) {
        Pageable pageable = PageRequest.of(pagina, tamanio, Sort.by("nombre").ascending());
        Page<Producto> page = repo.findByCategoriaCategoriaId(categoriaId, pageable);
        List<ProductoDTO> contenido = page.getContent().stream()
                .map(this::toDTO).collect(Collectors.toList());
        return new PaginaDTO<>(contenido, page.getNumber(), page.getTotalPages(),
                page.getTotalElements(), page.getSize());
    }
 
    public ProductoDTO obtenerProductoPorId(Integer id) {
        Producto p = repo.findById(id).orElse(null);
        return p == null ? null : toDTO(p);
    }
 
    public ProductoDTO agregarProducto(Integer categoriaId, Integer proveedorId, Producto producto) {
        Categoria cat = categoriaRepo.findById(categoriaId).orElse(null);
        Proveedor prov = proveedorRepo.findById(proveedorId).orElse(null);
        if (cat == null || prov == null) return null;
        producto.setCategoria(cat);
        producto.setProveedor(prov);
        return toDTO(repo.save(producto));
    }
 
    public ProductoDTO actualizarProducto(Integer id, Integer categoriaId, Integer proveedorId, Producto producto) {
        Producto existente = repo.findById(id).orElse(null);
        if (existente == null) return null;
        Categoria cat = categoriaRepo.findById(categoriaId).orElse(null);
        Proveedor prov = proveedorRepo.findById(proveedorId).orElse(null);
        if (cat == null || prov == null) return null;
        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setImagen(producto.getImagen());
        existente.setCategoria(cat);
        existente.setProveedor(prov);
        return toDTO(repo.save(existente));
    }
 
    public void eliminarProducto(Integer id) { repo.deleteById(id); }
 
    @Transactional
    public boolean descontarStock(Integer productoId, Integer cantidad) {
        return repo.descontarStock(productoId, cantidad) > 0;
    }
}