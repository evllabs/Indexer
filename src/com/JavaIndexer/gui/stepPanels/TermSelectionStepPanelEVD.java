package com.JavaIndexer.gui.stepPanels;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import com.JavaIndexer.generics.WordAttributes;
import com.JavaIndexer.gui.JavaIndexerGUI;
import com.JavaIndexer.gui.generics.StepPanel;
import com.JavaIndexer.processing.TFIDF;
import com.JavaIndexer.processing.Tagger;

/**
 * 
 * @author nicole.pernischova
 */
class Drawing extends JPanel {
	private static final long serialVersionUID = 1L;
	private int[][] coordinates;
	private int count = 0;
	boolean paint = false;
	public void drawIt (float[][] eVDCoords ){
		coordinates = new int[eVDCoords.length][2];
		for (int i=0; i<eVDCoords.length; i++){
			coordinates[count][0] = (int)(eVDCoords[i][0] * 1000 * 20);
			coordinates[count][1] = (int)(eVDCoords[i][1] * 1000 * 20);
			count++;
		}
		paint = true;
	}
	@Override public void paint(Graphics g){
		if (paint == true){
			String indexTerm="";
			g.drawLine(225, 75, 225, 475);
			g.drawLine(25, 275, 425, 275);
			try{
//				FileInputStream fis = new FileInputStream("objectFiles/termsToIndex.tti");
//			    ObjectInputStream ois = new ObjectInputStream(fis);
			    HashMap<String, WordAttributes> termsToIndex = Tagger.termsToIndex;
			    
//			    FileInputStream fis2 = new FileInputStream("objectFiles/idxArray.idx");
//			    ObjectInputStream ois2 = new ObjectInputStream(fis2);
			    ArrayList<String> idxArray = TermSelectionStepPanelFrequency.idxArray;
			    
				for (int i=25; i<=425;){
					g.drawLine(i, 275 - 5, i, 275 + 5);
					i=i+20;
				}
				for (int i=75; i<=475;){
					g.drawLine(225-5, i, 225 + 5, i);
					i=i+20;
				}
				for (int i = 0; i<coordinates.length; i++){
//					g.drawOval(coordinates[i][0] + 225 - 2, 275 - coordinates[i][1] - 2,
		//				4, 4);
					indexTerm=termsToIndex.get(idxArray.get(i)).getIndexTerm();
					g.drawString(indexTerm, coordinates[i][0] + 225, 275 - coordinates[i][1]);
	
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
public class TermSelectionStepPanelEVD extends StepPanel implements
		ActionListener {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	// ---- Global Variables ----

	private JLabel thresholdLabel, termsLabel, documentLabel;
	private JTextField threshold;
	private JButton setlimits;
	private JTextArea document;
	private Drawing terms;
	private JScrollPane documentsScrollPane, termsScrollPane;

	/**
	 * Default constructor.
	 */
	public TermSelectionStepPanelEVD() {
		super("EVD");
		setNextButtonText("Next");

		document = new JTextArea();
		terms = new Drawing();
		documentLabel = new JLabel();
		termsLabel = new JLabel();

		thresholdLabel = new JLabel();
		threshold = new JTextField();
		setlimits = new JButton();
		
		document.setEditable(false);
		document.setLineWrap(true);
		document.setWrapStyleWord(true);
		
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
		DocPanel.add(terms);

		JPanel DocPanel2 = new JPanel(new BorderLayout());

		JPanel DocPanel3 = new JPanel(new FlowLayout());

		thresholdLabel.setText("Threshold number of dimensions: ");
		DocPanel3.add(thresholdLabel);
		threshold.setText(" 100 ");
		DocPanel3.add(threshold);

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
				
				long startTime = System.currentTimeMillis();
			    
			    com.JavaIndexer.processing.EVD evd = 
			    		new com.JavaIndexer.processing.EVD(TFIDF.covarMatrix,Tagger.termsToIndex);
			    
			    terms.drawIt(evd.EVDCoords);
			    //terms.paint = true;
			    terms.repaint();
			    
//			    for (int i = 0; i<evd.EVDCoords.length; i++){
//			    	terms.coordinates(evd.EVDCoords[i][0], evd.EVDCoords[i][1]);
//			    	terms.repaint();
//			    }
//			    System.out.print(evd.EVDCoords[0][0]);
			    document.setText(evd.EVDlist.toString());
			    long timeElapsed = (System.currentTimeMillis() - startTime);
			    System.out.println("EVD: " + timeElapsed + " milliseconds");
			}catch(Exception e){
				document.setText("ERROR" + e);
			}

		}
		
/*		if (event.getSource() == setlimits) {
			String original = "";
			try{
				Scanner read = new Scanner(new File("sourceFile.txt"));
				while (read.hasNextLine())
					original = original + read.nextLine() + '\n';
				
			}catch (Exception e){}
			document.setText(original);
			
			try{
				FileInputStream fis = new FileInputStream("covarMatrix.cm");
			    ObjectInputStream ois = new ObjectInputStream(fis);;
			    double [][] cm = (double[][])ois.readObject();
			    
			    fis.close();
			    ois.close();
			    
			    com.JavaIndexer.processing.EVD evd = 
			    		new com.JavaIndexer.processing.EVD(cm);
			    terms.setText(evd.EVDlist);
			}catch(Exception e){
				terms.setText("ERROR");
			}
			
		}
		*/
	}

}
