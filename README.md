# Tarjeta-de-Fidelidad-Gamificada

## Diseño del sistema

A continuación se presenta el diseño inicial del sistema en formato UML:

```plantuml
@startuml
title Diagrama de Clases - Tarjeta de Fidelidad Gamificada

enum NivelFidelidad {
  BRONCE
  PLATA
  ORO
  PLATINO
}

class Cliente {
  - id: String
  - nombre: String
  - correo: String
  - puntos: int
  - nivel: NivelFidelidad
  - streakDias: int
  - historialCompras: List<Compra>

  + Cliente(id, nombre, correo)
  + agregarCompra(compra: Compra): void
  + calcularNivel(): void
  + calcularPuntos(compra: Compra): int
  + validarCorreo(): boolean
  + getResumen(): String
}

class Compra {
  - idCompra: String
  - idCliente: String
  - monto: double
  - fecha: LocalDateTime

  + Compra(idCompra, idCliente, monto, fecha)
  + getPuntosBase(): int
}

class ClienteRepository {
  - clientes: Map<String, Cliente>

  + agregar(cliente: Cliente): void
  + actualizar(cliente: Cliente): void
  + eliminar(id: String): void
  + listar(): List<Cliente>
  + buscarPorId(id: String): Cliente
}

class CompraRepository {
  - compras: List<Compra>

  + registrar(compra: Compra): void
  + listarPorCliente(idCliente: String): List<Compra>
  + listarTodas(): List<Compra>
}

Cliente "1" o-- "*" Compra : historial
ClienteRepository --> Cliente
CompraRepository --> Compra

@enduml
```
### Consideraciones
- Cliente: entidad central. Contiene lógica de puntos, nivel, y validación de correo.
- Compra: cada compra tiene un monto, fecha y otorga puntos base.
- NivelFidelidad: enum para facilitar la lógica de bonificación por nivel.
- ClienteRepository y CompraRepository: almacenan los datos en memoria.
