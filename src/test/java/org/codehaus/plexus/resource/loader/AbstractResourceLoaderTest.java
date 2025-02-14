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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.codehaus.plexus.util.IOUtil;

import java.io.InputStream;
import javax.inject.Inject;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public abstract class AbstractResourceLoaderTest
{
    @Inject
    protected ResourceLoader resourceLoader;

    protected void assertResource( String name, String expectedContent )
            throws Exception
    {

        InputStream is = resourceLoader.getResource( name ).getInputStream();

        assertNotNull( is, "The returned input stream is null, name: '" + name + "'." );

        String actualContent = IOUtil.toString( is, "UTF-8" );

        assertEquals( expectedContent, actualContent );
    }

    protected void assertMissingResource( String name )
            throws Exception
    {
        try
        {
            InputStream is = resourceLoader.getResource( name ).getInputStream();

            String content = IOUtil.toString( is, "UTF-8" );

            fail( "Expected ResourceNotFoundException while looking for a resource named '" + name + "'. Content:\n"
                    + content );
        }
        catch ( ResourceNotFoundException e )
        {
            // expected
        }
    }
}
