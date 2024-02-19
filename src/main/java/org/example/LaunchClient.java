package org.example;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

public class LaunchClient {
    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        try {
            // Especifica el puerto 2121 en la llamada connect
// Conéctate al servidor y autentícate primero
            client.connect("192.168.1.45", 2121);
            client.login("anonymous", "password");

// Intenta cambiar al directorio remoto. Si falla, intenta crearlo.
            String remoteDirPath = "/home/clientessh";
            if (!client.changeWorkingDirectory(remoteDirPath)) {
                if (client.makeDirectory(remoteDirPath)) {
                    System.out.println("Directorio remoto creado con éxito.");
                    client.changeWorkingDirectory(remoteDirPath);
                } else {
                    System.out.println("Fallo al crear el directorio remoto.");
                    // Manejar el fallo adecuadamente
                }
            }

            String remoteFile = "prueba.txt";
            File uploadFile = new File("/home/clientessh/prueba.txt");
            try (FileInputStream fis = new FileInputStream(uploadFile)) {
                boolean result = client.storeFile(remoteFile, fis);
                System.out.println("Subida realizada con éxito: " + client.getReplyString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
