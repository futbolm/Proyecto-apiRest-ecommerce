package com.proyecto.services;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.dto.LoginRequest;
import com.proyecto.dto.LoginResponse;
import com.proyecto.dto.RegistroRequest;
import com.proyecto.dto.UsuarioDTO;
import com.proyecto.model.Cliente;
import com.proyecto.model.Usuario;
import com.proyecto.repository.IClienteRepository;
import com.proyecto.repository.IUsuarioRepository;
import com.proyecto.security.JwtUtil;
 
@Service
public class UsuarioService {
 
    @Autowired
    private IUsuarioRepository repo;
 
    @Autowired
    private IClienteRepository clienteRepo;
 
    @Autowired
    private PasswordEncoder passwordEncoder;  // BCrypt inyectado desde SecurityConfig
 
    @Autowired
    private JwtUtil jwtUtil;
 
    private UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsuarioId(u.getUsuarioId());
        dto.setNombre(u.getNombre());
        dto.setEmail(u.getEmail());
        dto.setRol(u.getRol());
        dto.setFechaRegistro(u.getFechaRegistro());
        if (u.getCliente() != null)
            dto.setClienteId(u.getCliente().getClienteId());
        return dto;
    }
 
    // LOGIN — busca por email, verifica BCrypt, devuelve token + datos
    public LoginResponse login(LoginRequest request) {
        Usuario u = repo.findByEmail(request.getEmail());
        if (u == null) return null;
 
        // Verificar contraseña con BCrypt
        if (!passwordEncoder.matches(request.getPassword(), u.getPassword()))
            return null;
 
        // Generaramos token con email Y rol para que el filtro pueda leer el rol
        String token = jwtUtil.generateTokenConRol(u.getEmail(), u.getRol());
        return new LoginResponse(token, toDTO(u));
    }
 
    // REGISTRO CLIENTE — encripta password con BCrypt
    @Transactional
    public UsuarioDTO registroCliente(RegistroRequest request) {
        if (repo.existsByEmail(request.getEmail())) return null;
 
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setFechaRegistro(LocalDateTime.now());
        Cliente clienteGuardado = clienteRepo.save(cliente);
 
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        // Encriptamos la contraseña con BCrypt
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol("Cliente");
        usuario.setCliente(clienteGuardado);
        usuario.setFechaRegistro(LocalDateTime.now());
        return toDTO(repo.save(usuario));
    }
 
    public List<UsuarioDTO> listarUsuarios() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public UsuarioDTO obtenerUsuarioPorId(Integer id) {
        Usuario u = repo.findById(id).orElse(null);
        if (u == null) return null;
        return toDTO(u);
    }
}