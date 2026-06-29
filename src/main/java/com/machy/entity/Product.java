package com.machy.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "productos")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT DEFAULT ''")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Category categoriaRel;

    @Column(name = "categoria", length = 100)
    private String categoriaNombre;

    @Column(length = 50)
    private String unidad;

    @Column(name = "precio_compra", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Supplier proveedorRel;

    @Column(name = "proveedor_nombre", length = 200)
    private String proveedorNombre;

    @Column(length = 20, nullable = false)
    private String estado;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public Product() {}

    public Product(UUID id, String codigo, String nombre, String descripcion, Category categoriaRel, String categoriaNombre, String unidad, BigDecimal precioCompra, BigDecimal precioVenta, Integer stock, Integer stockMinimo, Supplier proveedorRel, String proveedorNombre, String estado, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaRel = categoriaRel;
        this.categoriaNombre = categoriaNombre;
        this.unidad = unidad;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.proveedorRel = proveedorRel;
        this.proveedorNombre = proveedorNombre;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Category getCategoriaRel() { return categoriaRel; }
    public void setCategoriaRel(Category categoriaRel) { this.categoriaRel = categoriaRel; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    public BigDecimal getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(BigDecimal precioCompra) { this.precioCompra = precioCompra; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    public Supplier getProveedorRel() { return proveedorRel; }
    public void setProveedorRel(Supplier proveedorRel) { this.proveedorRel = proveedorRel; }
    public String getProveedorNombre() { return proveedorNombre; }
    public void setProveedorNombre(String proveedorNombre) { this.proveedorNombre = proveedorNombre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product(id=" + id + ")";
    }

    @PrePersist
    void onCreate() { createdAt = Instant.now(); updatedAt = Instant.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }

    public boolean isStockBajo() {
        return "activo".equals(estado) && stock <= stockMinimo;
    }

    public boolean isSinStock() {
        return "activo".equals(estado) && stock == 0;
    }

    public static ProductBuilder builder() { return new ProductBuilder(); }

    public static class ProductBuilder {
        private UUID id;
        private String codigo;
        private String nombre;
        private String descripcion = "";
        private Category categoriaRel;
        private String categoriaNombre = "";
        private String unidad = "unidad";
        private BigDecimal precioCompra;
        private BigDecimal precioVenta;
        private Integer stock;
        private Integer stockMinimo = 5;
        private Supplier proveedorRel;
        private String proveedorNombre = "";
        private String estado = "activo";
        private Instant createdAt;
        private Instant updatedAt;

        public ProductBuilder id(UUID id) { this.id = id; return this; }
        public ProductBuilder codigo(String codigo) { this.codigo = codigo; return this; }
        public ProductBuilder nombre(String nombre) { this.nombre = nombre; return this; }
        public ProductBuilder descripcion(String descripcion) { this.descripcion = descripcion; return this; }
        public ProductBuilder categoriaRel(Category categoriaRel) { this.categoriaRel = categoriaRel; return this; }
        public ProductBuilder categoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; return this; }
        public ProductBuilder unidad(String unidad) { this.unidad = unidad; return this; }
        public ProductBuilder precioCompra(BigDecimal precioCompra) { this.precioCompra = precioCompra; return this; }
        public ProductBuilder precioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; return this; }
        public ProductBuilder stock(Integer stock) { this.stock = stock; return this; }
        public ProductBuilder stockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; return this; }
        public ProductBuilder proveedorRel(Supplier proveedorRel) { this.proveedorRel = proveedorRel; return this; }
        public ProductBuilder proveedorNombre(String proveedorNombre) { this.proveedorNombre = proveedorNombre; return this; }
        public ProductBuilder estado(String estado) { this.estado = estado; return this; }
        public ProductBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public ProductBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public Product build() {
            return new Product(id, codigo, nombre, descripcion, categoriaRel, categoriaNombre, unidad, precioCompra, precioVenta, stock, stockMinimo, proveedorRel, proveedorNombre, estado, createdAt, updatedAt);
        }
    }
}
