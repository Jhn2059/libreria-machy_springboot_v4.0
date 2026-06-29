package com.machy.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class DashboardResponse {
    private long productosActivos;
    private long productosDescontinuados;
    private long alertasStock;
    private BigDecimal valorInventario;
    private long ventasHoy;
    private long ventasTotales;
    private BigDecimal ingresosTotales;
    private long empleadosActivos;
    private List<AlertaStock> alertas;

    public DashboardResponse() {
    }

    public DashboardResponse(long productosActivos, long productosDescontinuados, long alertasStock, BigDecimal valorInventario, long ventasHoy, long ventasTotales, BigDecimal ingresosTotales, long empleadosActivos, List<AlertaStock> alertas) {
        this.productosActivos = productosActivos;
        this.productosDescontinuados = productosDescontinuados;
        this.alertasStock = alertasStock;
        this.valorInventario = valorInventario;
        this.ventasHoy = ventasHoy;
        this.ventasTotales = ventasTotales;
        this.ingresosTotales = ingresosTotales;
        this.empleadosActivos = empleadosActivos;
        this.alertas = alertas;
    }

    public static DashboardResponseBuilder builder() {
        return new DashboardResponseBuilder();
    }

    public long getProductosActivos() {
        return productosActivos;
    }

    public void setProductosActivos(long productosActivos) {
        this.productosActivos = productosActivos;
    }

    public long getProductosDescontinuados() {
        return productosDescontinuados;
    }

    public void setProductosDescontinuados(long productosDescontinuados) {
        this.productosDescontinuados = productosDescontinuados;
    }

    public long getAlertasStock() {
        return alertasStock;
    }

    public void setAlertasStock(long alertasStock) {
        this.alertasStock = alertasStock;
    }

    public BigDecimal getValorInventario() {
        return valorInventario;
    }

    public void setValorInventario(BigDecimal valorInventario) {
        this.valorInventario = valorInventario;
    }

    public long getVentasHoy() {
        return ventasHoy;
    }

    public void setVentasHoy(long ventasHoy) {
        this.ventasHoy = ventasHoy;
    }

    public long getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(long ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

    public BigDecimal getIngresosTotales() {
        return ingresosTotales;
    }

    public void setIngresosTotales(BigDecimal ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public long getEmpleadosActivos() {
        return empleadosActivos;
    }

    public void setEmpleadosActivos(long empleadosActivos) {
        this.empleadosActivos = empleadosActivos;
    }

    public List<AlertaStock> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<AlertaStock> alertas) {
        this.alertas = alertas;
    }

    public static class AlertaStock {
        private String id;
        private String nombre;
        private int stock;
        private int stockMinimo;
        private String categoria;

        public AlertaStock() {
        }

        public AlertaStock(String id, String nombre, int stock, int stockMinimo, String categoria) {
            this.id = id;
            this.nombre = nombre;
            this.stock = stock;
            this.stockMinimo = stockMinimo;
            this.categoria = categoria;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getStockMinimo() {
            return stockMinimo;
        }

        public void setStockMinimo(int stockMinimo) {
            this.stockMinimo = stockMinimo;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }
    }

    public static class DashboardResponseBuilder {
        private long productosActivos;
        private long productosDescontinuados;
        private long alertasStock;
        private BigDecimal valorInventario;
        private long ventasHoy;
        private long ventasTotales;
        private BigDecimal ingresosTotales;
        private long empleadosActivos;
        private List<AlertaStock> alertas;

        DashboardResponseBuilder() {
        }

        public DashboardResponseBuilder productosActivos(long productosActivos) {
            this.productosActivos = productosActivos;
            return this;
        }

        public DashboardResponseBuilder productosDescontinuados(long productosDescontinuados) {
            this.productosDescontinuados = productosDescontinuados;
            return this;
        }

        public DashboardResponseBuilder alertasStock(long alertasStock) {
            this.alertasStock = alertasStock;
            return this;
        }

        public DashboardResponseBuilder valorInventario(BigDecimal valorInventario) {
            this.valorInventario = valorInventario;
            return this;
        }

        public DashboardResponseBuilder ventasHoy(long ventasHoy) {
            this.ventasHoy = ventasHoy;
            return this;
        }

        public DashboardResponseBuilder ventasTotales(long ventasTotales) {
            this.ventasTotales = ventasTotales;
            return this;
        }

        public DashboardResponseBuilder ingresosTotales(BigDecimal ingresosTotales) {
            this.ingresosTotales = ingresosTotales;
            return this;
        }

        public DashboardResponseBuilder empleadosActivos(long empleadosActivos) {
            this.empleadosActivos = empleadosActivos;
            return this;
        }

        public DashboardResponseBuilder alertas(List<AlertaStock> alertas) {
            this.alertas = alertas;
            return this;
        }

        public DashboardResponse build() {
            return new DashboardResponse(productosActivos, productosDescontinuados, alertasStock, valorInventario, ventasHoy, ventasTotales, ingresosTotales, empleadosActivos, alertas);
        }
    }
}
