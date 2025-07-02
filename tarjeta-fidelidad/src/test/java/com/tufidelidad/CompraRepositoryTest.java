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
}