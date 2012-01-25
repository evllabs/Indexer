package com.JavaIndexer.gui.stepPanels;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.JavaIndexer.generics.WordAttributes;
import com.JavaIndexer.gui.generics.StepPanel;
import com.JavaIndexer.processing.Tagger;

/**
 * This panel represents a single step in a multi-step process, both graphically
 * and logically. It encapsulates all the necessary graphical elements and
 * program logic needed for this step, and provides access methods for necessary
 * data to be passed along to the next step. Specifically, it handles Report
 * generation and display for the JGAAP program.
 * 
 * Code modified from jgaap program by Nicole Pernischova
 * 
 * @author Chuck Liddell Created 11/13/08 TODO: Add functionality to reset and
 *         return to the first step in the process TODO: Clean up and
 *         dramatically improve the functionality of this panel
 */
class IndexPair implements Comparable<IndexPair>{

	private int startIdx, endIdx;
	private String word;
	
	IndexPair(int a, int b, String idxArray){
		this.startIdx = a;
		this.endIdx = b;
		this.word = idxArray;
	}
	public int getStartIdx(){
		return this.startIdx;
	}
	public int compareTo(IndexPair ip) {
		if (this.startIdx > ip.startIdx){
			return -1;
		}
		else if (this.startIdx < ip.startIdx)
			return 1;
		else 
			return 0;
	}
	public int getEndIdx() {
		return this.endIdx;
	}
	public String getWord() {
		return this.word;
	}
}
public class ReportStepPanel extends StepPanel implements ActionListener{

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	// ---- Global Variables ----
	private JScrollPane scrollPane;
	private JTextArea listResults;
	private JLabel resultLabel;
	private JButton generateIndex;

	// save / print functionality not yet implemented
	// private JButton saveButton, printButton;

	/**
	 * Default Constructor
	 */
	public ReportStepPanel() {

		super("Report");
		setNextButtonText("Done");

		scrollPane = new JScrollPane();
		listResults = new JTextArea();
		resultLabel = new JLabel();
		generateIndex = new JButton();
		generateIndex.setText("Generate Index");
		generateIndex.addActionListener(this);
		
		// save and print functionality not yet implemented
		// saveButton = new JButton("Save");
		// printButton = new JButton("Print");
		/* TODO: Re-disable listResults once Save and Print are working */
		// listResults.setEnabled(false);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

		resultLabel.setText("Index");
		add(resultLabel, BorderLayout.NORTH);
		listResults.setLineWrap(true);
        listResults.setWrapStyleWord(true);
		scrollPane = new JScrollPane(listResults);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane, BorderLayout.CENTER);
		add(generateIndex, BorderLayout.SOUTH);

	}

	/**
	 * Displays the string argument it receives in its main text area.
	 * 
	 * @param result
	 *            a String to display in this Report panel
	 */
	public void displayResult(String result) {
		listResults.setText(result);
	}
	public void actionPerformed(ActionEvent event){
		if (event.getSource() == generateIndex){
			try{
				String originalText = DocumentsStepPanel.Text;
				HashMap<String, WordAttributes> termsToIndex = Tagger.termsToIndex;
				ArrayList<String> idxArray = TermSelectionStepPanelFrequency.idxArray;
//			    FileInputStream fis = new FileInputStream("objectFiles/originalText.ot");
//			    ObjectInputStream ois = new ObjectInputStream(fis);
//			    String originalText = (String) ois.readObject();
//			    
//			    fis = new FileInputStream("objectFiles/termsToIndex.tti");
//			    ois = new ObjectInputStream(fis);
//			    WordAttributes[] termsToIndex = (WordAttributes[]) ois.readObject();
//			    
//			    fis = new FileInputStream("objectFiles/idxArray.idx");
//			    ois = new ObjectInputStream(fis);
//			    int[] idxArray = (int[]) ois.readObject();
//			    fis.close();
//			    ois.close();
			    
			    ArrayList<IndexPair> idxes = new ArrayList<IndexPair>();
			    
			    for (int i=0; i<idxArray.size(); i++){
			    	for (int j=0; j<termsToIndex.get(idxArray.get(i)).getCount(); j++){
				    	if (termsToIndex.get(idxArray.get(i)).isIndexed(j)){
				    		idxes.add(new IndexPair(termsToIndex.get(idxArray.get(i)).getStartIndex(j), 
				    					termsToIndex.get(idxArray.get(i)).getEndIndex(j), 
				    						idxArray.get(i)));
				    	}
			    	}
			    }
			    Collections.sort(idxes);
			    StringBuffer ot = new StringBuffer(originalText);
			    
			    for (int i=0; i<idxes.size(); i++){
			    	String word = termsToIndex.get(idxes.get(i).getWord()).getIndexTerm();
			    	StringBuffer insertString = new StringBuffer(word + "}");
			    	while (termsToIndex.containsKey(word) && termsToIndex.get(word).hasHigherTerm()){
			    		word = termsToIndex.get(word).getHigherTerm();
			    		insertString.insert(0, word + "!");
			    	}
			    	insertString.insert(0,"\\index{");
			    	ot.insert(idxes.get(i).getStartIdx(),insertString);
			    }
			    for (int i=0; i<ot.length(); i++){
			    	switch (ot.charAt(i)){
			    		case '\\': 
			    					if (ot.indexOf("\\index",i-1) == i){
			    						while (true){
			    							i = ot.indexOf("}",i);
			    							if (ot.charAt(i-1) != '\\')
			    								break;
			    						}
			    					}
			    					else{
			    						ot.insert(i+1,"backslash ");
			    						i += 10;
			    					}
			    					break;
			    		case '{' : 
			    		case '}' :
			    		case '$' :
			    		case '^' :
			    		case '_' :
			    		case '%' :
			    		case '~' :
			    		case '#' :
			    		case '&' :
			    					ot.insert(i, '\\'); i++;
			    					if (Character.isWhitespace(ot.charAt(i+1))){
			    						ot.insert(i+1, '\\');
				    					i++;
			    					}
			    					break;
			    							
			    		default: break; 
			    	}
			    }
			    ot.insert(0, "\\documentclass{article}\n" +
			    		"\\usepackage{makeidx}" +
			    		"\\makeindex\n" +
			    		"\\begin{document}\n");
			    ot.append("\\printindex\n" +
			    		"\\end{document}");
			    FileWriter fw = new FileWriter(new File("testFile.tex"));
			    BufferedWriter bw = new BufferedWriter(fw);
			    bw.write(ot.toString());
			    bw.close();
			    fw.close();
			   displayResult(ot.toString());
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
