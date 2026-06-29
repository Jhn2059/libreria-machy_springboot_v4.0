package com.machy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "venta_items")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id")
    private Sale venta;

    @Column(name = "producto_id")
    private UUID productoId;

    @Column(length = 50)
    private String codigo;

    @Column(name = "nombre_producto", length = 200)
    private String nombreProducto;

    @Column(length = 100)
    private String categoria;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public SaleItem() {}

    public SaleItem(UUID id, Sale venta, UUID productoId, String codigo, String nombreProducto, String categoria, Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal, Instant createdAt) {
        this.id = id;
        this.venta = venta;
        this.productoId = productoId;
        this.codigo = codigo;
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    @JsonIgnore
    public Sale getVenta() { return venta; }
    public void setVenta(Sale venta) { this.venta = venta; }
    public UUID getProductoId() { return productoId; }
    public void setProductoId(UUID productoId) { this.productoId = productoId; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleItem saleItem = (SaleItem) o;
        return Objects.equals(id, saleItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SaleItem(id=" + id + ")";
    }

    @PrePersist
    void onCreate() { createdAt = Instant.now(); }

    public static SaleItemBuilder builder() { return new SaleItemBuilder(); }

    public static class SaleItemBuilder {
        private UUID id;
        private Sale venta;
        private UUID productoId;
        private String codigo = "";
        private String nombreProducto = "";
        private String categoria = "";
        private Integer cantidad = 1;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
        private Instant createdAt;

        public SaleItemBuilder id(UUID id) { this.id = id; return this; }
        public SaleItemBuilder venta(Sale venta) { this.venta = venta; return this; }
        public SaleItemBuilder productoId(UUID productoId) { this.productoId = productoId; return this; }
        public SaleItemBuilder codigo(String codigo) { this.codigo = codigo; return this; }
        public SaleItemBuilder nombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; return this; }
        public SaleItemBuilder categoria(String categoria) { this.categoria = categoria; return this; }
        public SaleItemBuilder cantidad(Integer cantidad) { this.cantidad = cantidad; return this; }
        public SaleItemBuilder precioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; return this; }
        public SaleItemBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
        public SaleItemBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public SaleItem build() {
            return new SaleItem(id, venta, productoId, codigo, nombreProducto, categoria, cantidad, precioUnitario, subtotal, createdAt);
        }
    }
}
