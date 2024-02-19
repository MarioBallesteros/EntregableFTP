package org.example;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

public class LaunchClient {
    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        try {
            client.connect("192.168.1.45");
            client.login("anonymous", "password");

            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);

            // Subir archivo
            String remoteFile = "/home/clientessh/prueba.txt";
            File uploadFile = new File(remoteFile);
            try (FileInputStream fis = new FileInputStream(uploadFile)) {
                boolean result = client.storeFile(remoteFile, fis);
                System.out.println("Subida realizada con éxito: " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            client.logout();
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
