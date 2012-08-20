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

import org.junit.Test;
import soot.options.Options;

import java.util.Random;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Tests for {@link net.nicoulaj.maven.plugins.soot.SootMojo}.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 0.1
 */
public class SootMojoTest
{

    private static final Random RANDOM = new Random();

    @Test
    public void testConfigureLogging()
    {
        final SootMojo mojo = new SootMojo();
        mojo.configureLogging();
        assertTrue( soot.G.v().out instanceof MavenLogPrintStream );
    }

    @Test
    public void testConfigureOptions()
    {
        final SootMojo mojo = new SootMojo();
        mojo.help = RANDOM.nextBoolean();
        mojo.phaseList = RANDOM.nextBoolean();
        mojo.phaseHelp = asList( valueOf( RANDOM.nextLong() ), valueOf( RANDOM.nextLong() ) );
        mojo.version = RANDOM.nextBoolean();
        mojo.verbose = RANDOM.nextBoolean();
        mojo.interactiveMode = RANDOM.nextBoolean();
        mojo.unfriendlyMode = RANDOM.nextBoolean();
        mojo.applicationMode = RANDOM.nextBoolean();
        mojo.wholeProgram = RANDOM.nextBoolean();
        mojo.wholeShimple = RANDOM.nextBoolean();
        mojo.validate = RANDOM.nextBoolean();
        mojo.debug = RANDOM.nextBoolean();
        mojo.debugResolver = RANDOM.nextBoolean();
        mojo.sootClasspath = valueOf( RANDOM.nextLong() );
        mojo.prependClasspath = RANDOM.nextBoolean();
        mojo.processDirectory = asList( valueOf( RANDOM.nextLong() ), valueOf( RANDOM.nextLong() ) );
        mojo.astMetrics = RANDOM.nextBoolean();
        mojo.sourcePrecedence = SootMojo.SourcePrecedence.JIMPLE;
        mojo.fullResolver = RANDOM.nextBoolean();
        mojo.allowPhantomRefs = RANDOM.nextBoolean();
        mojo.noBodiesForExcluded = RANDOM.nextBoolean();
        mojo.j2me = RANDOM.nextBoolean();
        mojo.mainClass = valueOf( RANDOM.nextLong() );
        mojo.polyglot = RANDOM.nextBoolean();
        mojo.outputDirectory = valueOf( RANDOM.nextLong() );
        mojo.outputFormat = SootMojo.OutputFormat.JASMIN;
        mojo.outputJar = RANDOM.nextBoolean();
        mojo.xmlAttributes = RANDOM.nextBoolean();
        mojo.printTags = RANDOM.nextBoolean();
        mojo.noOutputSourceFileAttribute = RANDOM.nextBoolean();
        mojo.noOutputInnerClassesAttribute = RANDOM.nextBoolean();
        mojo.dumpBody = asList( valueOf( RANDOM.nextLong() ), valueOf( RANDOM.nextLong() ) );
        mojo.dumpCfg = asList( valueOf( RANDOM.nextLong() ), valueOf( RANDOM.nextLong() ) );
        mojo.showExceptionDests = RANDOM.nextBoolean();
        mojo.gzip = RANDOM.nextBoolean();
        mojo.viaGrimp = RANDOM.nextBoolean();
        mojo.viaShimple = RANDOM.nextBoolean();
        mojo.throwAnalysis = SootMojo.ThrowAnalysis.PEDANTIC;
        mojo.omitExceptingUnitEdges = RANDOM.nextBoolean();
        mojo.includes = asList( valueOf( RANDOM.nextLong() ), valueOf( RANDOM.nextLong() ) );
        mojo.excludes = asList( valueOf( RANDOM.nextLong() ), valueOf( RANDOM.nextLong() ) );
        mojo.includeAll = RANDOM.nextBoolean();
        mojo.dynamicClasses = asList( valueOf( RANDOM.nextLong() ), valueOf( RANDOM.nextLong() ) );
        mojo.dynamicDirectories = asList( valueOf( RANDOM.nextLong() ), valueOf( RANDOM.nextLong() ) );
        mojo.dynamicPackages = asList( valueOf( RANDOM.nextLong() ), valueOf( RANDOM.nextLong() ) );
        mojo.keepLineNumber = RANDOM.nextBoolean();
        mojo.keepBytecodeOffset = RANDOM.nextBoolean();
        mojo.time = RANDOM.nextBoolean();
        mojo.subtractGC = RANDOM.nextBoolean();

        mojo.configureOptions();

        assertEquals( mojo.help, Options.v().help() );
        assertEquals( mojo.phaseList, Options.v().phase_list() );
        assertEquals( mojo.phaseHelp, Options.v().phase_help() );
        assertEquals( mojo.version, Options.v().version() );
        assertEquals( mojo.verbose, Options.v().verbose() );
        assertEquals( mojo.interactiveMode, Options.v().interactive_mode() );
        assertEquals( mojo.unfriendlyMode, Options.v().unfriendly_mode() );
        assertEquals( mojo.applicationMode, Options.v().app() );
        assertEquals( mojo.wholeProgram, Options.v().whole_program() );
        assertEquals( mojo.wholeShimple, Options.v().whole_shimple() );
        assertEquals( mojo.validate, Options.v().validate() );
        assertEquals( mojo.debug, Options.v().debug() );
        assertEquals( mojo.debugResolver, Options.v().debug_resolver() );
        assertEquals( mojo.sootClasspath, Options.v().soot_classpath() );
        assertEquals( mojo.prependClasspath, Options.v().prepend_classpath() );
        assertEquals( mojo.processDirectory, Options.v().process_dir() );
        assertEquals( mojo.astMetrics, Options.v().ast_metrics() );
        assertEquals( mojo.sourcePrecedence.getValue(), Options.v().src_prec() );
        assertEquals( mojo.fullResolver, Options.v().full_resolver() );
        assertEquals( mojo.allowPhantomRefs, Options.v().allow_phantom_refs() );
        assertEquals( mojo.noBodiesForExcluded, Options.v().no_bodies_for_excluded() );
        assertEquals( mojo.j2me, Options.v().j2me() );
        assertEquals( mojo.mainClass, Options.v().main_class() );
        assertEquals( mojo.polyglot, Options.v().polyglot() );
        assertEquals( mojo.outputDirectory, Options.v().output_dir() );
        assertEquals( mojo.outputFormat.getValue(), Options.v().output_format() );
        assertEquals( mojo.outputJar, Options.v().output_jar() );
        assertEquals( mojo.xmlAttributes, Options.v().xml_attributes() );
        assertEquals( mojo.printTags, Options.v().print_tags_in_output() );
        assertEquals( mojo.noOutputSourceFileAttribute, Options.v().no_output_source_file_attribute() );
        assertEquals( mojo.noOutputInnerClassesAttribute, Options.v().no_output_inner_classes_attribute() );
        assertEquals( mojo.dumpBody, Options.v().dump_body() );
        assertEquals( mojo.dumpCfg, Options.v().dump_cfg() );
        assertEquals( mojo.showExceptionDests, Options.v().show_exception_dests() );
        assertEquals( mojo.gzip, Options.v().gzip() );
        assertEquals( mojo.viaGrimp, Options.v().via_grimp() );
        assertEquals( mojo.viaShimple, Options.v().via_shimple() );
        assertEquals( mojo.throwAnalysis.getValue(), Options.v().throw_analysis() );
        assertEquals( mojo.omitExceptingUnitEdges, Options.v().omit_excepting_unit_edges() );
        assertEquals( mojo.includes, Options.v().include() );
        assertEquals( mojo.excludes, Options.v().exclude() );
        assertEquals( mojo.includeAll, Options.v().include_all() );
        assertEquals( mojo.dynamicClasses, Options.v().dynamic_class() );
        assertEquals( mojo.dynamicDirectories, Options.v().dynamic_dir() );
        assertEquals( mojo.dynamicPackages, Options.v().dynamic_package() );
        assertEquals( mojo.keepLineNumber, Options.v().keep_line_number() );
        assertEquals( mojo.keepBytecodeOffset, Options.v().keep_offset() );
        assertEquals( mojo.time, Options.v().time() );
        assertEquals( mojo.subtractGC, Options.v().subtract_gc() );
    }
}
