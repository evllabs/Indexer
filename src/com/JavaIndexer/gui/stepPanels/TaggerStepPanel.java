package com.JavaIndexer.gui.stepPanels;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import com.JavaIndexer.JavaIndexerConstants;
import com.JavaIndexer.gui.generics.IndeterminateProgressPopup;
import com.JavaIndexer.gui.generics.StepPanel;
import com.JavaIndexer.gui.JavaIndexerGUI;

public class TaggerStepPanel extends StepPanel implements ActionListener {

	/**
	 * @author nicole.pernischova
	 */
	private static final long serialVersionUID = 1L;

	// ---- Global Variables ----
	private JLabel labelTaggedDocument;
	private JButton buttonMod, buttonConfirm, buttonTag;
	private JTextArea TdocumentViewer;
	private JScrollPane documentsScrollPane;

	/**
	 * Default constructor.
	 */
	public TaggerStepPanel() {
		super("Tagger");
		setNextButtonText("Next");
		// add document sub-panel

		labelTaggedDocument = new JLabel();
		TdocumentViewer = new JTextArea();
		buttonMod = new JButton();
		buttonConfirm = new JButton();
		buttonTag = new JButton();

		setLayout(new BorderLayout());
		// setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

		// ======== add document panel ========

		JPanel addDocPanel = new JPanel(new BorderLayout()); //Label and TextViewer
		JPanel bottomPanel = new JPanel(new BorderLayout()); //bottomPanel consists only of buttonPanel
		JPanel buttonPanel = new JPanel();					 //consists of three buttons
		labelTaggedDocument.setText("Tagged Text:");
		addDocPanel.add(labelTaggedDocument, BorderLayout.NORTH);

		TdocumentViewer.setEditable(false);
		TdocumentViewer.setLineWrap(true);
		TdocumentViewer.setWrapStyleWord(true);
		
		TdocumentViewer.setText("");

		documentsScrollPane = new JScrollPane(TdocumentViewer);
		documentsScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		addDocPanel.add(documentsScrollPane, BorderLayout.CENTER);
		add(addDocPanel, BorderLayout.CENTER);
		
		buttonTag.setText("Tag");
		buttonTag.addActionListener(this);;
		buttonPanel.add(buttonTag);
		
		buttonMod.setText("Modify");
		buttonMod.addActionListener(this);
		buttonPanel.add(buttonMod);

		buttonConfirm.setText("Confirm");
		buttonConfirm.addActionListener(this);
		buttonPanel.add(buttonConfirm);
	
		bottomPanel.add(buttonPanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);

	}

	/**
	 * Called by various buttons when they are activated.
	 * 
	 * @param event
	 *            The event passed along to us by the event pipeline
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == buttonMod) {
			TdocumentViewer.setEditable(true);
		}

		if (event.getSource() == buttonConfirm) {
			TdocumentViewer.setEditable(false);
		}
		if (event.getSource() == buttonTag) {
			try {
				long startTime = System.currentTimeMillis(); //determine how long tagger takes
				
				SwingWorker<String,Void> worker = new SwingWorker<String, Void>() {
				    @Override
				    public String doInBackground() {
				    	com.JavaIndexer.processing.Tagger tagger = new com.JavaIndexer.processing.Tagger(DocumentsStepPanel.Text);
				    	return tagger.procText;
				    }
				    @Override
				    public void done(){
				    	try {
							TdocumentViewer.setText(get());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
				};
				
				TdocumentViewer.setText("Please wait...");
				worker.execute();
            	long timeElapsed = (System.currentTimeMillis() - startTime);
            	System.out.println("Whole Tagger Panel is done after: " + timeElapsed + " milliseconds");
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public void focusGained(FocusEvent event) {

	}

	public void focusLost(FocusEvent event) {

	}

	/**
	 * Return a Vector of String arrays containing all the documents currently
	 * referenced by this panel.
	 * 
	 * @return a Vector of Documents
	 */

}