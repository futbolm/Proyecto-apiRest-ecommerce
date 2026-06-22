package com.proyecto.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Column(name = "Calificacion")
    private Integer calificacion;

    @Size(max = 500, message = "El comentario no puede superar 500 caracteres")
    @Column(name = "Comentario")
    private String comentario;

    @Column(name = "FechaResena")
    private LocalDateTime fechaResena;
}