/*
 * Copyright 2012 Julien Nicoulaud <julien.nicoulaud@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.nicoulaj.maven.plugins.soot;

import org.apache.maven.plugin.logging.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * A {@link PrintStream} that redirects all output to a Maven {@link Log}.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 0.1
 */
public final class MavenLogPrintStream
    extends PrintStream
{

    /**
     * System-dependent line separator character.
     */
    private static final String LINE_SEPARATOR = System.getProperty( "line.separator" );

    /**
     * Constructor.
     *
     * @param log the {@link Log} to which all output should be redirected.
     */
    public MavenLogPrintStream( final Log log )
    {
        super( new ByteArrayOutputStream()
        {
            @Override
            public void flush()
                throws IOException
            {
                synchronized ( this )
                {
                    super.flush();
                    final String buffer = toString();
                    if ( buffer.length() > 0 && !LINE_SEPARATOR.equals( buffer ) )
                    {
                        log.info( buffer );
                    }
                    super.reset();
                }
            }
        }, true );
    }
}
