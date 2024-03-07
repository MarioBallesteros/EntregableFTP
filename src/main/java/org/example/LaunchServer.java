package org.example;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LaunchServer {
    public static void main(String[] args) {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();

        // Configurar el puerto, por ejemplo 2221
        listenerFactory.setPort(2221);
        serverFactory.addListener("default", listenerFactory.createListener());

        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File("myusers.properties")); // Path to user properties
        userManagerFactory.setPasswordEncryptor(new PasswordEncryptor() {
            @Override
            public String encrypt(String password) {
                return password; // In a real scenario, use a secure encryptor
            }
            @Override
            public boolean matches(String passwordToCheck, String storedPassword) {
                return passwordToCheck.equals(storedPassword);
            }
        });

        BaseUser user = new BaseUser();
        user.setName("anonymous");
        user.setPassword("");
        user.setHomeDirectory("/home/psp/anonimos");

        List<Authority> authorities = new ArrayList<>();
        authorities.add(new WritePermission()); // Adding write permission
        user.setAuthorities(authorities);

        try {
            UserManager um = userManagerFactory.createUserManager();
            um.save(user); // Save the user
            serverFactory.setUserManager(um);
            FtpServer server = serverFactory.createServer();
            HiloComprobar hiloComprobar = new HiloComprobar("/home/psp/anonimos/nuevosUsuarios",um);
            hiloComprobar.start();
            server.start();
            System.out.println("FTP Server started on port 2221.");
        } catch (FtpException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }
}
