package com.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "CategoriaId")
   private Integer categoriaId;

   @NotBlank(message = "La descripción es obligatoria")
   @Size(min = 2, max = 60, message = "La descripción debe tener entre 2 y 60 caracteres")
   @Column(name = "Descripcion", nullable = false, length = 60)
   private String descripcion;
}