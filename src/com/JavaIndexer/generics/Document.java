package com.JavaIndexer.generics;

/**
 * 
 * Code modified from jgaap project
 * @author Nicole Pernischova
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.text.*;
import javax.swing.text.html.*;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.hwpf.*;
import org.apache.poi.hwpf.extractor.*;

/**
 * Code for storing and processing individual documents of any type.
 */
public class Document extends Parameterizable {

	public String displayName() {
		return "Document";
	}

	public String tooltipText() {
		return "For storing and processing individual document of any type";
	}

	public boolean showInGUI() {
		return false;
	}

	private String filepath;
	private String title;
	private StringBuffer rawText = new StringBuffer();
	private int size;
	private DocType docType;

	/**
	 * List of possible document types.
	 */
	public static enum DocType {
		TXT, TEX, PDF, URL, DOC, HTML, GENERIC
	};

	/**
	 * Flag indicating whether this document has had its canonicizers processed
	 * yet.
	 */
	private boolean hasBeenProcessed = false;

	/** Contains current processed text **/
	public String procText;

	/** Create document with no text **/
	public Document() {
		filepath = null;
		docType = DocType.GENERIC;
		title = "";
		size = 0;
		procText = "";

	}

	/**
	 * Create and read in document with only the filepath.
	 * 
	 * @param filepath
	 *            file path of the document to be read
	 */
	public Document(String filepath) {
		this.filepath = filepath;
		this.title = getTitleFromPath(filepath);

		if (filepath.startsWith("http://") || filepath.startsWith("https://")) {
			docType = DocType.URL;
			readURLText(filepath);
		} else if (filepath.endsWith(".pdf")) {
			docType = DocType.PDF;
			loadPDF(filepath);
		} else if (filepath.endsWith(".doc")) {
			docType = DocType.DOC;
			loadMSWord(filepath);
		} else if (filepath.endsWith(".htm") || filepath.endsWith(".html")) {
			docType = DocType.HTML;
			loadHTML(filepath);
		} else {
			docType = DocType.GENERIC;
			readLocalText(filepath);
		}
	}

	/**
	 * Takes a file path and returns only the file name.
	 * 
	 * @param filePath
	 *            the full path to the file
	 * @return A document title derived from the file path.
	 */
	private static String getTitleFromPath(String filePath) {
		String[] split = filePath.split("[\\\\[\\/]]");
		return split[split.length - 1];
	}

