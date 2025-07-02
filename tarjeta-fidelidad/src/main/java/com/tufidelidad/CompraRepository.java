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

        if (aplicaBonusPorTresCompras(compra)) {
            compra.setBonus(10);
        }
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
        if (compra == null) {
            throw new IllegalArgumentException("La compra no puede ser null");
        }

        for (int i = 0; i < compras.size(); i++) {
            if (compras.get(i).getIdCompra().equals(compra.getIdCompra())) {
                compras.set(i, compra);
                return;
            }
        }

        throw new IllegalArgumentException("La compra no existe y no puede ser actualizada");
    }

    public void eliminar(String idCompra) {
        if (idCompra == null) {
            throw new IllegalArgumentException("El ID de compra no puede ser null");
        }

        boolean eliminado = compras.removeIf(c -> c.getIdCompra().equals(idCompra));
        if (!eliminado) {
            throw new IllegalArgumentException("No se encontró una compra con el ID proporcionado");
        }
    }

    private boolean aplicaBonusPorTresCompras(Compra nuevaCompra) {
        long comprasMismoDia = compras.stream()
            .filter(c -> c.getIdCliente().equals(nuevaCompra.getIdCliente()))
            .filter(c -> c.getFecha().toLocalDate().equals(nuevaCompra.getFecha().toLocalDate()))
            .count();
        return comprasMismoDia == 3; // Esta es la tercera compra del día
    }


}