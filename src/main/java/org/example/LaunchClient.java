package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;

public class LaunchClient {

    public static void main(String[] args) {
        String servidorFTP = "10.18.0.174"; // Cambiar por la IP del servidor
        FTPClient clienteFTP = new FTPClient();

        try {
            clienteFTP.connect(servidorFTP, 2221);
            clienteFTP.login("anonymous", ""); // O tus credenciales si es necesario
            clienteFTP.enterLocalPassiveMode();
            clienteFTP.setFileType(FTPClient.BINARY_FILE_TYPE);

            // Crear un archivo .txt de ejemplo
            File archivoLocal = new File("usuario.txt");

            // Subir el archivo al servidor
            String archivoDestino = "/nuevosUsuarios/usuario.txt";
            try (FileInputStream fis = new FileInputStream(archivoLocal)) {
                clienteFTP.storeFile(archivoDestino, fis);
                System.out.println("Archivo subido exitosamente.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clienteFTP.isConnected()) {
                try {
                    clienteFTP.logout();
                    clienteFTP.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
