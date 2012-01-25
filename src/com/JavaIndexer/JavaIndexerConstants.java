package com.JavaIndexer;

import java.util.HashMap;

import com.JavaIndexer.generics.Parameterizable;

/**
 * Defines a whole slew of public static constants that can be used system-wide.
 */
public class JavaIndexerConstants 
{

    /*
     * n.b. these are NOT final but should nevertheless not be changed.
     */

    /**
     * Location for binary (executable) objects. Not final but should not be
     * changed.
     */
    public static String          JAVAINDEXER_BINDIR  = "../bin/";
    /**
     * Location for libraries such as word lists. Not final but should not be
     * changed.
     */
    public static String          JAVAINDEXER_LIBDIR  = "../lib/";
    /**
     * Location for source code for JGAAP project. Not final but should not be
     * changed.
     */
    public static String          JAVAINDEXER_SRCDIR  = "../src/";
   

    /**
     * Java Prefix for different types of object collections 
     * Again, not final but should not be changed
     */
   
    public static String          JAVAINDEXER_EVENTDRIVERPREFIX = "com.JavaIndexer.eventDrivers.";
    public static String          JAVAINDEXER_PROCESSINGPREFIX = "com.JavaIndexer.processing.";
    public static String          JAVAINDEXER_GUIPREFIX = "com.JavaIndexer.gui.";
    public static String          JAVAINDEXER_GENERICSPREFIX = "com.JavaIndexer.generics.";
    public static String          JAVAINDEXER_BACKENDPREFIX = "com.JavaIndexer.backend.";
    public static String          JAVAINDEXER_LANGUAGEPREFIX = "com.JavaIndexer.languages.";

    // MVR This can and should be changed.
    /** Set of global parameters, to change via usual schemes. */
    public static Parameterizable globalParams  = new Parameterizable();
    
    /** Set of global objects, accessed via a HashMap 
     * (when Parameterizable is not sufficient because 
     * we need to store generic objects)
     */
    public static HashMap<String, Object> globalObjects = new HashMap<String,Object>();
    
}
