package com.JavaIndexer.gui.stepPanels;

import java.awt.BorderLayout;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent; //import java.awt.event.ActionListener;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.JavaIndexer.generics.Document;
import com.JavaIndexer.generics.WordAttributes;
import com.JavaIndexer.gui.JavaIndexerGUI;
import com.JavaIndexer.gui.generics.StepPanel;
import com.JavaIndexer.processing.Tagger;

/**
 * @author Nicole Pernischova
 */
public class TermSelectionStepPanelTFIDF extends StepPanel implements
		ActionListener {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	// ---- Global Variables ----

	private JLabel weightingLabel, termsLabel, documentLabel;
	private JButton setlimits;
	private JComboBox weighting;
	private JTextArea document, terms;
	private JScrollPane documentsScrollPane, termsScrollPane;
	private String[] description = { "Log-Entropy Method",
			"Supervised Weighting", "Confidence-Based Weighting" };

	/**
	 * Default constructor.
	 */
	public TermSelectionStepPanelTFIDF() {
		super("TF-IDF");
		setNextButtonText("Next");

		document = new JTextArea();
		terms = new JTextArea();
		documentLabel = new JLabel();
		termsLabel = new JLabel();

		weighting = new JComboBox(description);
		weightingLabel = new JLabel();
		setlimits = new JButton();

		document.setEditable(false);
		document.setLineWrap(true);
		document.setWrapStyleWord(true);
		
		terms.setEditable(false);
		terms.setLineWrap(true);
		terms.setWrapStyleWord(true);
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

		// ======== document panel ========

		JPanel LPanel = new JPanel();
		LPanel.setLayout(new GridLayout(1, 2));
		// labels
		documentLabel.setText("Text");
		LPanel.add(documentLabel);
		termsLabel.setText("terms to index");
		LPanel.add(termsLabel);

		JPanel DocPanel = new JPanel();
		DocPanel.setLayout(new GridLayout(1, 2));
		documentsScrollPane = new JScrollPane(document);
		documentsScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		DocPanel.add(documentsScrollPane);

		termsScrollPane = new JScrollPane(terms);
		termsScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		DocPanel.add(termsScrollPane);

		JPanel DocPanel2 = new JPanel(new BorderLayout());
		// GridBagConstraints constraints2 = new GridBagConstraints();

		JPanel DocPanel3 = new JPanel(new FlowLayout());
		weightingLabel.setText("Weighting Method ");
		DocPanel3.add(weightingLabel);
		weighting.setSelectedIndex(0);
		weighting.addActionListener(this);
		DocPanel3.add(weighting);

		setlimits.setText("Set/Update");
		setlimits.addActionListener(this);
		DocPanel2.add(setlimits, BorderLayout.EAST);
		DocPanel2.add(DocPanel3, BorderLayout.CENTER);

		add(LPanel, BorderLayout.NORTH);
		add(DocPanel, BorderLayout.CENTER);
		add(DocPanel2, BorderLayout.PAGE_END);
		
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == setlimits) {
			try{
				document.setText(DocumentsStepPanel.Text);
			}catch (Exception e){}

			if (weighting.getSelectedIndex() == 0){
				terms.setText("Test0");
				try{
					long startTime = System.currentTimeMillis();
					
					
//					FileInputStream fis = new FileInputStream("objectFiles/termsToIndex.tti");
//				    ObjectInputStream ois = new ObjectInputStream(fis);
//				    WordAttributes [] tti = (WordAttributes[])ois.readObject();
//				    fis.close();
//				    ois.close();
				    /*
				    fis = new FileInputStream("objectFiles/originalText.ot");
				    ois = new ObjectInputStream(fis);
				    String ot = (String) ois.readObject();
				    fis.close();
				    ois.close();*/
				    
				    /*fis = new FileInputStream("objectFiles/paragraphOffsets.po");
				    ois = new ObjectInputStream(fis);
				    int[][] po = (int[][]) ois.readObject();
				    fis.close();
				    ois.close();*/
				    
				    com.JavaIndexer.processing.TFIDF tfidf = 
				    		new com.JavaIndexer.processing.TFIDF(Tagger.termsToIndex, Tagger.paragraphOffsets);
				    terms.setText(tfidf.TFIDFlist.toString());
				    long timeElapsed = (System.currentTimeMillis() - startTime);
				    System.out.println("Total TFIDF: " + timeElapsed + " milliseconds");
				}catch(Exception e){
					terms.setText("ERROR: " + e);
					e.printStackTrace();
				}
			}else if (weighting.getSelectedIndex() == 1){
					terms.setText("Test1");
			}else if (weighting.getSelectedIndex() == 2){
				terms.setText("Test2");
			}
		}
	}

}
