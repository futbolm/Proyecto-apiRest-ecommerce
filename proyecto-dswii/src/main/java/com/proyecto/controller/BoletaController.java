package com.proyecto.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyecto.services.BoletaService;
 
@RestController
@RequestMapping("/api/boleta")
public class BoletaController {
 
    @Autowired
    private BoletaService boletaService;
 
    // GET /api/boleta/{pedidoId}?nombre=Luis Torres&email=luis@gmail.com
    // Token requerido (cualquier rol autenticado)
    @GetMapping("/{pedidoId}")
    public ResponseEntity<byte[]> descargarBoleta(
            @PathVariable Integer pedidoId,
            @RequestParam String nombre,
            @RequestParam String email) {
 
        byte[] pdf = boletaService.generarBoleta(pedidoId, nombre, email);
 
        if (pdf == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Esto hace que Postman lo muestre como PDF descargable
        headers.setContentDispositionFormData("attachment",
            "Boleta_Pedido_" + pedidoId + ".pdf");
        headers.setContentLength(pdf.length);
 
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}