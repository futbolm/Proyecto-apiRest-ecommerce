package com.proyecto.dto;
 
public class ChatMensaje {
    private String role;    // para saber si es "user" o "assistant"
    private String content;
 
    public ChatMensaje() {}
 
    public ChatMensaje(String role, String content) {
        this.role = role;
        this.content = content;
    }
 
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}