package com.projects.farmaciamongodb;

import com.projects.farmaciamongodb.service.JsonExportService;
import com.projects.farmaciamongodb.service.MongoPersistenceService;
import com.projects.farmaciamongodb.model.Venta;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * Punto de entrada de la aplicación Spring Boot.
 */
@SpringBootApplication
public class FarmaciaMongodbApplication implements CommandLineRunner {

    private final JsonExportService jsonExportService;
    private final MongoPersistenceService mongoPersistenceService;

    public FarmaciaMongodbApplication(JsonExportService jsonExportService, MongoPersistenceService mongoPersistenceService) {
        this.jsonExportService = jsonExportService;
        this.mongoPersistenceService = mongoPersistenceService;
    }

    public static void main(String[] args) {
        SpringApplication.run(FarmaciaMongodbApplication.class, args);
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        System.out.println("Iniciando TP Farmacia - Generador NoSQL/MongoDB...");

        // 1. Generar datos y exportar a JSON
        List<Venta> todasLasVentas = jsonExportService.generarYExportarDatos();

        // 2. Limpiar colección e insertar en MongoDB
        System.out.println("\n--- Persistiendo datos en MongoDB ---");
        mongoPersistenceService.limpiarColeccion();
        mongoPersistenceService.insertarVentas(todasLasVentas);

        // 3. Ejecutar consultas obligatorias (1 y 4)
        mongoPersistenceService.ejecutarConsultas();

        System.out.println("Proceso finalizado exitosamente.");
    }
}
