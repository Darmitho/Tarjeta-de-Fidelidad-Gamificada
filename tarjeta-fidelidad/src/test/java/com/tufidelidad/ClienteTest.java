package com.tufidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    @Test
    public void noDebePermitirCorreoInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("001", "Juan", "juan.invalido.com");
        });

        String mensajeEsperado = "Correo inválido";
        String mensajeObtenido = exception.getMessage();
        assertTrue(mensajeObtenido.contains(mensajeEsperado));
    }
}
