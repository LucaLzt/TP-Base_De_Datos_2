# Farmacia NoSQL - MongoDB

Trabajo Practico Integrador Nro. 3 - Base de Datos 2  
Universidad Nacional de Lanus (UNLa)

## Descripcion

Este proyecto es una herramienta de generacion de datos y consultas para una cadena de farmacias, desarrollada en Java con Spring Boot y MongoDB. Simula la operatoria de ventas de multiples sucursales, persiste los datos en MongoDB (driver nativo) y ejecuta 8 consultas de agregacion, mostrando los resultados en consola.

## Tecnologias

- **Java 21** - Lenguaje de programacion
- **Spring Boot 4.0.6** - Framework de aplicacion
- **MongoDB 7.0** - Base de datos NoSQL documental
- **MongoDB Java Driver 5.6.5** - Driver nativo de MongoDB
- **Docker** - Contenedor para MongoDB (docker-compose)
- **Jackson** - Serializacion/deserializacion JSON
- **Maven 3.9.16** - Herramienta de build (con Maven Wrapper incluido)
- **JUnit 5 / Spring Boot Test** - Testing

## Estructura del proyecto

```
farmacia-mongodb/
├── pom.xml
├── docker-compose.yml
├── mvnw / mvnw.cmd
├── ventas_entregable.json       (archivo generado al ejecutar)
├── consultas.js                 (consultas nativas MongoDB)
│
└── src/
    ├── main/
    │   ├── java/.../farmaciamongodb/
    │   │   ├── FarmaciaMongodbApplication.java
    │   │   ├── model/
    │   │   │   ├── Venta.java
    │   │   │   ├── Cliente.java
    │   │   │   ├── DetalleVenta.java
    │   │   │   ├── Direccion.java
    │   │   │   ├── Empleado.java
    │   │   │   ├── ObraSocial.java
    │   │   │   ├── Producto.java
    │   │   │   ├── Sucursal.java
    │   │   │   └── TipoProducto.java (enum)
    │   │   └── service/
    │   │       ├── JsonExportService.java
    │   │       └── MongoPersistenceService.java
    │   └── resources/
    │       └── application.yaml
    └── test/
        └── java/.../FarmaciaMongodbApplicationTests.java
```

## Modelo de datos

El diseno sigue un esquema **denormalizado con documentos embebidos**, propio de MongoDB. Cada `Venta` es un documento que contiene toda la informacion relacionada:

```
Venta
├── numeroTicket: String
├── fecha: Date
├── totalVenta: Double (auto-calculado)
├── formaPago: String (Efectivo / Tarjeta / Debito)
├── sucursal: Sucursal (embebido)
│   ├── puntoVenta, direccion (Direccion embebida)
│   ├── encargado: Empleado (embebido)
│   └── empleados: List<Empleado>
├── cliente: Cliente (embebido)
│   ├── dni, nombre, apellido
│   ├── direccion: Direccion
│   └── obraSocial: ObraSocial (nullable)
├── empleadoAtencion: Empleado (embebido)
├── empleadoCaja: Empleado (embebido)
└── detalles: List<DetalleVenta> (embebidos)
    ├── cantidad, precioUnitarioHistorico, subtotal
    └── producto: Producto (embebido)
```

## Funcionamiento

Al ejecutar la aplicacion (CommandLineRunner), se realizan los siguientes pasos automaticamente:

1. **Inicializar catalogos**: Crea 10 productos (7 medicamentos, 3 perfumeria), 10 clientes y 3 sucursales (Lanus, Avellaneda, Banfield) con 3 empleados cada una.
2. **Simular ventas**: Genera entre 24 y 36 ventas aleatorias por sucursal (90 a 108 ventas totales), con metodo de pago, cliente, empleados y productos aleatorios. Las fechas se distribuyen entre enero y junio de 2026. Los totales y subtotales se calculan automaticamente.
3. **Exportar a JSON**: Escribe todas las ventas generadas en `ventas_entregable.json`.
4. **Persistir en MongoDB**: Limpia la coleccion `ventas` existente e inserta cada venta como un documento individual en la base de datos `farmacia_db`.
5. **Ejecutar 8 consultas de agregacion**: Corre consultas sobre MongoDB usando el framework de agregacion y muestra los resultados formateados en consola.

## Consultas implementadas

Cada consulta tiene dos variantes: **cadena completa** y **por sucursal**.

| # | Consulta | Variantes |
|---|----------|-----------|
| 1 | Total de ventas entre fechas | Cadena y sucursal |
| 2 | Ventas por obra social (o privado) | Cadena y sucursal |
| 3 | Ventas por medio de pago | Cadena y sucursal |
| 4 | Ventas por tipo de producto (MEDICAMENTO / PERFUMERIA) | Cadena y sucursal |
| 5 | Ranking de productos por monto | Cadena y sucursal |
| 6 | Ranking de productos por cantidad vendida | Cadena y sucursal |
| 7 | Ranking de clientes por monto de compras | Cadena y sucursal |
| 8 | Ranking de clientes por cantidad de compras | Cadena y sucursal |

Las consultas tambien estan disponibles en `consultas.js` como pipelines nativas de MongoDB, listas para ejecutar en MongoDB Shell o Compass.

## Como ejecutar

### Requisitos

- Java 21 JDK
- Docker (para el contenedor de MongoDB)
- Git (opcional, para clonar)

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/LucaLzt/TP-Base_De_Datos_2.git
cd farmacia-mongodb

# 2. Iniciar MongoDB con Docker
docker-compose up -d

# 3. Ejecutar la aplicacion con Maven Wrapper
./mvnw spring-boot:run
```

Al ejecutarse, la aplicacion:
- Genera el archivo `ventas_entregable.json`
- Inserta los datos en MongoDB (coleccion `ventas`, base `farmacia_db`)
- Imprime en consola los reportes de las 8 consultas

## Decisiones de diseno

- **Modelo embebido (denormalizado)**: Cada venta contiene todos sus datos relacionados dentro de un unico documento, optimo para MongoDB.
- **Precio historico por linea**: Cada `DetalleVenta` almacena `precioUnitarioHistorico` para conservar el precio del producto al momento de la venta.
- **Totales auto-calculados**: Los subtotales y el total se calculan en memoria mediante metodos de dominio.
- **Driver nativo MongoDB**: Se utiliza el driver sincronico de MongoDB (`mongodb-driver-sync`) en lugar de Spring Data, alineado con la arquitectura de Spring Boot 4.
- **Sin Lombok**: Todos los getters, setters y constructores estan escritos manualmente.
- **Maven Wrapper**: Permite compilar el proyecto sin tener Maven instalado.
- **Docker Compose**: Incluye `docker-compose.yml` para levantar MongoDB sin instalacion manual.

## Estado del proyecto

Este proyecto corresponde a la **Entrega Nro. 3** de 3. Estado actual:

- [x] Modelo de clases (POJOs) con estructura embebida para MongoDB
- [x] Generador de datos de prueba y exportacion a JSON
- [x] Persistencia en MongoDB mediante driver nativo
- [x] Consultas 1 a 8 con framework de agregacion (cadena + sucursal)
- [x] Documentacion de consultas nativas en `consultas.js`
