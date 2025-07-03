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


    @Test
    void calcularPuntosConNivelInvalidoDebeRetornarPuntosNormales() {
        Compra compra = new Compra("C1", "CL1", 10000, LocalDateTime.now());
        int puntos = compra.calcularPuntosTotales("nivel_invalido");
        assertEquals(100, puntos, "Debe usar puntos base si el nivel no es válido");
    }

    @Test
    void compraConMontoCeroDebeGenerarCeroPuntos() {
        Compra compra = new Compra("C2", "CL2", 0, LocalDateTime.now());
        int puntos = compra.calcularPuntosTotales("bronce");
        assertEquals(0, puntos);
    }

    @Test
    void puntosPorNivelSonCorrectos() {
        Compra compra = new Compra("C3", "CL3", 10000, LocalDateTime.now());

        assertEquals(100, compra.calcularPuntosTotales("bronce"));
        assertEquals(120, compra.calcularPuntosTotales("plata"));
        assertEquals(150, compra.calcularPuntosTotales("oro"));
        assertEquals(200, compra.calcularPuntosTotales("platino"));
    }

    @Test
    void noDebePermitirFechaNula() {
        assertThrows(NullPointerException.class, () -> {
            new Compra("C4", "CL4", 1000, null);
        });
    }

    @Test
    void noDebePermitirIdCompraNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Compra(null, "CL4", 1000, LocalDateTime.now());
        });
    }

    @Test
    void noDebePermitirIdClienteNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Compra("C5", null, 1000, LocalDateTime.now());
        });
    }

    @Test
    void noDebePermitirMontoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Compra("C6", "CL6", -1000, LocalDateTime.now());
        });
    }

    @Test
    void compraConMontoMuyGrandeDebeCalcularPuntosCorrectamente() {
        Compra compra = new Compra("C6", "CL6", Integer.MAX_VALUE, LocalDateTime.now());
        int puntos = compra.calcularPuntosTotales("platino"); // *2 el valor base

        // 1% de MAX_VALUE = 21.474.836 → *2 = 42.949.672
        assertEquals((int)((Integer.MAX_VALUE / 100.0) * 2), puntos);
    }

    @Test
    void compraConFechaFuturaEsInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            Compra compra = new Compra("C7", "CL7", 10000, LocalDateTime.now().plusDays(5));
        });
    }

    @Test
    void dosComprasConMismoContenidoNoSonIguales() {
        LocalDateTime fecha = LocalDateTime.now();
        Compra c1 = new Compra("C8", "CL8", 10000, fecha);
        Compra c2 = new Compra("C8", "CL8", 10000, fecha);

        assertNotEquals(c1, c2, "Aunque los datos coincidan, no son el mismo objeto");
    }

    @Test
    void noDebePermitirMontoCero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Compra("C9", "CL9", 0, LocalDateTime.now());
        });
    }
    
}
