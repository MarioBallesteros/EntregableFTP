package org.example;

import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;

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
        user.setHomeDirectory("/home/anonimo");

        try {
            serverFactory.getUserManager().save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        serverFactory.addListener("default", listenerFactory.createListener());
        FtpServer server = serverFactory.createServer();

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
