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
package org.oddcyb.webbit.util;

import org.apache.commons.text.StringSubstitutor;

import io.helidon.webserver.ServerRequest;

/**
 * Utilities for handling URLs.
 */
public class URLs
{

    /**
     * Substitute variables into a URL template String.
     * 
     * @param urlTemplate the url String to do the substitutions on
     * @param req the request to get parameters from
     * @param config the config to get parameters from
     * @return
     */
    public static final String substitute(String urlTemplate, 
                                          ServerRequest req)
    {
        var strSub = 
            new StringSubstitutor( (s) -> findSubstitution(s, req) );

        return strSub.replace(urlTemplate);
    }

    public static final String findSubstitution(String f, 
                                    ServerRequest req)
    {
        switch(f.toLowerCase())
        {
            case "host": 
                return req.headers()
                          .value("Host")
                          .orElse("")
                          .split(":")[0];

            case "port":
                var urlBits = req.headers()
                                 .value("Host")
                                 .orElse("")
                                 .split(":");
                return ( urlBits.length > 1 ) ? 
                            urlBits[1] : 
                            Integer.toString(req.localPort());

            case "uri": 
                return req.uri().toString();

            case "path":
                return req.path().toString();

            case "query":
                return req.query();

            case "fragment": 
                return req.fragment();

            default: 
                return null;
        }
    }
}