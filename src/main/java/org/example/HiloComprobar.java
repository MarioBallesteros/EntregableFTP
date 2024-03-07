package org.example;

import org.apache.ftpserver.usermanager.impl.BaseUser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.impl.WritePermission;

public class HiloComprobar extends Thread {

    private final Path rutaDirectorio;
    private final UserManager userManager;

    public HiloComprobar(String rutaDirectorio, UserManager userManager) {
        this.rutaDirectorio = Paths.get(rutaDirectorio);
        this.userManager = userManager;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            File folder = rutaDirectorio.toFile();
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            String userName = reader.readLine();
                            System.out.println("Nombre: "+userName);
                            String password = reader.readLine();
                            System.out.println("Contraseña: "+password);
                            if (userName != null && password != null) {
                                BaseUser newUser = new BaseUser();
                                newUser.setName(userName);
                                newUser.setPassword(password);
                                newUser.setHomeDirectory("/home/psp/anonimos/" + userName);
                                List<Authority> authorities = new ArrayList<>();
                                authorities.add(new WritePermission());
                                newUser.setAuthorities(authorities);

                                userManager.save(newUser);
                                guardarUsuarioEnFichero(userName);
                            }
                            // borrar archivo
                            Files.delete(file.toPath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            try {
                Thread.sleep(10000); // esperar 10 segundos
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }

    private void guardarUsuarioEnFichero(String nombreUsuario) {
        Path path = Paths.get("/home/psp/anonimos/TodosUsuarios.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(nombreUsuario);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}
