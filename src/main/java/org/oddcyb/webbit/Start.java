/*
 * Copyright 2019, 2020, Matt "Deany" Dean.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.oddcyb.webbit;

import java.util.logging.Logger;

import org.oddcyb.webbit.plugins.RedirectPlugin;
import org.oddcyb.webbit.plugins.StaticContentPlugin;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.config.yaml.YamlConfigParserBuilder;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
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
    public static final String DEFAULT_CONFIG_FILE = "/webbit/webbit.yaml";

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

        var config = Config.create(
            ConfigSources.file(configFile)
                         .parser(YamlConfigParserBuilder.buildDefault())
                         .build()
        );

        var routing = Routing.builder();
        routing = StaticContentPlugin.enable(routing, config);
        routing = RedirectPlugin.enable(routing, config);
        
        var serverConfig = ServerConfiguration.builder(config.get("server"))
                                              .build();

        WebServer.builder(routing)
                 .config(serverConfig)
                 .build()
                 .start()
                 .toCompletableFuture()
                 .thenRun( () ->
                       LOG.info("Webbit running....") );
    }
}
