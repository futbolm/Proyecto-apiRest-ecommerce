package com.proyecto.dto;
 
import java.util.List;
 
public class ConfirmarPedidoRequest {
    private Integer clienteId;
    private List<CarritoItemRequest> items;
 
    public ConfirmarPedidoRequest() {}
 
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
    public List<CarritoItemRequest> getItems() { return items; }
    public void setItems(List<CarritoItemRequest> items) { this.items = items; }
}