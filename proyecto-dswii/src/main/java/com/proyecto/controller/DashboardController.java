package com.proyecto.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyecto.dto.DashboardDTO;
import com.proyecto.model.ApiResponse;
import com.proyecto.services.DashboardService;
 
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
 
    @Autowired
    private DashboardService servicio;
 
    // GET /api/dashboard  — solo Admin
    @GetMapping
    public ResponseEntity<ApiResponse<DashboardDTO>> obtenerDashboard() {
        DashboardDTO dashboard = servicio.obtenerDashboard();
        return ResponseEntity.ok(new ApiResponse<>("Dashboard obtenido correctamente", dashboard));
    }
}