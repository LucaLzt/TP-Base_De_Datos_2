package com.projects.farmaciamongodb.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Capa de Modelo (POJO).
 */
public class Sucursal {

    private String puntoVenta;
    private Direccion direccion;
    private Empleado encargado;
    private List<Empleado> empleados = new ArrayList<>();

    /**
     * Constructor vacío.
     */
    public Sucursal() {
        this.empleados = new ArrayList<>();
    }

    /**
     * Constructor con parámetros.
     */
    public Sucursal(String puntoVenta, Direccion direccion, Empleado encargado, List<Empleado> empleados) {
        this.puntoVenta = puntoVenta;
        this.direccion = direccion;
        this.encargado = encargado;

        // Validación de seguridad para la lista
        if (empleados != null) {
            this.empleados = empleados;
        } else {
            this.empleados = new ArrayList<>();
        }
    }

    // GETTERS Y SETTERS
    public String getPuntoVenta() {
        return puntoVenta;
    }

    public void setPuntoVenta(String puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Empleado getEncargado() {
        return encargado;
    }

    public void setEncargado(Empleado encargado) {
        this.encargado = encargado;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    // Helpers
    public void addEmpleado(Empleado empleado) {
        this.empleados.add(empleado);
    }
}
