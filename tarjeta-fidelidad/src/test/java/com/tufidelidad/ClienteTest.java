package com.tufidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class ClienteTest {

    @Test
    public void noDebePermitirCorreoInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("001", "Juan", "juan.invalido.com");
        });

        String mensajeEsperado = "Correo inválido";
        String mensajeObtenido = exception.getMessage();
        assertTrue(mensajeObtenido.contains(mensajeEsperado));
    }

    @Test
    public void clienteDebeIniciarConPuntosYCategoriaIniciales() {
        Cliente cliente = new Cliente("001", "Juan", "juan@email.com");

        assertEquals(0, cliente.getPuntos());
        assertEquals(NivelFidelidad.BRONCE, cliente.getNivel());
    }

    @Test
    public void clienteDebeAlmacenarCorrectamenteSusDatos() {
        Cliente cliente = new Cliente("123", "Daniel", "daniel@email.com");

        assertEquals("123", cliente.getId());
        assertEquals("Daniel", cliente.getNombre());
        assertEquals("daniel@email.com", cliente.getCorreo());
    }

    @Test
    public void clienteDebeIniciarConStreakEnCero() {
        Cliente cliente = new Cliente("999", "Lucía", "lucia@email.com");
        assertEquals(0, cliente.getStreakDias());
    }

    @Test
    public void clienteToStringDebeMostrarInformacionLegible() {
        Cliente cliente = new Cliente("123", "Daniel", "daniel@email.com");

        String esperado = "Cliente{id='123', nombre='Daniel', correo='daniel@email.com', puntos=0, nivel=BRONCE, streakDias=0}";
        assertEquals(esperado, cliente.toString());
    }

    @Test
    void agregarCompra_sumaPuntosYActualizaHistorial() {
        Cliente cliente = new Cliente("CL1", "Ana", "ana@mail.com");
        Compra compra = new Compra("C1", "CL1", 250.0, LocalDateTime.of(2025, 7, 2, 10, 0));

        cliente.agregarCompra(compra);

        assertEquals(2, cliente.getPuntos()); // 2 puntos base * 1.0
        assertEquals(1, cliente.getHistorialCompras().size());
        assertEquals("C1", cliente.getHistorialCompras().get(0).getIdCompra());
    }

    
}
