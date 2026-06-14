package com.proyecto.services;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
 

import com.proyecto.dto.ChatMensaje;
import com.proyecto.dto.ProductoDTO;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
 
@Service
public class ChatService {
 
    @Value("${groq.api.key}")
    private String apiKey;
 
    @Value("${groq.api.url}")
    private String apiUrl;
 
    @Value("${groq.api.model}")
    private String model;
 
    @Autowired
    private ProductoService productoService;
 
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
 
    public String responder(String mensaje, List<ChatMensaje> historial) {
        try {
            // 1. Obtener catálogo actualizado de la BD
            String catalogo = obtenerCatalogo();
 
            // 2. System prompt igual al del proyecto .NET que realize pero mejorado
            String systemPrompt = "Eres ShopBot 🤖, el asistente virtual de ShopZone, " +
                "una tienda online peruana.\n" +
                "Ayudas a los clientes a encontrar productos y responder dudas.\n" +
                "Responde siempre en español y de forma breve y amigable.\n\n" +
                "CATÁLOGO ACTUAL (datos reales de la tienda):\n" +
                catalogo + "\n\n" +
                "POLÍTICAS:\n" +
                "- Envíos a todo el Perú en 3-5 días hábiles\n" +
                "- Devoluciones dentro de los 7 días de recibido\n" +
                "- Atención: shopzone855@gmail.com\n\n" +
                "IMPORTANTE: Si el stock es 0, indica que el producto no está disponible.\n" +
                "Si recomiendas productos, menciona el ID, nombre, precio y stock disponible.";
 
            // 3. Construir lista de mensajes para la API
            List<Map<String, String>> messages = new ArrayList<>();
 
            // System message
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", systemPrompt);
            messages.add(systemMsg);
 
            // Historial previo
            if (historial != null) {
                for (ChatMensaje h : historial) {
                    Map<String, String> msg = new HashMap<>();
                    msg.put("role", h.getRole());
                    msg.put("content", h.getContent());
                    messages.add(msg);
                }
            }
 
            // Mensaje actual del usuario
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", mensaje);
            messages.add(userMsg);
 
            // 4. Construir request body para Groq
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("max_tokens", 500);
            requestBody.put("messages", messages);
 
            // 5. Headers con API Key
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
 
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
 
            // 6. Llamar a Groq
            ResponseEntity<String> response = restTemplate.postForEntity(
                apiUrl, entity, String.class);
 
            // 7. Parsear respuesta
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices")
                       .get(0)
                       .path("message")
                       .path("content")
                       .asText("Lo siento, no pude procesar tu mensaje.");
 
        } catch (Exception e) {
            return "Error al conectar con el asistente: " + e.getMessage();
        }
    }
 
    // con esto Construimos el catálogo en texto para el contexto del bot
    private String obtenerCatalogo() {
        List<ProductoDTO> productos = productoService.listarProductos();
        if (productos.isEmpty()) return "No hay productos disponibles.";
 
        return productos.stream()
            .map(p -> String.format(
                "- ID:%d | %s | Precio: S/%.2f | Stock: %s | Categoría: %s",
                p.getProductoId(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock() != null && p.getStock() > 0
                    ? p.getStock().toString() + " unidades"
                    : "Sin stock",
                p.getCategoriaNombre() != null ? p.getCategoriaNombre() : "Sin categoría"
            ))
            .collect(Collectors.joining("\n"));
    }
}