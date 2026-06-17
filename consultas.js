// CONSULTAS MONGODB - TP Base de Datos 2 (Entrega 3)
// Cadena de Farmacias - Modelo NoSQL
// Para ejecutar: copiar y pegar en MongoDB Shell o Compass
// Base de datos: farmacia_db
// Colección: ventas

// CONSULTA 1A: Total ventas para la cadena completa
// entre el 01/01/2026 y el 17/06/2026
db.ventas.aggregate([
  {
    $match: {
      fecha: {
        $gte: ISODate("2026-01-01T00:00:00Z"),
        $lte: ISODate("2026-06-17T23:59:59Z")
      }
    }
  },
  {
    $group: {
      _id: null,
      totalVendidoCadena: { $sum: "$totalVenta" },
      cantidadVentas: { $sum: 1 }
    }
  }
]);

// CONSULTA 1B: Total ventas agrupado por sucursal
// entre el 01/01/2026 y el 17/06/2026
db.ventas.aggregate([
  {
    $match: {
      fecha: {
        $gte: ISODate("2026-01-01T00:00:00Z"),
        $lte: ISODate("2026-06-17T23:59:59Z")
      }
    }
  },
  {
    $group: {
      _id: {
        puntoVenta: "$sucursal.puntoVenta",
        localidad: "$sucursal.direccion.localidad"
      },
      totalVendidoSucursal: { $sum: "$totalVenta" },
      cantidadVentas: { $sum: 1 }
    }
  }
]);

// CONSULTA 4A: Ventas por tipo de producto (cadena completa)
// entre el 01/01/2026 y el 17/06/2026
db.ventas.aggregate([
  {
    $match: {
      fecha: {
        $gte: ISODate("2026-01-01T00:00:00Z"),
        $lte: ISODate("2026-06-17T23:59:59Z")
      }
    }
  },
  { $unwind: "$detalles" },
  {
    $group: {
      _id: "$detalles.producto.tipo",
      totalMonto: { $sum: "$detalles.subtotal" },
      cantidadUnidades: { $sum: "$detalles.cantidad" }
    }
  }
]);

// CONSULTA 4B: Ventas por tipo de producto y por sucursal
// entre el 01/01/2026 y el 17/06/2026
db.ventas.aggregate([
  {
    $match: {
      fecha: {
        $gte: ISODate("2026-01-01T00:00:00Z"),
        $lte: ISODate("2026-06-17T23:59:59Z")
      }
    }
  },
  { $unwind: "$detalles" },
  {
    $group: {
      _id: {
        puntoVenta: "$sucursal.puntoVenta",
        localidad: "$sucursal.direccion.localidad",
        tipo: "$detalles.producto.tipo"
      },
      totalMonto: { $sum: "$detalles.subtotal" },
      cantidadUnidades: { $sum: "$detalles.cantidad" }
    }
  }
]);
