package com.JavaIndexer.gui;

/**
 * guiMenuBar.java
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import com.JavaIndexer.generics.Language;

/**
 * This class encapsulates the menus and menu items used in the JavaIndexer GUI.
 * It handles their events and calls specific methods in the main GUI as needed.
 * 
 * @author Nicole Pernischova This code is a modification from JGAAP JGAAP @author
 *         Chuck Liddell created 11/12/08 TODO: Finish code for saving / loading
 */
public class guiMenuBar extends JMenuBar implements ActionListener {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	// ---- Global Variables ----
	private JMenu menuFile;
	private JMenuItem menuFileExit;
	private JMenuItem menuFileLoad;
	private JMenuItem menuFileSaveCorpus;
	private JMenuItem menuFileSaveUnknown;
	private JMenuItem menuFileSaveAll;
	private JMenu menuFileLanguage;
	private ButtonGroup menuFileLanguageSelection;
	private JRadioButtonMenuItem[] menuFileLanguageOptions;
	private Vector<Language> languages;

	private JMenuItem menuHelpAbout;

	/**
	 * A reference to the main GUI that we will use to call certain methods.
	 */
	private final JavaIndexerGUI theGUI;

	/**
	 * A brief message that provides basic information about this program.
	 */
	private final String ABOUT_MESSAGE = "Java Computer Aided Indexer\n\n"
			+ "Version: " + JavaIndexerGUI.VERSION;

	/**
	 * Default constructor.
	 */
	public guiMenuBar(JavaIndexerGUI theGUI) {
		super();

		this.theGUI = theGUI; // Set a reference to the gui object

		// -------------------------------------
		// -- Instantiate Menus and MenuItems --
		// -------------------------------------
		menuFile = new JMenu("File");
		menuFile.setEnabled(false);
		menuFileLoad = new JMenuItem("Load");
		menuFileSaveCorpus = new JMenuItem("Save Corpus");
		menuFileSaveAll = new JMenuItem("Save All");
		menuFileSaveUnknown = new JMenuItem("Save Unknown");
		menuFileLanguage = new JMenu("Language");
		menuFileLanguageSelection = new ButtonGroup();

		languages = new Vector<Language>();

		menuFileLanguageOptions = new JRadioButtonMenuItem[languages.size()];
		menuFileExit = new JMenuItem("Exit");
		menuHelpAbout = new JMenuItem("About");

		// -------------------------------------------------------------
		// -- Set additional properties and add components to the GUI --
		// -------------------------------------------------------------

		// -- File Menu --
		menuFileLoad.addActionListener(this);
		menuFile.add(menuFileLoad);
		menuFileSaveCorpus.addActionListener(this);
		menuFile.add(menuFileSaveCorpus);
		menuFileSaveUnknown.addActionListener(this);
		menuFile.add(menuFileSaveUnknown);
		menuFileSaveAll.addActionListener(this);
		menuFile.add(menuFileSaveAll);
		menuFile.add(menuFileLanguage);
		// -- File Sub-Menu Language --
		int l = 0;
		for (Language lang : languages) {
			menuFileLanguageOptions[l] = new JRadioButtonMenuItem(lang
					.getName());
			menuFileLanguage.add(menuFileLanguageOptions[l]);
			menuFileLanguageSelection.add(menuFileLanguageOptions[l]);
			menuFileLanguageOptions[l].setActionCommand(lang.getName());
			if (lang.getName().equalsIgnoreCase("English"))
				menuFileLanguageOptions[l].setSelected(true);
			l++;
		}
		// -- END Language Sub-Menu --
		menuFileExit.addActionListener(this);
		menuFile.add(menuFileExit);
		this.add(menuFile); // Add this Menu to the Menu Bar

	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		String command = e.getActionCommand();

		if (src == menuFileExit) {
			System.exit(0);
		}

		if (src == menuHelpAbout) {
			JOptionPane.showMessageDialog(null, ABOUT_MESSAGE,
					"JavaIndexer - About", JOptionPane.INFORMATION_MESSAGE);
		}

		if (command.startsWith("Save")) {
			System.out.println(command + ":");
			theGUI.saveFiles(command);
		}

		if (src == menuFileLoad) {
			if (theGUI.DEBUG) {
				System.out.println("Load: ");
			}

		}
	}

}
