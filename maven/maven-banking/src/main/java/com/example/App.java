package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;

public class App {

    // 🔹 1. Leer el archivo JSON desde un .txt
    public static String leerArchivo(String rutaArchivo) {
        try {
            return new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔹 2. Obtener transacciones de un usuario específico
    public static List<JSONObject> obtenerTransacciones(String jsonData, String usuario) {
        List<JSONObject> transaccionesUsuario = new ArrayList<>();
        try {
            JSONObject transacciones = new JSONObject(jsonData);
            if (transacciones.has(usuario)) {
                JSONArray transaccionesArray = transacciones.getJSONArray(usuario);
                for (int i = 0; i < transaccionesArray.length(); i++) {
                    transaccionesUsuario.add(transaccionesArray.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return transaccionesUsuario;
    }

    // 🔹 3. Generar extracto bancario en un archivo .txt
    public static void generarExtracto(String usuario, List<JSONObject> transacciones) {
        StringBuilder extracto = new StringBuilder();
        extracto.append("Extracto bancario para: ").append(usuario).append("\n\n");
        for (JSONObject transaccion : transacciones) {
            extracto.append("Fecha: ").append(transaccion.getString("timestamp")).append("\n");
            extracto.append("Monto: ").append(transaccion.getString("balance")).append("\n");
            extracto.append("Descripción: ").append(transaccion.getString("type")).append("\n\n");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usuario + "_extracto.txt"))) {
            writer.write(extracto.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre de usuario: ");
        String usuario = scanner.nextLine();

        String rutaArchivo = "transactions.txt"; // Ruta del archivo JSON en la raíz del proyecto maven
        String jsonData = leerArchivo(rutaArchivo);

        if (jsonData != null) {
            List<JSONObject> transacciones = obtenerTransacciones(jsonData, usuario);
            generarExtracto(usuario, transacciones);
            System.out.println("Extracto generado para el usuario: " + usuario);
        } else {
            System.out.println("Error al leer el archivo de transacciones.");
        }

        scanner.close();
    }
}