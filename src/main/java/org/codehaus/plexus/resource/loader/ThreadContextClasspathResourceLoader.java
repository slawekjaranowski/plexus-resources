package org.codehaus.plexus.resource.loader;

/*
 * The MIT License
 *
 * Copyright (c) 2004, The Codehaus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.codehaus.plexus.resource.PlexusResource;

import java.net.URL;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Named( ThreadContextClasspathResourceLoader.ID )
@Singleton
public class ThreadContextClasspathResourceLoader
        extends AbstractResourceLoader
{
    public static final String ID = "classloader";

    // ----------------------------------------------------------------------
    // ResourceLoader Implementation
    // ----------------------------------------------------------------------

    @Override
    public PlexusResource getResource( String name )
            throws ResourceNotFoundException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if ( classLoader == null )
        {
            throw new ResourceNotFoundException( name );
        }

        if ( name != null && name.startsWith( "/" ) )
        {
            name = name.substring( 1 );
        }

        final URL url = classLoader.getResource( name );
        if ( url == null )
        {
            throw new ResourceNotFoundException( name );
        }

        return new URLPlexusResource( url );
    }
}
