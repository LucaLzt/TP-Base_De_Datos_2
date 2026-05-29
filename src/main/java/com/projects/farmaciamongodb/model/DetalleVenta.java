package com.projects.farmaciamongodb.model;

/**
 * Capa de Modelo (POJO).
 */
public class DetalleVenta {

    private Integer cantidad;
    private Double precioUnitarioHistorico;
    private Double subtotal;
    private Producto producto;

    /**
     * Constructor vacío.
     */
    public DetalleVenta() {
    }

    /**
     * Constructor con parámetros.
     */
    public DetalleVenta(Integer cantidad, Double precioUnitarioHistorico, Producto producto) {
        this.cantidad = cantidad;
        this.precioUnitarioHistorico = precioUnitarioHistorico;
        this.producto = producto;

        // Calculo el subtotal de forma segura
        this.calcularSubtotal();
    }

    // GETTERS Y SETTERS
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        this.calcularSubtotal();
    }

    public Double getPrecioUnitarioHistorico() {
        return precioUnitarioHistorico;
    }

    public void setPrecioUnitarioHistorico(Double precioUnitarioHistorico) {
        this.precioUnitarioHistorico = precioUnitarioHistorico;
        this.calcularSubtotal();
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // Helpers
    public void calcularSubtotal() {
        if (this.cantidad != null && this.precioUnitarioHistorico != null) {
            this.subtotal = this.cantidad * this.precioUnitarioHistorico;
        } else {
            this.subtotal = 0.0;
        }
    }
}
