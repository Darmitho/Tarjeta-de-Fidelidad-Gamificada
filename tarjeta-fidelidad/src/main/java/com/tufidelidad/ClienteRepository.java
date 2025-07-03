package com.tufidelidad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClienteRepository {

    private final Map<String, Cliente> clientes = new HashMap<>();


    /**
     * Agrega un cliente al repositorio.
     * Si el cliente ya existe, lanza una excepción.
     *
     * @param cliente Cliente a agregar
     * @throws IllegalArgumentException si el cliente es null o ya existe
     */
    public void agregar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no puede ser null");
        }

        if (clientes.containsKey(cliente.getId())) {
            throw new IllegalArgumentException("Ya existe un cliente con ID: " + cliente.getId());
        }

        clientes.put(cliente.getId(), cliente);
    }

    /**
     * Busca un cliente por su ID.
     * Si no existe, lanza una excepción.
     *
     * @param id ID del cliente a buscar
     * @return Cliente encontrado
     * @throws IllegalArgumentException si el ID es null o no existe el cliente
     */
    public Cliente buscarPorId(String id) {
        if (!clientes.containsKey(id)) {
            throw new IllegalArgumentException("No existe cliente con ID: " + id);
        }
        return clientes.get(id);
    }


    /**
     * Actualiza un cliente existente en el repositorio.
     * Si el cliente no existe, lanza una excepción.
     * @param cliente Cliente a actualizar
     * @throws IllegalArgumentException si el cliente es null o no existe
     */
    public void actualizar(Cliente cliente) {
        Objects.requireNonNull(cliente, "El cliente no puede ser null");
        String id = cliente.getId();
        if (!clientes.containsKey(id)) {
            throw new IllegalArgumentException("No existe cliente con ID: " + id);
        }
        clientes.put(id, cliente);
    }

    /**
     * Elimina un cliente del repositorio.
     * Si el cliente no existe, lanza una excepción.
     * @param id ID del cliente a eliminar
     * @throws IllegalArgumentException si el ID es null o no existe
     */
    public void eliminar(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID no puede ser nulo o vacío");
        }
        if (!clientes.containsKey(id)) {
            throw new IllegalArgumentException("No existe cliente con ID: " + id);
        }
        clientes.remove(id);
    }

    /**
     * Lista todos los clientes en el repositorio.
     * Si no hay clientes, devuelve una lista vacía.
     * @return Lista de clientes
     */
    public List<Cliente> listar() {
        if (clientes.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(clientes.values());
    }
}