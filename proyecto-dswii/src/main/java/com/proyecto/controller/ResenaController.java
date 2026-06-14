package com.proyecto.controller;
 
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyecto.dto.ResenaDTO;
import com.proyecto.dto.ResenaResumenDTO;
import com.proyecto.model.ApiResponse;
import com.proyecto.model.Resena;
import com.proyecto.services.ResenaService;
 
@RestController
@RequestMapping("/api/resena")
public class ResenaController {
 
    @Autowired
    private ResenaService servicio;
 
    //  todas las reseñas
    @GetMapping
    public ResponseEntity<ApiResponse<List<ResenaDTO>>> listarResenas() {
        List<ResenaDTO> lista = servicio.listarResenas();
        if (lista.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>("No hay reseñas registradas", null));
        return ResponseEntity.ok(new ApiResponse<>("Lista de reseñas obtenida correctamente", lista));
    }
 
    //  reseñas de un producto específico
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<ApiResponse<List<ResenaDTO>>> listarPorProducto(
            @PathVariable Integer productoId) {
        List<ResenaDTO> lista = servicio.listarResenasPorProducto(productoId);
        if (lista.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>("No hay reseñas para este producto", null));
        return ResponseEntity.ok(new ApiResponse<>("Reseñas del producto obtenidas correctamente", lista));
    }
 
    //  resumen/promedio de calificaciones de un producto
    @GetMapping("/producto/{productoId}/resumen")
    public ResponseEntity<ApiResponse<ResenaResumenDTO>> resumenProducto(
            @PathVariable Integer productoId) {
        ResenaResumenDTO resumen = servicio.obtenerResumen(productoId);
        return ResponseEntity.ok(new ApiResponse<>("Resumen de reseñas obtenido correctamente", resumen));
    }
 
    // POST agregar reseña
    // asi le mandamos al Body: { "calificacion": 5, "comentario": "Excelente producto" }
    @PostMapping("/producto/{productoId}/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<ResenaDTO>> agregarResena(
            @PathVariable Integer productoId,
            @PathVariable Integer clienteId,
            @RequestBody Resena resena) {
        ResenaDTO creada = servicio.agregarResena(productoId, clienteId, resena);
        if (creada == null)
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Ya reseñaste este producto o datos inválidos", null));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Reseña registrada correctamente", creada));
    }
}