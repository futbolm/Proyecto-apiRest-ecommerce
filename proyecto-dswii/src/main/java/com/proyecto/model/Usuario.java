package com.proyecto.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UsuarioId")
    private Integer usuarioId;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "Rol")
    private String rol;

    @ManyToOne
    @JoinColumn(name = "ClienteId")
    private Cliente cliente;

    @Column(name = "FechaRegistro")
    private LocalDateTime fechaRegistro;
}