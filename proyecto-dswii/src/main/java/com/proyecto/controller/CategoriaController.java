package com.proyecto.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.model.ApiResponse;
import com.proyecto.model.Categoria;
import com.proyecto.services.CategoriaService;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService servicio;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Categoria>>> listarCategorias() {
        List<Categoria> lista = servicio.listarCategorias();
        if (lista.isEmpty()) {
            ApiResponse<List<Categoria>> response = new ApiResponse<>("No hay categorías registradas", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        ApiResponse<List<Categoria>> response = new ApiResponse<>("Lista de categorías obtenida correctamente", lista);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> obtenerCategoriaPorId(@PathVariable Integer id) {
        Categoria categoria = servicio.obtenerCategoriaPorId(id);
        if (categoria == null) {
            ApiResponse<Categoria> response = new ApiResponse<>("Categoría no encontrada", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ApiResponse<Categoria> response = new ApiResponse<>("Categoría encontrada correctamente", categoria);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Categoria>> agregarCategoria(@RequestBody Categoria nueva) {
        Categoria creada = servicio.agregarCategoria(nueva);
        ApiResponse<Categoria> response = new ApiResponse<>("Categoría creada correctamente", creada);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> actualizarCategoria(
            @PathVariable Integer id,
            @RequestBody Categoria categoria) {
        Categoria actualizada = servicio.actualizarCategoria(id, categoria);
        if (actualizada == null) {
            ApiResponse<Categoria> response = new ApiResponse<>("Categoría no encontrada para actualizar", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ApiResponse<Categoria> response = new ApiResponse<>("Categoría actualizada correctamente", actualizada);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCategoria(@PathVariable Integer id) {
        Categoria existe = servicio.obtenerCategoriaPorId(id);
        if (existe == null) {
            ApiResponse<Void> response = new ApiResponse<>("Categoría no encontrada para eliminar", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        servicio.eliminarCategoria(id);
        ApiResponse<Void> response = new ApiResponse<>("Categoría eliminada correctamente", null);
        return ResponseEntity.ok(response);
    }
}