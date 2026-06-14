package com.proyecto.controller;
 
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyecto.dto.ChatMensaje;
import com.proyecto.dto.ChatRequest;
import com.proyecto.dto.ChatResponse;
import com.proyecto.model.ApiResponse;
import com.proyecto.services.ChatService;
 
@RestController
@RequestMapping("/api/chat")
public class ChatController {
 
    @Autowired
    private ChatService chatService;
 
    // POST /api/chat
    // Body:
    // {
    //   "mensaje": "¿Qué laptops tienen disponibles?",
    //   "historial": []           ← vacío en el primer mensaje para el historial 
    // }
    //
    // Para continuar la conversación, incluir historial:
    // {
    //   "mensaje": "¿Y el mouse?",
    //   "historial": [
    //     { "role": "user",      "content": "¿Qué laptops tienen?" },
    //     { "role": "assistant", "content": "Tenemos la Laptop HP 15 a S/2500..." } y asi continuamos la conversacion
    //   ]
    // }
    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse>> chat(@RequestBody ChatRequest request) {
        if (request.getMensaje() == null || request.getMensaje().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("El mensaje no puede estar vacío", null));
        }
 
        List<ChatMensaje> historial = request.getHistorial() != null
                ? request.getHistorial()
                : List.of();
 
        String respuesta = chatService.responder(request.getMensaje(), historial);
 
        return ResponseEntity.ok(new ApiResponse<>(
                "Respuesta generada correctamente",
                new ChatResponse(respuesta)));
    }
}