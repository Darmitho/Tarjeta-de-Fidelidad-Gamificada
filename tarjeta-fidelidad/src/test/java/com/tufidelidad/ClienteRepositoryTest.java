package com.tufidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteRepositoryTest {

    @Test
    void agregarCliente_deberiaGuardarClienteEnElRepositorio() {
        ClienteRepository repo = new ClienteRepository();
        Cliente cliente = new Cliente("1", "Luis", "luis@mail.com");

        repo.agregar(cliente);

        Cliente resultado = repo.buscarPorId("1");
        assertEquals("Luis", resultado.getNombre());
        assertEquals("luis@mail.com", resultado.getCorreo());
    }
}
