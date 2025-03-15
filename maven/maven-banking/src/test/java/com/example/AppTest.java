package com.example;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testLeerArchivo() {
        String rutaArchivo = "transactions.txt"; // Ruta del archivo JSON en la raíz del proyecto maven
        String contenidoEsperado = "{...}"; // Contenido esperado del archivo (puedes poner el contenido completo aquí)
        String contenidoLeido = App.leerArchivo(rutaArchivo);
        assertNotNull(contenidoLeido);
    }

    @Test
    public void testObtenerTransacciones() {
        String jsonData = "{"
                        + "\"juan.jose@urosario.edu.co\": [{\"balance\": \"50\", \"type\": \"Deposit\", \"timestamp\": \"2025-02-11 14:17:21.921536\"}],"
                        + "\"maria@urosario.edu.co\": [{\"balance\": \"200\", \"type\": \"Deposit\", \"timestamp\": \"2025-02-20 09:45:30.654321\"}]"
                        + "}";
        List<JSONObject> transacciones = App.obtenerTransacciones(jsonData, "juan.jose@urosario.edu.co");
        assertEquals(1, transacciones.size());
        assertEquals("50", transacciones.get(0).getString("balance"));
        assertEquals("Deposit", transacciones.get(0).getString("type"));
        assertEquals("2025-02-11 14:17:21.921536", transacciones.get(0).getString("timestamp"));
    }

    @Test
    public void testGenerarExtracto() throws IOException {
        String usuario = "juan.jose@urosario.edu.co";
        String jsonData = "{"
                        + "\"juan.jose@urosario.edu.co\": [{\"balance\": \"50\", \"type\": \"Deposit\", \"timestamp\": \"2025-02-11 14:17:21.921536\"}]"
                        + "}";
        List<JSONObject> transacciones = App.obtenerTransacciones(jsonData, usuario);
        App.generarExtracto(usuario, transacciones);

        String rutaArchivo = usuario + "_extracto.txt";
        assertTrue(Files.exists(Paths.get(rutaArchivo)));

        String contenidoEsperado = "Extracto bancario para: juan.jose@urosario.edu.co\n\n"
                                 + "Fecha: 2025-02-11 14:17:21.921536\n"
                                 + "Monto: 50\n"
                                 + "Descripción: Deposit\n\n";
        String contenidoLeido = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        assertEquals(contenidoEsperado, contenidoLeido);

        // Limpiar el archivo generado después de la prueba
        Files.delete(Paths.get(rutaArchivo));
    }
}