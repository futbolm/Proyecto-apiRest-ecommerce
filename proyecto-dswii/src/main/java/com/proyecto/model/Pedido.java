package com.proyecto.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PedidoId")
    private Integer pedidoId;

    @ManyToOne
    @JoinColumn(name = "ClienteId")
    private Cliente cliente;

    @Column(name = "FechaPedido")
    private LocalDateTime fechaPedido;

    @Column(name = "Total")
    private BigDecimal total;

    @Column(name = "Estado")
    private String estado;
}