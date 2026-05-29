package com.projects.farmaciamongodb.model;

/**
 * Capa de Modelo (POJO).
 */
public class ObraSocial {

    private String nombre;
    private Long numeroAfiliado;

    /**
     * Constructor vacío.
     */
    public ObraSocial() {}

    /**
     * Constructor con parámetros
     */
    public ObraSocial(String nombre, Long numeroAfiliado) {
        this.nombre = nombre;
        this.numeroAfiliado = numeroAfiliado;
    }

    // GETTERS Y SETTERS
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getNumeroAfiliado() {
        return numeroAfiliado;
    }

    public void setNumeroAfiliado(Long numeroAfiliado) {
        this.numeroAfiliado = numeroAfiliado;
    }
}
