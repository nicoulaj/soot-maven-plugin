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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import soot.Main;
import soot.options.Options;

import java.util.List;

/**
 * Mojo that invokes <a href="http://www.sable.mcgill.ca/soot">Soot</a>.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 0.1
 */
@Mojo(
    name = SootMojo.NAME,
    defaultPhase = LifecyclePhase.COMPILE,
    threadSafe = false )
public final class SootMojo
    extends AbstractMojo
{

    /**
     * Mojo name.
     */
    public static final String NAME = "soot";

    /**
     * The Maven project.
     *
     * @since 0.1
     */
    @Parameter( property = "project", required = true, readonly = true )
    protected MavenProject project;

    /**
     * Display the textual help message and exit immediately without further processing.
     */
    @Parameter( defaultValue = "false" )
    protected boolean help;

    /**
     * Print a list of the available phases and sub-phases, then exit.
     */
    @Parameter( defaultValue = "false" )
    protected boolean phaseList;

    /**
     * Print a help message about the phase or sub-phase named phase, then exit. To see the help message of more than
     * one phase, specify multiple phase-help options.
     */
    @Parameter
    protected List phaseHelp;

    /**
     * Display information about the version of Soot being run, then exit without further processing.
     */
    @Parameter( defaultValue = "false" )
    protected boolean version;

    /**
     * Provide detailed information about what Soot is doing as it runs.
     */
    @Parameter( defaultValue = "false" )
    protected boolean verbose;

    /**
     * Runs interactively, with Soot providing detailed information as it iterates through intra-procedural analyses.
     */
    @Parameter( defaultValue = "false" )
    protected boolean interactiveMode;

    /**
     * With this option, Soot does not stop even if it received no command-line options. Useful when setting Soot options
     * programmatically and then calling soot.Main.main() with an empty list.
     */
    @Parameter( defaultValue = "true" )
    protected boolean unfriendlyMode;

    /**
     * Run in application mode, processing all classes referenced by argument classes.
     */
    @Parameter( defaultValue = "false" )
    protected boolean applicationMode;

    /**
     * Run in whole program mode, taking into consideration the whole program when performing analyses and transformations.
     * Soot uses the Call Graph Constructor to build a call graph for the program, then applies enabled transformations in
     * the Whole-Jimple Transformation, Whole-Jimple Optimization, and Whole-Jimple Annotation packs before applying
     * enabled intraprocedural transformations.
     * Note that the Whole-Jimple Optimization pack is normally disabled (and thus not applied by whole program mode),
     * unless you also specify the Whole Program Optimize option.
     */
    @Parameter( defaultValue = "false" )
    protected boolean wholeProgram;

    /**
     * Run in whole shimple mode, taking into consideration the whole program when performing Shimple analyses and
     * transformations. Soot uses the Call Graph Constructor to build a call graph for the program, then applies enabled
     * transformations in the Whole-Shimple Transformation and Whole-Shimple Optimization before applying enabled
     * intraprocedural transformations.
     * Note that the Whole-Shimple Optimization pack is normally disabled (and thus not applied by whole shimple mode),
     * unless you also specify the Whole Program Optimize option.
     */
    @Parameter( defaultValue = "false" )
    protected boolean wholeShimple;

    /**
     * Causes internal checks to be done on bodies in the various Soot IRs,to make sure the transformations have not done
     * something strange. This option may degrade Soot's performance.
     */
    @Parameter( defaultValue = "false" )
    protected boolean validate;

    /**
     * Print various debugging information as Soot runs, particularly from the Baf Body Phase and the Jimple Annotation
     * Pack Phase.
     */
    @Parameter( defaultValue = "false" )
    protected boolean debug;

    /**
     * Print debugging information about class resolving.
     */
    @Parameter( defaultValue = "false" )
    protected boolean debugResolver;

    /**
     * Use path as the list of directories in which Soot should search for classes. path should be a series of directories,
     * separated by the path separator character for your system.
     * If no classpath is set on the command line, but the system property <tt>soot.class.path</tt> has been set, Soot
     * uses its value as the classpath.
     * If neither the command line nor the system properties specify a Soot classpath, Soot falls back on a default
     * classpath consisting of the value of the system property <tt>java.class.path</tt> followed by
     * <var>java.home</var><tt>/lib/rt.jar</tt>, where <var>java.home</var> stands for the contents of the system property
     * <tt>java.home</tt> and <tt>/</tt> stands for the system file separator.
     */
    @Parameter( property = "project.build.outputDirectory" )
    protected String sootClasspath;

    /**
     * Instead of replacing the default soot classpath with the classpath given on the command line, prepent it with that
     * classpath. The default classpath holds whatever is set in the CLASSPATH environment variable, followed by rt.jar
     * (resolved through the JAVA-UNDERSCORE-HOME environment variable). If whole-program mode is enabled, jce.jar is
     * also appended in the end.
     */
    @Parameter( defaultValue = "true" )
    protected boolean prependClasspath;

    /**
     * Add all classes found in directory to the set of argument classes which is analyzed and transformed by Soot. You
     * can specify the option more than once, to add argument classes from multiple directories. You can also state JAR
     * files.
     * If subdirectories of directory contain <tt>.class</tt> or <tt>.jimple</tt> files, Soot assumes that the subdirectory
     * names correspond to components of the classes' package names. If directory contains<tt>subA/subB/MyClass.class</tt>,
     * for instance, then Soot assumes <tt>MyClass</tt> is in package <tt>subA.subB</tt>.
     */
    @Parameter( property = "project.build.outputDirectory" )
    protected List processDirectory;

    /**
     * If this flag is set and soot converts java to jimple then AST metrics will be computed.
     */
    @Parameter( defaultValue = "TODO" )
    protected boolean astMetrics;

    /**
     * Values for {@link #sourcePrecedence} option.
     */
    @SuppressWarnings( "unused" )
    public enum SourcePrecedence
    {

        /**
         * Favour class files as Soot source.
         * <p/>
         * Try to resolve classes first from <tt>.class</tt> files found in the Soot classpath. Fall back to
         * <tt>.jimple</tt> files only when unable to find a <tt>.class</tt> file.
         */
        CLASS( Options.src_prec_class ),

        /**
         * Use only class files as Soot source.
         * <p/>
         * Try to resolve classes first from <tt>.class</tt> files found in the Soot classpath. Do not try any other
         * types of files even when unable to find a <tt>.class</tt> file.
         */
        ONLY_CLASS( Options.src_prec_only_class ),

        /**
         * Favour Jimple files as Soot source.
         * <p/>
         * Try to resolve classes first from <tt>.jimple</tt> files found in the Soot classpath. Fall back to
         * <tt>.class</tt> files only when unable to find a <tt>.jimple</tt> file.
         */
        JIMPLE( Options.src_prec_jimple ),

        /**
         * Favour Java files as Soot source.
         * <p/>
         * Try to resolve classes first from <tt>.java</tt> files found in the Soot classpath. Fall back to
         * <tt>.class</tt> files only when unable to find a <tt>.java</tt> file.
         */
        JAVA( Options.src_prec_java );

        protected int value;

        private SourcePrecedence( int value )
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * Sets format as Soot's preference for the type of source files to read when it looks for a class.
     */
    @Parameter( defaultValue = "CLASS" )
    protected SourcePrecedence sourcePrecedence;

    /**
     * Normally, Soot resolves only that application classes and any classes that they refer to, along with any classes
     * it needs for the Jimple typing, but it does not transitively resolve references in these additional classes that
     * were resolved only because they were referenced. This switch forces full transitive resolution of all references
     * found in all classes that are resolved, regardless of why they were resolved.
     * In whole-program mode, class resolution is always fully transitive. Therefore, in whole-program mode, this switch
     * has no effect, and class resolution is always performed as if it were turned on.
     */
    @Parameter( defaultValue = "false" )
    protected boolean fullResolver;

    /**
     * Allow Soot to process a class even if it cannot find all classes referenced by that class. This may cause Soot to
     * produce incorrect results.
     */
    @Parameter( defaultValue = "false" )
    protected boolean allowPhantomRefs;

    /**
     * Prevents Soot from loading method bodies for all excluded classes (see exclude option), even when running in
     * whole-program mode. This is useful for computing a shallow points-to analysis that does not, for instance, take
     * into account the JDK. Of course, such analyses may be unsound. You get what you are asking for.
     */
    @Parameter( defaultValue = "false" )
    protected boolean noBodiesForExcluded;

    /**
     * Use J2ME mode. J2ME does not have class Cloneable nor Serializable, so we have to change type assignment to not
     * refer to those classes.
     */
    @Parameter( defaultValue = "false" )
    protected boolean j2me;

    /**
     * By default, the first class encountered with a main method is treated as the main class (entry point) in whole-
     * program analysis. This option overrides this default.
     */
    @Parameter
    protected String mainClass;

    /**
     * Use Java 1.4 Polyglot frontend instead of JastAdd, which supports Java 5 syntax.
     */
    @Parameter( defaultValue = "false" )
    protected boolean polyglot;

    /**
     * Store output files in directory. directory may be relative to the working directory.
     */
    @Parameter( property = "project.build.outputDirectory" )
    protected String outputDirectory;

    /**
     * Values for {@link #outputFormat} option.
     */
    @SuppressWarnings( "unused" )
    public enum OutputFormat
    {

        /**
         * Produce <tt>.jimple</tt> files.
         * <p/>
         * Produce <tt>.jimple</tt> files, which contain a textual form of Soot's Jimple internal representation.
         */
        JIMPLE( Options.output_format_jimple ),

        /**
         * Produce <tt>.jimp</tt> (abbreviated Jimple) files.
         * <p/>
         * Produce <tt>.jimp</tt> files, which contain an abbreviated form of Jimple.
         */
        JIMP( Options.output_format_jimp ),

        /**
         * Produce <tt>.shimple</tt> files.
         * <p/>
         * Produce<tt>.shimple files</tt>, containing a textual form of Soot's SSA Shimple internal representation.
         * Shimple adds Phi nodes to Jimple.
         */
        SHIMPLE( Options.output_format_shimple ),

        /**
         * Produce <tt>.shimp</tt> (abbreviated Shimple) files.
         * <p/>
         * Produce .shimp files, which contain an abbreviated form of Shimple.
         */
        SHIMP( Options.output_format_shimp ),

        /**
         * Produce <tt>.baf</tt> files.
         * <p/>
         * Produce <tt>.baf</tt> files, which contain a textual form of Soot's Baf internal representation.
         */
        BAF( Options.output_format_baf ),

        /**
         * Produce <tt>.b</tt> (abbreviated Baf) files.
         * <p/>
         * Produce <tt>.b</tt> files, which contain an abbreviated form of Baf.
         */
        B( Options.output_format_b ),

        /**
         * Produce <tt>.grimple</tt> files.
         * <p/>
         * Produce <tt>.grimple</tt> files, which contain a textual form of Soot's Grimp internal representation.
         */
        GRIMPLE( Options.output_format_grimple ),

        /**
         * Produce <tt>.grimp</tt> (abbreviated Grimp) files.
         * <p/>
         * Produce <tt>.grimp</tt> files, which contain an abbreviated form of Grimp.
         */
        GRIMP( Options.output_format_grimp ),

        /**
         * Produce <tt>.xml</tt> files.
         * <p/>
         * Produce <tt>.xml</tt> files containing an annotated version of the Soot's Jimple internal representation.
         */
        XML( Options.output_format_xml ),

        /**
         * Produce no output.
         * <p/>
         * Produce no output files.
         */
        NONE( Options.output_format_none ),

        /**
         * Produce <tt>.jasmin</tt> files.
         * <p/>
         * Produce <tt>.jasmin</tt> files, suitable as input to the jasmin bytecode assembler.
         */
        JASMIN( Options.output_format_jasmin ),

        /**
         * Produce <tt>.class</tt> files.
         * <p/>
         * Produce Java <tt>.class</tt> files, executable by any Java Virtual Machine.
         */
        CLASS( Options.output_format_class ),

        /**
         * Produce dava-decompiled <tt>.java</tt> files.
         * <p/>
         * Produce <tt>.java</tt> files generated by the Dava decompiler.
         */
        DAVA( Options.output_format_dava ),

        /**
         * Produce <tt>.java</tt> files with Jimple templates.
         * <p/>
         * Produce <tt>.java</tt> files with Jimple templates.
         */
        TEMPLATE( Options.output_format_template );

        protected int value;

        private OutputFormat( int value )
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * Specify the format of output files Soot should produce, if any.
     * Note that while the abbreviated formats (<tt>jimp</tt>, <tt>shimp</tt>,<tt>b</tt>, and<tt>grimp</tt>) are easier
     * to read than their unabbreviated counterparts (<tt>jimple</tt>, <tt>shimple</tt>,<tt>baf</tt>, and
     * <tt>grimple</tt>), they may contain ambiguities. Method signatures in the abbreviated formats, for instance, are
     * not uniquely determined.
     */
    @Parameter( defaultValue = "CLASS" )
    protected OutputFormat outputFormat;

    /**
     * Make output dir a Jar file instead of dir.
     * <p/>
     * The output Jar file name should be specified using the {@link #outputDirectory} option. Note that if the output
     * Jar file exists before Soot runs, any files inside it will first be removed.
     */
    @Parameter( defaultValue = "false" )
    protected boolean outputJar;

    /**
     * Save in XML format a variety of tags which Soot has attached to its internal representations of the application
     * classes. The XML file can then be read by the Soot plug-in for the Eclipse IDE, which can display the annotations
     * together with the program source, to aid program understanding.
     */
    @Parameter( defaultValue = "false" )
    protected boolean xmlAttributes;

    /**
     * Print in output files (either in Jimple or Dave) a variety of tags which Soot has attached to its internal
     * representations of the application classes. The tags will be printed on the line succeeding the stmt that they
     * are attached to.
     */
    @Parameter( defaultValue = "false" )
    protected boolean printTags;

    /**
     * Don't output Source File Attribute when producing class files.
     */
    @Parameter( defaultValue = "false" )
    protected boolean noOutputSourceFileAttribute;

    /**
     * Don't output inner classes attribute in class files.
     * <p/>
     * Don't output inner classes attribute in class files.
     */
    @Parameter( defaultValue = "false" )
    protected boolean noOutputInnerClassesAttribute;

    /**
     * Dump the internal representation of each method before and after given phase.
     */
    @Parameter
    protected List dumpBody;

    /**
     * Dump the internal representation of each CFG constructed during given phase.
     */
    @Parameter
    protected List dumpCfg;

    /**
     * Include exception destination edges as well as CFG edges in dumped CFGs.
     * <p/>
     * Indicate whether to show exception destination edges as well as control flow edges in dumps of exceptional control
     * flow graphs.
     */
    @Parameter( defaultValue = "true" )
    protected boolean showExceptionDests;

    /**
     * GZip IR output files.
     * <p/>
     * This option causes Soot to compress output files of intermediate representations with GZip. It does not apply to
     * class files output by Soot.
     */
    @Parameter( defaultValue = "false" )
    protected boolean gzip;

//    /**
//     * Perform intraprocedural optimizations on the application classes.
//     */
//    @Parameter(defaultValue = "true")
//    protected boolean optimize;
//
//    /**
//     * Perform whole program optimizations.
//     * <p/>
//     * Perform whole program optimizations on the application classes. This enables the Whole-Jimple Optimization pack as
//     * well as whole program mode and intraprocedural optimizations.
//     */
//    @Parameter(defaultValue = "false")
//    protected boolean wholeOptimize;

    /**
     * Convert Jimple to bytecode via the Grimp intermediate representation instead of via the Baf intermediate
     * representation.
     */
    @Parameter( defaultValue = "false" )
    protected boolean viaGrimp;

    /**
     * Enable Shimple, Soot's SSA representation. This generates Shimple bodies for the application classes, optionally
     * transforms them with analyses that run on SSA form, then turns them back into Jimple for processing by the rest of
     * Soot. For more information, see the documentation for the<tt>shimp</tt>,<tt>stp</tt>, and <tt>sop</tt> phases.
     */
    @Parameter( defaultValue = "false" )
    protected boolean viaShimple;

    /**
     * Values for {@link #throwAnalysis} option.
     */
    @SuppressWarnings( "unused" )
    public enum ThrowAnalysis
    {

        /**
         * Pedantically conservative throw analysis.
         * <p/>
         * Says that any instruction may throw any <code>Throwable</code> whatsoever. Strictly speaking this is correct,
         * since the Java libraries include the <code>Thread.stop(Throwable)</code> method, which allows other threads
         * to cause arbitrary exceptions to occur at arbitrary points in the execution of a victim thread.
         */
        PEDANTIC( Options.throw_analysis_pedantic ),

        /**
         * Unit Throw Analysis.
         * <p/>
         * Says that each statement in the intermediate representation may throw those exception types associated with
         * the corresponding Java bytecode instructions in the JVM Specification. The analysis deals with each statement
         * in isolation, without regard to the surrounding program.
         */
        UNIT( Options.throw_analysis_unit );

        protected int value;

        private ThrowAnalysis( int value )
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * This option specifies how to estimate the exceptions which each statement may throw when constructing
     * exceptional CFGs.
     */
    @Parameter( defaultValue = "UNIT" )
    protected ThrowAnalysis throwAnalysis;

    /**
     * Omit CFG edges to handlers from excepting units which lack side effects.
     * <p/>
     * When constructing an <code>ExceptionalUnitGraph</code> or <code>ExceptionalBlockGraph</code>, include edges to an
     * exception handler only from the predecessors of an instruction which may throw an exception to the handler, and
     * not from the excepting instruction itself, unless the excepting instruction has potential side effects.
     * Omitting edges from excepting units allows more accurate flow analyses (since if an instruction without side
     * effects throws an exception, it has not changed the state of the computation). This accuracy, though, could lead
     * optimizations to generate unverifiable code, since the dataflow analyses performed by bytecode verifiers might
     * include paths to exception handlers from all protected instructions, regardless of whether the instructions have
     * side effects. (In practice, the pedantic throw analysis suffices to pass verification in all VMs tested with Soot
     * to date, but the JVM specification does allow for less discriminating verifiers which would reject some code that
     * might be generated using the pedantic throw analysis without also adding edges from all excepting units.)
     */
    @Parameter( defaultValue = "false" )
    protected boolean omitExceptingUnitEdges;

//    /**
//     * Trim unrealizable exceptional edges from CFGs.
//     * <p/>
//     * When constructing CFGs which include exceptional edges, minimize the number of edges leading to exception handlers
//     * by analyzing which instructions might actually be executed before an exception is thrown, instead of assuming that
//     * every instruction protected by a handler has the potential to throw an exception the handler catches.
//     */
//    @Parameter(defaultValue = "false")
//    protected boolean trimCfgs;

//    /**
//     * Does not throw an exception when a program references an undeclared field or method..
//     * <p/>
//     * Some programs may contain dead code that references fields or methods that do not exist. By default, Soot exists
//     * with an exception when this happens. If this option is enabled, Soot only prints a warning but does not exit.
//     */
//    @Parameter(defaultValue = "false")
//    protected boolean ignoreResolutionErrors;

    /**
     * Include classes in pkg as application classes.
     * <p/>
     * Designate classes in packages whose names begin with pkg (e.g.<tt>java.util.</tt>) as application classes which
     * should be analyzed and output. This option allows you to selectively analyze classes in some packages that Soot
     * normally treats as library classes.
     * You can use the include option multiple times, to designate the classes of multiple packages as application classes.
     * If you specify both include and exclude options, first the classes from all excluded packages are marked as library
     * classes, then the classes from all included packages are marked as application classes.
     */
    @Parameter
    protected List includes;

    /**
     * Exclude classes in pkg from application classes.
     * <p/>
     * Excludes any classes in packages whose names begin with pkg from the set of application classes which are analyzed
     * and output, treating them as library classes instead.
     * This option allows you to selectively exclude classes which would normally be treated as application classes.
     * You can use the exclude option multiple times, to designate the classes of multiple packages as library classes.
     * If you specify both include and exclude options, first the classes from all excluded packages are marked as library
     * classes, then the classes from all included packages are marked as application classes.
     */
    @Parameter
    protected List excludes;

    /**
     * Set default excluded packages to empty list.
     * <p/>
     * Soot uses a default list of packages (such as java.) which are deemed to contain library classes. This switch
     * removes the default packages from the list of packages containing library classes.
     * Individual packages can then be added using the exclude option.
     */
    @Parameter( defaultValue = "TODO" )
    protected boolean includeAll;

    /**
     * Note that class may be loaded dynamically.
     * <p/>
     * Mark class as a class which the application may load dynamically. Soot will read it as a library class even if it
     * is not referenced from the argument classes. This permits whole program optimizations on programs which load
     * classes dynamically if the set of classes that can be loaded is known at compile time.
     */
    @Parameter
    protected List dynamicClasses;

    /**
     * Mark all class files in directory  as classes that may be loaded dynamically. Soot will read them as library
     * classes even if they are not referenced from the argument classes.
     * You can specify more than one directory of potentially dynamic classes by specifying multiple dynamic directory
     * options.
     */
    @Parameter
    protected List dynamicDirectories;

    /**
     * Marks all class files belonging to the package package or any of its subpackages as classes which the application
     * may load dynamically.
     * Soot will read all classes in package as library classes, even if they are not referenced by any of the argument
     * classes.
     */
    @Parameter
    protected List dynamicPackages;

    /**
     * Keep line number tables.
     * <p/>
     * Preserve line number tables for class files throughout the
     * //            transformations.
     */
    @Parameter( defaultValue = "true" )
    protected boolean keepLineNumber;

    /**
     * Maintain bytecode offset tables for class files throughout the transformations.
     */
    @Parameter( defaultValue = "false" )
    protected boolean keepBytecodeOffset;

//    /**
//     * Emit purity attributes.
//     * <p/>
//     * Purity anaysis implemented by Antoine Mine and based on the paper A Combined Pointer and Purity Analysis Java
//     * Programs by Alexandru Salcianu and Martin Rinard.
//     */
//    @Parameter(defaultValue = "false")
//    protected boolean annotPurity;

//    /**
//     * Emit null pointer attributes.
//     * <p/>
//     * Perform a static analysis of which dereferenced pointers may have null values, and annotate class files with
//     * attributes encoding the results of the analysis. For details, see the documentation for Null Pointer Annotation
//     * and for the Array Bounds and Null Pointer Check Tag Aggregator.
//     */
//    @Parameter(defaultValue = "false")
//    protected boolean annotNullPointer;

//    /**
//     * Emit array bounds check attributes.
//     * <p/>
//     * Perform a static analysis of which array bounds checks may safely be eliminated and annotate output class files
//     * with attributes encoding the results of the analysis. For details, see the documentation for Array Bounds
//     * Annotation and for the Array Bounds and Null Pointer Check Tag Aggregator.
//     */
//    @Parameter(defaultValue = "false")
//    protected boolean annotArrayBounds;

//    /**
//     * Enable the generation of side-effect attributes.
//     */
//    @Parameter(defaultValue = "false")
//    protected boolean annotSideEffect;

//    /**
//     * Enable the generation of field read/write attributes.
//     */
//    @Parameter(defaultValue = "false")
//    protected boolean annotFieldReadWrite;

    /**
     * Report the time required to perform some of Soot's transformations.
     */
    @Parameter( defaultValue = "false" )
    protected boolean time;

    /**
     * Attempt to subtract time spent in garbage collection from the reports of times required for transformations.
     */
    @Parameter( defaultValue = "false" )
    protected boolean subtractGC;

    /**
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        configureLogging();
        configureOptions();
        run();
    }

    protected void configureLogging()
    {
        soot.G.v().out = new MavenLogPrintStream( getLog() );
    }

    protected void configureOptions()
    {
        final Options options = Options.v();
        options.set_help( help );
        options.set_phase_list( phaseList );
        options.set_phase_help( phaseHelp );
        options.set_version( version );
        options.set_verbose( verbose );
        options.set_interactive_mode( interactiveMode );
        options.set_unfriendly_mode( unfriendlyMode );
        options.set_app( applicationMode );
        options.set_whole_program( wholeProgram );
        options.set_whole_shimple( wholeShimple );
        options.set_validate( validate );
        options.set_debug( debug );
        options.set_debug_resolver( debugResolver );
        options.set_soot_classpath( sootClasspath );
        options.set_prepend_classpath( prependClasspath );
        options.set_process_dir( processDirectory );
        options.set_ast_metrics( astMetrics );
        options.set_src_prec( sourcePrecedence.getValue() );
        options.set_full_resolver( fullResolver );
        options.set_allow_phantom_refs( allowPhantomRefs );
        options.set_no_bodies_for_excluded( noBodiesForExcluded );
        options.set_j2me( j2me );
        options.set_main_class( mainClass );
        options.set_polyglot( polyglot );
        options.set_output_dir( outputDirectory );
        options.set_output_format( outputFormat.getValue() );
        options.set_output_jar( outputJar );
        options.set_xml_attributes( xmlAttributes );
        options.set_print_tags_in_output( printTags );
        options.set_no_output_source_file_attribute( noOutputSourceFileAttribute );
        options.set_no_output_inner_classes_attribute( noOutputInnerClassesAttribute );
        options.set_dump_body( dumpBody );
        options.set_dump_cfg( dumpCfg );
        options.set_show_exception_dests( showExceptionDests );
        options.set_gzip( gzip );
//        options.set_XXXXXXX(optimize);
//        options.set_XXXXXXX(wholeOptimize);
        options.set_via_grimp( viaGrimp );
        options.set_via_shimple( viaShimple );
        options.set_throw_analysis( throwAnalysis.getValue() );
        options.set_omit_excepting_unit_edges( omitExceptingUnitEdges );
//        options.set_XXXXXXX(trimCfgs);
//        options.set_XXXXXXX(ignoreResolutionErrors);
        options.set_include( includes );
        options.set_exclude( excludes );
        options.set_include_all( includeAll );
        options.set_dynamic_class( dynamicClasses );
        options.set_dynamic_dir( dynamicDirectories );
        options.set_dynamic_package( dynamicPackages );
        options.set_keep_line_number( keepLineNumber );
        options.set_keep_offset( keepBytecodeOffset );
//        options.set_XXXXXXX(annotPurity);
//        options.set_XXXXXXX(annotNullPointer);
//        options.set_XXXXXXX(annotArrayBounds);
//        options.set_XXXXXXX(annotSideEffect);
//        options.set_XXXXXXX(annotFieldReadWrite);
        options.set_time( time );
        options.set_subtract_gc( subtractGC );
    }

    protected void run()
        throws MojoFailureException
    {
        try
        {
            Main.v().run( new String[0] );
        }
        catch ( soot.CompilationDeathException e )
        {
            throw new MojoFailureException( "Soot execution failed", e );
        }
    }
}
