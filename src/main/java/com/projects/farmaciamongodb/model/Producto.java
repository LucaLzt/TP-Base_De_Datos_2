package com.projects.farmaciamongodb.model;

/**
 * Capa de Modelo (POJO).
 */
public class Producto {

    private Long codigoNumerico;
    private TipoProducto tipo;
    private String descripcion;
    private String laboratorio;
    private Double precio;

    /**
     * Constructor vacío.
     */
    public Producto() {}

    /**
     * Constructor con parámetros.
     */
    public Producto(Long codigoNumerico, Double precio, String laboratorio, String descripcion, TipoProducto tipo) {
        this.codigoNumerico = codigoNumerico;
        this.precio = precio;
        this.laboratorio = laboratorio;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    // GETTERS Y SETTERS
    public Long getCodigoNumerico() {
        return codigoNumerico;
    }

    public void setCodigoNumerico(Long codigoNumerico) {
        this.codigoNumerico = codigoNumerico;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }
}
