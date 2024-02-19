package org.example;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

public class LaunchClient {
    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        try {
            client.connect("192.168.1.45", 2121);
            client.login("anonymous", "");

            // No es necesario verificar ni crear un directorio remoto nuevo.
            // Directamente se procede a subir el archivo al directorio del usuario.

            // Asegúrate de especificar la ruta completa del archivo a subir.
            String localFilePath = "/home/clientessh/prueba.txt"; // Asegúrate de que esta ruta sea correcta
            File uploadFile = new File(localFilePath);
            if (uploadFile.exists()) { // Verifica que el archivo exista antes de intentar subirlo
                try (FileInputStream fis = new FileInputStream(uploadFile)) {
                    client.setFileType(FTP.BINARY_FILE_TYPE);
                    String remoteFileName = "prueba.txt"; // Nombre del archivo en el servidor
                    boolean result = client.storeFile(remoteFileName, fis);
                    System.out.println("Subida realizada con éxito: " + result + ", " + client.getReplyString());
                }
            } else {
                System.out.println("El archivo local no existe.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.logout();
                client.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
