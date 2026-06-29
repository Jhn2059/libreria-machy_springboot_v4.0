package com.machy.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public class SaleRequest {
    private String cliente;
    private String clienteDni;

    @PositiveOrZero
    private BigDecimal descuento;

    private String tipoDescuento;

    @PositiveOrZero
    private BigDecimal pagaCon;

    @Valid
    private List<SaleItemRequest> items;

    public SaleRequest() {
    }

    public SaleRequest(String cliente, String clienteDni, BigDecimal descuento, String tipoDescuento, BigDecimal pagaCon, List<SaleItemRequest> items) {
        this.cliente = cliente;
        this.clienteDni = clienteDni;
        this.descuento = descuento;
        this.tipoDescuento = tipoDescuento;
        this.pagaCon = pagaCon;
        this.items = items;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getClienteDni() {
        return clienteDni;
    }

    public void setClienteDni(String clienteDni) {
        this.clienteDni = clienteDni;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public String getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(String tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public BigDecimal getPagaCon() {
        return pagaCon;
    }

    public void setPagaCon(BigDecimal pagaCon) {
        this.pagaCon = pagaCon;
    }

    public List<SaleItemRequest> getItems() {
        return items;
    }

    public void setItems(List<SaleItemRequest> items) {
        this.items = items;
    }

    public static class SaleItemRequest {
        private String productoId;
        private String codigo;
        private String nombreProducto;
        private String categoria;
        private Integer cantidad;
        private BigDecimal precioUnitario;

        public SaleItemRequest() {
        }

        public SaleItemRequest(String productoId, String codigo, String nombreProducto, String categoria, Integer cantidad, BigDecimal precioUnitario) {
            this.productoId = productoId;
            this.codigo = codigo;
            this.nombreProducto = nombreProducto;
            this.categoria = categoria;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
        }

        public String getProductoId() {
            return productoId;
        }

        public void setProductoId(String productoId) {
            this.productoId = productoId;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getNombreProducto() {
            return nombreProducto;
        }

        public void setNombreProducto(String nombreProducto) {
            this.nombreProducto = nombreProducto;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public BigDecimal getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(BigDecimal precioUnitario) {
            this.precioUnitario = precioUnitario;
        }
    }
}
