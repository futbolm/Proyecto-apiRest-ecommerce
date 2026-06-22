package com.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProveedorId")
    private Integer proveedorId;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Email(message = "El email no tiene un formato válido")
    @Column(name = "Email")
    private String email;

    @Pattern(regexp = "^[0-9]{9,15}$", message = "El teléfono debe tener entre 9 y 15 dígitos")
    @Column(name = "Telefono")
    private String telefono;

    @Pattern(regexp = "^[0-9]{11}$", message = "El RUC debe tener 11 dígitos")
    @Column(name = "RUC")
    private String ruc;
}