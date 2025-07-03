package com.tufidelidad;

import java.util.HashMap;
import java.util.Map;

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
        return clientes.get(id);
    }

    public void actualizar(Cliente cliente) {
    if (!clientes.containsKey(cliente.getId())) {
        throw new IllegalArgumentException("No existe cliente con ID: " + cliente.getId());
    }
    clientes.put(cliente.getId(), cliente);
}

}
