package com.JavaIndexer.backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;


import com.JavaIndexer.JavaIndexerConstants;
import com.JavaIndexer.generics.Document;
import com.JavaIndexer.generics.DocumentSet;
import com.JavaIndexer.generics.Language;
import com.JavaIndexer.gui.JavaIndexerGUI;
import com.JavaIndexer.languages.*;

public class guiDriver {

	Vector<Document> u_docs;
	Vector<DocumentSet> u_docsets;



	HashMap<String, Language> languages;
	// Vector<Canonicizer> processEngine; CKL 1/24/2009 Removed, no longer
	// needed

	private boolean processError = false; // Flag to indicate
	// whether some error has
	// resulted in a default
	// action being taken
	// (e.g. falling back to
	// default events "word")
	private String processErrorMessage = ""; // Error message detailing

	// exact error encountered

	public guiDriver() {
		// Create a hidden document for tracking canonicizers
		JavaIndexerConstants.globalObjects.put("hDoc", (new Document()));
		u_docs = new Vector<Document>();
		u_docsets = new Vector<DocumentSet>();
		
		
		
		// Load the classifiers dynamically
		languages = new HashMap<String, Language>();
		

	}

	// ---------------------------------------------------
	// ------------- Document Manipulation ---------------
	// ---------------------------------------------------

	/**
	 * Add a document to the GUI backend.
	 * 
	 * @param newDocument
	 *            An instance of the Document class being added to the backend
	 */
	public void addDocument(Document newDocument) {
			u_docs.add(newDocument);
	}

	/**
	 * Method for adding more than one document simultaneously.
	 * 
	 * @param newDocuments
	 *            Vector of documents to be added to the backend
	 */
	public void addDocuments(Collection<Document> newDocuments) {
		for (Document doc : newDocuments) {
			addDocument(doc);
			JavaIndexerGUI.incProgress();
		}
	}

	/**
	 * Method for adding documents using only a few of the document attributes.
	 * 
	 * @param path
	 *            the path to the document
	 * @param author
	 *            the author of the document
	 */
	// CKL 1/24/2009 re-designed and re-coded
	public void addDocument(String path) {
		this.addDocument(new Document(path));
	}

	

	public Collection<Document> getAllDocuments() {
		ArrayList<Document> documents = new ArrayList<Document>();
		documents.addAll(u_docs);
		return documents;
	}

	

	/**
	 * Take the Document instances that are stored internally and call their
	 * 'processCanonicizers()' methods.
	 */
	// CKL 1/24/2009 re-designed and re-coded
	public void tag(String infile, String outfile) {
		u_docsets.clear();
		for (Document doc : u_docs) {
			u_docsets.add(new DocumentSet(doc));
		}
		

		/*
		 * Once we have begun canonicizing the documents, each document will be
		 * processed in a different thread, and will set a hasBeenProcessed()
		 * flag when it is finished. So, busy wait until this happens.
		 * 
		 * TODO: Put this into its own thread, have it sleep and have each
		 * document call notifyAll() when done processing. Have this thread
		 * check if there are more documents waiting and sleep if there are.
		 */
		
		for (Document doc : u_docs) {
			while (!doc.hasBeenProcessed())
				Thread.yield(); // FIXME: PJR 09/14/2009 This is a bad solution to the threading problem
		}

	}

	// MVR 4/23/2009 fixed to work with the new canon system
	// left the method deprecated so this get a
	// CKL 1/24/2009 This will probably need to be re-designed and re-coded due
	// to the new canonicizer system
	// MVR 10/6/2008 a redesign of the canonicizer
	/*public void canonicizerProcessEngine(String canon) {
		if (canonicizers.containsKey(canon.toLowerCase())) {
			addCanonicizerToAllDocuments(canonicizers.get(canon.toLowerCase()));
		} else {
			System.out
					.println("Canonicizer: " + canon + " was not recognized.");
		}
	}*/

	

	public void clearAll() {
		u_docs = new Vector<Document>();
		u_docsets = new Vector<DocumentSet>();
		
	}

	

	public void loadDocuments(Vector<Vector<String>> authorFileTable) {
		for (int i = 0; i < authorFileTable.size(); i++) {
			addDocument(authorFileTable.elementAt(i).elementAt(1));
		}
	}

