package com.tufidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;

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

    @Test
    void calcularNivel_debeActualizarCorrectamenteElNivel() {
        Cliente cliente = new Cliente("CL1", "Mario", "mario@mail.com");

        Map<Integer, NivelFidelidad> casos = Map.of(
            -10, NivelFidelidad.BRONCE,
            0, NivelFidelidad.BRONCE,
            499, NivelFidelidad.BRONCE,
            500, NivelFidelidad.PLATA,
            1499, NivelFidelidad.PLATA,
            1500, NivelFidelidad.ORO,
            2999, NivelFidelidad.ORO,
            3000, NivelFidelidad.PLATINO,
            9999, NivelFidelidad.PLATINO
        );

        for (Map.Entry<Integer, NivelFidelidad> entrada : casos.entrySet()) {
            cliente.setPuntos(entrada.getKey()); // usaremos un setter temporal o reflejo
            cliente.calcularNivel();
            assertEquals(entrada.getValue(), cliente.getNivel(), 
                "Fallo con puntos: " + entrada.getKey());
        }
    }

    @Test
    void actualizarStreak_calculaCorrectamenteLaRacha() {
        Cliente cliente = new Cliente("CL1", "Luis", "luis@mail.com");

        cliente.agregarCompra(new Compra("C1", "CL1", 100, LocalDateTime.of(2025, 7, 1, 10, 0))); // día -2
        cliente.agregarCompra(new Compra("C2", "CL1", 200, LocalDateTime.of(2025, 7, 2, 12, 0))); // día -1
        cliente.agregarCompra(new Compra("C3", "CL1", 300, LocalDateTime.of(2025, 7, 3, 9, 0)));  // día 0

        assertEquals(3, cliente.getStreakDias());
    }

    @Test
    void actualizarStreak_seCortaSiHayUnDiaFaltante() {
        Cliente cliente = new Cliente("CL1", "María", "maria@mail.com");

        // Compra días 1 y 3, falta el día 2 => streak = 1 (por el día 3 solamente)
        cliente.agregarCompra(new Compra("C1", "CL1", 150, LocalDateTime.of(2025, 7, 1, 9, 0)));
        cliente.agregarCompra(new Compra("C2", "CL1", 200, LocalDateTime.of(2025, 7, 3, 10, 0)));

        assertEquals(1, cliente.getStreakDias());
    }

}
