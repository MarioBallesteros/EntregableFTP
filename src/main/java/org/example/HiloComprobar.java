package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HiloComprobar extends Thread {

    private final Path rutaaArchivo;

    public HiloComprobar(String rutaaArchivo) {
        this.rutaaArchivo = Paths.get(rutaaArchivo);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (Files.exists(rutaaArchivo)) {
                try {
                    String content = Files.readString(rutaaArchivo, StandardCharsets.UTF_8);
                    System.out.println("Contenido del archivo: " + content);
                    Files.delete(rutaaArchivo); // Opcional: Borrar el archivo después de leerlo.
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(5000); // Esperar 5 segundos antes de la próxima comprobación.
            } catch (InterruptedException e) {
                interrupt(); // Restaurar el estado de interrupción si ocurre una InterruptedException.
            }
        }
    }
}
