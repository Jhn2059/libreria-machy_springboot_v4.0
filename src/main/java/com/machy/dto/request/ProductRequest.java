package com.machy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

public class ProductRequest {
    @NotBlank
    private String codigo;

    @NotBlank
    private String nombre;

    private String descripcion;
    private String categoriaNombre;
    private UUID categoriaId;

    private String unidad;

    @PositiveOrZero
    private BigDecimal precioCompra;

    @PositiveOrZero
    private BigDecimal precioVenta;

    @PositiveOrZero
    private Integer stock;

    @PositiveOrZero
    private Integer stockMinimo;

    private UUID proveedorId;
    private String proveedorNombre;

    public ProductRequest() {
    }

    public ProductRequest(String codigo, String nombre, String descripcion, String categoriaNombre, UUID categoriaId, String unidad, BigDecimal precioCompra, BigDecimal precioVenta, Integer stock, Integer stockMinimo, UUID proveedorId, String proveedorNombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaNombre = categoriaNombre;
        this.categoriaId = categoriaId;
        this.unidad = unidad;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.proveedorId = proveedorId;
        this.proveedorNombre = proveedorNombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public UUID getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(UUID categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public UUID getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(UUID proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }
}
