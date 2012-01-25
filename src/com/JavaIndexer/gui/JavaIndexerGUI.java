package com.JavaIndexer.gui;

/*
 * JavaIndexerGUI.java
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.ProgressMonitor;

import com.JavaIndexer.backend.guiDriver;
import com.JavaIndexer.generics.Document;
import com.JavaIndexer.gui.generics.StepListener;
import com.JavaIndexer.gui.generics.StepWizard;
import com.JavaIndexer.gui.stepPanels.DocumentsStepPanel;
import com.JavaIndexer.gui.stepPanels.TermSelectionStepPanelEVD;
import com.JavaIndexer.gui.stepPanels.TermSelectionStepPanelFrequency;
import com.JavaIndexer.gui.stepPanels.TermSelectionStepPanelWSD;
import com.JavaIndexer.gui.stepPanels.ReportStepPanel;
import com.JavaIndexer.gui.stepPanels.TaggerStepPanel;
import com.JavaIndexer.gui.stepPanels.TermSelectionStepPanelHCA;
import com.JavaIndexer.gui.stepPanels.TermSelectionStepPanelTFIDF;

/**
 * This class is the primary class responsible for managing and displaying the
 * GUI interface for the JavaIndexer project. It interacts with the engine of
 * the project through a GUIDriver class.
 * 
 * @author Nicole Pernischova
 * 
 *         This file had been modified, jgaap was used as a template
 * 
 *         JGAAP authors:
 * @author Chuck Liddell
 * @author Patrick Juola
 * @author Mike Ryan
 * @author John Noecker Re-created 11/12/08
 */
public class JavaIndexerGUI extends JFrame implements StepListener {
	/**
     *
     */
	private static final long serialVersionUID = 1L;

	/**
	 * The current program version.
	 */
	public final static String VERSION = "1.0";

	/**
	 * The current progress of the GUI's Progress Bar
	 */
	public static int currentProgress = 0;

	/**
	 * A flag for more verbose output in System.out.
	 */
	public final boolean DEBUG = true;

	/**
	 * The guiDriver class that provides a means of interfacing with the JGAAP
	 * back-end.
	 */
	private guiDriver driver;

	/**
	 * Encapsulates the menu instantiation and logic, and calls appropriate
	 * methods in this class as needed.
	 */
	private guiMenuBar menuBar;

	/**
	 * This is the main component that controls the different steps in our
	 * process.
	 */
	private StepWizard stepWizard;

	private JFileChooser saveFile = new JFileChooser();

	private static ProgressMonitor progressMonitor;

	// ---- Various StepPanels ----
	private DocumentsStepPanel documentsStepPanel;
	private TaggerStepPanel taggerStepPanel;
	private TermSelectionStepPanelFrequency termSelectionStepPanelFrequency;
	private TermSelectionStepPanelTFIDF termSelectionStepPanelTFIDF;
	private TermSelectionStepPanelEVD termSelectionStepPanelEVD;
	private TermSelectionStepPanelHCA termSelectionStepPanelHCA;
	private TermSelectionStepPanelWSD termSelectionStepPanelWSD;
	private ReportStepPanel reportStepPanel;

