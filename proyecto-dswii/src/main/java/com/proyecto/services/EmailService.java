package com.proyecto.services;
 
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.proyecto.dto.DetallePedidoDTO;
 
@Service
public class EmailService {
 
    @Value("${brevo.api.key}")
    private String apiKey;
 
    @Value("${brevo.from.email}")
    private String fromEmail;
 
    @Value("${brevo.from.name}")
    private String fromName;
 
    private final RestTemplate restTemplate = new RestTemplate();
 
    @Async
    public void enviarConfirmacionPedido(
            String emailDestino,
            String nombreCliente,
            Integer pedidoId,
            BigDecimal total,
            List<DetallePedidoDTO> items) {
        try {
            String html = construirHtml(nombreCliente, pedidoId, total, items);
 
            // Headers Brevo
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey); // Brevo usa api-key, no Bearer
 
            // Sender
            Map<String, String> sender = new HashMap<>();
            sender.put("email", fromEmail);
            sender.put("name", fromName);
 
            // Destinatario
            Map<String, String> destinatario = new HashMap<>();
            destinatario.put("email", emailDestino);
            destinatario.put("name", nombreCliente);
 
            // Body completo
            Map<String, Object> body = new HashMap<>();
            body.put("sender", sender);
            body.put("to", List.of(destinatario));
            body.put("subject", "✅ ShopZone — Pedido #" + pedidoId + " confirmado");
            body.put("htmlContent", html);
 
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
 
            // Llamamos a Brevo API
            restTemplate.postForEntity(
                "https://api.brevo.com/v3/smtp/email",
                entity,
                String.class
            );
 
            System.out.println("✅ Email enviado correctamente a: " + emailDestino);
 
        } catch (Exception e) {
            System.err.println("❌ Error al enviar email con Brevo: " + e.getMessage());
        }
    }
 
    private String construirHtml(
            String nombreCliente,
            Integer pedidoId,
            BigDecimal total,
            List<DetallePedidoDTO> items) {
 
        StringBuilder filas = new StringBuilder();
        for (DetallePedidoDTO item : items) {
            filas.append(String.format("""
                <tr>
                    <td style='padding:8px;border-bottom:1px solid #e5e7eb;'>%s</td>
                    <td style='padding:8px;border-bottom:1px solid #e5e7eb;
                               text-align:center;'>%d</td>
                    <td style='padding:8px;border-bottom:1px solid #e5e7eb;
                               text-align:right;color:#4f46e5;font-weight:700;'>
                        S/ %.2f
                    </td>
                </tr>
                """,
                item.getProductoNombre() != null ? item.getProductoNombre() : "—",
                item.getCantidad() != null ? item.getCantidad() : 0,
                item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO
            ));
        }
 
        return String.format("""
            <div style='font-family:Arial,sans-serif;max-width:600px;
                        margin:0 auto;background:#f8f7ff;padding:20px;'>
 
                <div style='background:linear-gradient(135deg,#3730a3,#4f46e5);
                            border-radius:16px;padding:30px;text-align:center;
                            margin-bottom:20px;'>
                    <h1 style='color:#fff;margin:0;font-size:1.8rem;'>
                        ShopZone
                    </h1>
                    <p style='color:rgba(255,255,255,.8);margin:8px 0 0;'>
                        Tu pedido fue confirmado!
                    </p>
                </div>
 
                <div style='background:#fff;border-radius:16px;padding:25px;
                            margin-bottom:16px;'>
                    <h2 style='color:#1e1b4b;margin-top:0;'>
                        Hola, %s
                    </h2>
                    <p style='color:#6b7280;'>
                        Tu pedido
                        <strong style='color:#4f46e5;'>#%d</strong>
                        ha sido registrado correctamente.
                    </p>
 
                    <table style='width:100%%;border-collapse:collapse;margin-top:16px;'>
                        <thead>
                            <tr style='background:#ede9fe;'>
                                <th style='padding:10px;text-align:left;
                                           color:#4f46e5;'>Producto</th>
                                <th style='padding:10px;text-align:center;
                                           color:#4f46e5;'>Cant.</th>
                                <th style='padding:10px;text-align:right;
                                           color:#4f46e5;'>Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>%s</tbody>
                    </table>
 
                    <div style='text-align:right;margin-top:16px;
                                padding-top:16px;border-top:2px solid #e0e7ff;'>
                        <span style='font-size:1.3rem;font-weight:800;color:#4f46e5;'>
                            Total: S/ %.2f
                        </span>
                    </div>
                </div>
 
                <p style='text-align:center;color:#9ca3af;font-size:.85rem;'>
                    Gracias por comprar en ShopZone 💪
                </p>
            </div>
            """,
            nombreCliente, pedidoId, filas.toString(), total
        );
    }
}