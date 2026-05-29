package com.projects.farmaciamongodb.model;

/**
 * Capa de Modelo (POJO).
 */
public class Empleado {

    private String cuil;
    private Long dni;
    private String nombre;
    private String apellido;
    private Direccion direccion;
    private ObraSocial obraSocial;

    /**
     * Constructor vacío.
     */
    public Empleado() {}

    /**
     * Constructor con parámetros.
     */
    public Empleado(String cuil, Long dni, String nombre, String apellido, Direccion direccion, ObraSocial obraSocial) {
        this.cuil = cuil;
        this.obraSocial = obraSocial;
        this.direccion = direccion;
        this.apellido = apellido;
        this.nombre = nombre;
        this.dni = dni;
    }

    // GETTERS Y SETTERS
    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }
}
