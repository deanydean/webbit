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

import java.util.logging.Logger;

import org.oddcyb.webbit.util.URLs;

import io.helidon.common.http.Http.ResponseStatus;
import io.helidon.config.Config;
import io.helidon.webserver.Handler;
import io.helidon.webserver.Routing;

/**
 * A plugin class that enables HTTP Url redirects.
 */
public class RedirectPlugin
{
    private static final Logger LOG = 
        Logger.getLogger(RedirectPlugin.class.getName());

    /**
     * Enable the redirect plugin.
     * 
     * @param routing the routing builder to use to enable this plugin
     * @param config the config for the plugin
     * @return a routing builder with the redirects enabled
     */
    public static Routing.Builder enable(Routing.Builder routing, Config config)
    {
        var redirectConfig = config.get("www.redirects");

        if ( redirectConfig.exists() )
        {
            return redirectConfig.asNodeList()
                                 .get()
                                 .stream()
                                 .collect( () -> routing,
                                           (r, p) -> enableRedirect(r, p),
                                           (r1,r2) -> {} );
        }

        return routing;
    }

    /**
     * Enable a single redirect.
     * 
     * @param routing the router builder to use to enable the redirect
     * @param redirectConfig the config for the redirect
     * @return a router builder with the redirect enabled
     */
    public static Routing.Builder enableRedirect(Routing.Builder routing, 
                                                  Config redirectConfig)
    {
        String path = redirectConfig.get("path").asString().orElse(null);
        String to = redirectConfig.get("to").asString().get();

        var handler = createHandler(to);

        if ( redirectConfig.get("all").asBoolean().orElse(false) )
        {
            LOG.info( () -> "Adding redirect for all -> "+to );
            return routing.any( handler );
        }
        else
        {
            LOG.info( () -> "Adding redirect "+path+" -> "+to );
            return routing.get(path, handler );
        }
    }
    
    /**
     * Create a Handler that redirects to the provided URL.
     * 
     * @param to the URL to redirect to
     * @return a Handler that handles the redirect
     */
    public static Handler createHandler(String to)
    {
        return (req, res) -> {
            String redirectUrl = URLs.substitute(to, req);
            res.headers().add("Location", redirectUrl);
            res.status(ResponseStatus.create(301));
            res.send();
        };
    }
    
}