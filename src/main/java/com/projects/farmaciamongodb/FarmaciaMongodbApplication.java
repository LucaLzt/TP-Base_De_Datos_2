package com.projects.farmaciamongodb;

import com.projects.farmaciamongodb.service.JsonExportService;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación Spring Boot.
 */
@SpringBootApplication
public class FarmaciaMongodbApplication implements CommandLineRunner {

    private final JsonExportService jsonExportService;

    public FarmaciaMongodbApplication(JsonExportService jsonExportService) {
        this.jsonExportService = jsonExportService;
    }

    public static void main(String[] args) {
        SpringApplication.run(FarmaciaMongodbApplication.class, args);
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        System.out.println("Iniciando TP Farmacia - Generador NoSQL/MongoDB...");

        // Llamo al servicio de JsonExportService.
        jsonExportService.generarYExportarDatos();

        System.out.println("Proceso finalizado exitosamente.");
    }
}
