package com.proyecto.dto;
 
import java.time.LocalDateTime;
 
public class ResenaDTO {
    private Integer resenaId;
    private Integer productoId;
    private String productoNombre;
    private Integer clienteId;
    private String clienteNombre;
    private Integer calificacion;
    private String comentario;
    private LocalDateTime fechaResena;
 
    public ResenaDTO() {}
 
    public Integer getResenaId() { return resenaId; }
    public void setResenaId(Integer resenaId) { this.resenaId = resenaId; }
    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getFechaResena() { return fechaResena; }
    public void setFechaResena(LocalDateTime fechaResena) { this.fechaResena = fechaResena; }
}