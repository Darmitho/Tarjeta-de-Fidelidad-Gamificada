package com.tufidelidad;

import java.util.Objects;

public class Cliente {

    private final String id;
    private final String nombre;
    private final String correo;
    private int puntos;
    private NivelFidelidad nivel;

    public Cliente(String id, String nombre, String correo) {
        this.id = Objects.requireNonNull(id, "ID no puede ser null");
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser null");
        this.correo = Objects.requireNonNull(correo, "Correo no puede ser null");

        validarCorreo(this.correo);

        this.puntos = 0;
        this.nivel = NivelFidelidad.BRONCE;
    }

    private void validarCorreo(String correo) {
        if (!correo.contains("@")) {
            throw new IllegalArgumentException("Correo inválido");
        }
    }

    public int getPuntos() {
        return puntos;
    }

    public NivelFidelidad getNivel() {
        return nivel;
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
}
