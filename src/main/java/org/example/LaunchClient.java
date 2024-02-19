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

            String remoteDirPath = "/home/clientessh/pruebaa";
            if (!client.changeWorkingDirectory(remoteDirPath)) {
                if (client.makeDirectory(remoteDirPath)) {
                    System.out.println("Directorio remoto creado con éxito.");
                    client.changeWorkingDirectory(remoteDirPath);
                } else {
                    System.out.println("Fallo al crear el directorio remoto.");
                    return; // Importante salir si no se puede cambiar al directorio deseado.
                }
            }

            // Asegúrate de especificar la ruta completa del archivo a subir.
            String localFilePath = "/home/mballesterosv/prueba.txt";
            File uploadFile = new File(localFilePath);
            try (FileInputStream fis = new FileInputStream(uploadFile)) {
                client.setFileType(FTP.BINARY_FILE_TYPE);
                boolean result = client.storeFile("prueba.txt", fis);
                System.out.println("Subida realizada con éxito: " + result + ", " + client.getReplyString());
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
