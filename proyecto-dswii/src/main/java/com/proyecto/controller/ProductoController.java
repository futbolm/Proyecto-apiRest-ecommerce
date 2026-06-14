package com.proyecto.controller;
 
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.proyecto.dto.ActualizarStockRequest;
import com.proyecto.dto.PaginaDTO;
import com.proyecto.dto.ProductoDTO;
import com.proyecto.model.ApiResponse;
import com.proyecto.model.Producto;
import com.proyecto.services.ProductoService;
import jakarta.validation.Valid;
 
@RestController
@RequestMapping("/api/producto")
public class ProductoController {
 
    @Autowired
    private ProductoService servicio;
 
    // GET /api/producto              → todos sin paginar (para chatbot)
    // GET /api/producto?pagina=0&tamanio=5  → paginado
    // GET /api/producto?nombre=laptop       → búsqueda
    // GET /api/producto?nombre=laptop&pagina=0&tamanio=5 → búsqueda paginada
    // GET /api/producto?categoriaId=1       → por categoría
    @GetMapping
    public ResponseEntity<ApiResponse<?>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) Integer tamanio) {
 
        int p = (pagina != null) ? pagina : 0;
        int t = (tamanio != null) ? tamanio : 10;
 
        //  buscamos por nombre
        if (nombre != null && !nombre.isBlank()) {
            PaginaDTO<ProductoDTO> resultado = servicio.buscarPorNombre(nombre, p, t);
            if (resultado.getTotalElementos() == 0)
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse<>("No se encontraron productos con ese nombre", null));
            return ResponseEntity.ok(new ApiResponse<>("Búsqueda completada", resultado));
        }
 
        //  filtramos por categoría
        if (categoriaId != null) {
            PaginaDTO<ProductoDTO> resultado = servicio.buscarPorCategoria(categoriaId, p, t);
            return ResponseEntity.ok(new ApiResponse<>("Productos por categoría", resultado));
        }
 
        // para la paginación
        if (pagina != null || tamanio != null) {
            PaginaDTO<ProductoDTO> resultado = servicio.listarPaginado(p, t);
            return ResponseEntity.ok(new ApiResponse<>("Lista paginada de productos", resultado));
        }
 
        // Sin parámetros → lista completa
        List<ProductoDTO> lista = servicio.listarProductos();
        if (lista.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>("No hay productos registrados", null));
        return ResponseEntity.ok(new ApiResponse<>("Lista de productos obtenida correctamente", lista));
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtenerProductoPorId(@PathVariable Integer id) {
        if (id <= 0)
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("El ID debe ser mayor a 0", null));
        ProductoDTO producto = servicio.obtenerProductoPorId(id);
        if (producto == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Producto no encontrado", null));
        return ResponseEntity.ok(new ApiResponse<>("Producto encontrado correctamente", producto));
    }
 
    // @Valid activa las validaciones del modelo Producto
    @PostMapping
    public ResponseEntity<ApiResponse<ProductoDTO>> agregarProducto(
            @RequestParam Integer categoriaId,
            @RequestParam Integer proveedorId,
            @Valid @RequestBody Producto nuevo) {
        ProductoDTO creado = servicio.agregarProducto(categoriaId, proveedorId, nuevo);
        if (creado == null)
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Categoría o proveedor no encontrado", null));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Producto creado correctamente", creado));
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(
            @PathVariable Integer id,
            @RequestParam Integer categoriaId,
            @RequestParam Integer proveedorId,
            @Valid @RequestBody Producto producto) {
        ProductoDTO actualizado = servicio.actualizarProducto(id, categoriaId, proveedorId, producto);
        if (actualizado == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Producto, categoría o proveedor no encontrado", null));
        return ResponseEntity.ok(new ApiResponse<>("Producto actualizado correctamente", actualizado));
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(@PathVariable Integer id) {
        if (servicio.obtenerProductoPorId(id) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Producto no encontrado para eliminar", null));
        servicio.eliminarProducto(id);
        return ResponseEntity.ok(new ApiResponse<>("Producto eliminado correctamente", null));
    }
 
    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<Void>> descontarStock(
            @PathVariable Integer id,
            @RequestBody ActualizarStockRequest request) {
        boolean ok = servicio.descontarStock(id, request.getCantidad());
        if (!ok)
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Stock insuficiente o producto no encontrado", null));
        return ResponseEntity.ok(new ApiResponse<>("Stock actualizado correctamente", null));
    }
}