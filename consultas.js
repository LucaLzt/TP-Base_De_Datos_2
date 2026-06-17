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

// CONSULTA 2A: Ventas por obra social (cadena completa)
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
      _id: { $ifNull: ["$cliente.obraSocial.nombre", "Privado"] },
      totalVendido: { $sum: "$totalVenta" },
      cantidadVentas: { $sum: 1 }
    }
  }
]);

// CONSULTA 2B: Ventas por obra social y sucursal
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
        localidad: "$sucursal.direccion.localidad",
        obraSocial: { $ifNull: ["$cliente.obraSocial.nombre", "Privado"] }
      },
      totalVendido: { $sum: "$totalVenta" },
      cantidadVentas: { $sum: 1 }
    }
  }
]);

// CONSULTA 3A: Ventas por medio de pago (cadena completa)
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
      _id: "$formaPago",
      totalVendido: { $sum: "$totalVenta" },
      cantidadVentas: { $sum: 1 }
    }
  }
]);

// CONSULTA 3B: Ventas por medio de pago y sucursal
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
        localidad: "$sucursal.direccion.localidad",
        formaPago: "$formaPago"
      },
      totalVendido: { $sum: "$totalVenta" },
      cantidadVentas: { $sum: 1 }
    }
  }
]);

// CONSULTA 5A: Ranking de productos por monto (cadena completa)
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
        codigo: "$detalles.producto.codigoNumerico",
        descripcion: "$detalles.producto.descripcion"
      },
      totalMonto: { $sum: "$detalles.subtotal" },
      cantidadUnidades: { $sum: "$detalles.cantidad" }
    }
  },
  { $sort: { totalMonto: -1 } }
]);

// CONSULTA 5B: Ranking de productos por monto por sucursal
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
        codigo: "$detalles.producto.codigoNumerico",
        descripcion: "$detalles.producto.descripcion"
      },
      totalMonto: { $sum: "$detalles.subtotal" },
      cantidadUnidades: { $sum: "$detalles.cantidad" }
    }
  },
  { $sort: { totalMonto: -1 } }
]);

// CONSULTA 6A: Ranking de productos por cantidad vendida (cadena completa)
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
        codigo: "$detalles.producto.codigoNumerico",
        descripcion: "$detalles.producto.descripcion"
      },
      cantidadUnidades: { $sum: "$detalles.cantidad" },
      totalMonto: { $sum: "$detalles.subtotal" }
    }
  },
  { $sort: { cantidadUnidades: -1 } }
]);

// CONSULTA 6B: Ranking de productos por cantidad vendida por sucursal
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
        codigo: "$detalles.producto.codigoNumerico",
        descripcion: "$detalles.producto.descripcion"
      },
      cantidadUnidades: { $sum: "$detalles.cantidad" },
      totalMonto: { $sum: "$detalles.subtotal" }
    }
  },
  { $sort: { cantidadUnidades: -1 } }
]);

// CONSULTA 7A: Ranking de clientes por monto de compras (cadena completa)
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
        dni: "$cliente.dni",
        nombre: "$cliente.nombre",
        apellido: "$cliente.apellido"
      },
      totalCompras: { $sum: "$totalVenta" },
      cantidadVentas: { $sum: 1 }
    }
  },
  { $sort: { totalCompras: -1 } }
]);

// CONSULTA 7B: Ranking de clientes por monto de compras por sucursal
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
        localidad: "$sucursal.direccion.localidad",
        dni: "$cliente.dni",
        nombre: "$cliente.nombre",
        apellido: "$cliente.apellido"
      },
      totalCompras: { $sum: "$totalVenta" },
      cantidadVentas: { $sum: 1 }
    }
  },
  { $sort: { totalCompras: -1 } }
]);

// CONSULTA 8A: Ranking de clientes por cantidad de unidades compradas (cadena completa)
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
        dni: "$cliente.dni",
        nombre: "$cliente.nombre",
        apellido: "$cliente.apellido"
      },
      cantidadUnidades: { $sum: "$detalles.cantidad" },
      totalGastado: { $sum: "$detalles.subtotal" }
    }
  },
  { $sort: { cantidadUnidades: -1 } }
]);

// CONSULTA 8B: Ranking de clientes por cantidad de unidades compradas por sucursal
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
        dni: "$cliente.dni",
        nombre: "$cliente.nombre",
        apellido: "$cliente.apellido"
      },
      cantidadUnidades: { $sum: "$detalles.cantidad" },
      totalGastado: { $sum: "$detalles.subtotal" }
    }
  },
  { $sort: { cantidadUnidades: -1 } }
]);
