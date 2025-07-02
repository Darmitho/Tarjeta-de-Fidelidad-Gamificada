package com.tufidelidad;

import java.util.ArrayList;
import java.util.List;

public class CompraRepository {

    private final List<Compra> compras = new ArrayList<>();

    public void registrar(Compra compra) {
        compras.add(compra);
    }

    public List<Compra> listarTodas() {
        return new ArrayList<>(compras);
    }
}
