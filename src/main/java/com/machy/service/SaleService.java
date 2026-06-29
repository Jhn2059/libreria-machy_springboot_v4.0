package com.machy.service;

import com.machy.dto.request.SaleRequest;
import com.machy.entity.*;
import com.machy.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ProductRepository productRepository;
    private final LogRepository logRepository;
    private final ProductService productService;

    public SaleService(SaleRepository saleRepository, SaleItemRepository saleItemRepository, ProductRepository productRepository, LogRepository logRepository, ProductService productService) {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.productRepository = productRepository;
        this.logRepository = logRepository;
        this.productService = productService;
    }

    private static final BigDecimal IGV_RATE = new BigDecimal("0.18");
    private static final BigDecimal IGV_DIVISOR = new BigDecimal("1.18");

    public List<Sale> findAll() {
        return saleRepository.findAllByOrderByCreatedAtDesc();
    }

    public Page<Sale> findAll(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }

    public List<Sale> findByVendedor(UUID vendedorId) {
        return saleRepository.findByVendedorIdOrderByCreatedAtDesc(vendedorId);
    }

    public Page<Sale> findByVendedor(UUID vendedorId, Pageable pageable) {
        return saleRepository.findByVendedorIdOrderByCreatedAtDesc(vendedorId, pageable);
    }

    public Sale findById(UUID id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    @Transactional
    public Sale create(SaleRequest req, User vendedor) {
        BigDecimal subtotal = BigDecimal.ZERO;
        List<SaleItem> saleItems = new ArrayList<>();

        for (var itemReq : req.getItems()) {
            Product product = productRepository.findById(UUID.fromString(itemReq.getProductoId()))
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemReq.getProductoId()));

            if (product.getStock() < itemReq.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + product.getNombre());
            }

            BigDecimal itemSubtotal = itemReq.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(itemReq.getCantidad()));
            subtotal = subtotal.add(itemSubtotal);

            productService.ajustarStock(product.getId(), -itemReq.getCantidad());

            SaleItem item = SaleItem.builder()
                    .productoId(product.getId())
                    .codigo(product.getCodigo())
                    .nombreProducto(product.getNombre())
                    .categoria(product.getCategoriaNombre())
                    .cantidad(itemReq.getCantidad())
                    .precioUnitario(itemReq.getPrecioUnitario())
                    .subtotal(itemSubtotal)
                    .build();
            saleItems.add(item);
        }

        BigDecimal descuento = req.getDescuento() != null ? req.getDescuento() : BigDecimal.ZERO;
        if ("pct".equals(req.getTipoDescuento())) {
            descuento = subtotal.multiply(descuento).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
        BigDecimal total = subtotal.subtract(descuento);
        BigDecimal igv = total.multiply(IGV_RATE).divide(IGV_DIVISOR, 2, RoundingMode.HALF_UP);
        BigDecimal pagaCon = req.getPagaCon() != null ? req.getPagaCon() : BigDecimal.ZERO;
        BigDecimal vuelto = pagaCon.compareTo(total) >= 0 ? pagaCon.subtract(total) : BigDecimal.ZERO;
        boolean emiteBoleta = total.compareTo(new BigDecimal("5.00")) >= 0;

        Integer nextNum = saleRepository.maxNumero();
        int num = (nextNum != null ? nextNum : 0) + 1;

        Sale sale = Sale.builder()
                .numero(num)
                .numComp(num)
                .vendedor(vendedor)
                .vendedorNombre(vendedor.getNombreCompleto())
                .subtotal(subtotal)
                .descuento(descuento)
                .total(total.setScale(2, RoundingMode.HALF_UP))
                .igv(igv.setScale(2, RoundingMode.HALF_UP))
                .cliente(req.getCliente() != null ? req.getCliente() : "VENTA AL CONTADO")
                .clienteDni(req.getClienteDni() != null ? req.getClienteDni() : "")
                .estado("confirmada")
                .boleta(emiteBoleta)
                .boletaGenerada(emiteBoleta)
                .pagaCon(pagaCon)
                .vuelto(vuelto)
                .items(saleItems)
                .build();

        saleItems.forEach(item -> item.setVenta(sale));

        logRepository.save(LogEntry.builder()
                .nivel("info").modulo("ventas")
                .mensaje("Venta #" + num + " confirmada por " + vendedor.getNombreCompleto())
                .usuario(vendedor)
                .build());

        return saleRepository.save(sale);
    }

    @Transactional
    public Sale anular(UUID id, String motivo, User admin) {
        if (!"admin".equals(admin.getRol())) {
            throw new RuntimeException("Solo administradores pueden anular ventas");
        }

        Sale sale = findById(id);
        if (!"confirmada".equals(sale.getEstado())) {
            throw new RuntimeException("La venta no está en estado confirmada");
        }

        sale.setEstado("anulada");
        sale.setMotivoAnulacion(motivo);

        for (SaleItem item : sale.getItems()) {
            productService.ajustarStock(item.getProductoId(), item.getCantidad());
        }

        logRepository.save(LogEntry.builder()
                .nivel("info").modulo("ventas")
                .mensaje("Venta #" + sale.getNumero() + " anulada por " + admin.getNombreCompleto() + ": " + motivo)
                .usuario(admin)
                .build());

        return saleRepository.save(sale);
    }

    public Map<String, Object> getReporteVentas() {
        List<Sale> confirmadas = saleRepository.findByEstado("confirmada");
        BigDecimal ingresos = confirmadas.stream()
                .map(Sale::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        long boletas = confirmadas.stream().filter(Sale::getBoleta).count();
        BigDecimal ticketPromedio = confirmadas.isEmpty() ? BigDecimal.ZERO
                : ingresos.divide(BigDecimal.valueOf(confirmadas.size()), 2, RoundingMode.HALF_UP);

        List<Object[]> top = saleItemRepository.topProductos();
        List<Map<String, Object>> topProductos = top.stream().limit(8).map(row -> Map.of(
                "nombre", row[0], "categoria", row[1],
                "unidades", row[2], "ingresos", row[3]
        )).collect(Collectors.toList());

        ZoneId lima = ZoneId.of("America/Lima");
        List<Map<String, Object>> ventasPorDia = new ArrayList<>();
        String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (int i = 6; i >= 0; i--) {
            LocalDate d = LocalDate.now(lima).minusDays(i);
            BigDecimal total = confirmadas.stream()
                    .filter(s -> {
                        Instant c = s.getCreatedAt();
                        return c != null && c.atZone(lima).toLocalDate().equals(d);
                    })
                    .map(Sale::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
            ventasPorDia.add(Map.of("label", dias[(d.getDayOfWeek().getValue() + 6) % 7],
                    "valor", total, "dia", d.toString()));
        }

        return Map.of(
                "ventasConfirmadas", confirmadas.size(),
                "ingresosTotales", ingresos,
                "ticketPromedio", ticketPromedio,
                "boletasEmitidas", boletas,
                "topProductos", topProductos,
                "ventasPorDia", ventasPorDia
        );
    }
}
