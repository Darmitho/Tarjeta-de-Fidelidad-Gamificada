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

}