package org.example;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

public class LaunchClient {
    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        try {
            client.connect("direccion_del_servidor");
            client.login("anonymous", "password");

            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);

            // Subir archivo
            String remoteFile = "/path/to/destination/file.txt";
            File uploadFile = new File("/path/to/source/file.txt");
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
