package com.proyecto.services;
 
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.proyecto.dto.DetallePedidoDTO;
import com.proyecto.dto.PedidoDTO;
 
@Service
public class BoletaService {
 
    @Autowired
    private PedidoService pedidoService;
 
    // Colores igual que el .NET (#1e1b4b, #4f46e5, #d4c9a8)
    private static final BaseColor COLOR_TITULO    = new BaseColor(30, 27, 75);   // #1e1b4b
    private static final BaseColor COLOR_ACENTO    = new BaseColor(79, 70, 229);  // #4f46e5
    private static final BaseColor COLOR_TABLA_HDR = new BaseColor(212, 201, 168); // #d4c9a8
    private static final BaseColor COLOR_FILA_PAR  = new BaseColor(248, 247, 255); // #f8f7ff
    private static final BaseColor COLOR_GRIS      = new BaseColor(107, 114, 128); // #6b7280
    private static final BaseColor COLOR_LINEA     = new BaseColor(224, 231, 255); // #e0e7ff
 
    public byte[] generarBoleta(Integer pedidoId, String nombreCliente, String emailCliente) {
        try {
            // Obtener datos del pedido y detalle
            PedidoDTO pedido = pedidoService.obtenerPedidoPorId(pedidoId);
            if (pedido == null) return null;
            List<DetallePedidoDTO> detalle = pedidoService.listarDetallePorPedido(pedidoId);
 
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, baos);
            document.open();
 
            // ── FUENTES ──────────────────────────────────────
            Font fuenteTitulo    = new Font(Font.FontFamily.HELVETICA, 26, Font.BOLD,   COLOR_TITULO);
            Font fuenteSubtitulo = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_GRIS);
            Font fuenteRuc       = new Font(Font.FontFamily.HELVETICA,  9, Font.NORMAL, COLOR_GRIS);
            Font fuenteBoleta    = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,   BaseColor.WHITE);
            Font fuenteInfo      = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.DARK_GRAY);
            Font fuenteInfoBold  = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   BaseColor.DARK_GRAY);
            Font fuenteTotalGrande = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD, COLOR_ACENTO);
            Font fuenteTotalLabel  = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, COLOR_GRIS);
            Font fuenteTablaHdr  = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   BaseColor.DARK_GRAY);
            Font fuenteTabla     = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.DARK_GRAY);
            Font fuenteTotalFin  = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,   COLOR_ACENTO);
            Font fuenteEstado    = new Font(Font.FontFamily.HELVETICA,  9, Font.NORMAL, COLOR_GRIS);
            Font fuenteGracias   = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,   COLOR_TITULO);
            Font fuentePie       = new Font(Font.FontFamily.HELVETICA,  9, Font.NORMAL, COLOR_GRIS);
 
            // ── ENCABEZADO ───────────────────────────────────
            Paragraph titulo = new Paragraph("ShopZone", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
 
            Paragraph sub = new Paragraph("Tu tienda online favorita", fuenteSubtitulo);
            sub.setAlignment(Element.ALIGN_CENTER);
            document.add(sub);
 
            Paragraph ruc = new Paragraph("RUC: 20123456789  |  Av. Lima 123, Lima, Perú", fuenteRuc);
            ruc.setAlignment(Element.ALIGN_CENTER);
            document.add(ruc);
 
            // Línea separadora
            document.add(new Chunk(new LineSeparator(1f, 100f, COLOR_LINEA, Element.ALIGN_CENTER, -2)));
            document.add(Chunk.NEWLINE);
 
            // ── TÍTULO BOLETA (fondo oscuro) ─────────────────
            PdfPTable tablaTitulo = new PdfPTable(1);
            tablaTitulo.setWidthPercentage(100);
            PdfPCell celdaTitulo = new PdfPCell(new Phrase("BOLETA DE VENTA", fuenteBoleta));
            celdaTitulo.setBackgroundColor(COLOR_TITULO);
            celdaTitulo.setPadding(10);
            celdaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaTitulo.setBorder(Rectangle.NO_BORDER);
            tablaTitulo.addCell(celdaTitulo);
            document.add(tablaTitulo);
            document.add(Chunk.NEWLINE);
 
            // ── INFO PEDIDO ──────────────────────────────────
            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(100);
 
            PdfPCell celdaOp = new PdfPCell(new Phrase(
                "N° Operación: " + pedido.getPedidoId(), fuenteInfo));
            celdaOp.setBorder(Rectangle.NO_BORDER);
            tablaInfo.addCell(celdaOp);
 
            String fecha = pedido.getFechaPedido() != null
                ? pedido.getFechaPedido().toString().substring(0, 10) : "—";
            PdfPCell celdaFecha = new PdfPCell(new Phrase("Fecha Emisión: " + fecha, fuenteInfo));
            celdaFecha.setBorder(Rectangle.NO_BORDER);
            celdaFecha.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaInfo.addCell(celdaFecha);
 
            document.add(tablaInfo);
 
            Paragraph labelCliente = new Paragraph("CLIENTE:", fuenteInfoBold);
            labelCliente.setSpacingBefore(6);
            document.add(labelCliente);
 
            Paragraph datosCliente = new Paragraph(
                nombreCliente + "  |  " + emailCliente, fuenteInfo);
            document.add(datosCliente);
 
            document.add(new Chunk(new LineSeparator(1f, 100f, COLOR_LINEA, Element.ALIGN_CENTER, -2)));
            document.add(Chunk.NEWLINE);
 
            // ── TOTAL DESTACADO ──────────────────────────────
            Paragraph labelTotal = new Paragraph("TOTAL PAGADO", fuenteTotalLabel);
            labelTotal.setAlignment(Element.ALIGN_CENTER);
            document.add(labelTotal);
 
            String totalStr = pedido.getTotal() != null
                ? String.format("S/ %.2f", pedido.getTotal()) : "S/ 0.00";
            Paragraph totalGrande = new Paragraph(totalStr, fuenteTotalGrande);
            totalGrande.setAlignment(Element.ALIGN_CENTER);
            document.add(totalGrande);
            document.add(Chunk.NEWLINE);
 
            document.add(new Chunk(new LineSeparator(1f, 100f, COLOR_LINEA, Element.ALIGN_CENTER, -2)));
            document.add(Chunk.NEWLINE);
 
            // ── DETALLE DE COMPRA ────────────────────────────
            Paragraph labelDetalle = new Paragraph("DETALLE DE COMPRA", fuenteInfoBold);
            labelDetalle.setSpacingAfter(6);
            document.add(labelDetalle);
 
            PdfPTable tablaDetalle = new PdfPTable(4);
            tablaDetalle.setWidthPercentage(100);
            tablaDetalle.setWidths(new float[]{4f, 1f, 2f, 2f});
 
            // Encabezados de tabla
            String[] headers = {"Producto", "Cant.", "P.Unit", "Subtotal"};
            int[] aligns = {Element.ALIGN_LEFT, Element.ALIGN_CENTER,
                            Element.ALIGN_RIGHT, Element.ALIGN_RIGHT};
            for (int i = 0; i < headers.length; i++) {
                PdfPCell hdr = new PdfPCell(new Phrase(headers[i], fuenteTablaHdr));
                hdr.setBackgroundColor(COLOR_TABLA_HDR);
                hdr.setPadding(6);
                hdr.setHorizontalAlignment(aligns[i]);
                hdr.setBorderColor(COLOR_LINEA);
                tablaDetalle.addCell(hdr);
            }
 
            // Filas de productos
            boolean filaPar = false;
            for (DetallePedidoDTO item : detalle) {
                BaseColor bg = filaPar ? COLOR_FILA_PAR : BaseColor.WHITE;
                filaPar = !filaPar;
 
                PdfPCell cNombre = new PdfPCell(new Phrase(
                    item.getProductoNombre() != null ? item.getProductoNombre() : "—", fuenteTabla));
                cNombre.setBackgroundColor(bg);
                cNombre.setPadding(6);
                cNombre.setBorderColor(COLOR_LINEA);
                tablaDetalle.addCell(cNombre);
 
                PdfPCell cCant = new PdfPCell(new Phrase(
                    item.getCantidad() != null ? item.getCantidad().toString() : "0", fuenteTabla));
                cCant.setBackgroundColor(bg);
                cCant.setPadding(6);
                cCant.setHorizontalAlignment(Element.ALIGN_CENTER);
                cCant.setBorderColor(COLOR_LINEA);
                tablaDetalle.addCell(cCant);
 
                PdfPCell cPrecio = new PdfPCell(new Phrase(
                    item.getPrecioUnitario() != null
                        ? String.format("S/ %.2f", item.getPrecioUnitario()) : "—", fuenteTabla));
                cPrecio.setBackgroundColor(bg);
                cPrecio.setPadding(6);
                cPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cPrecio.setBorderColor(COLOR_LINEA);
                tablaDetalle.addCell(cPrecio);
 
                PdfPCell cSub = new PdfPCell(new Phrase(
                    item.getSubtotal() != null
                        ? String.format("S/ %.2f", item.getSubtotal()) : "—", fuenteTabla));
                cSub.setBackgroundColor(bg);
                cSub.setPadding(6);
                cSub.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cSub.setBorderColor(COLOR_LINEA);
                tablaDetalle.addCell(cSub);
            }
            document.add(tablaDetalle);
            document.add(Chunk.NEWLINE);
 
            // ── TOTAL FINAL ──────────────────────────────────
            PdfPTable tablaTotal = new PdfPTable(2);
            tablaTotal.setWidthPercentage(40);
            tablaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
 
            PdfPCell cLabelT = new PdfPCell(new Phrase("TOTAL:", fuenteTotalFin));
            cLabelT.setPadding(8);
            cLabelT.setBorderColor(COLOR_LINEA);
            tablaTotal.addCell(cLabelT);
 
            PdfPCell cValorT = new PdfPCell(new Phrase(totalStr, fuenteTotalFin));
            cValorT.setPadding(8);
            cValorT.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cValorT.setBorderColor(COLOR_LINEA);
            tablaTotal.addCell(cValorT);
            document.add(tablaTotal);
 
            // Estado del pedido
            Paragraph estado = new Paragraph(
                "Estado: " + (pedido.getEstado() != null ? pedido.getEstado() : "—"), fuenteEstado);
            estado.setAlignment(Element.ALIGN_RIGHT);
            estado.setSpacingBefore(4);
            document.add(estado);
 
            document.add(Chunk.NEWLINE);
            document.add(new Chunk(new LineSeparator(1f, 100f, COLOR_LINEA, Element.ALIGN_CENTER, -2)));
            document.add(Chunk.NEWLINE);
 
            // ── PIE DE BOLETA ────────────────────────────────
            Paragraph gracias = new Paragraph("¡GRACIAS POR TU COMPRA!", fuenteGracias);
            gracias.setAlignment(Element.ALIGN_CENTER);
            document.add(gracias);
 
            Paragraph conserva = new Paragraph(
                "Conserva este recibo como comprobante de pago", fuentePie);
            conserva.setAlignment(Element.ALIGN_CENTER);
            document.add(conserva);
 
            Paragraph contacto = new Paragraph(
                "www.shopzone.pe  |  contacto@shopzone.pe", fuentePie);
            contacto.setAlignment(Element.ALIGN_CENTER);
            document.add(contacto);
 
            document.close();
            return baos.toByteArray();
 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}