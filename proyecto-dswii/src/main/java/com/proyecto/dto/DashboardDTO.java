package com.proyecto.dto;
 
import java.math.BigDecimal;
import java.util.List;
 
public class DashboardDTO {
 
    private Long totalClientes;
    private Long totalProductos;
    private Long totalPedidos;
    private Long pedidosPendientes;
    private Long pedidosCompletados;
    private Long pedidosCancelados;
    private BigDecimal totalIngresos;
    private Long totalResenas;
    private Double promedioCalificaciones;
    private List<ProductoStockBajoDTO> productosStockBajo;
    private List<TopClienteDTO> topClientes;
 
    public DashboardDTO() {}
 
    // Clase interna: productos con stock bajo
    public static class ProductoStockBajoDTO {
        private Integer productoId;
        private String nombre;
        private Integer stock;
 
        public ProductoStockBajoDTO(Integer productoId, String nombre, Integer stock) {
            this.productoId = productoId;
            this.nombre = nombre;
            this.stock = stock;
        }
        public Integer getProductoId() { return productoId; }
        public String getNombre() { return nombre; }
        public Integer getStock() { return stock; }
    }
 
    // Clase interna: top clientes por pedidos
    public static class TopClienteDTO {
        private Integer clienteId;
        private String nombre;
        private Long totalPedidos;
        private BigDecimal totalGastado;
 
        public TopClienteDTO(Integer clienteId, String nombre, Long totalPedidos, BigDecimal totalGastado) {
            this.clienteId = clienteId;
            this.nombre = nombre;
            this.totalPedidos = totalPedidos;
            this.totalGastado = totalGastado;
        }
        public Integer getClienteId() { return clienteId; }
        public String getNombre() { return nombre; }
        public Long getTotalPedidos() { return totalPedidos; }
        public BigDecimal getTotalGastado() { return totalGastado; }
    }
 
    // Getters y Setters
    public Long getTotalClientes() { return totalClientes; }
    public void setTotalClientes(Long totalClientes) { this.totalClientes = totalClientes; }
    public Long getTotalProductos() { return totalProductos; }
    public void setTotalProductos(Long totalProductos) { this.totalProductos = totalProductos; }
    public Long getTotalPedidos() { return totalPedidos; }
    public void setTotalPedidos(Long totalPedidos) { this.totalPedidos = totalPedidos; }
    public Long getPedidosPendientes() { return pedidosPendientes; }
    public void setPedidosPendientes(Long pedidosPendientes) { this.pedidosPendientes = pedidosPendientes; }
    public Long getPedidosCompletados() { return pedidosCompletados; }
    public void setPedidosCompletados(Long pedidosCompletados) { this.pedidosCompletados = pedidosCompletados; }
    public Long getPedidosCancelados() { return pedidosCancelados; }
    public void setPedidosCancelados(Long pedidosCancelados) { this.pedidosCancelados = pedidosCancelados; }
    public BigDecimal getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }
    public Long getTotalResenas() { return totalResenas; }
    public void setTotalResenas(Long totalResenas) { this.totalResenas = totalResenas; }
    public Double getPromedioCalificaciones() { return promedioCalificaciones; }
    public void setPromedioCalificaciones(Double promedioCalificaciones) { this.promedioCalificaciones = promedioCalificaciones; }
    public List<ProductoStockBajoDTO> getProductosStockBajo() { return productosStockBajo; }
    public void setProductosStockBajo(List<ProductoStockBajoDTO> productosStockBajo) { this.productosStockBajo = productosStockBajo; }
    public List<TopClienteDTO> getTopClientes() { return topClientes; }
    public void setTopClientes(List<TopClienteDTO> topClientes) { this.topClientes = topClientes; }
}