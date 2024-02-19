package org.example;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

public class LaunchClient {
    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        try {
            // Especifica el puerto 2121 en la llamada connect
            client.connect("192.168.1.45", 2121);
            client.login("anonymous", "password");

            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);

            // Asegúrate de que el archivo que intentas subir existe y la ruta es correcta
            String localFilePath = "/path/to/local/file.txt"; // Ruta local del archivo a subir
            String remoteFile = "/home/anonimo"; // Ruta remota donde se subirá el archivo

            File uploadFile = new File(localFilePath);
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
