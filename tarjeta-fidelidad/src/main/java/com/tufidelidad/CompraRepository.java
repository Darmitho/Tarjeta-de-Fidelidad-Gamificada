package com.tufidelidad;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class CompraRepository {

    private final List<Compra> compras = new ArrayList<>();

    /**
     * Registra una nueva compra en el repositorio.
     * Si la compra es null, lanza una excepción.
     * Si es la tercera compra del día para el cliente, se aplica un bonus de 10 puntos.
     *
     * @param compra Compra a registrar
     */
    public void registrar(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("La compra no puede ser null");
        }
        compras.add(compra);

        if (aplicaBonusPorTresCompras(compra)) {
            compra.setBonus(10);
        }
    }

    /**
     * Lista todas las compras registradas.
     * Si no hay compras, devuelve una lista vacía.
     *
     * @return Lista de compras
     */
    public List<Compra> listarTodas() {
        return Collections.unmodifiableList(compras);
    }

    /**
     * Lista todas las compras realizadas por un cliente específico.
     * Si el ID del cliente es null, lanza una excepción.
     *
     * @param idCliente ID del cliente
     * @return Lista de compras del cliente
     */
    public List<Compra> listarPorCliente(String idCliente) {
        if (idCliente == null) {
            throw new IllegalArgumentException("El ID del cliente no puede ser null");
        }
        return compras.stream()
                .filter(c -> c.getIdCliente().equals(idCliente))
                .toList();
    }

    /**
     * Actualiza una compra existente en el repositorio.
     * Si la compra es null o no existe, lanza una excepción.
     *
     * @param compra Compra a actualizar
     */
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

    /**
     * Elimina una compra del repositorio.
     * Si la compra no existe, lanza una excepción.
     *
     * @param idCompra ID de la compra a eliminar
     */
    public void eliminar(String idCompra) {
        if (idCompra == null) {
            throw new IllegalArgumentException("El ID de compra no puede ser null");
        }

        boolean eliminado = compras.removeIf(c -> c.getIdCompra().equals(idCompra));
        if (!eliminado) {
            throw new IllegalArgumentException("No se encontró una compra con el ID proporcionado");
        }
    }

    /**
     * Verifica si se aplica un bonus por la tercera compra del día.
     *
     * @param nuevaCompra La nueva compra a verificar
     * @return true si se aplica el bonus, false en caso contrario para asegurarse de que se aplique solo en la tercera compra del día
     */
    private boolean aplicaBonusPorTresCompras(Compra nuevaCompra) {
        long comprasMismoDia = compras.stream()
            .filter(c -> c.getIdCliente().equals(nuevaCompra.getIdCliente()))
            .filter(c -> c.getFecha().toLocalDate().equals(nuevaCompra.getFecha().toLocalDate()))
            .count();
        return comprasMismoDia == 3; // Esta es la tercera compra del día
    }

    /**
     * Busca una compra por su ID.
     * Si el ID es null, lanza una excepción.
     *
     * @param idCompra ID de la compra a buscar
     * @return Compra encontrada o null si no existe
     */
    public Compra buscarPorId(String idCompra) {
        if (idCompra == null) {
            throw new IllegalArgumentException("El ID de compra no puede ser null");
        }

        return compras.stream()
                .filter(c -> idCompra.equals(c.getIdCompra()))
                .findFirst()
                .orElse(null);
    }
}