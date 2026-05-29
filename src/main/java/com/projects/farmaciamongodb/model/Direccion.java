package com.projects.farmaciamongodb.model;

/**
 * Capa de Modelo (POJO).
 */
public class Direccion {

    private String calle;
    private Integer numero;
    private String localidad;
    private String provincia;

    /**
     * Constructor vacío,
     */
    public Direccion() {}

    /**
     * Constructor con parámetros.
     */
    public Direccion(String calle, Integer numero, String provincia, String localidad) {
        this.calle = calle;
        this.numero = numero;
        this.provincia = provincia;
        this.localidad = localidad;
    }

    // GETTERS Y SETTERS
    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}
