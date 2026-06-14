package com.proyecto.controller;
 
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyecto.dto.LoginRequest;
import com.proyecto.dto.LoginResponse;
import com.proyecto.dto.RegistroRequest;
import com.proyecto.dto.UsuarioDTO;
import com.proyecto.model.ApiResponse;
import com.proyecto.services.UsuarioService;

import jakarta.validation.Valid;
 
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
 
    @Autowired
    private UsuarioService servicio;
 
    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> listarUsuarios() {
        List<UsuarioDTO> lista = servicio.listarUsuarios();
        if (lista.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>("No hay usuarios registrados", null));
        return ResponseEntity.ok(new ApiResponse<>("Lista de usuarios obtenida correctamente", lista));
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerUsuarioPorId(@PathVariable Integer id) {
        UsuarioDTO usuario = servicio.obtenerUsuarioPorId(id);
        if (usuario == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Usuario no encontrado", null));
        return ResponseEntity.ok(new ApiResponse<>("Usuario encontrado correctamente", usuario));
    }
 
    // POST /api/usuario/login
    // Body: { "email": "luis@gmail.com", "password": "1234" }
    // Respuesta: { token: "eyJ...", usuario: { ... } }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse response = servicio.login(request);
        if (response == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("Credenciales incorrectas", null));
        return ResponseEntity.ok(new ApiResponse<>("Login exitoso", response));
    }
 
    // POST /api/usuario/registro  — público, no necesitamos token para poder registrar un usuario
    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<UsuarioDTO>> registro(@Valid @RequestBody RegistroRequest request) {
        UsuarioDTO creado = servicio.registroCliente(request);
        if (creado == null)
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("El email ya está registrado", null));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Registro exitoso", creado));
    }
}