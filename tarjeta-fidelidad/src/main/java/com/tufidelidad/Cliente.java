package com.tufidelidad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    public String getResumen() {
        return String.format(
            "Cliente %s: %s - %s\nNivel: %s\nPuntos: %d\nStreak: %d días",
            id, nombre, correo, nivel, puntos, streakDias
        );
    }

    public void agregarCompra(Compra compra) {
        historialCompras.add(compra);
        sumarPuntos(compra);
        calcularNivel();
        actualizarStreak();
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

    private void actualizarStreak() {
        // Obtener fechas únicas de compra ordenadas de más reciente a más antigua
        List<LocalDate> fechas = historialCompras.stream()
            .map(compra -> compra.getFecha().toLocalDate())
            .distinct()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());

        int racha = 0;
        LocalDate diaReferencia = fechas.isEmpty() ? null : fechas.get(0);

        for (LocalDate fecha : fechas) {
            if (fecha.equals(diaReferencia.minusDays(racha))) {
                racha++;
            } else {
                break;
            }
        }

        this.streakDias = racha;
    }
}
