package com.proyecto.dto;
 
public class ResenaResumenDTO {
    private Integer productoId;
    private Long totalResenas;
    private Double promedio;
 
    public ResenaResumenDTO() {}
 
    public ResenaResumenDTO(Integer productoId, Long totalResenas, Double promedio) {
        this.productoId = productoId;
        this.totalResenas = totalResenas;
        this.promedio = promedio;
    }
 
    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
    public Long getTotalResenas() { return totalResenas; }
    public void setTotalResenas(Long totalResenas) { this.totalResenas = totalResenas; }
    public Double getPromedio() { return promedio; }
    public void setPromedio(Double promedio) { this.promedio = promedio; }
}