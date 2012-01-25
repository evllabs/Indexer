package com.JavaIndexer;

import com.JavaIndexer.gui.JavaIndexerGUI;


/**
 * The JavaIndexer main file.
 * 
 * @author Nicole Pernischova
 *  
 *         This file is a modification of the jgaap's main file by Mike Ryan
 * 
 */

public class JavaIndexer {

	public static boolean commandline = false;
	public static final boolean DEBUG = true;

	/**
	 * Launches the JavaIndexer GUI.
	 */
	private static void createAndShowGUI() {
		// Note that the GUI object is stored in the globalObjects HashMap
		// under the key "gui" so other objects can access it easily
		JavaIndexerConstants.globalObjects.put("gui", (new JavaIndexerGUI()));
	}

	/**
	 * Initializes predefined global parameters.
	 */
	public static void initParameters() {
		JavaIndexerConstants.globalParams.setParameter("Language", "English");
	}

	/**
	 * launches either the CLI or the GUI based on the command line arguments
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		initParameters();
		if (args.length == 0) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createAndShowGUI();
				}
			});
		} else {
			// do nothing
			// not using command line
		}
	}
}
