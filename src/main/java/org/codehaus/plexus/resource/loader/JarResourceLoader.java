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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Named;

/**
 * @author Jason van Zyl
 */
@Named( JarResourceLoader.ID )
public class JarResourceLoader
        extends AbstractResourceLoader
{
    private static final Logger LOGGER = LoggerFactory.getLogger( JarResourceLoader.class );

    public static final String ID = "jar";

    /**
     * Maps entries to the parent JAR File (key = the entry *excluding* plain directories, value = the JAR URL).
     */
    private final Map<String, String> entryDirectory = new LinkedHashMap<>( 559 );

    /**
     * Maps JAR URLs to the actual JAR (key = the JAR URL, value = the JAR).
     */
    private final Map<String, JarHolder> jarFiles = new LinkedHashMap<>( 89 );

    private void loadJar( String path )
    {
        LOGGER.debug( "JarResourceLoader : trying to load '{}'", path );

        // Check path information
        if ( path == null )
        {
            LOGGER.error( "JarResourceLoader : can not load JAR - JAR path is null" );
            return;
        }
        if ( !path.startsWith( "jar:" ) )
        {
            LOGGER.error( "JarResourceLoader : JAR path must start with jar: -> "
                    + "see java.net.JarURLConnection for information" );
            return;
        }
        if ( !path.endsWith( "!/" ) )
        {
            path += "!/";
        }

        // Close the jar if it's already open this is useful for a reload
        closeJar( path );

        // Create a new JarHolder
        JarHolder temp = new JarHolder( path );

        // Add it's entries to the entryCollection
        addEntries( temp.getEntries() );

        // Add it to the Jar table
        jarFiles.put( temp.getUrlPath(), temp );
    }

    /**
     * Closes a Jar file and set its URLConnection to null.
     */
    private void closeJar( String path )
    {
        if ( jarFiles.containsKey( path ) )
        {
            JarHolder theJar = jarFiles.get( path );

            theJar.close();
        }
    }

    /**
     * Copy all the entries into the entryDirectory. It will overwrite any duplicate keys.
     */
    private void addEntries( Map<String, String> entries )
    {
        entryDirectory.putAll( entries );
    }

    /**
     * Get an {@link PlexusResource} by name.
     *
     * @param source name of resource to get
     * @return PlexusResource containing the resource
     * @throws ResourceNotFoundException if resource not found.
     */
    @Override
    public PlexusResource getResource( String source )
            throws ResourceNotFoundException
    {
        if ( source == null || source.length() == 0 )
        {
            throw new ResourceNotFoundException( "Need to have a resource!" );
        }

        /*
         * if a / leads off, then just nip that :)
         */
        if ( source.startsWith( "/" ) )
        {
            source = source.substring( 1 );
        }

        if ( entryDirectory.containsKey( source ) )
        {
            String jarurl = entryDirectory.get( source );

            final JarHolder holder = jarFiles.get( jarurl );
            if ( holder != null )
            {
                return holder.getPlexusResource( source );
            }
        }

        throw new ResourceNotFoundException( "JarResourceLoader Error: cannot find resource " + source );
    }

    @Override
    public void addSearchPath( String path )
    {
        if ( !paths.contains( path ) )
        {
            loadJar( path );
            paths.add( path );
        }
    }
}
