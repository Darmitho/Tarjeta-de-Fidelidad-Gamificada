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
}
