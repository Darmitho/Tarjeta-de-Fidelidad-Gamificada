package com.tufidelidad;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CompraTest {

    @Test
    public void compraDebeAlmacenarSusDatosCorrectamente() {
        LocalDateTime fecha = LocalDateTime.of(2025, 7, 2, 12, 30);
        Compra compra = new Compra("C001", "CL123", 1500.0, fecha);

        assertEquals("C001", compra.getIdCompra());
        assertEquals("CL123", compra.getIdCliente());
        assertEquals(1500.0, compra.getMonto());
        assertEquals(fecha, compra.getFecha());
    }

    @Test
    public void calcularPuntosBaseDebeRetornarUnoPorCada100Pesos() {
        LocalDateTime fecha = LocalDateTime.of(2025, 7, 2, 12, 30);
        Compra compra = new Compra("C001", "CL123", 380.0, fecha);

        int puntos = compra.calcularPuntosBase();

        assertEquals(3, puntos);
    }

    @Test
    public void calcularPuntosTotalesDebeAplicarMultiplicadorSegunNivel() {
        LocalDateTime fecha = LocalDateTime.of(2025, 7, 2, 12, 30);
        Compra compra = new Compra("C002", "CL123", 1000.0, fecha); // 10 puntos

        assertEquals(10, compra.calcularPuntosTotales("Bronce"));    // 10 × 1.0 = 10
        assertEquals(12, compra.calcularPuntosTotales("Plata"));     // 10 × 1.2 = 12
        assertEquals(15, compra.calcularPuntosTotales("Oro"));       // 10 × 1.5 = 15
        assertEquals(20, compra.calcularPuntosTotales("Platino"));   // 10 × 2.0 = 20
    }

    
}
