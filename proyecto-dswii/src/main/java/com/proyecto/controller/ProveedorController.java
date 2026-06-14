package com.proyecto.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.model.ApiResponse;
import com.proyecto.model.Proveedor;
import com.proyecto.services.ProveedorService;

@RestController
@RequestMapping("/api/proveedor")
public class ProveedorController {

    @Autowired
    private ProveedorService servicio;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Proveedor>>> listarProveedores() {
        List<Proveedor> lista = servicio.listarProveedores();
        if (lista.isEmpty()) {
            ApiResponse<List<Proveedor>> response = new ApiResponse<>("No hay proveedores registrados", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        ApiResponse<List<Proveedor>> response = new ApiResponse<>("Lista de proveedores obtenida correctamente", lista);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Proveedor>> obtenerProveedorPorId(@PathVariable Integer id) {
        Proveedor proveedor = servicio.obtenerProveedorPorId(id);
        if (proveedor == null) {
            ApiResponse<Proveedor> response = new ApiResponse<>("Proveedor no encontrado", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ApiResponse<Proveedor> response = new ApiResponse<>("Proveedor encontrado correctamente", proveedor);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Proveedor>> agregarProveedor(@RequestBody Proveedor nuevo) {
        Proveedor creado = servicio.agregarProveedor(nuevo);
        ApiResponse<Proveedor> response = new ApiResponse<>("Proveedor creado correctamente", creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Proveedor>> actualizarProveedor(
            @PathVariable Integer id,
            @RequestBody Proveedor proveedor) {
        Proveedor actualizado = servicio.actualizarProveedor(id, proveedor);
        if (actualizado == null) {
            ApiResponse<Proveedor> response = new ApiResponse<>("Proveedor no encontrado para actualizar", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ApiResponse<Proveedor> response = new ApiResponse<>("Proveedor actualizado correctamente", actualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarProveedor(@PathVariable Integer id) {
        Proveedor existe = servicio.obtenerProveedorPorId(id);
        if (existe == null) {
            ApiResponse<Void> response = new ApiResponse<>("Proveedor no encontrado para eliminar", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        servicio.eliminarProveedor(id);
        ApiResponse<Void> response = new ApiResponse<>("Proveedor eliminado correctamente", null);
        return ResponseEntity.ok(response);
    }
}