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

import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.resource.PlexusResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public abstract class AbstractResourceLoader
    extends AbstractLogEnabled
    implements ResourceLoader
{
    protected List<String> paths = new ArrayList<String>();

    public void addSearchPath( String path )
    {
        if ( !paths.contains( path ) )
        {
            paths.add( path );
        }
    }

    public InputStream getResourceAsInputStream( String name )
        throws ResourceNotFoundException
    {
        PlexusResource resource = getResource( name );
        try
        {
            return resource.getInputStream();
        }
        catch ( IOException e )
        {
            throw new ResourceIOException( "Failed to open resource " + resource.getName() + ": " + e.getMessage(), e );
        }
    }
}
