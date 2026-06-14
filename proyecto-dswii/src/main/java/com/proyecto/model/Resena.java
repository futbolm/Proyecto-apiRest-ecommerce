package com.proyecto.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resenas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ResenaId")
    private Integer resenaId;

    @ManyToOne
    @JoinColumn(name = "ProductoId")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "ClienteId")
    private Cliente cliente;

    @Column(name = "ClienteNombre")
    private String clienteNombre;

    @Column(name = "Calificacion")
    private Integer calificacion;

    @Column(name = "Comentario")
    private String comentario;

    @Column(name = "FechaResena")
    private LocalDateTime fechaResena;
}