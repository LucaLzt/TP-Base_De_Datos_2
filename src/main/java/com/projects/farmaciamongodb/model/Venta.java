package com.projects.farmaciamongodb.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Capa de Modelo (POJO).
 */
public class Venta {

    private String numeroTicket;
    private Date fecha;
    private Double totalVenta;
    private String formaPago;
    private Sucursal sucursal;
    private Cliente cliente;
    private Empleado empleadoAtencion;
    private Empleado empleadoCaja;
    private List<DetalleVenta> detalles = new ArrayList<>();

    /**
     * Constructor vacío.
     */
    public Venta() {
        this.detalles = new ArrayList<>();
    }

    /**
     * Constructor con parámetros.
     */
    public Venta(String numeroTicket, Date fecha, String formaPago) {
        this.numeroTicket = numeroTicket;
        this.fecha = fecha;
        this.formaPago = formaPago;
        this.totalVenta = 0.0;
        this.detalles = new ArrayList<>();
    }

    // GETTERS Y SETTERS
    public String getNumeroTicket() {
        return numeroTicket;
    }

    public void setNumeroTicket(String numeroTicket) {
        this.numeroTicket = numeroTicket;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(Double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEmpleadoAtencion() {
        return empleadoAtencion;
    }

    public void setEmpleadoAtencion(Empleado empleadoAtencion) {
        this.empleadoAtencion = empleadoAtencion;
    }

    public Empleado getEmpleadoCaja() {
        return empleadoCaja;
    }

    public void setEmpleadoCaja(Empleado empleadoCaja) {
        this.empleadoCaja = empleadoCaja;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
        this.recalcularTotal();
    }

    // Helpers
    public void addDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
        recalcularTotal();
    }

    private void recalcularTotal() {
        this.totalVenta = 0.0;
        for (DetalleVenta d : detalles) {
            if (d.getSubtotal() != null) {
                this.totalVenta += d.getSubtotal();
            }
        }
    }
}
