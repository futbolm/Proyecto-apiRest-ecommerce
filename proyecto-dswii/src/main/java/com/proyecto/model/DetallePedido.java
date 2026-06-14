package com.proyecto.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detallepedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DetallePedidoId")
    private Integer detallePedidoId;

    @ManyToOne
    @JoinColumn(name = "PedidoId")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "ProductoId")
    private Producto producto;

    @Column(name = "Cantidad")
    private Integer cantidad;

    @Column(name = "PrecioUnitario")
    private BigDecimal precioUnitario;
}