	/**
	 * Default constructor.
	 */
	public JavaIndexerGUI() {
		driver = new guiDriver();
		initComponents();
		this.setTitle("Java Computer Aided Indexer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Increment the progress bar by one.
	 */
	public static synchronized void incProgress() {
		currentProgress++;
		if (progressMonitor != null) {
			progressMonitor.setProgress(currentProgress);
		}
	}

	/**
	 * Convenience method for adding multiple documents to the GUI display
	 * simultaneously.
	 * 
	 * @param newDocuments
	 *            Vector of Vectors of Strings with document info
	 */
	public void addDocumentsToGUI(Vector<Vector<String>> newDocuments) {
		for (Vector<String> docInfo : newDocuments) {
			addDocumentToGUI(docInfo);
		}
	}

	/**
	 * Called to add the different attributes of a document to the GUI display.
	 * 
	 * @param title
	 *            the title of the document
	 * @param filePath
	 *            the file path to the document
	 */
	public void addDocumentToGUI(String filePath) {
		documentsStepPanel.addDocument(new Document(filePath));
	}

	/**
	 * Overloaded method in to make it possible to add documents to the GUI as a
	 * vector of Strings.
	 * 
	 * @param newDocument
	 *            vector of Strings with document info
	 */
	public void addDocumentToGUI(Vector<String> newDocument) {
		if ((newDocument.size() < 2)) {
			return; // If we don't have at least an author and a file, don't add
			// it
		}

		// Support old-style CSV files without the "Title" field. If the Title
		// field is present, load it. Otherwise, leave it blank
		addDocumentToGUI(newDocument.get(1));
	}

	/**
	 * Called when a StepPanel is executed by the StepWizard.
	 * 
	 * @param stepName
	 *            the name of the step that was called for execution
	 */
	public void executeStep(String stepName) {
		currentProgress = 0;
		if (stepName.equals("Documents")) {

			progressMonitor = new ProgressMonitor(null, "Loading Document", "",
					0, 1);
			// documentsStepPanel.getDocuments().size() + 1);
			incProgress();
			progressMonitor.setMillisToDecideToPopup(1);
			progressMonitor.setMillisToPopup(2);

			driver.clearAll();
		}
		if (stepName.equals("Tagger")) {
			System.out.println(" ===== TAG =====");
			progressMonitor = new ProgressMonitor(null, "Tagging in Progress",
					"", 0, 1);// documentsStepPanel.getDocuments().size() + 1);
			incProgress();
			progressMonitor.setMillisToDecideToPopup(1);
			progressMonitor.setMillisToPopup(2);
		}
		if (stepName.equals("Term Selection")) {
			System.out.println(" ===== Term Selection =====");
			progressMonitor = new ProgressMonitor(null,
					"Generating Event Sets", "", 0, 1);// documentsStepPanel
			// .getDocuments().size() + 1);
			incProgress();
			progressMonitor.setMillisToDecideToPopup(1);
			progressMonitor.setMillisToPopup(2);

			String listResults = "";
			reportStepPanel.displayResult(listResults);
		}
	}

	/**
	 * Initializes all the GUI components.
	 */
	private void initComponents() {

		setMinimumSize(new Dimension(800, 400));
		setPreferredSize(new Dimension(1000, 800));
		setLayout(new BorderLayout());

		menuBar = new guiMenuBar(this);
		this.add(menuBar, BorderLayout.NORTH);

		// -- The StepWizard and its StepPanels --
		stepWizard = new StepWizard();
		documentsStepPanel = new DocumentsStepPanel();
		documentsStepPanel.addStepListener(this);
		stepWizard.addStep(documentsStepPanel);

		taggerStepPanel = new TaggerStepPanel();
		taggerStepPanel.addStepListener(this);
		stepWizard.addStep(taggerStepPanel);

		termSelectionStepPanelFrequency = new TermSelectionStepPanelFrequency();
		termSelectionStepPanelFrequency.addStepListener(this);
		stepWizard.addStep(termSelectionStepPanelFrequency);

		termSelectionStepPanelTFIDF = new TermSelectionStepPanelTFIDF();
		termSelectionStepPanelTFIDF.addStepListener(this);
		stepWizard.addStep(termSelectionStepPanelTFIDF);

		termSelectionStepPanelEVD = new TermSelectionStepPanelEVD();
		termSelectionStepPanelEVD.addStepListener(this);
		stepWizard.addStep(termSelectionStepPanelEVD);
		
		termSelectionStepPanelHCA = new TermSelectionStepPanelHCA();
		termSelectionStepPanelHCA.addStepListener(this);
		stepWizard.addStep(termSelectionStepPanelHCA);

		termSelectionStepPanelWSD = new TermSelectionStepPanelWSD();
		termSelectionStepPanelWSD.addStepListener(this);
		stepWizard.addStep(termSelectionStepPanelWSD);

		reportStepPanel = new ReportStepPanel();
		stepWizard.addStep(reportStepPanel);

		this.add(stepWizard, BorderLayout.CENTER);
	}

	public void saveFiles(String command) {
		driver.clearAll(); // First, remove all documents that were
		// already loaded
		// for (Document doc : documentsStepPanel.getDocuments()) {
		// driver.addDocument(doc);
		// }
		int returnVal = saveFile.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = saveFile.getSelectedFile();
			driver.saveDocuments(command, file);
			System.out.println("Saving: " + file.getName() + ".");
		} else
			System.out.println("User canceled save.");
		driver.clearAll();
	}

}
