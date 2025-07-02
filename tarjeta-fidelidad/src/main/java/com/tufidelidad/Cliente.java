package com.tufidelidad;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cliente {

    private record NivelRegla(int umbral, NivelFidelidad nivel) {}
    
    private static final List<NivelRegla> REGLAS_NIVELES = List.of(
        new NivelRegla(3000, NivelFidelidad.PLATINO),
        new NivelRegla(1500, NivelFidelidad.ORO),
        new NivelRegla(500, NivelFidelidad.PLATA),
        new NivelRegla(0, NivelFidelidad.BRONCE)
    );

    private final String id;
    private final String nombre;
    private final String correo;
    private int puntos;
    private NivelFidelidad nivel;
    private int streakDias;
    private final List<Compra> historialCompras = new ArrayList<>();

    public Cliente(String id, String nombre, String correo) {
        this.id = Objects.requireNonNull(id, "ID no puede ser null");
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser null");
        this.correo = Objects.requireNonNull(correo, "Correo no puede ser null");

        validarCorreo(this.correo);

        this.puntos = 0;
        this.nivel = NivelFidelidad.BRONCE;
        this.streakDias = 0;
    }

    private void validarCorreo(String correo) {
        if (!correo.contains("@")) {
            throw new IllegalArgumentException("Correo inválido");
        }
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public int getPuntos() {
        return puntos;
    }

    public NivelFidelidad getNivel() {
        return nivel;
    }

    public int getStreakDias() {
        return streakDias;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", puntos=" + puntos +
                ", nivel=" + nivel +
                ", streakDias=" + streakDias +
                '}';
    }

    public void agregarCompra(Compra compra) {
        historialCompras.add(compra);
        sumarPuntos(compra);
        calcularNivel();
    }

    public List<Compra> getHistorialCompras() {
        return historialCompras;
    }

    private void sumarPuntos(Compra compra) {
        int puntosGanados = compra.calcularPuntosTotales(nivel.name().toLowerCase());
        this.puntos += puntosGanados;
    }

    void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void calcularNivel() {
        int puntosValidos = Math.max(0, puntos);
        for (NivelRegla regla : REGLAS_NIVELES) {
            if (puntosValidos >= regla.umbral()) {
                nivel = regla.nivel();
                return;
            }
        }
    }
}
