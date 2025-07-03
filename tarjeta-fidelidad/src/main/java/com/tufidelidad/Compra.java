package com.tufidelidad;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class Compra {
    private static final int PUNTOS_POR_CADA_X_MONTO = 100;

    private static final Map<String, Double> MULTIPLICADORES = Map.of(
        "bronce", 1.0,
        "plata", 1.2,
        "oro", 1.5,
        "platino", 2.0
    );

    private final String idCompra;
    private final String idCliente;
    private final double monto;
    private final LocalDateTime fecha;
    private int bonus;

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

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    /**
     * Calcula los puntos base de la compra.
     * @return Puntos base
     */
    public int calcularPuntosBase() {
        return (int) (monto / PUNTOS_POR_CADA_X_MONTO);
    }

    /**
     * Calcula los puntos totales de la compra considerando el nivel del cliente.
     * @param nivelCliente Nivel de fidelidad del cliente
     * @return Puntos totales
     */
    public int calcularPuntosTotales(String nivelCliente) {
        int puntosBase = calcularPuntosBase();
        double multiplicador = obtenerMultiplicador(nivelCliente);
        return (int) (puntosBase * multiplicador) + bonus;
    }
    /**
     * Obtiene el multiplicador de puntos según el nivel del cliente.
     * @param nivelCliente Nivel de fidelidad del cliente
     * @return Multiplicador de puntos
     */
    private double obtenerMultiplicador(String nivelCliente) {
        return MULTIPLICADORES.getOrDefault(nivelCliente.toLowerCase(), 1.0);
    }
}