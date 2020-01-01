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

import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.StaticContentSupport;

/**
 * Plugin class that enables providing static HTTP content.
 */
public class StaticContentPlugin
{

    /**
     * Enable the static content plugin.
     * 
     * @param routing the routing builder to use to enable this plugin
     * @param root the root path to the static content
     * @return a routing builder with the static content enabled
     */
    public static Routing.Builder enable(Routing.Builder routing, Config root)
    {
        if ( root.hasValue() )
        {
            Path rootPath = Paths.get(root.asString().get());
            var staticContent = StaticContentSupport.builder(rootPath)
                                                    .welcomeFileName("index.html")
                                                    .build();
            return routing.register("/", staticContent);
        }

        return routing;
    }
    
}