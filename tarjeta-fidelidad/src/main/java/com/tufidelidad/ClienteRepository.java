package com.tufidelidad;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClienteRepository {

    private final Map<String, Cliente> clientes = new HashMap<>();

    public void agregar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no puede ser null");
        }

        if (clientes.containsKey(cliente.getId())) {
            throw new IllegalArgumentException("Ya existe un cliente con ID: " + cliente.getId());
        }

        clientes.put(cliente.getId(), cliente);
    }


    public Cliente buscarPorId(String id) {
    if (!clientes.containsKey(id)) {
        throw new IllegalArgumentException("No existe cliente con ID: " + id);
    }
    return clientes.get(id);
}


    public void actualizar(Cliente cliente) {
        Objects.requireNonNull(cliente, "El cliente no puede ser null");
        String id = cliente.getId();
        if (!clientes.containsKey(id)) {
            throw new IllegalArgumentException("No existe cliente con ID: " + id);
        }
        clientes.put(id, cliente);
    }

    public void eliminar(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID no puede ser nulo o vacío");
        }
        if (!clientes.containsKey(id)) {
            throw new IllegalArgumentException("No existe cliente con ID: " + id);
        }
        clientes.remove(id);
    }


}
