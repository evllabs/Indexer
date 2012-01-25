package com.JavaIndexer.gui.stepPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Vector;
import java.util.Scanner;


import javax.swing.*;
import javax.swing.border.*;

import com.JavaIndexer.generics.Document;
import com.JavaIndexer.gui.generics.StepPanel;

/**
 * This panel represents a single step in a multi-step process, both graphically
 * and logically. It encapsulates all the necessary graphical elements and
 * program logic needed for this step, and provides access methods for necessary
 * data to be passed along to the next step. Specifically, it handles Documents
 * processing for the JavaIndexer program.
 * 
 * Code modified from jgaap project by Nicole Pernischova
 * @author Chuck Liddell Created 11/12/08 TODO: Implement saving once the new
 *         package system allows for references to the needed classes TODO: Add
 *         some more commenting
 */
public class DocumentsStepPanel extends StepPanel implements ActionListener,
        FocusListener {

	public static String Text;
    /**
     *
     */
    private static final long   serialVersionUID = 1L;

    // ---- Global Variables ----
    private JFileChooser        chooseFile;
    private JTextArea           documentViewer;
    private JLabel              labelDocument, errorMsg;
    private JTextField          docTitleField, filePathField;
    private JScrollPane         documentsScrollPane;
   

    private JButton             browseButton, viewButton;
    
    /**
     * Data structure to store the Document information.
     */
    protected Vector<Document> data = new Vector<Document>();

   

    /**
     * Default constructor.
     */
    public DocumentsStepPanel() {
        super("Documents");

        documentViewer = new JTextArea();
        labelDocument = new JLabel();
        docTitleField = new JTextField();
        filePathField = new JTextField();
        browseButton = new JButton();
        chooseFile = new JFileChooser();
        errorMsg = new JLabel();
        viewButton = new JButton();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

        // ======== documentsTable panel ========
        JPanel docsPanel = new JPanel(new BorderLayout());

        documentViewer.setEditable(false);
        documentViewer.setLineWrap(true);
        documentViewer.setWrapStyleWord(true);
        
        documentsScrollPane = new JScrollPane(documentViewer);
        documentsScrollPane.setVerticalScrollBarPolicy(
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        docsPanel.add(documentsScrollPane, BorderLayout.CENTER);

        add(docsPanel, BorderLayout.CENTER);

        // ======== add document panel ========
        Border lineBorder = BorderFactory.createLineBorder(Color.black);
        Border addDocBorder = BorderFactory.createTitledBorder(lineBorder,
                "Select Document");
        JPanel addDocPanel = new JPanel(new GridBagLayout());
        addDocPanel.setBorder(addDocBorder);
        GridBagConstraints constraints = new GridBagConstraints();

       
        labelDocument.setText("File: ");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        addDocPanel.add(labelDocument, constraints);

        filePathField.setText("");
        filePathField.addFocusListener(this);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2; // two columns wide
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        addDocPanel.add(filePathField, constraints);

        browseButton.setText("Browse");
        browseButton.addActionListener(this);
        constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = 0;
        addDocPanel.add(browseButton, constraints);

        errorMsg.setText("Error messages go here.");
        errorMsg.setForeground(Color.red);
        errorMsg.setVisible(false);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        addDocPanel.add(errorMsg, constraints);        

        viewButton.setText("View");
        viewButton.addActionListener(this);
        constraints.gridx = 5;
        constraints.gridy = 2;
        addDocPanel.add(viewButton, constraints);
        

        add(addDocPanel, BorderLayout.SOUTH);

       
    }

    /**
     * Called by various buttons when they are activated.
     * 
     * @param event
     *            The event passed along to us by the event pipeline
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == browseButton) {
            int returnVal = chooseFile.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooseFile.getSelectedFile();
                filePathField.setText(file.toString());
            	try{
            		//read file and write to text file and String object file
            		FileInputStream fis = new FileInputStream(file);
        			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        			int x;
                	StringBuffer sb = new StringBuffer();
            		while (true){
            			x = br.read();
            			if (x == -1)
            				break;
            			else{
            				sb.append((char) x);
            			}
            		}
            		br.close();
            		fis.close();
////            		FileOutputStream fos = new FileOutputStream("objectFiles/originalText.ot");
////        			ObjectOutputStream oos = new ObjectOutputStream(fos);
//        			oos.writeObject(sb.toString());
//        			oos.close();
//        			fos.close();
            		Text = sb.toString();
            		System.out.println("Finished");
            	}
            	catch(Exception e){
            		System.out.println("Error: cannot open source file");
            		e.printStackTrace();
            	}
                
            }
        }

        if (event.getSource() == viewButton) {
            // add document to table
            if (docTitleField.getText().equals(
                    "Enter document title here (optional)...")) {
                docTitleField.setText("");
            }
            addDocument( new Document( filePathField.getText()));
            

            // clear the add document panel fields
            //docTitleField.setText("Enter document title here (optional)...");
            //filePathField.setText("Enter file name here...");
            //viewButton.setEnabled(false);
        }

        

    }
    
    /**
     * Adds a Document to this StepPanel. Documents can be retrieved from this
     * panel by using the 'getDocuments()' method.
     * 
     * @param newDocument the Document to be added
     */
    public void addDocument( Document newDocument ){
    	data.add( newDocument );
        //fireTableRowsInserted( data.size() - 2, data.size() - 1);
    	//documentsTableModel.addDocument( newDocument );
    	String t = newDocument.getProcessedText();
    	documentViewer.setText(t);
    }

    /**
     * Adds a Document to this StepPanel. Documents can be retrieved from this
     * panel by using the 'getDocuments()' method.
     * 
     * @param title
     *            the title of the document
     * @param filePath
     *            the file path to the document
     * @param authorName
     *            the name of the document's author
     */
    public void addDocument( String filePath) {
    	//String t = Document.getProcessedText();
    	documentViewer.setText("something will be here");
    }
    
    /**
     * Remove all current documents from this panel.
     */
    public void clearDocuments() {
        documentViewer.setText("");
    }

    public void focusGained(FocusEvent event) {
        if (event.getSource() == docTitleField) {
            docTitleField.setText("");
        }

        if (event.getSource() == filePathField) {
            filePathField.setText("");
        }
    }

    public void focusLost(FocusEvent event) {

    }
    
   
   
    
    /**
     * Returns a new instance of Vector<Document> that contains the Documents stored in this table model.
     * 
     * @return 
     * 		A copy (new instance) of the list of Documents stored in this table model
     */
    public Collection<Document> getDocumentsCopy(){
    	return new Vector<Document>( data );
    }

   
}
