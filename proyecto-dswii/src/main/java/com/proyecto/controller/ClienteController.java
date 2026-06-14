package com.proyecto.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.model.ApiResponse;
import com.proyecto.model.Cliente;
import com.proyecto.services.ClienteService;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService servicio;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Cliente>>> listarClientes() {
        List<Cliente> lista = servicio.listarClientes();
        if (lista.isEmpty()) {
            ApiResponse<List<Cliente>> response = new ApiResponse<>("No hay clientes registrados", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        ApiResponse<List<Cliente>> response = new ApiResponse<>("Lista de clientes obtenida correctamente", lista);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> obtenerClientePorId(@PathVariable Integer id) {
        Cliente cliente = servicio.obtenerClientePorId(id);
        if (cliente == null) {
            ApiResponse<Cliente> response = new ApiResponse<>("Cliente no encontrado", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ApiResponse<Cliente> response = new ApiResponse<>("Cliente encontrado correctamente", cliente);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Cliente>> agregarCliente(@RequestBody Cliente nuevo) {
        Cliente creado = servicio.agregarCliente(nuevo);
        ApiResponse<Cliente> response = new ApiResponse<>("Cliente creado correctamente", creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> actualizarCliente(
            @PathVariable Integer id,
            @RequestBody Cliente cliente) {
        Cliente actualizado = servicio.actualizarCliente(id, cliente);
        if (actualizado == null) {
            ApiResponse<Cliente> response = new ApiResponse<>("Cliente no encontrado para actualizar", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ApiResponse<Cliente> response = new ApiResponse<>("Cliente actualizado correctamente", actualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCliente(@PathVariable Integer id) {
        Cliente existe = servicio.obtenerClientePorId(id);
        if (existe == null) {
            ApiResponse<Void> response = new ApiResponse<>("Cliente no encontrado para eliminar", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        servicio.eliminarCliente(id);
        ApiResponse<Void> response = new ApiResponse<>("Cliente eliminado correctamente", null);
        return ResponseEntity.ok(response);
    }
}