package com.machy.controller;

import com.machy.dto.response.ApiResponse;
import com.machy.service.AttendanceService;
import com.machy.service.DashboardService;
import com.machy.service.ProductService;
import com.machy.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final DashboardService dashboardService;
    private final SaleService saleService;
    private final ProductService productService;
    private final AttendanceService attendanceService;

    public ReportController(DashboardService dashboardService, SaleService saleService, ProductService productService, AttendanceService attendanceService) {
        this.dashboardService = dashboardService;
        this.saleService = saleService;
        this.productService = productService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<?>> dashboard() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getDashboard()));
    }

    @GetMapping("/sales")
    public ResponseEntity<ApiResponse<?>> sales() {
        return ResponseEntity.ok(ApiResponse.ok(saleService.getReporteVentas()));
    }

    @GetMapping("/inventory")
    public ResponseEntity<ApiResponse<?>> inventory() {
        return ResponseEntity.ok(ApiResponse.ok(productService.getResumenInventario()));
    }

    @GetMapping("/attendance")
    public ResponseEntity<ApiResponse<?>> attendance() {
        return ResponseEntity.ok(ApiResponse.ok(attendanceService.getInformeSemanal()));
    }
}
