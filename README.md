# Farmacia NoSQL - MongoDB

Trabajo Practico Integrador Nro. 2 - Base de Datos 2  
Universidad Nacional de Lanus (UNLa)

## Descripcion

Este proyecto es una herramienta de generacion de datos para una cadena de farmacias, desarrollada en Java con Spring Boot. Simula la operatoria de ventas de multiples sucursales y exporta los resultados a un archivo JSON (`ventas_entregable.json`) utilizando un modelo de datos completamente embebido (denormalizado), disenado para ser persistido en MongoDB.

## Tecnologias

- **Java 21** - Lenguaje de programacion
- **Spring Boot 4.0.6** - Framework de aplicacion
- **MongoDB** - Base de datos NoSQL documental (dependencia incluida para futura integracion)
- **Jackson** - Serializacion/deserializacion JSON
- **Maven 3.9.16** - Herramienta de build (con Maven Wrapper incluido)
- **JUnit 5 / Spring Boot Test** - Testing

## Estructura del proyecto

```
farmacia-mongodb/
├── pom.xml
├── mvnw / mvnw.cmd
├── ventas_entregable.json       (archivo generado al ejecutar)
│
└── src/
    ├── main/
    │   ├── java/.../farmaciamongodb/
    │   │   ├── FarmaciaMongodbApplication.java
    │   │   ├── controller/           (vacio - planificado)
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
    │   │   ├── repository/           (vacio - planificado)
    │   │   └── service/
    │   │       └── JsonExportService.java
    │   └── resources/
    │       └── application.yaml
    └── test/
        └── java/.../FarmaciaMongodbApplicationTests.java
```

## Modelo de datos

El diseno sigue un esquema **denormalizado con documentos embebidos**, pensado para MongoDB. Cada `Venta` contiene toda la informacion relacionada dentro de un unico documento:

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

### Clases del modelo

| Clase | Descripcion |
|-------|-------------|
| `Venta` | Documento raiz. Representa una venta con ticket, fecha, total, forma de pago, sucursal, cliente, empleados y detalle. |
| `Cliente` | Cliente de la farmacia. Incluye DNI, nombre, direccion y obra social (opcional). |
| `DetalleVenta` | Linea de detalle de una venta. Almacena cantidad, precio historico (al momento de la venta) y subtotal. |
| `Direccion` | Direccion con calle, numero, localidad y provincia. |
| `Empleado` | Empleado de la farmacia. Incluye CUIL, DNI, nombre, direccion y obra social. |
| `ObraSocial` | Obra social con nombre y numero de afiliado (nullable). |
| `Producto` | Producto con codigo numerico, tipo (MEDICAMENTO / PERFUMERIA), descripcion, laboratorio y precio. |
| `Sucursal` | Sucursal con punto de venta, direccion, encargado y lista de empleados. |
| `TipoProducto` | Enumeracion con dos valores: `MEDICAMENTO` y `PERFUMERIA`. |

## Funcionamiento

La aplicacion se ejecuta como una herramienta de linea de comandos (CommandLineRunner) y realiza los siguientes pasos:

1. **Inicializar catalogos**: Crea 10 productos (7 medicamentos, 3 perfumeria), 10 clientes y 3 sucursales (Lanus, Avellaneda, Banfield) con 3 empleados cada una.
2. **Simular ventas**: Genera entre 24 y 36 ventas aleatorias por sucursal, con metodo de pago, cliente, empleados y productos aleatorios. Los totales y subtotales se calculan automaticamente.
3. **Exportar a JSON**: Escribe todas las ventas generadas en `ventas_entregable.json` con formato pretty-print.

### Datos de prueba

- Marcas realistas del mercado argentino: Bayer, Pfizer, Roemmers, Bago, Elea, Casasco, Dove, Natura, Carolina Herrera.
- Obras sociales: OSDE, Swiss Medical.
- Localidades del conurbano bonaerense: Lanus, Avellaneda, Banfield.
- Precios en pesos argentinos (ARS).

## Como ejecutar

### Requisitos

- Java 21 JDK
- Git (opcional, para clonar)

### Pasos

```bash
# Clonar el repositorio
git clone https://github.com/LucaLzt/TP-Base_De_Datos_2.git
cd farmacia-mongodb

# Ejecutar con Maven Wrapper
./mvnw spring-boot:run

# O bien compilar y ejecutar el JAR
./mvnw clean package
java -jar target/farmacia-mongodb-0.0.1-SNAPSHOT.jar
```

Al ejecutarse, la aplicacion genera el archivo `ventas_entregable.json` en la raiz del proyecto y finaliza.

**Nota**: Actualmente la aplicacion no requiere una instancia de MongoDB en ejecucion, ya que solo genera un archivo JSON. La dependencia de MongoDB esta incluida para futuras etapas del proyecto.

## Estado del proyecto

Este proyecto corresponde a la **Entrega Nro. 2** de 3. El estado actual es:

- [x] Modelo de clases (POJOs) con estructura embebida para MongoDB
- [x] Generador de datos de prueba y exportacion a JSON
- [ ] Repositorios MongoDB (planificado para entrega futura)
- [ ] Controladores REST (planificado para entrega futura)
- [ ] Interfaz web / templates (planificado para entrega futura)

## Decisiones de diseno

- **Modelo embebido (denormalizado)**: Se opto por un diseno documental donde cada venta contiene todos sus datos relacionados dentro de un unico documento, evitando referencias y joins. Esto es apropiado para MongoDB y para el patron de acceso tipico de consulta de ventas.
- **Precio historico por linea**: Cada `DetalleVenta` almacena `precioUnitarioHistorico` para conservar el precio del producto al momento de la venta, independientemente de cambios futuros en el catalogo.
- **Totales auto-calculados**: Los subtotales y el total de la venta se calculan en memoria a traves de metodos de dominio (`calcularSubtotal()`, `recalcularTotal()`), no en la base de datos.
- **Sin Lombok**: Todos los getters, setters y constructores estan escritos manualmente.
- **Maven Wrapper**: Se incluye `mvnw` para poder compilar el proyecto sin necesidad de tener Maven instalado previamente.
