package com.tufidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
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
    public void getResumenDebeMostrarInformacionLegible() {
        Cliente cliente = new Cliente("123", "Daniel", "daniel@email.com");

        String esperado = """
            Cliente 123: Daniel - daniel@email.com
            Nivel: BRONCE
            Puntos: 0
            Streak: 0 días""";

        assertEquals(esperado, cliente.getResumen());
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

    @Test
    void realizarCompra_actualizaNivelDeBronceAPlata() {
        Cliente cliente = new Cliente("CL1", "Ana", "ana@mail.com");
        Compra compra = new Compra("C1", "CL1", 50000, LocalDateTime.now());

        cliente.agregarCompra(compra);

        assertEquals(NivelFidelidad.PLATA, cliente.getNivel());
        assertEquals(500, cliente.getPuntos());
    }

    @Test 
    void actualizarStreak_conComprasEnDiasNoConsecutivos() {
        Cliente cliente = new Cliente("CL1", "Carlos", "carlos@mail.com");

        cliente.agregarCompra(new Compra("C1", "CL1", 100, LocalDateTime.of(2025, 7, 2, 9, 0)));
        cliente.agregarCompra(new Compra("C2", "CL1", 200, LocalDateTime.of(2025, 7, 1, 10, 0)));
        cliente.agregarCompra(new Compra("C3", "CL1", 300, LocalDateTime.of(2025, 6, 29, 11, 0)));

        assertEquals(2, cliente.getStreakDias());
    }

    @Test
    void actualizarStreak_sinOrdenDeComprasAgregado() {
        Cliente cliente = new Cliente("CL1", "Sofía", "sofia@mail.com");

        cliente.agregarCompra(new Compra("C1", "CL1", 100, LocalDateTime.of(2025, 4, 1, 9, 0)));
        cliente.agregarCompra(new Compra("C2", "CL1", 200, LocalDateTime.of(2025, 4, 2, 10, 0)));
        cliente.agregarCompra(new Compra("C1", "CL1", 100, LocalDateTime.of(2025, 4, 4, 9, 0)));
        cliente.agregarCompra(new Compra("C2", "CL1", 200, LocalDateTime.of(2025, 4, 3, 10, 0)));

        assertEquals(4, cliente.getStreakDias());
    }

    @Test
    void eliminarCompra_debeActualizarHistorialPuntosNivelYStreak() {
        Cliente cliente = new Cliente("CL1", "Pedro", "pedro@mail.com");

        // Agregamos una compra que genera 2 puntos base * 1.0 (Bronce)
        Compra compra = new Compra("C1", "CL1", 250, LocalDateTime.of(2025, 7, 2, 9, 0));
        cliente.agregarCompra(compra);

        assertEquals(2, cliente.getPuntos());
        assertEquals(NivelFidelidad.BRONCE, cliente.getNivel());
        assertEquals(1, cliente.getStreakDias());

        // Eliminamos la compra
        cliente.eliminarCompra("C1");

        // Luego de eliminar: puntos, racha, historial, nivel deben actualizarse
        assertEquals(0, cliente.getPuntos(), "Los puntos deben resetearse a 0");
        assertEquals(0, cliente.getStreakDias(), "La racha debe ser 0 al eliminar la única compra");
        assertEquals(NivelFidelidad.BRONCE, cliente.getNivel(), "El nivel debe volver a Bronce");
        assertTrue(cliente.getHistorialCompras().isEmpty(), "El historial de compras debe estar vacío");
    }

    @Test
    void noDebePermitirNombreVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("CL100", "", "mail@dominio.com");
        });
    }

    @Test
    void noDebePermitirNombreNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Cliente("CL100", null, "mail@dominio.com");
        });
    }

    @Test
    void noDebePermitirCorreoNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Cliente("CL101", "Pedro", null);
        });
    }

    @Test
    void agregarPuntosNegativosNoDeberiaCambiarNada() {
        Cliente c = new Cliente("CL102", "Ana", "ana@mail.com");
        Compra compra = new Compra("C1", "CL102", -100, LocalDateTime.now());
        c.agregarCompra(compra);
        assertEquals(0, c.getPuntos(), "No debe aceptar puntos negativos");
    }

    @Test
    void nivelDebeActualizarseCorrectamentePorPuntos() {
        Cliente c = new Cliente("CL104", "Miguel", "miguel@mail.com");

        // Compra de 25.000 → 250 puntos → Nivel: Bronce
        c.agregarCompra(new Compra("C1", "CL104", 25000, LocalDateTime.now()));
        assertEquals(NivelFidelidad.BRONCE, c.getNivel());

        // Compra de 25.000 → 250 puntos → Total: 500 → Nivel: Plata
        c.agregarCompra(new Compra("C2", "CL104", 25000, LocalDateTime.now()));
        assertEquals(NivelFidelidad.PLATA, c.getNivel());

        // Compra de 50.000 → 600 puntos (20% bonus) → Total: 1100 → Nivel: Plata aún
        c.agregarCompra(new Compra("C3", "CL104", 50000, LocalDateTime.now()));
        assertEquals(NivelFidelidad.PLATA, c.getNivel());

        // Compra de 100.000 → 1500 puntos (50% bonus) → Total: 2600 → Nivel: Oro
        c.agregarCompra(new Compra("C4", "CL104", 100000, LocalDateTime.now()));
        assertEquals(NivelFidelidad.ORO, c.getNivel());

        // Compra de 100.000 → 1500 puntos (50% bonus) → Total: 4100 → Nivel: Platino
        c.agregarCompra(new Compra("C5", "CL104", 100000, LocalDateTime.now()));
        assertEquals(NivelFidelidad.PLATINO, c.getNivel());
    }

    @Test
    void streakDebeCortarseSiHayDiasSaltados() {
        Cliente c = new Cliente("CL106", "Juan", "juan@mail.com");

        LocalDateTime hoy = LocalDateTime.now();
        c.agregarCompra(new Compra("C1", "CL106", 10000, hoy));
        c.agregarCompra(new Compra("C2", "CL106", 20000, hoy.minusDays(2)));
        c.agregarCompra(new Compra("C3", "CL106", 30000, hoy.minusDays(3)));

        assertEquals(1, c.getStreakDias(), "Solo hoy cuenta, los demás no son consecutivos");
    }

    @Test
    void eliminarCompraInexistenteLanzaExcepcion() {
        Cliente c = new Cliente("CL108", "Mario", "mario@mail.com");

        assertThrows(IllegalArgumentException.class, () -> {
            c.eliminarCompra("NO_EXISTE");
        });
    }

    @Test
    void agregarBonusPorTresComprasEnElMismoDia() {
        Cliente c = new Cliente("CL109", "Laura", "laura@mail.com");
        LocalDateTime hoy = LocalDateTime.now();

        c.agregarCompra(new Compra("C1", "CL109", 10000, hoy));
        c.agregarCompra(new Compra("C2", "CL109", 10000, hoy));
        c.agregarCompra(new Compra("C3", "CL109", 10000, hoy));

        assertEquals(310, c.getPuntos(), "Debería haber recibido el bono por 3 compras en el mismo día");
    }

    @Test
    void clienteConMilComprasDebeTenerPuntosCorrectos() {
        Cliente c = new Cliente("MIL", "StressTest", "stress@mail.com");
        for (int i = 0; i < 1000; i++) {
            c.agregarCompra(new Compra("C" + i, "MIL", 1000, LocalDateTime.now().minusDays(i)));
        }
        assertTrue(c.getPuntos() > 10000); // Mayor a 10000 por que el usuario va subiendo de nivel
        assertEquals(1000, c.getStreakDias()); // Solo una compra por día, no consecutiva
    }

    @Test
    void eliminarCompraIntermediaDebeRecalcularBienStreak() {
        Cliente c = new Cliente("ELIM", "Eliminar", "elim@mail.com");

        LocalDateTime hoy = LocalDateTime.now();
        c.agregarCompra(new Compra("C1", "ELIM", 10000, hoy));
        c.agregarCompra(new Compra("C2", "ELIM", 10000, hoy.minusDays(1)));
        c.agregarCompra(new Compra("C3", "ELIM", 10000, hoy.minusDays(2)));

        assertEquals(3, c.getStreakDias());

        c.eliminarCompra("C2");
        assertEquals(1, c.getStreakDias(), "El streak debería romperse sin el día intermedio");
    }

    @Test
    void modificarListaExternaDeHistorialNoDebeAfectarInterno() {
        Cliente c = new Cliente("SAFE", "Seguro", "seguro@mail.com");
        c.agregarCompra(new Compra("CX", "SAFE", 10000, LocalDateTime.now()));

        List<Compra> historial = c.getHistorialCompras();
        assertEquals(1, c.getHistorialCompras().size(), "Debería haber una compra en el historial");

        historial.clear();  // Si es la lista real, esto es un error

        assertEquals(1, c.getHistorialCompras().size(), "La lista interna no debería verse afectada");
    }

    @Test
    void agregarCompraConFechaFuturaNoDeberiaPermitirlo() {
        Cliente c = new Cliente("FUTURO", "Futuro", "futuro@mail.com");
        assertThrows(IllegalArgumentException.class, () -> {
            c.agregarCompra(new Compra("C1", "FUTURO", 10000, LocalDateTime.now().plusDays(1)));
        });

    }

    @Test
    void agregarCompraConMontoCeroNoDeberiaAfectarPuntos() {
        Cliente c = new Cliente("CERO", "Cero", "cero@mail.com");
        c.agregarCompra(new Compra("C1", "CERO", 0, LocalDateTime.now()));
        assertEquals(0, c.getPuntos(), "No debería haber puntos por una compra de cero");
        assertTrue(c.getHistorialCompras().isEmpty(), "No debería haber compras registradas con monto cero");
    }

}
