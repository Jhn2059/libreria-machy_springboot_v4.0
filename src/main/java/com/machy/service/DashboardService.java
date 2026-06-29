package com.machy.service;

import com.machy.dto.response.DashboardResponse;
import com.machy.entity.Product;
import com.machy.entity.Sale;
import com.machy.repository.ProductRepository;
import com.machy.repository.SaleRepository;
import com.machy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class DashboardService {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;

    public DashboardService(ProductRepository productRepository, SaleRepository saleRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
    }

    public DashboardResponse getDashboard() {
        List<Product> allProducts = productRepository.findAll();
        List<Product> active = allProducts.stream()
                .filter(p -> "activo".equals(p.getEstado())).toList();
        List<Product> alerts = active.stream()
                .filter(Product::isStockBajo).toList();

        BigDecimal valorInventario = active.stream()
                .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Sale> confirmedSales = saleRepository.findByEstado("confirmada");
        BigDecimal ingresos = confirmedSales.stream()
                .map(Sale::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate hoy = LocalDate.now(ZoneId.of("America/Lima"));
        long ventasHoy = confirmedSales.stream()
                .filter(s -> s.getCreatedAt() != null
                        && s.getCreatedAt().atZone(ZoneId.of("America/Lima")).toLocalDate().equals(hoy))
                .count();

        List<DashboardResponse.AlertaStock> alertasList = alerts.stream()
                .map(p -> new DashboardResponse.AlertaStock(
                        p.getId().toString(), p.getNombre(),
                        p.getStock(), p.getStockMinimo(), p.getCategoriaNombre()))
                .toList();

        return DashboardResponse.builder()
                .productosActivos(active.size())
                .productosDescontinuados(allProducts.size() - active.size())
                .alertasStock(alerts.size())
                .valorInventario(valorInventario)
                .ventasHoy(ventasHoy)
                .ventasTotales(confirmedSales.size())
                .ingresosTotales(ingresos)
                .empleadosActivos(userRepository.findByRolAndActivoTrue("vendedor").size())
                .alertas(alertasList)
                .build();
    }
}
