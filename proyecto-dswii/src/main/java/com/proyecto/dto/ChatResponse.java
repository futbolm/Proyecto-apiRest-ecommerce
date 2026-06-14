package com.proyecto.dto;
 
public class ChatResponse {
    private String respuesta;
    private String rol = "assistant";
 
    public ChatResponse() {}
 
    public ChatResponse(String respuesta) {
        this.respuesta = respuesta;
    }
 
    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}