package org.example;

import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.apache.ftpserver.ftplet.Authority;
import java.util.Arrays;

public class LaunchServer {
    public static void main(String[] args) {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();

        // Configurar el puerto, por ejemplo 2221
        listenerFactory.setPort(2221);

        // Configurar el modo pasivo
        DataConnectionConfigurationFactory dataConnectionConf = new DataConnectionConfigurationFactory();
        dataConnectionConf.setPassivePorts("10000-11000"); // Rango de puertos para el modo pasivo
        listenerFactory.setDataConnectionConfiguration(dataConnectionConf.createDataConnectionConfiguration());

        serverFactory.addListener("default", listenerFactory.createListener());

        // Configuración para permitir el acceso anónimo
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        BaseUser user = new BaseUser();
        user.setName("anonymous");
        user.setPassword("password"); // Por simplicidad, pero en un entorno real, se debería manejar de forma segura.
        user.setHomeDirectory("/path/to/anonymous/folder"); // Ruta al directorio para el usuario anónimo

        Authority writePermission = new WritePermission();
        user.setAuthorities(Arrays.asList(writePermission));

        try {
            userManagerFactory.createUserManager().save(user);
        } catch (Exception e) {
            System.err.println("Error al crear el usuario: " + e.getMessage());
        }

        serverFactory.setUserManager(userManagerFactory.createUserManager());

        // Iniciar el servidor
        FtpServer server = serverFactory.createServer();
        try {
            server.start();
            System.out.println("Servidor FTP iniciado correctamente en el puerto 2221.");
        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor FTP: " + e.getMessage());
        }
    }
}
