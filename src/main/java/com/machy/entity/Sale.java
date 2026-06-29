package com.machy.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ventas")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer numero;

    @Column(name = "num_comp")
    private Integer numComp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    private User vendedor;

    @Column(length = 200)
    private String vendedorNombre;

    @Column(columnDefinition = "TEXT")
    private String itemsJson = "[]";

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal descuento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal igv;

    @Column(length = 200)
    private String cliente;

    @Column(name = "cliente_dni", length = 20)
    private String clienteDni;

    @Column(length = 20)
    private String estado;

    @Column(nullable = false)
    private Boolean boleta;

    @Column(name = "boleta_generada")
    private Boolean boletaGenerada;

    @Column(name = "paga_con", precision = 10, scale = 2)
    private BigDecimal pagaCon;

    @Column(precision = 10, scale = 2)
    private BigDecimal vuelto;

    @Column(name = "motivo_anulacion", columnDefinition = "TEXT DEFAULT ''")
    private String motivoAnulacion;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public Sale() {}

    public Sale(UUID id, Integer numero, Integer numComp, User vendedor, String vendedorNombre, String itemsJson, BigDecimal subtotal, BigDecimal descuento, BigDecimal total, BigDecimal igv, String cliente, String clienteDni, String estado, Boolean boleta, Boolean boletaGenerada, BigDecimal pagaCon, BigDecimal vuelto, String motivoAnulacion, List<SaleItem> items, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.numero = numero;
        this.numComp = numComp;
        this.vendedor = vendedor;
        this.vendedorNombre = vendedorNombre;
        this.itemsJson = itemsJson;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.igv = igv;
        this.cliente = cliente;
        this.clienteDni = clienteDni;
        this.estado = estado;
        this.boleta = boleta;
        this.boletaGenerada = boletaGenerada;
        this.pagaCon = pagaCon;
        this.vuelto = vuelto;
        this.motivoAnulacion = motivoAnulacion;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public Integer getNumComp() { return numComp; }
    public void setNumComp(Integer numComp) { this.numComp = numComp; }
    public User getVendedor() { return vendedor; }
    public void setVendedor(User vendedor) { this.vendedor = vendedor; }
    public String getVendedorNombre() { return vendedorNombre; }
    public void setVendedorNombre(String vendedorNombre) { this.vendedorNombre = vendedorNombre; }
    public String getItemsJson() { return itemsJson; }
    public void setItemsJson(String itemsJson) { this.itemsJson = itemsJson; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public BigDecimal getIgv() { return igv; }
    public void setIgv(BigDecimal igv) { this.igv = igv; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getClienteDni() { return clienteDni; }
    public void setClienteDni(String clienteDni) { this.clienteDni = clienteDni; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Boolean getBoleta() { return boleta; }
    public void setBoleta(Boolean boleta) { this.boleta = boleta; }
    public Boolean getBoletaGenerada() { return boletaGenerada; }
    public void setBoletaGenerada(Boolean boletaGenerada) { this.boletaGenerada = boletaGenerada; }
    public BigDecimal getPagaCon() { return pagaCon; }
    public void setPagaCon(BigDecimal pagaCon) { this.pagaCon = pagaCon; }
    public BigDecimal getVuelto() { return vuelto; }
    public void setVuelto(BigDecimal vuelto) { this.vuelto = vuelto; }
    public String getMotivoAnulacion() { return motivoAnulacion; }
    public void setMotivoAnulacion(String motivoAnulacion) { this.motivoAnulacion = motivoAnulacion; }
    public List<SaleItem> getItems() { return items; }
    public void setItems(List<SaleItem> items) { this.items = items; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Sale(id=" + id + ")";
    }

    @PrePersist
    void onCreate() { createdAt = Instant.now(); updatedAt = Instant.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }

    public static SaleBuilder builder() { return new SaleBuilder(); }

    public static class SaleBuilder {
        private UUID id;
        private Integer numero;
        private Integer numComp;
        private User vendedor;
        private String vendedorNombre = "";
        private String itemsJson = "[]";
        private BigDecimal subtotal;
        private BigDecimal descuento = BigDecimal.ZERO;
        private BigDecimal total;
        private BigDecimal igv = BigDecimal.ZERO;
        private String cliente = "VENTA AL CONTADO";
        private String clienteDni = "";
        private String estado = "confirmada";
        private Boolean boleta = false;
        private Boolean boletaGenerada = false;
        private BigDecimal pagaCon;
        private BigDecimal vuelto;
        private String motivoAnulacion = "";
        private List<SaleItem> items = new ArrayList<>();
        private Instant createdAt;
        private Instant updatedAt;

        public SaleBuilder id(UUID id) { this.id = id; return this; }
        public SaleBuilder numero(Integer numero) { this.numero = numero; return this; }
        public SaleBuilder numComp(Integer numComp) { this.numComp = numComp; return this; }
        public SaleBuilder vendedor(User vendedor) { this.vendedor = vendedor; return this; }
        public SaleBuilder vendedorNombre(String vendedorNombre) { this.vendedorNombre = vendedorNombre; return this; }
        public SaleBuilder itemsJson(String itemsJson) { this.itemsJson = itemsJson; return this; }
        public SaleBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
        public SaleBuilder descuento(BigDecimal descuento) { this.descuento = descuento; return this; }
        public SaleBuilder total(BigDecimal total) { this.total = total; return this; }
        public SaleBuilder igv(BigDecimal igv) { this.igv = igv; return this; }
        public SaleBuilder cliente(String cliente) { this.cliente = cliente; return this; }
        public SaleBuilder clienteDni(String clienteDni) { this.clienteDni = clienteDni; return this; }
        public SaleBuilder estado(String estado) { this.estado = estado; return this; }
        public SaleBuilder boleta(Boolean boleta) { this.boleta = boleta; return this; }
        public SaleBuilder boletaGenerada(Boolean boletaGenerada) { this.boletaGenerada = boletaGenerada; return this; }
        public SaleBuilder pagaCon(BigDecimal pagaCon) { this.pagaCon = pagaCon; return this; }
        public SaleBuilder vuelto(BigDecimal vuelto) { this.vuelto = vuelto; return this; }
        public SaleBuilder motivoAnulacion(String motivoAnulacion) { this.motivoAnulacion = motivoAnulacion; return this; }
        public SaleBuilder items(List<SaleItem> items) { this.items = items; return this; }
        public SaleBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public SaleBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public Sale build() {
            return new Sale(id, numero, numComp, vendedor, vendedorNombre, itemsJson, subtotal, descuento, total, igv, cliente, clienteDni, estado, boleta, boletaGenerada, pagaCon, vuelto, motivoAnulacion, items, createdAt, updatedAt);
        }
    }
}
