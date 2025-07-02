package com.tufidelidad;

import java.time.LocalDateTime;

public class Compra {
    private String idCompra;
    private String idCliente;
    private double monto;
    private LocalDateTime fecha;

    public Compra(String idCompra, String idCliente, double monto, LocalDateTime fecha) {
        this.idCompra = idCompra;
        this.idCliente = idCliente;
        this.monto = monto;
        this.fecha = fecha;
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
}
