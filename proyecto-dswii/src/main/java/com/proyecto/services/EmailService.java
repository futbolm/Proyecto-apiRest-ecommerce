package com.proyecto.services;
 
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.proyecto.dto.DetallePedidoDTO;
import jakarta.mail.internet.MimeMessage;
 
@Service
public class EmailService {
 
    @Autowired
    private JavaMailSender mailSender;
 
    @Value("${spring.mail.username}")
    private String remitente;
 
    // @Async para que el email se envíe en segundo plano
    // y no bloquee la respuesta del pedido al cliente
    @Async
    public void enviarConfirmacionPedido(
            String emailDestino,
            String nombreCliente,
            Integer pedidoId,
            java.math.BigDecimal total,
            List<DetallePedidoDTO> items) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
 
            helper.setFrom(remitente);
            helper.setTo(emailDestino);
            helper.setSubject("✅ ShopZone — Pedido #" + pedidoId + " confirmado");
            helper.setText(construirHtml(nombreCliente, pedidoId, total, items), true);
 
            mailSender.send(message);
        } catch (Exception e) {
            // Si falla el email, el pedido igual queda confirmado
            System.err.println("Error al enviar email: " + e.getMessage());
        }
    }
 
    // HTML del email — igual al del proyecto .NET con MailKit
    private String construirHtml(
            String nombreCliente,
            Integer pedidoId,
            java.math.BigDecimal total,
            List<DetallePedidoDTO> items) {
 
        // Construir filas de productos
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
                item.getProductoNombre(),
                item.getCantidad(),
                item.getSubtotal()
            ));
        }
 
        return String.format("""
            <div style='font-family:Arial,sans-serif;max-width:600px;
                        margin:0 auto;background:#f8f7ff;padding:20px;'>
 
                <div style='background:linear-gradient(135deg,#3730a3,#4f46e5);
                            border-radius:16px;padding:30px;text-align:center;
                            margin-bottom:20px;'>
                    <h1 style='color:#fff;margin:0;font-size:1.8rem;'>
                        🛍️ Shop<span style='color:#06b6d4;'>Zone</span>
                    </h1>
                    <p style='color:rgba(255,255,255,.8);margin:8px 0 0;'>
                        ¡Tu pedido fue confirmado!
                    </p>
                </div>
 
                <div style='background:#fff;border-radius:16px;padding:25px;
                            margin-bottom:16px;'>
                    <h2 style='color:#1e1b4b;margin-top:0;'>
                        Hola, %s 👋
                    </h2>
                    <p style='color:#6b7280;'>
                        Tu pedido <strong style='color:#4f46e5;'>#%d</strong>
                        ha sido registrado correctamente.
                    </p>
 
                    <table style='width:100%%;border-collapse:collapse;margin-top:16px;'>
                        <thead>
                            <tr style='background:#ede9fe;'>
                                <th style='padding:10px;text-align:left;color:#4f46e5;'>
                                    Producto
                                </th>
                                <th style='padding:10px;text-align:center;color:#4f46e5;'>
                                    Cant.
                                </th>
                                <th style='padding:10px;text-align:right;color:#4f46e5;'>
                                    Subtotal
                                </th>
                            </tr>
                        </thead>
                        <tbody>%s</tbody>
                    </table>
 
                    <div style='text-align:right;margin-top:16px;padding-top:16px;
                                border-top:2px solid #e0e7ff;'>
                        <span style='font-size:1.3rem;font-weight:800;color:#4f46e5;'>
                            Total: S/ %.2f
                        </span>
                    </div>
                </div>
 
                <p style='text-align:center;color:#9ca3af;font-size:.85rem;'>
                    Gracias por comprar en ShopZone 💜
                </p>
            </div>
            """,
            nombreCliente, pedidoId, filas.toString(), total
        );
    }
}