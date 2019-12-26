package org.oddcyb.webbit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.gson.Gson;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.StaticContentSupport;
import io.helidon.webserver.WebServer;

/**
 * Start the webbit webserver.
 */
public class Start
{
    private static final Logger LOG = Logger.getLogger(Start.class.getName());

    /**
     * The path to the default webbit json config file.
     */
    public static final String DEFAULT_CONFIG_FILE = "/webbit/webbit.json";

    /**
     * Main method to start the webbit webserver.
     *
     * @throws Exception if something goes wrong
     */
    public static void main(String[] args) throws Exception
    {
        String configFile = (args.length == 0) ? 
                                DEFAULT_CONFIG_FILE :
                                args[0];
        BufferedReader configFileReader = 
            new BufferedReader(new FileReader(configFile));
        Map<String, Object> config = 
            new Gson().fromJson(configFileReader, Map.class);

        var host = config.getOrDefault("host", "0.0.0.0").toString();
        var port = config.getOrDefault("port", "80").toString();
        var root = config.getOrDefault("www-root", "/webbit/www").toString();

        var serverConfig = ServerConfiguration.builder()
                .bindAddress(InetAddress.getByName(host))
                .port(Integer.parseInt(port))
                .build();

        var staticContent = StaticContentSupport.builder(Paths.get(root))
                                                .welcomeFileName("index.html")
                                                .build();

        var routing = Routing.builder()
                .register("/", staticContent)
                .build();

        var webserver = WebServer.create(serverConfig, routing)
                .start()
                .toCompletableFuture()
                .get(10, TimeUnit.SECONDS);

        LOG.info("Webserver running on port "+webserver.port());
    }

}
