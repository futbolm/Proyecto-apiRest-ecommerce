package com.proyecto.dto;
 
import java.util.List;
 
public class ChatRequest {
    private String mensaje;
    private List<ChatMensaje> historial; // mensajes anteriores para contexto esto es para el historial del chat
 
    public ChatRequest() {}
 
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public List<ChatMensaje> getHistorial() { return historial; }
    public void setHistorial(List<ChatMensaje> historial) { this.historial = historial; }
}