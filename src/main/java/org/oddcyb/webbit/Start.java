package org.oddcyb.webbit;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.StaticContentSupport;
import io.helidon.webserver.WebServer;

import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Start the webbit webserver.
 */
public class Start
{
    private static final Logger LOG = Logger.getLogger(Start.class.getName());

    /**
     * Main method to start the webbit webserver.
     *
     * @param args the params for the webserver. Expected to be an array of
*                  [ listenHost, listenPort, contentPath ]
     * @throws Exception if something goes wrong or if the params are invalid
     */
    public static void main(String[] args) throws Exception
    {
        var host = args[0];
        var port = Integer.parseInt(args[1]);
        var content = Paths.get(args[2]);

        var serverConfig = ServerConfiguration.builder()
                .bindAddress(InetAddress.getByName(host))
                .port(port)
                .build();

        var routing = Routing.builder()
                .register("/",
                    StaticContentSupport.builder(content)
                        .welcomeFileName("index.html")
                        .build())
                .build();

        var webserver = WebServer.create(serverConfig, routing)
                .start()
                .toCompletableFuture()
                .get(10, TimeUnit.SECONDS);

        LOG.info("Webserver running on port "+webserver.port());
    }

}
