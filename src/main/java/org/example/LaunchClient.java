package org.example;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

public class LaunchClient {
    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        try {
            client.connect("10.0.2.15", 2121);
            client.login("anonymous","anonymous");

            String remoteDirPath = "/home/psp";
            if (!client.changeWorkingDirectory(remoteDirPath)) {
                if (client.makeDirectory(remoteDirPath)) {
                    System.out.println("Directorio remoto creado con éxito.");
                    client.changeWorkingDirectory(remoteDirPath);
                } else {
                    System.out.println("Fallo al crear el directorio remoto.");
                }
            }

            String localFilePath = "/home/mballesterosv/";
            Set<PosixFilePermission> permisos = Set.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE);
            Files.setPosixFilePermissions(Paths.get(localFilePath), permisos);

            String remoteFile = "prueba.txt";
            File uploadFile = new File(localFilePath);
            try (FileInputStream fis = new FileInputStream(uploadFile)) {
                boolean result = client.storeFile(remoteFile, fis);
                System.out.println("Subida realizada con éxito: " + client.getReplyString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.logout();
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
