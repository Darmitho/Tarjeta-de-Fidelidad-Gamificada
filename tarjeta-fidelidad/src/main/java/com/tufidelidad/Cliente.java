package com.tufidelidad;

public class Cliente {

    private String id;
    private String nombre;
    private String correo;
    private int puntos;
    private NivelFidelidad nivel;

    public Cliente(String id, String nombre, String correo) {
        validarCorreo(correo);

        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        
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
}
