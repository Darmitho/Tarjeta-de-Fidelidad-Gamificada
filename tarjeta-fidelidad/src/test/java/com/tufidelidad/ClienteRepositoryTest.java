package com.tufidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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

    @Test
    void actualizarClienteExistente_deberiaActualizarDatos() {
        ClienteRepository repo = new ClienteRepository();
        Cliente cliente = new Cliente("1", "Luis", "luis@mail.com");
        repo.agregar(cliente);

        Cliente actualizado = new Cliente("1", "Luis Renovado", "nuevo@mail.com");
        repo.actualizar(actualizado);

        Cliente resultado = repo.buscarPorId("1");
        assertEquals("Luis Renovado", resultado.getNombre());
        assertEquals("nuevo@mail.com", resultado.getCorreo());
    }

    @Test
    void actualizarClienteInexistente_deberiaLanzarExcepcion() {
        ClienteRepository repo = new ClienteRepository();
        Cliente cliente = new Cliente("999", "Fantasma", "ghost@mail.com");

        Exception e = assertThrows(IllegalArgumentException.class, () -> repo.actualizar(cliente));
        assertEquals("No existe cliente con ID: 999", e.getMessage());
    }

    @Test
    public void eliminarClienteExistenteDebeRemoverloDelRepositorio() {
        ClienteRepository repositorio = new ClienteRepository();
        Cliente cliente = new Cliente("123", "Daniel", "daniel@email.com");
        repositorio.agregar(cliente);

        repositorio.eliminar("123");

        assertThrows(IllegalArgumentException.class, () -> repositorio.buscarPorId("123"));
    }

    @Test
    public void listarDebeRetornarTodosLosClientesAgregados() {
        ClienteRepository repo = new ClienteRepository();

        Cliente cliente1 = new Cliente("1", "Ana", "ana@mail.com");
        Cliente cliente2 = new Cliente("2", "Luis", "luis@mail.com");

        repo.agregar(cliente1);
        repo.agregar(cliente2);

        List<Cliente> resultado = repo.listar();

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(cliente1));
        assertTrue(resultado.contains(cliente2));
    }


}
