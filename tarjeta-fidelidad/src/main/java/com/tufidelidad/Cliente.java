package com.tufidelidad;

public class Cliente {

    private String id;
    private String nombre;
    private String correo;

    public Cliente(String id, String nombre, String correo) {
        if (!correo.contains("@")) {
            throw new IllegalArgumentException("Correo inválido");
        }

        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }
}
