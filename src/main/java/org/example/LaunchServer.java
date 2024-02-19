package org.example;

import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;
import java.util.List;

public class LaunchServer {
    public static void main(String[] args) {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();

        // Configuración del rango de puertos para el modo pasivo
        DataConnectionConfigurationFactory dataConnectionConf = new DataConnectionConfigurationFactory();
        listenerFactory.setPort(2121);
        listenerFactory.setDataConnectionConfiguration(dataConnectionConf.createDataConnectionConfiguration());

        // Configurar usuario anónimo con acceso a una carpeta específica
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        BaseUser user = new BaseUser();
        user.setName("anonymous");
        user.setPassword("");
        user.setHomeDirectory("/home/clientessh");
        // Crear una lista de autoridades (permisos)
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new WritePermission());
        user.setAuthorities(authorities);

        try {
            serverFactory.getUserManager().save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        serverFactory.addListener("default", listenerFactory.createListener());
        FtpServer server = serverFactory.createServer();
        // Iniciar el hilo de monitoreo de archivo
        HiloComprobar fileWatcher = new HiloComprobar("/home/clientessh/prueba.txt");
        fileWatcher.start();

        try {
            server.start();
            System.out.println("Servidor FTP corriendo. Presione enter para detener...");
            System.out.println(System.in.read());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
            fileWatcher.interrupt();
            try {
                fileWatcher.join(); // Esperar a que el hilo termine
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