	/**
	 * 
	 * Performs the selected analysis on the corpus
	 * 
	 * @param action
	 *            the analysis or distance that is going to be used
	 * @param currentDivergenceMethod
	 *            the version of the divergence method that is going to be used
	 * @return
	 */

	public String runStatisticalAnalysis(String action) {
		
		String results = new String();
		System.out.println("Action is " + action);

		
		/*
		 * JIN - 7/31/08 Unknown event sets will default to Histogram Distance,
		 * as this is pretty fast. This works on the assumption that it's better
		 * to produce unexpected/incorrect results than it is for the program to
		 * crash.
		 */
		
		

		if (processError) {
			results += "*** Warning ***  An error in processing the EventSet and/or " +
					"Statistical Analysis Method choices has resulted in one or more " +
					"default actions being used, which may result in unexpected or incorrect results\n\n";
			results += processErrorMessage + "\n\n";
		}
		if (action.equalsIgnoreCase("Linear SVM")
				|| action.equalsIgnoreCase("SVM")
				|| action.equalsIgnoreCase("Gaussian SVM")) {

			
		} else {
			
		}

		return results;

	}

	public void saveDocuments(String which, File file) {
		Vector<Vector<String>> authorFileTable = new Vector<Vector<String>>();
		if (which.equals("Save Corpus")) {
			
		} else if (which.equals("Save Unknown")) {
			for (int i = 0; i < u_docs.size(); i++) {
				authorFileTable.add(new Vector<String>());
				authorFileTable.elementAt(i).add("");
				authorFileTable.elementAt(i).add(
						u_docs.elementAt(i).getFilePath());
				authorFileTable.elementAt(i)
						.add(u_docs.elementAt(i).getTitle());
			}
		} else if (which.equals("Save All")) {
			
		}
		System.out.println(authorFileTable.toString());
		
	}

	public void saveResults(String fileName, String results) {
		try {
			Writer output = new BufferedWriter(new FileWriter(fileName));
			output.write(results);
			output.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	@Deprecated
	public void setCharSet(String lang) {
		
		boolean found = false;
		
		if (!found) {
			System.out.println("Language not found defaulting to English.");
		}
	}

	

	/**
	 * Checks the selected language against those loaded from com.jgaap.language
	 * 
	 * @param e
	 *             string representing a language class
	 * @return  an instance of the specified language class
	 */
	public Language selectedLanguage(String e) {
		Language selected;
		if (languages.containsKey(e.toLowerCase())) {
			try {
				selected = languages.get(e.toLowerCase()).getClass()
						.newInstance();
			} catch (Exception l) {
				System.err.println("Language selected " + e.toString()
						+ " not recognized using default English.");
				selected = new English();
			}
		} else {
			System.err.println("Language selected " + e.toString()
					+ " not recognized using default English.");
			selected = new English();
		}
		return selected;
	}

	/**
	 * Parses all documents using the language defined parseLanguage method.
	 * 
	 * @param lang
	 *             the language the documents are in / the parse method to use.
	 */
	public void evaluateLanguage(Language lang) {
		if (lang.isParseable()) {
			
			for (Document doc : this.u_docs) {
				doc.setProcessedText(lang.parseLanguage(doc.stringify()));
			}
		}
	}

	
	

	/**
	 * Lists the currently available arguments for the specified part of the
	 * jgaap pipeline
	 * 
	 * @param action
	 *             a part of the pipeline
	 * @return  list of all arguments from the dynamically loaded classes for
	 *         action
	 */
	public Vector<String> argumentList(String action) {
		Vector<String> result = new Vector<String>();
		if (action.equalsIgnoreCase("a")
				|| action.equalsIgnoreCase("analysis")) {
			
			
		} else if (action.equalsIgnoreCase("lang")
				|| action.equalsIgnoreCase("language")) {
			result.addAll(this.languages.keySet());
		} else {
			result.add("Argument type: " + action + " was not recognized.");
			result.add("Please choose a valid argument:");
			result.add("t, tagger");
			result.add("es, event set");
			result.add("a, analysis");
			result.add("lang, language");
		}
		return result;
	}

}
