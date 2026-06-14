package com.proyecto.model;
 
import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
 
@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductoId")
    private Integer productoId;
 
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "Nombre", nullable = false)
    private String nombre;
 
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Column(name = "Precio")
    private BigDecimal precio;
 
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "Stock")
    private Integer stock;
 
    @Column(name = "Imagen")
    private String imagen;
 
    @ManyToOne
    @JoinColumn(name = "CategoriaId")
    private Categoria categoria;
 
    @ManyToOne
    @JoinColumn(name = "ProveedorId")
    private Proveedor proveedor;
}