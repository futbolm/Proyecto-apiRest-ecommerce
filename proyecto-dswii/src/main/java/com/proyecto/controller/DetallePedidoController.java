package com.proyecto.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.model.ApiResponse;
import com.proyecto.model.DetallePedido;
import com.proyecto.services.DetallePedidoService;

@RestController
@RequestMapping("/api/detallepedido")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService servicio;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DetallePedido>>> listarDetalles() {
        List<DetallePedido> lista = servicio.listarDetalles();
        if (lista.isEmpty()) {
            ApiResponse<List<DetallePedido>> response = new ApiResponse<>("No hay detalles de pedido registrados", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        ApiResponse<List<DetallePedido>> response = new ApiResponse<>("Lista de detalles obtenida correctamente", lista);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DetallePedido>> obtenerDetallePorId(@PathVariable Integer id) {
        DetallePedido detalle = servicio.obtenerDetallePorId(id);
        if (detalle == null) {
            ApiResponse<DetallePedido> response = new ApiResponse<>("Detalle de pedido no encontrado", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ApiResponse<DetallePedido> response = new ApiResponse<>("Detalle encontrado correctamente", detalle);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DetallePedido>> agregarDetalle(@RequestBody DetallePedido detalle) {
        DetallePedido creado = servicio.agregarDetalle(detalle);
        ApiResponse<DetallePedido> response = new ApiResponse<>("Detalle de pedido agregado correctamente", creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}