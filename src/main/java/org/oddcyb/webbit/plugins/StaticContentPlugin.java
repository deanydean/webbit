/*
 * Copyright 2020, Matt "Deany" Dean.
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
package org.oddcyb.webbit.plugins;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.StaticContentSupport;

/**
 * Plugin class that enables providing static HTTP content.
 */
public class StaticContentPlugin
{

    private static final Logger LOG = 
        Logger.getLogger(StaticContentPlugin.class.getName());

    /**
     * Enable the static content plugin.
     * 
     * @param routing the routing builder to use to enable this plugin
     * @param config the config for the plugin to use
     * @return a routing builder with the static content enabled
     */
    public static Routing.Builder enable(Routing.Builder routing, Config config)
    {
        var staticConfig = config.get("www.static");

        if ( staticConfig.exists() )
        {
            return staticConfig.asNodeList()
                               .get()
                               .stream()
                               .collect( () -> routing,
                                         (r, p) -> enablePath(r, p),
                                         (r1,r2) -> {} );
        }

        return routing;
    }

    /**
     * Enable static content for a path.
     * 
     * @param routing the router builder to enable the static content on.
     * @param pathConfig the config for the static content
     * @return a router builder with the static content enabled
     */
    private static Routing.Builder enablePath(Routing.Builder routing, 
                                              Config pathConfig)
    {
        String path = pathConfig.get("path").asString().get();
        String url = pathConfig.get("url").asString().get();

        LOG.info( () -> "Adding static path "+url+" -> "+path );
        var staticContent = StaticContentSupport.builder(Paths.get(path))
                                                .welcomeFileName("index.html")
                                                .build();
        return routing.register(url, staticContent);
    }
    
}