	/**
	 * Extracts text from a PDF and stores it in the document.
	 * 
	 * @param filepath
	 *            The filepath of the PDF to be read.
	 */
	private void loadPDF(String filepath) {
		PDDocument doc;
		try {
			doc = PDDocument.load(new FileInputStream(filepath));
			PDFTextStripper pdfStripper = new PDFTextStripper();
			pdfStripper.setSortByPosition(false);

			char[] origText = pdfStripper.getText(doc).toCharArray();

			for (Character c : origText) { // while not end of file
				rawText.append(c.toString());
			}

			doc.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		procText = rawText.toString();
	}

	/**
	 * Extracts text from an HTML document and stores it in the document.
	 * 
	 * @param filepath
	 *            The filepath of the HTML document to be read.
	 */
	private void loadHTML(String filepath) {
		System.out.println("HTML Document");
		try {
			EditorKit kit = new HTMLEditorKit();
			javax.swing.text.Document doc = kit.createDefaultDocument();
			kit.read((InputStream) (new FileInputStream(filepath)), doc, 0);

			System.out.println(doc.getText(0, doc.getLength()));
			char[] origText = doc.getText(0, doc.getLength()).toCharArray();
			for (Character c : origText) { // while not end of file
				rawText.append(c.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		procText = rawText.toString();
	}

	/**
	 * Extracts text from an HTML document and stores it in the document.
	 * 
	 * @param filesInputStream
	 *            An input stream pointing to the HTML document to be read.
	 */
	private void loadHTML(InputStream filesInputStream) {
		System.out.println("HTML Document");
		try {
			EditorKit kit = new HTMLEditorKit();
			javax.swing.text.Document doc = kit.createDefaultDocument();
			kit.read(filesInputStream, doc, 0);
			procText = "";
			System.out.println(doc.getText(0, doc.getLength()));
			char[] origText = doc.getText(0, doc.getLength()).toCharArray();
			for (Character c : origText) { // while not end of file
				rawText.append(c.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		procText = rawText.toString();
	}

	/**
	 * Extracts text from a Word document and stores it in the document.
	 * 
	 * @param filepath
	 *            The filepath of the Word document to be read.
	 */
	private void loadMSWord(String filepath) {
		System.out.println("Word Document");

		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
					filepath));
			HWPFDocument doc = new HWPFDocument(fs);
			WordExtractor we = new WordExtractor(doc);
			System.out.println(we.getText());
			char[] origText = we.getText().toCharArray();
			for (Character c : origText) { // while not end of file
				rawText.append(c.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		procText = rawText.toString();
	}

	public void print() {
		char[] text = procText.toCharArray();
		for (Character c : text) {
			System.out.print(c);
		}
	}

	/**
	 * Reads text from a local file. Exceptions are not caught by name. Rather,
	 * all exceptions are handled through just printing the error message to
	 * stdout. This should probably be changed for robustness. The raw text of
	 * the file is stored for quick access in an array.
	 **/

	public void readLocalText(String filepath) {

		try {
			File input = new File(filepath);
			size = (int) input.length();

			FileInputStream fis = new FileInputStream(input);
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(fis));

			int x;

			while (true) { // while not end of file
				x = br.read(); // save to x

				if (x == -1) // breaks if end of file
					break;

				String m = new Character((char) x).toString(); // gets char from
				// file and
				// turns into
				// string
				//rawText = rawText + m;
				rawText.append(m);
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		procText = rawText.toString();
	}

	public void readStringText(String text) {
		rawText.append(text);
		procText = rawText.toString();
	}

	/**
	 * Reads text from a local file. Exceptions are not caught by name. Rather,
	 * all exceptions are handled through just printing the error message to
	 * stdout. This should probably be changed for robustness. The raw text of
	 * the file is stored for quick access in an array. Modeled from
	 * readLocalText PMJ 10/25/08
	 **/

	public void readURLText(String url) {
		try {
			URL input = new URL(url);
			InputStream ir = input.openStream();
			BufferedReader br;

			procText = "";

			if (filepath.endsWith(".htm") || filepath.endsWith(".html")) {
				loadHTML(input.openStream());
				return;
			}

			br = new BufferedReader(new InputStreamReader(ir));
			int x;

			while (true) { // while not end of file
				x = br.read(); // save to x

				if (x == -1) // breaks if end of file
					break;

				String m = new Character((char) x).toString(); // gets char from
				// file and
				// turns into
				// string
				rawText.append(m);
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		procText = rawText.toString();
	}

	// --------------------------------------------------
	// ------------ Getters and Setters -----------------
	// --------------------------------------------------

	/** Returns the docType of the current document **/
	public DocType getDocType() {
		return docType;
	}

	/** Returns the full filepath of the current document **/
	public String getFilePath() {
		return filepath;
	}

	/**
	 * Returns text with preprocessing done. Preprocessing can include stripping
	 * whitespace or normalizing the case
	 **/
	public String getProcessedText() {
		return procText;
	}

	public void setProcessedText(String procText) {
		this.procText = procText;
	}

	/**
	 * Returns the size of the document. Size is determined by the number of
	 * characters plus whitespace
	 **/
	public int getSize() {
		return size;
	}

	/** Returns the title of the current document **/
	public String getTitle() {
		return title;
	}

	/** Sets the docType of the current document **/
	public void setDocType(DocType t) {
		docType = t;
	}

	/** Sets the title of the current document **/
	public void setTitle(String t) {
		title = t;
	}

	/**
	 * Checks to see if this document has been processed with tagger.
	 * 
	 * @return boolean value indicating whether the tagger associated with this
	 *         document have been applied to the document
	 */
	public boolean hasBeenProcessed() {
		return hasBeenProcessed;
	}

	/**
	 * Convert processed document into one really long string.
	 **/
	public String stringify() {
		/*
		 * JN 3/11/08 - Changed the String to a StringBuffer. This has reduced
		 * the running time from several minutes to instantaneous.
		 */
		StringBuffer t = new StringBuffer(procText.length());
		for (int i = 0; i < procText.length(); i++) {
			t.append((char) procText.charAt(i));
		}
		return t.toString();
	}

	@Override
	public String toString() {
		String t = new String();
		t = "Title:  " + title + "\n";
		t += "Path:   " + filepath + "\n";
		return t;
	}
}
