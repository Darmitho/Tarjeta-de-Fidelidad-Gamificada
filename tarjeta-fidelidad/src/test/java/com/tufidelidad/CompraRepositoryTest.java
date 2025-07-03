package com.tufidelidad;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompraRepositoryTest {

    @Test
    void registrarCompraDebeGuardarCompraEnLaLista() {
        CompraRepository repo = new CompraRepository();
        Compra compra = new Compra("C001", "CL001", 300, LocalDateTime.now());

        repo.registrar(compra);

        List<Compra> compras = repo.listarTodas();
        assertEquals(1, compras.size());
        assertEquals("C001", compras.get(0).getIdCompra());
    }

    @Test
    void registrarCompraNull_lanzaExcepcion() {
        CompraRepository repo = new CompraRepository();
        assertThrows(IllegalArgumentException.class, () -> repo.registrar(null));
    }

    @Test
    void registrarCompraEnRepositorioExistente_agregaCorrectamente() {
        CompraRepository repo = new CompraRepository();
        LocalDateTime fecha = LocalDateTime.of(2025, 7, 2, 10, 0);
        Compra primeraCompra = new Compra("C001", "CL01", 250.0, fecha);

        repo.registrar(primeraCompra);

        Compra segundaCompra = new Compra("C002", "CL02", 500.0, fecha.plusDays(1));
        repo.registrar(segundaCompra);

        List<Compra> compras = repo.listarTodas();
        assertEquals(2, compras.size());
        assertEquals("C001", compras.get(0).getIdCompra());
        assertEquals("C002", compras.get(1).getIdCompra());
    }

    @Test
    void listarPorCliente_devuelveSoloComprasDelCliente() {
        CompraRepository repo = new CompraRepository();
        LocalDateTime fecha = LocalDateTime.of(2025, 7, 2, 10, 0);

        Compra compra1 = new Compra("C001", "CL01", 100.0, fecha);
        Compra compra2 = new Compra("C002", "CL01", 200.0, fecha);
        Compra compra3 = new Compra("C003", "CL02", 150.0, fecha);

        repo.registrar(compra1);
        repo.registrar(compra2);
        repo.registrar(compra3);

        List<Compra> comprasCliente1 = repo.listarPorCliente("CL01");

        assertEquals(2, comprasCliente1.size());
        assertTrue(comprasCliente1.stream().allMatch(c -> c.getIdCliente().equals("CL01")));
    }

    @Test
    void listarPorCliente_sinComprasParaCliente_retornaListaVacia() {
        CompraRepository repo = new CompraRepository();
        LocalDateTime fecha = LocalDateTime.of(2025, 7, 2, 10, 0);
        repo.registrar(new Compra("C001", "CL01", 100.0, fecha));

        List<Compra> resultado = repo.listarPorCliente("CL99");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarPorCliente_conIdNull_lanzaExcepcion() {
        CompraRepository repo = new CompraRepository();
        assertThrows(IllegalArgumentException.class, () -> repo.listarPorCliente(null));
    }

    @Test
    void actualizarCompra_modificaCompraExistente() {
        CompraRepository repo = new CompraRepository();
        LocalDateTime fechaOriginal = LocalDateTime.of(2025, 7, 2, 10, 0);
        LocalDateTime fechaActualizada = LocalDateTime.of(2025, 7, 3, 15, 0);

        Compra original = new Compra("C001", "CL01", 100.0, fechaOriginal);
        repo.registrar(original);

        Compra actualizada = new Compra("C001", "CL01", 300.0, fechaActualizada);
        repo.actualizar(actualizada);

        List<Compra> comprasCliente = repo.listarPorCliente("CL01");

        assertEquals(1, comprasCliente.size());
        assertEquals(300.0, comprasCliente.get(0).getMonto());
        assertEquals(fechaActualizada, comprasCliente.get(0).getFecha());
    }

    @Test
        void actualizarCompra_nullDebeLanzarExcepcion() {
            CompraRepository repo = new CompraRepository();
            assertThrows(IllegalArgumentException.class, () -> repo.actualizar(null));
        }

    @Test
    void actualizarCompra_noExistenteDebeLanzarExcepcion() {
        CompraRepository repo = new CompraRepository();
        Compra noExistente = new Compra("NOEXISTE", "CL99", 200.0, LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> repo.actualizar(noExistente));
    }

    @Test
    void eliminarCompra_existenteLaEliminaCorrectamente() {
        CompraRepository repo = new CompraRepository();
        Compra compra = new Compra("C001", "CL01", 100.0, LocalDateTime.now());
        repo.registrar(compra);

        repo.eliminar("C001");

        List<Compra> comprasCliente = repo.listarPorCliente("CL01");
        assertTrue(comprasCliente.isEmpty());
    }

    @Test
    void eliminarCompra_nullDebeLanzarExcepcion() {
        CompraRepository repo = new CompraRepository();
        assertThrows(IllegalArgumentException.class, () -> repo.eliminar(null));
    }

    @Test
        void eliminarCompra_noExistenteDebeLanzarExcepcion() {
            CompraRepository repo = new CompraRepository();
            repo.registrar(new Compra("C001", "CL01", 150.0, LocalDateTime.now()));

            assertThrows(IllegalArgumentException.class, () -> repo.eliminar("NOEXISTE"));
        }

    @Test
    void bonoPorTresComprasEnMismoDia_otorgaBonusEnLaTerceraCompra() {
        CompraRepository repo = new CompraRepository();
        Cliente cliente = new Cliente("CL01", "Juan", "juan@mail.com");

        LocalDateTime ahora = LocalDateTime.now();
        Compra c1 = new Compra("C001", "CL01", 100.0, ahora.withHour(9));
        Compra c2 = new Compra("C002", "CL01", 200.0, ahora.withHour(12));
        Compra c3 = new Compra("C003", "CL01", 300.0, ahora.withHour(18));

        repo.registrar(c1);
        repo.registrar(c2);
        repo.registrar(c3);

        List<Compra> comprasCliente = repo.listarPorCliente("CL01");

        assertEquals(3, comprasCliente.size());

        int puntosTotales = comprasCliente.get(2).calcularPuntosTotales(cliente.getNivel().name().toLowerCase());
        assertEquals(13, puntosTotales); // 3 puntos por $300 + 10 bonus
    }

    @Test
    void bonoPorCuatroComprasEnMismoDia_otorgaBonusEnLaTerceraCompra() {
        CompraRepository repo = new CompraRepository();
        Cliente cliente = new Cliente("CL01", "Juan", "juan@mail.com");

        LocalDateTime ahora = LocalDateTime.now();
        Compra c1 = new Compra("C001", "CL01", 100.0, ahora.withHour(9));
        Compra c2 = new Compra("C002", "CL01", 200.0, ahora.withHour(12));
        Compra c3 = new Compra("C003", "CL01", 500.0, ahora.withHour(18));
        Compra c4 = new Compra("C004", "CL01", 400.0, ahora.withHour(20));

        repo.registrar(c1);
        repo.registrar(c2);
        repo.registrar(c3);
        repo.registrar(c4);

        List<Compra> comprasCliente = repo.listarPorCliente("CL01");

        assertEquals(4, comprasCliente.size());

        int puntosTotalesTerceraCompra = comprasCliente.get(2).calcularPuntosTotales(cliente.getNivel().name().toLowerCase());
        assertEquals(15, puntosTotalesTerceraCompra); // 5 puntos por $500 + 10 bonus

        int puntosTotalesCuartaCompra = comprasCliente.get(3).calcularPuntosTotales(cliente.getNivel().name().toLowerCase());
        assertEquals(4, puntosTotalesCuartaCompra); // solo 4 puntos ya que no aplica bonus
    }

    @Test
    void obtenerCompraPorID_debeRetornarCompraExistente() {
        CompraRepository repo = new CompraRepository();
        Compra compra = new Compra("C001", "CL01", 100.0, LocalDateTime.now());
        repo.registrar(compra);

        Compra compraEncontrada = repo.buscarPorId("C001");
        assertNotNull(compraEncontrada);
        assertEquals("C001", compraEncontrada.getIdCompra());
    }

}