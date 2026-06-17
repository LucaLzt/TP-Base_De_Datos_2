package com.projects.farmaciamongodb.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.projects.farmaciamongodb.model.Venta;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MongoPersistenceService {

    private final MongoCollection<Document> ventasCollection;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public MongoPersistenceService(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("farmacia_db");
        this.ventasCollection = database.getCollection("ventas");
    }

    public void limpiarColeccion() {
        ventasCollection.drop();
        System.out.println("[MongoDB] Colección 'ventas' limpiada.");
    }

    public void insertarVentas(List<Venta> ventas) {
        List<Document> documentos = new ArrayList<>();
        for (Venta venta : ventas) {
            Document doc = ventaToDocument(venta);
            documentos.add(doc);
        }
        ventasCollection.insertMany(documentos);
        System.out.println("[MongoDB] Se insertaron " + ventas.size() + " ventas en la coleccion 'ventas'.");
    }

    public void ejecutarConsultas() {
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date fechaInicio = cal.getTime();
        cal.set(2026, Calendar.JUNE, 17, 23, 59, 59);
        Date fechaFin = cal.getTime();

        System.out.println("\n======================================================");
        System.out.println("  REPORTE DE CONSULTAS - FARMACIA NOSQL");
        System.out.println("======================================================");
        System.out.println("  Periodo: " + dateFormat.format(fechaInicio) + " al " + dateFormat.format(fechaFin));
        System.out.println("======================================================\n");

        consulta1Cadena(fechaInicio, fechaFin);
        consulta1PorSucursal(fechaInicio, fechaFin);
        consulta4Cadena(fechaInicio, fechaFin);
        consulta4PorSucursal(fechaInicio, fechaFin);
    }

    private void consulta1Cadena(Date fechaInicio, Date fechaFin) {
        System.out.println("--- CONSULTA 1: Total Ventas por Cadena ---");

        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.and(Filters.gte("fecha", fechaInicio), Filters.lte("fecha", fechaFin))),
                Aggregates.group(null, Accumulators.sum("totalVendidoCadena", "$totalVenta"), Accumulators.sum("cantidadVentas", 1))
        );

        Document result = ventasCollection.aggregate(pipeline).first();

        if (result != null) {
            double totalVendido = result.getDouble("totalVendidoCadena");
            int cantidadVentas = result.getInteger("cantidadVentas");
            System.out.println("  Cantidad de ventas: " + cantidadVentas);
            System.out.println("  Total vendido cadena: $ " + String.format("%.2f", totalVendido));
        } else {
            System.out.println("  Sin resultados para el periodo indicado.");
        }
        System.out.println();
    }

    private void consulta1PorSucursal(Date fechaInicio, Date fechaFin) {
        System.out.println("--- CONSULTA 1: Total Ventas por Sucursal ---");
        System.out.printf("%-15s %-15s %15s %15s%n", "Punto Venta", "Localidad", "Total Vendido", "Cant. Ventas");
        System.out.println("----------------------------------------------------------------");

        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.and(Filters.gte("fecha", fechaInicio), Filters.lte("fecha", fechaFin))),
                Aggregates.group(
                        new Document("puntoVenta", "$sucursal.puntoVenta")
                                .append("localidad", "$sucursal.direccion.localidad"),
                        Accumulators.sum("totalVendido", "$totalVenta"),
                        Accumulators.sum("cantidadVentas", 1)
                )
        );

        for (Document doc : ventasCollection.aggregate(pipeline)) {
            Document idDoc = doc.get("_id", Document.class);
            String pv = idDoc != null ? idDoc.getString("puntoVenta") : "";
            String loc = idDoc != null ? idDoc.getString("localidad") : "";
            double total = doc.getDouble("totalVendido");
            int cantidad = doc.getInteger("cantidadVentas");
            System.out.printf("%-15s %-15s $ %13.2f %15d%n", pv, loc, total, cantidad);
        }
        System.out.println();
    }

    private void consulta4Cadena(Date fechaInicio, Date fechaFin) {
        System.out.println("--- CONSULTA 4: Ventas por Tipo de Producto (Cadena Completa) ---");
        System.out.printf("%-20s %15s %20s%n", "Tipo Producto", "Total Monto", "Cant. Unidades");
        System.out.println("------------------------------------------------------------");

        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.and(Filters.gte("fecha", fechaInicio), Filters.lte("fecha", fechaFin))),
                Aggregates.unwind("$detalles"),
                Aggregates.group("$detalles.producto.tipo",
                        Accumulators.sum("totalMonto", "$detalles.subtotal"),
                        Accumulators.sum("cantidadUnidades", "$detalles.cantidad"))
        );

        for (Document doc : ventasCollection.aggregate(pipeline)) {
            String tipo = doc.getString("_id");
            double monto = doc.getDouble("totalMonto");
            int unidades = doc.getInteger("cantidadUnidades");
            System.out.printf("%-20s $ %12.2f %20d%n", tipo, monto, unidades);
        }
        System.out.println();
    }

    private void consulta4PorSucursal(Date fechaInicio, Date fechaFin) {
        System.out.println("--- CONSULTA 4: Ventas por Tipo de Producto y Sucursal ---");
        System.out.printf("%-15s %-15s %-20s %15s %20s%n", "Punto Venta", "Localidad", "Tipo Producto", "Total Monto", "Cant. Unidades");
        System.out.println("----------------------------------------------------------------------------------------");

        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.and(Filters.gte("fecha", fechaInicio), Filters.lte("fecha", fechaFin))),
                Aggregates.unwind("$detalles"),
                Aggregates.group(
                        new Document("puntoVenta", "$sucursal.puntoVenta")
                                .append("localidad", "$sucursal.direccion.localidad")
                                .append("tipo", "$detalles.producto.tipo"),
                        Accumulators.sum("totalMonto", "$detalles.subtotal"),
                        Accumulators.sum("cantidadUnidades", "$detalles.cantidad"))
        );

        for (Document doc : ventasCollection.aggregate(pipeline)) {
            Document idDoc = doc.get("_id", Document.class);
            String pv = idDoc != null ? idDoc.getString("puntoVenta") : "";
            String loc = idDoc != null ? idDoc.getString("localidad") : "";
            String tipo = idDoc != null ? idDoc.getString("tipo") : "";
            double monto = doc.getDouble("totalMonto");
            int unidades = doc.getInteger("cantidadUnidades");
            System.out.printf("%-15s %-15s %-20s $ %12.2f %20d%n", pv, loc, tipo, monto, unidades);
        }
        System.out.println();
    }

    private Document ventaToDocument(Venta venta) {
        Document doc = new Document();
        doc.append("numeroTicket", venta.getNumeroTicket());
        doc.append("fecha", venta.getFecha());
        doc.append("totalVenta", venta.getTotalVenta());
        doc.append("formaPago", venta.getFormaPago());

        if (venta.getSucursal() != null) {
            Document sucursalDoc = new Document();
            sucursalDoc.append("puntoVenta", venta.getSucursal().getPuntoVenta());
            if (venta.getSucursal().getDireccion() != null) {
                Document dirDoc = new Document();
                dirDoc.append("calle", venta.getSucursal().getDireccion().getCalle());
                dirDoc.append("numero", venta.getSucursal().getDireccion().getNumero());
                dirDoc.append("localidad", venta.getSucursal().getDireccion().getLocalidad());
                dirDoc.append("provincia", venta.getSucursal().getDireccion().getProvincia());
                sucursalDoc.append("direccion", dirDoc);
            }
            if (venta.getSucursal().getEncargado() != null) {
                sucursalDoc.append("encargado", empleadoToDocument(venta.getSucursal().getEncargado()));
            }
            if (venta.getSucursal().getEmpleados() != null) {
                List<Document> empleadosDoc = new ArrayList<>();
                for (var emp : venta.getSucursal().getEmpleados()) {
                    empleadosDoc.add(empleadoToDocument(emp));
                }
                sucursalDoc.append("empleados", empleadosDoc);
            }
            doc.append("sucursal", sucursalDoc);
        }

        if (venta.getCliente() != null) {
            doc.append("cliente", clienteToDocument(venta.getCliente()));
        }

        if (venta.getEmpleadoAtencion() != null) {
            doc.append("empleadoAtencion", empleadoToDocument(venta.getEmpleadoAtencion()));
        }

        if (venta.getEmpleadoCaja() != null) {
            doc.append("empleadoCaja", empleadoToDocument(venta.getEmpleadoCaja()));
        }

        if (venta.getDetalles() != null) {
            List<Document> detallesDoc = new ArrayList<>();
            for (var detalle : venta.getDetalles()) {
                Document detalleDoc = new Document();
                detalleDoc.append("cantidad", detalle.getCantidad());
                detalleDoc.append("precioUnitarioHistorico", detalle.getPrecioUnitarioHistorico());
                detalleDoc.append("subtotal", detalle.getSubtotal());
                if (detalle.getProducto() != null) {
                    Document prodDoc = new Document();
                    prodDoc.append("codigoNumerico", detalle.getProducto().getCodigoNumerico());
                    prodDoc.append("descripcion", detalle.getProducto().getDescripcion());
                    prodDoc.append("laboratorio", detalle.getProducto().getLaboratorio());
                    prodDoc.append("precio", detalle.getProducto().getPrecio());
                    prodDoc.append("tipo", detalle.getProducto().getTipo().name());
                    detalleDoc.append("producto", prodDoc);
                }
                detallesDoc.add(detalleDoc);
            }
            doc.append("detalles", detallesDoc);
        }

        return doc;
    }

    private Document empleadoToDocument(com.projects.farmaciamongodb.model.Empleado empleado) {
        Document doc = new Document();
        doc.append("cuil", empleado.getCuil());
        doc.append("dni", empleado.getDni());
        doc.append("nombre", empleado.getNombre());
        doc.append("apellido", empleado.getApellido());
        if (empleado.getDireccion() != null) {
            Document dirDoc = new Document();
            dirDoc.append("calle", empleado.getDireccion().getCalle());
            dirDoc.append("numero", empleado.getDireccion().getNumero());
            dirDoc.append("localidad", empleado.getDireccion().getLocalidad());
            dirDoc.append("provincia", empleado.getDireccion().getProvincia());
            doc.append("direccion", dirDoc);
        }
        if (empleado.getObraSocial() != null) {
            Document osDoc = new Document();
            osDoc.append("nombre", empleado.getObraSocial().getNombre());
            osDoc.append("numeroAfiliado", empleado.getObraSocial().getNumeroAfiliado());
            doc.append("obraSocial", osDoc);
        }
        return doc;
    }

    private Document clienteToDocument(com.projects.farmaciamongodb.model.Cliente cliente) {
        Document doc = new Document();
        doc.append("dni", cliente.getDni());
        doc.append("nombre", cliente.getNombre());
        doc.append("apellido", cliente.getApellido());
        if (cliente.getDireccion() != null) {
            Document dirDoc = new Document();
            dirDoc.append("calle", cliente.getDireccion().getCalle());
            dirDoc.append("numero", cliente.getDireccion().getNumero());
            dirDoc.append("localidad", cliente.getDireccion().getLocalidad());
            dirDoc.append("provincia", cliente.getDireccion().getProvincia());
            doc.append("direccion", dirDoc);
        }
        if (cliente.getObraSocial() != null) {
            Document osDoc = new Document();
            osDoc.append("nombre", cliente.getObraSocial().getNombre());
            osDoc.append("numeroAfiliado", cliente.getObraSocial().getNumeroAfiliado());
            doc.append("obraSocial", osDoc);
        }
        return doc;
    }
}
