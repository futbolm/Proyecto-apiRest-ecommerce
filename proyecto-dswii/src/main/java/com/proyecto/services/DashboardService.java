package com.proyecto.services;
 
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.proyecto.dto.DashboardDTO;
import com.proyecto.dto.DashboardDTO.ProductoStockBajoDTO;
import com.proyecto.dto.DashboardDTO.TopClienteDTO;
import com.proyecto.model.Producto;
import com.proyecto.repository.IClienteRepository;
import com.proyecto.repository.IPedidoRepository;
import com.proyecto.repository.IProductoRepository;
import com.proyecto.repository.IResenaRepository;
 
@Service
public class DashboardService {
 
    @Autowired private IClienteRepository clienteRepo;
    @Autowired private IProductoRepository productoRepo;
    @Autowired private IPedidoRepository pedidoRepo;
    @Autowired private IResenaRepository resenaRepo;
 
    public DashboardDTO obtenerDashboard() {
        DashboardDTO dashboard = new DashboardDTO();
 
        // Contadores generales
        dashboard.setTotalClientes(clienteRepo.count());
        dashboard.setTotalProductos(productoRepo.count());
        dashboard.setTotalPedidos(pedidoRepo.count());
        dashboard.setTotalResenas(resenaRepo.count());
 
        // Pedidos por estado
        dashboard.setPedidosPendientes(pedidoRepo.countByEstado("Pendiente"));
        dashboard.setPedidosCompletados(pedidoRepo.countByEstado("Completado"));
        dashboard.setPedidosCancelados(pedidoRepo.countByEstado("Cancelado"));
 
        // Ingresos totales
        BigDecimal ingresos = pedidoRepo.sumTotalIngresos();
        dashboard.setTotalIngresos(ingresos != null ? ingresos : BigDecimal.ZERO);
 
        // Promedio global de calificaciones
        Double promedio = resenaRepo.promedioGlobal();
        dashboard.setPromedioCalificaciones(promedio != null
            ? Math.round(promedio * 100.0) / 100.0 : 0.0);
 
        // Productos con stock bajo (≤ 5 unidades)
        List<Producto> stockBajo = productoRepo.findProductosStockBajo(5);
        List<ProductoStockBajoDTO> stockBajoDTO = stockBajo.stream()
            .map(p -> new ProductoStockBajoDTO(p.getProductoId(), p.getNombre(), p.getStock()))
            .collect(Collectors.toList());
        dashboard.setProductosStockBajo(stockBajoDTO);
 
        // Top 5 clientes
        List<Object[]> topRaw = pedidoRepo.findTopClientes(PageRequest.of(0, 5));
        List<TopClienteDTO> topClientes = topRaw.stream()
            .map(row -> new TopClienteDTO(
                (Integer) row[0],
                (String)  row[1],
                (Long)    row[2],
                (BigDecimal) row[3]
            ))
            .collect(Collectors.toList());
        dashboard.setTopClientes(topClientes);
 
        return dashboard;
    }
}