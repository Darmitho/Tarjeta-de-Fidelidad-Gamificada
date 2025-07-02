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
}
