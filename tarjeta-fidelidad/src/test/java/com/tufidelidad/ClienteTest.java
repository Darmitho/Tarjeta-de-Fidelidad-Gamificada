package com.tufidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
    
}
