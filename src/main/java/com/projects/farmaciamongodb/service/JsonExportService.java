package com.projects.farmaciamongodb.service;

import com.projects.farmaciamongodb.model.*;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Capa de Servicio.
 * Orquesta la creación de datos de prueba y la exportación a JSON.
 */
@Service
public class JsonExportService {

    private final Random random = new Random();

    // Catálogos en memoria
    private List<Producto> catalogoProductos = new ArrayList<>();
    private List<Cliente> catalogoClientes = new ArrayList<>();
    private List<Sucursal> catalogoSucursales = new ArrayList<>();

    public void generarYExportarDatos() {
        System.out.println("Generando catálogos de datos...");
        inicializarCatalogos();

        System.out.println("Simulando operatoria de ventas...");
        List<Venta> todasLasVentas = simularVentas();

        System.out.println("Generadas " + todasLasVentas.size() + " ventas en total.");
        exportarAArchivoJson(todasLasVentas, "ventas_entregable.json");
    }

    private void inicializarCatalogos() {
        // 1. Creo 10 Productos (7 medicamentos, 3 perfumería)
        catalogoProductos.add(new Producto(100L, 1500.0, "Bayer", "Aspirina 500mg", TipoProducto.MEDICAMENTO));
        catalogoProductos.add(new Producto(101L, 2500.0, "Roemmers", "Ibuprofeno 600mg", TipoProducto.MEDICAMENTO));
        catalogoProductos.add(new Producto(102L, 8500.0, "Pfizer", "Amoxicilina 500mg", TipoProducto.MEDICAMENTO));
        catalogoProductos.add(new Producto(103L, 3000.0, "Bagó", "Paracetamol 1g", TipoProducto.MEDICAMENTO));
        catalogoProductos.add(new Producto(104L, 12000.0, "Elea", "Loratadina 10mg", TipoProducto.MEDICAMENTO));
        catalogoProductos.add(new Producto(105L, 5000.0, "Casasco", "Omeprazol 20mg", TipoProducto.MEDICAMENTO));
        catalogoProductos.add(new Producto(106L, 1800.0, "Bayer", "Cafiaspirina", TipoProducto.MEDICAMENTO));
        catalogoProductos.add(new Producto(200L, 25000.0, "Carolina Herrera", "Perfume 212 VIP", TipoProducto.PERFUMERIA));
        catalogoProductos.add(new Producto(201L, 15000.0, "Natura", "Crema Hidratante Tododia", TipoProducto.PERFUMERIA));
        catalogoProductos.add(new Producto(202L, 8000.0, "Dove", "Desodorante Antitranspirante", TipoProducto.PERFUMERIA));

        // 2. Creo 10 Clientes (Algunos con Obra Social, otros Privados)
        ObraSocial osde = new ObraSocial("OSDE", 123456L);
        ObraSocial swiss = new ObraSocial("Swiss Medical", 987654L);
        Direccion dirGen = new Direccion("Av. Siempreviva", 742, "Buenos Aires", "Springfield");

        for (int i = 1; i <= 10; i++) {
            ObraSocial os = (i % 3 == 0) ? null : (i % 2 == 0 ? osde : swiss); // Distribución de cobertura
            catalogoClientes.add(new Cliente(30000000L + i, "Nombre" + i, "Apellido" + i, dirGen, os));
        }

        // 3. Creo 3 Sucursales con 3 Empleados cada una
        String[] localidades = {"Lanús", "Avellaneda", "Banfield"};
        for (int i = 1; i <= 3; i++) {
            Sucursal sucursal = new Sucursal("000" + i, new Direccion("Calle Principal", i * 100, "BA", localidades[i-1]), null, null);

            // Creo 3 Empleados para esta Sucursal
            Empleado encargado = new Empleado("20-11111111-" + i, 11111111L, "Encargado" + i, "Jefe", dirGen, osde);
            Empleado vendedor1 = new Empleado("20-22222222-" + i, 22222222L, "Cajero" + i, "Ventas", dirGen, null);
            Empleado vendedor2 = new Empleado("20-33333333-" + i, 33333333L, "Atención" + i, "Ventas", dirGen, swiss);

            sucursal.setEncargado(encargado);
            sucursal.addEmpleado(encargado);
            sucursal.addEmpleado(vendedor1);
            sucursal.addEmpleado(vendedor2);

            catalogoSucursales.add(sucursal);
        }
    }

    private List<Venta> simularVentas() {
        List<Venta> ventasGeneradas = new ArrayList<>();
        String[] formasPago = {"Efectivo", "Tarjeta", "Débito"};

        for (Sucursal sucursal : catalogoSucursales) {
            // Calculo cant de ventas para la sucursal (30 +/- 20% = entre 24 y 36 ventas)
            int cantidadVentas = 24 + random.nextInt(13); // random de 0 a 12 + 24

            for (int i = 1; i <= cantidadVentas; i++) {
                // Instancio la venta
                String ticketSecuencial = String.format("%08d", i);
                String nroTicketCompleto = sucursal.getPuntoVenta() + "-" + ticketSecuencial;
                Venta venta = new Venta(nroTicketCompleto, new Date(), formasPago[random.nextInt(formasPago.length)]);

                // Asigno las relaciones
                venta.setSucursal(sucursal);
                venta.setCliente(catalogoClientes.get(random.nextInt(catalogoClientes.size())));

                // Selecciono los empleados aleatorios de ESTA sucursal
                venta.setEmpleadoAtencion(sucursal.getEmpleados().get(random.nextInt(3)));
                venta.setEmpleadoCaja(sucursal.getEmpleados().get(random.nextInt(3)));

                // Genero los detalles de la venta (entre 1 y 3 productos por factura)
                int cantidadDetalles = 1 + random.nextInt(3);
                for (int j = 0; j < cantidadDetalles; j++) {
                    Producto prodRandom = catalogoProductos.get(random.nextInt(catalogoProductos.size()));
                    int cantidadLlevada = 1 + random.nextInt(2); // lleva 1 o 2 unidades

                    DetalleVenta detalle = new DetalleVenta(cantidadLlevada, prodRandom.getPrecio(), prodRandom);
                    venta.addDetalle(detalle);
                }

                ventasGeneradas.add(venta);
            }
        }
        return ventasGeneradas;
    }

    private void exportarAArchivoJson(List<Venta> ventas, String nombreArchivo) {
        // Configuro Jackson para que el JSON se vea ordenado
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            File archivoSalida = new File(nombreArchivo);
            mapper.writeValue(archivoSalida, ventas);
            System.out.println("ÉXITO: Archivo JSON generado en la raíz del proyecto: " + archivoSalida.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("ERROR al generar el archivo JSON: " + e.getMessage());
        }
    }
}
