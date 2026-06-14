package com.proyecto.controller;
 
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyecto.dto.ActualizarEstadoRequest;
import com.proyecto.dto.ConfirmarPedidoRequest;
import com.proyecto.dto.DetallePedidoDTO;
import com.proyecto.dto.PedidoDTO;
import com.proyecto.model.ApiResponse;
import com.proyecto.services.PedidoService;
 
@RestController
@RequestMapping("/api/pedido")
public class PedidoController {
 
    @Autowired
    private PedidoService servicio;
 
    //  todos los pedidos (Admin)
    @GetMapping
    public ResponseEntity<ApiResponse<List<PedidoDTO>>> listarPedidos() {
        List<PedidoDTO> lista = servicio.listarPedidos();
        if (lista.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>("No hay pedidos registrados", null));
        return ResponseEntity.ok(new ApiResponse<>("Lista de pedidos obtenida correctamente", lista));
    }
 
    // pedidos de un cliente específico
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<PedidoDTO>>> listarPedidosPorCliente(
            @PathVariable Integer clienteId) {
        List<PedidoDTO> lista = servicio.listarPedidosPorCliente(clienteId);
        if (lista.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>("No hay pedidos para este cliente", null));
        return ResponseEntity.ok(new ApiResponse<>("Pedidos del cliente obtenidos correctamente", lista));
    }
 
    //  pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidoDTO>> obtenerPedidoPorId(@PathVariable Integer id) {
        PedidoDTO pedido = servicio.obtenerPedidoPorId(id);
        if (pedido == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Pedido no encontrado", null));
        return ResponseEntity.ok(new ApiResponse<>("Pedido encontrado correctamente", pedido));
    }
 
    //  detalle de un pedido
    @GetMapping("/{id}/detalle")
    public ResponseEntity<ApiResponse<List<DetallePedidoDTO>>> listarDetalle(
            @PathVariable Integer id) {
        List<DetallePedidoDTO> lista = servicio.listarDetallePorPedido(id);
        if (lista.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>("No hay detalle para este pedido", null));
        return ResponseEntity.ok(new ApiResponse<>("Detalle del pedido obtenido correctamente", lista));
    }
 
    // POST confirmar pedido completo (carrito → pedido + detalle + descuenta stock)
    // Body esperado:
    // {
    //   "clienteId": 1,
    //   "items": [
    //     { "productoId": 1, "cantidad": 2, "precioUnitario": 2500.00 },
    //     { "productoId": 2, "cantidad": 1, "precioUnitario": 45.00 }
    //   ]
    // }
    @PostMapping("/confirmar")
    public ResponseEntity<ApiResponse<PedidoDTO>> confirmarPedido(
            @RequestBody ConfirmarPedidoRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty())
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("El carrito está vacío", null));
        PedidoDTO pedido = servicio.confirmarPedido(request);
        if (pedido == null)
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Cliente no encontrado o error al crear pedido", null));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Pedido confirmado correctamente", pedido));
    }
 
    // PUT actualizar estado del pedido (Admin)
    // en el Body: { "estado": "Completado" }
    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<PedidoDTO>> actualizarEstado(
            @PathVariable Integer id,
            @RequestBody ActualizarEstadoRequest request) {
        PedidoDTO actualizado = servicio.actualizarEstado(id, request.getEstado());
        if (actualizado == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Pedido no encontrado", null));
        return ResponseEntity.ok(new ApiResponse<>(
                "Estado actualizado a '" + request.getEstado() + "' correctamente", actualizado));
    }
 
    // eliminar pedido pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarPedido(@PathVariable Integer id) {
        PedidoDTO existe = servicio.obtenerPedidoPorId(id);
        if (existe == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Pedido no encontrado para eliminar", null));
        servicio.eliminarPedido(id);
        return ResponseEntity.ok(new ApiResponse<>("Pedido eliminado correctamente", null));
    }
}