package com.projects.farmaciamongodb.model;

/**
 * Capa de Modelo (POJO).
 */
public class Cliente {

    private Long dni;
    private String nombre;
    private String apellido;
    private Direccion direccion;
    private ObraSocial obraSocial;

    /**
     * Constructor vacío.
     */
    public Cliente() {}

    /**
     * Constructor con parámetros
     */
    public Cliente(Long dni, String nombre, String apellido, Direccion direccion, ObraSocial obraSocial) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.obraSocial = obraSocial;
    }

    // GETTERS Y SETTERS
    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }
}
