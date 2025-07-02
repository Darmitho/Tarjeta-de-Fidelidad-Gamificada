package com.tufidelidad;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class CompraRepository {

    private final List<Compra> compras = new ArrayList<>();

    public void registrar(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("La compra no puede ser null");
        }
        compras.add(compra);
    }

    public List<Compra> listarTodas() {
        return Collections.unmodifiableList(compras);
    }

    public List<Compra> listarPorCliente(String idCliente) {
        if (idCliente == null) {
            throw new IllegalArgumentException("El ID del cliente no puede ser null");
        }
        return compras.stream()
                .filter(c -> c.getIdCliente().equals(idCliente))
                .toList();
    }

    public void actualizar(Compra compra) {
    for (int i = 0; i < compras.size(); i++) {
        if (compras.get(i).getIdCompra().equals(compra.getIdCompra())) {
            compras.set(i, compra);
            return;
        }
    }
}
}
