package com.tufidelidad;

import java.util.HashMap;
import java.util.Map;

public class ClienteRepository {

    private final Map<String, Cliente> clientes = new HashMap<>();

    public void agregar(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
    }

    public Cliente buscarPorId(String id) {
        return clientes.get(id);
    }
}
