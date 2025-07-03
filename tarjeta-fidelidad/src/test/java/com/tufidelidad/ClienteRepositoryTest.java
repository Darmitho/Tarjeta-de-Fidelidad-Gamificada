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

    @Test
    void agregarClienteExistente_deberiaLanzarExcepcion() {
        ClienteRepository repo = new ClienteRepository();
        Cliente cliente1 = new Cliente("1", "Luis", "luis@mail.com");
        repo.agregar(cliente1);

        Cliente clienteDuplicado = new Cliente("1", "Otro", "otro@mail.com");

        Exception e = assertThrows(IllegalArgumentException.class, () -> repo.agregar(clienteDuplicado));
        assertEquals("Ya existe un cliente con ID: 1", e.getMessage());
    }

    @Test
    void agregarClienteNull_deberiaLanzarExcepcion() {
        ClienteRepository repo = new ClienteRepository();

        Exception e = assertThrows(IllegalArgumentException.class, () -> repo.agregar(null));
        assertEquals("Cliente no puede ser null", e.getMessage());
    }

}
