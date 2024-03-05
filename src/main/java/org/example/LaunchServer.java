import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTP;

public class ListaFicherosFTP {

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("ERROR: indicar como parámetros:");
            System.out.println("servidor [usuario] [contraseña]");
            System.exit(1);
        }

        String servidorFTP = args[0];
        String usuario = args.length >= 2 ? args[1] : "anonymous";
        String password = args.length >= 3 ? args[2] : "";

        FTPClient clienteFTP = new FTPClient();

        try {
            clienteFTP.connect(servidorFTP);
            int codResp = clienteFTP.getReplyCode();
            if (!FTPReply.isPositiveCompletion(codResp)) {
                System.out.printf("ERROR: Conexión rechazada con código de respuesta %d.\n", codResp);
                System.exit(2);
            }

            clienteFTP.enterLocalPassiveMode();
            clienteFTP.setFileType(FTP.BINARY_FILE_TYPE);

            boolean loginOK = clienteFTP.login(usuario, password);
            if (!loginOK) {
                System.out.printf("ERROR: Login con usuario %s rechazado.\n", usuario);
                return;
            }

            System.out.printf("INFO: Conexión establecida. Mensaje de bienvenida del servidor:\n====\n%s\n====\n", clienteFTP.getReplyString());
            System.out.printf("INFO: Directorio actual en servidor: %s. Contenidos:\n", clienteFTP.printWorkingDirectory());

            FTPFile[] fichServ = clienteFTP.listFiles();
            for (FTPFile f : fichServ) {
                String infoAdicFich = "";
                if (f.getType() == FTPFile.DIRECTORY_TYPE) {
                    infoAdicFich = "/";
                } else if (f.getType() == FTPFile.SYMBOLIC_LINK_TYPE) {
                    infoAdicFich = " -> " + f.getLink();
                }
                System.out.printf("%s%s\n", f.getName(), infoAdicFich);
            }
        } catch (IOException e) {
            System.out.println("ERROR: conectando al servidor");
            e.printStackTrace();
        } finally {
            if (clienteFTP.isConnected()) {
                try {
                    clienteFTP.disconnect();
                    System.out.println("INFO: conexión cerrada.");
                } catch (IOException e) {
                    System.out.println("AVISO: no se pudo cerrar la conexión.");
                }
            }
        }
    }
}
