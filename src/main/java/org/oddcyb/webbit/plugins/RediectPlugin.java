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

import io.helidon.config.Config;
import io.helidon.webserver.Routing;

/**
 * A plugin class that enables HTTP Url redirects.
 */
public class RediectPlugin
{

    /**
     * Enable the redirect plugin.
     * 
     * @param routing the routing builder to use to enable this plugin
     * @param redirectUrl the URL to redirect to
     * @return a routing builder with the redirects enabled
     */
    public static Routing.Builder enable(Routing.Builder routing, Config redirectUrl)
    {
        if ( redirectUrl.hasValue() )
        {
            String redirectURL = redirectUrl.asString().get();
            return routing.any( (req, res) -> {
                // TODO - Implement the redirects
                res.send("Redirect is to "+redirectURL);
            });
        }

        return routing;
    }
    
}