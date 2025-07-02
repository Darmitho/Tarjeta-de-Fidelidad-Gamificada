package com.tufidelidad;

import java.time.LocalDateTime;
import java.util.Objects;

public class Compra {
    private static final int PUNTOS_POR_CADA_X_MONTO = 100;

    private final String idCompra;
    private final String idCliente;
    private final double monto;
    private final LocalDateTime fecha;

    public Compra(String idCompra, String idCliente, double monto, LocalDateTime fecha) {
        this.idCompra = Objects.requireNonNull(idCompra);
        this.idCliente = Objects.requireNonNull(idCliente);
        this.monto = monto;
        this.fecha = Objects.requireNonNull(fecha);
    }

    public String getIdCompra() {
        return idCompra;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int calcularPuntosBase() {
        return (int) (monto / PUNTOS_POR_CADA_X_MONTO);
    }

    public int calcularPuntosTotales(String nivelCliente)
}
