package com.proyecto.model;
 
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
 
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClienteId")
    private Integer clienteId;
 
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    @Column(name = "Nombre", nullable = false)
    private String nombre;
 
    @Email(message = "El email no tiene un formato válido")
    @Column(name = "Email")
    private String email;
 
    @Pattern(regexp = "^[0-9]{9,15}$", message = "El teléfono debe tener entre 9 y 15 dígitos")
    @Column(name = "Telefono")
    private String telefono;
 
    @Column(name = "FechaRegistro")
    private LocalDateTime fechaRegistro;
}