package com.proyecto.dto;
 
import java.util.List;
 
public class PaginaDTO<T> {
    private List<T> contenido;       // los datos de esa página
    private int paginaActual;        // página actual (empieza en 0)
    private int totalPaginas;        // cuántas páginas hay en total
    private long totalElementos;     // cuántos registros hay en total
    private int tamanioPagina;       // cuántos por página
    private boolean primera;         // ¿es la primera página?
    private boolean ultima;          // ¿es la última página?
 
    public PaginaDTO() {}
 
    public PaginaDTO(List<T> contenido, int paginaActual, int totalPaginas,
                     long totalElementos, int tamanioPagina) {
        this.contenido = contenido;
        this.paginaActual = paginaActual;
        this.totalPaginas = totalPaginas;
        this.totalElementos = totalElementos;
        this.tamanioPagina = tamanioPagina;
        this.primera = paginaActual == 0;
        this.ultima = paginaActual >= totalPaginas - 1;
    }
 
    public List<T> getContenido() { return contenido; }
    public void setContenido(List<T> contenido) { this.contenido = contenido; }
    public int getPaginaActual() { return paginaActual; }
    public void setPaginaActual(int paginaActual) { this.paginaActual = paginaActual; }
    public int getTotalPaginas() { return totalPaginas; }
    public void setTotalPaginas(int totalPaginas) { this.totalPaginas = totalPaginas; }
    public long getTotalElementos() { return totalElementos; }
    public void setTotalElementos(long totalElementos) { this.totalElementos = totalElementos; }
    public int getTamanioPagina() { return tamanioPagina; }
    public void setTamanioPagina(int tamanioPagina) { this.tamanioPagina = tamanioPagina; }
    public boolean isPrimera() { return primera; }
    public void setPrimera(boolean primera) { this.primera = primera; }
    public boolean isUltima() { return ultima; }
    public void setUltima(boolean ultima) { this.ultima = ultima; }
}