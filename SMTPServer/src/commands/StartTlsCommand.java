/*Class responsible for upgrading the connection to a secure encrypted connection.
 *It upgrades the socket in use to a secure socket.
 */

package commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SMTPUtils;
import utils.SmtpMessage;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.logging.Level;

public class StartTlsCommand implements SmtpCommand{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void execute(SessionContext sc, String line) throws IOException {

        SMTPUtils.sendMessage(sc.getSocket(), sc.getWriter(), SmtpMessage.READY_FOR_TLS.getFullText());

        try {

            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream("C:\\dev\\certs\\smtpserver.jks")) {
                keyStore.load(fis, "changeit".toCharArray());
            }

            // Initialize key manager with our key
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, "changeit".toCharArray());

            // Create SSLContext (use default system trust store for now)
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);

            // Create SSLSocket wrapping the existing socket
            SSLSocketFactory factory = sslContext.getSocketFactory();
            SSLSocket sslSocket = (SSLSocket) factory.createSocket(
                    sc.getSocket(),
                    sc.getSocket().getInetAddress().getHostAddress(),
                    sc.getSocket().getPort(),
                    true // auto-close the old socket when SSLSocket is closed
            );

            // Perform the TLS handshake
            sslSocket.startHandshake();

            // Upgrade the SessionContext connection
            sc.upgradeConnection(sslSocket);
            logger.trace("Connection successfully upgraded to TLS.");


        } catch (Exception e) {
            logger.warn("Failed to start TLS {}", e.getMessage());
            SMTPUtils.sendMessage(sc.getSocket(), sc.getWriter(), SmtpMessage.TLS_FAILURE.getFullText());
        }


    }
}
