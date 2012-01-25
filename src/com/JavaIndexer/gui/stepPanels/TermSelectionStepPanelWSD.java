package com.JavaIndexer.gui.stepPanels;
import cern.colt.matrix.impl.SparseDoubleMatrix3D;

import com.JavaIndexer.generics.ParticularWord;
import com.JavaIndexer.generics.WordAttributes;
import com.JavaIndexer.gui.generics.StepPanel;
import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;

import java.awt.event.ActionEvent; //import java.awt.event.ActionListener;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.StyledDocument;

import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.*;

import com.JavaIndexer.gui.JavaIndexerGUI;
import com.JavaIndexer.processing.EVD;
import com.JavaIndexer.processing.Tagger;

/**
 * 
 * @author Nicole Pernischova
 */
/*
class Drawing2 extends JPanel {
	private static final long serialVersionUID = 1L;
	private int[][] coordinates;
	private int count = 0;
	boolean paint = false;
	public void drawIt (double[][] coords ){
		coordinates = new int[coords.length][2];
		for (int i=0; i<coords.length; i++){
			coordinates[count][0] = (int)(coords[i][0] * 100 * 20);
			coordinates[count][1] = (int)(coords[i][1] * 100 * 20);
			count++;
		}
		paint = true;
	}
	@Override public void paint(Graphics g){
		if (paint == true){
			g.drawLine(225, 75, 225, 475);
			g.drawLine(25, 275, 425, 275);
			try{			    
				for (int i=25; i<=425;){
					g.drawLine(i, 275 - 5, i, 275 + 5);
					i=i+20;
				}
				for (int i=75; i<=475;){
					g.drawLine(225-5, i, 225 + 5, i);
					i=i+20;
				}
				for (int i = 0; i<coordinates.length; i++){
					g.drawOval(coordinates[i][0] + 225 - 2, 275 - coordinates[i][1] - 2,
						4, 4);
	
				}
			}catch(Exception e){
				
			}
		}
	}
}*/
public class TermSelectionStepPanelWSD extends StepPanel implements
		ActionListener {

	/**
     *
     */
	private boolean check = false, check2 = false; 
	private static final long serialVersionUID = 1L;
	// ---- Global Variables ----
	private DefaultListModel myModel;
	private JGraph graph;
	private JLabel termsLabel, contextLabel, graphLabel, popupLabel, popupLabel2,
			coordPopupLabel, XMinL, XMaxL, YMinL, YMaxL;
	private JTextField XMinTF, XMaxTF, YMinTF, YMaxTF;
	private JButton set, split, bestFit, changeCoords;
	//private JTextArea document;
	private JList context, terms;
	private JPanel graphPanel;
	private JScrollPane contextScrollPane, termsScrollPane;
	private JComboBox clusters, clusters2;
	private PopupFactory factory;
	private Popup popup, popup2;
	private String[] clusterList = {};
	private String originalText;
	private HashMap<String, WordAttributes> termsToIndex;
	private double[][] termCoords;
	private float[][] coordMatrix;
	private ArrayList<String> idxArray;
	
    

	// private JGraphModelAdapter m_jgAdapter;

	/**
	 * Default constructor.
	 */
	public TermSelectionStepPanelWSD() {
		super("WSD");
		setNextButtonText("Next");
		
		//labels
		JPanel LPanel = new JPanel();
		LPanel.setLayout(new GridLayout(1, 3));
		termsLabel = new JLabel("terms to index");
		LPanel.add(termsLabel);
		contextLabel = new JLabel("Text");
		LPanel.add(contextLabel);
		graphLabel = new JLabel("Graph");
		LPanel.add(graphLabel);
		
		//termsPane
		terms = new JList();
		terms.addListSelectionListener(new ListSelectionListener(){
		    public void valueChanged(ListSelectionEvent e) {
		    	myModel.removeAllElements();
		        JList termsList = (JList)e.getSource();
		        boolean modified = e.getValueIsAdjusting();
		        if (modified == false && termsList.getMinSelectionIndex() != -1){		        		
		        	int num = termsToIndex.get(idxArray.get(termsList.getMinSelectionIndex())).getCount();
		        	for (int i=0; i<num; i++){
		        		myModel.addElement(getString(termsToIndex.get(idxArray.get(termsList.getMinSelectionIndex())).getStartIndex(i)));
		        	}
				    generateGraph();
		        }
		    }
		});
		termsScrollPane = new JScrollPane(terms);
		termsScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		termsScrollPane.setPreferredSize(new Dimension(200,50));
		
		//contextPanel
		myModel = new DefaultListModel();
		context = new JList(myModel);
		context.addMouseListener(new MouseAdapter(){
	        public void mouseClicked(MouseEvent me) {
					//JList contextList = (JList) theEvent.getSource();
	        		if (me.getClickCount() == 2) {
	        			String theText = getTheText(terms.getSelectedIndex(), context.getSelectedIndex());
	        			popupLabel.setText(theText);
	        			factory = PopupFactory.getSharedInstance();	
	        			JPanel temp = new JPanel();
	        			temp.add(popupLabel);
	        			JScrollPane popupPane = new JScrollPane(temp);   
	        			popupPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        			popupPane.setPreferredSize(new Dimension(500,250));
	        			popup = factory.getPopup(null, popupPane, me.getLocationOnScreen().x, me.getLocationOnScreen().y);
	        			popup.show();
	        			check = true;
	        		}
	        		else if (me.getClickCount() == 1 && check) {
	        			popup.hide();
	        		}
	        		Object[] initialCells = (Object[]) graph.getGraphLayoutCache().getCells(false,true,false,false);
	        		int[] selectedIndices = context.getSelectedIndices();
	        		DefaultGraphCell[] finalCells = new DefaultGraphCell[selectedIndices.length];
	        		int incIdx = 0;
	        		
	        		for (int i=0; i<initialCells.length; i++){
	        			if (initialCells[i].toString() == null){
	        				continue;
	        			}
	        			for (int j=0; j<selectedIndices.length; j++){
	        				if (initialCells[i].toString().compareTo(Integer.toString(selectedIndices[j])) == 0){
	        					finalCells[incIdx] = (DefaultGraphCell) initialCells[i];
	        					incIdx++;
	        				}
	        			}
	        		}
	        		graph.setSelectionCells(finalCells);
	        	}
	      });
		context.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e){
			}
			public void focusLost(FocusEvent e){
				if (check){
					popup.hide();
				}
			}
		});

		
		contextScrollPane = new JScrollPane(context);
		contextScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		contextScrollPane.setPreferredSize(new Dimension(300,50));
		
		//graph
		graphPanel = new JPanel();
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,	new	DefaultCellViewFactory());
		graph = new JGraph(model, view);
		popupLabel = new JLabel();
		popupLabel2 = new JLabel();
		
		XMinL = new JLabel();
		XMaxL = new JLabel();
		YMinL = new JLabel();
		YMaxL = new JLabel();
		XMinTF = new JTextField(2);
		XMaxTF = new JTextField(2);
		YMinTF = new JTextField(2);
		YMaxTF = new JTextField(2);		
		
		bestFit = new JButton();
		changeCoords = new JButton();

		graphPanel=new JPanel(new BorderLayout());
		graphPanel.setPreferredSize(new Dimension(410,50));
		JPanel coordPanel = new JPanel();
		coordPanel.setLayout(new GridLayout(2,5));
		XMinL.setText("XMin");
		XMaxL.setText("XMax");
		YMinL.setText("YMin");
		YMaxL.setText("YMax");
		bestFit.setText("BestFit");
		bestFit.addActionListener(this);

		changeCoords.setText("Update");
		changeCoords.addActionListener(this);
		coordPanel.add(XMinL);
		coordPanel.add(XMaxL);
		coordPanel.add(YMinL);
		coordPanel.add(YMaxL);
		coordPanel.add(bestFit);
		coordPanel.add(XMinTF);
		coordPanel.add(XMaxTF);
		coordPanel.add(YMinTF);
		coordPanel.add(YMaxTF);
		coordPanel.add(changeCoords);
		graphPanel.add(graph);
		graphPanel.add(coordPanel, BorderLayout.PAGE_END);
		
		//bottom buttons
		JPanel buttonPanel = new JPanel();
		set = new JButton("set");
		set.addActionListener(this);
		split = new JButton("split");
		split.addActionListener(this);
		buttonPanel.add(set);
		buttonPanel.add(split);
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));


//		JPanel SPanel = new JPanel(new GridLayout(1, 2));
//
//		JPanel CPanel = new JPanel(new BorderLayout());
//		JPanel CPanelW = new JPanel(new GridLayout(2, 1));
//		JPanel CPanelC = new JPanel(new GridLayout(2, 1));
//		JPanel CPanelE = new JPanel(new GridLayout(2, 1));
//		newClusterLabel.setText("New cluster name: ");
//		cluster.setText(" ");
//		addcluster.setText("Add");
//		addcluster.addActionListener(this);
//		removeClusterLabel.setText("Clusters: ");
//		clusters.addActionListener(this);
//		deletecluster.setText("Delete");
//		deletecluster.addActionListener(this);
//		CPanelW.add(newClusterLabel);
//		CPanelC.add(cluster);
//		CPanelE.add(addcluster);
//		CPanelW.add(removeClusterLabel);
//		CPanelC.add(clusters);
//		CPanelE.add(deletecluster);
//
//		CPanel.add(CPanelW, BorderLayout.WEST);
//		CPanel.add(CPanelC, BorderLayout.CENTER);
//		CPanel.add(CPanelE, BorderLayout.EAST);
//
//		JPanel PPanel = new JPanel(new BorderLayout());
//
//		JPanel PPanelC = new JPanel();
//		PPanelC.setLayout(new BoxLayout(PPanelC, BoxLayout.Y_AXIS));
//
//		JPanel PPanelB = new JPanel();
//		PPanelB.setLayout(new BorderLayout());
//
//		JPanel PPanelB2 = new JPanel();
//		PPanelB2.setLayout(new FlowLayout());
//
//		selectPointLabel
//				.setText(" Select a point to add or remove from a cluster.");
//		PPanelC.add(selectPointLabel);
//
//		addPointLabel.setText("Cluster:");
//		clusters2.addActionListener(this);
//		addpoint.setText("Add");
//		addpoint.addActionListener(this);
//		removepoint.setText("Remove");
//		removepoint.addActionListener(this);
//
//		PPanelB.add(addPointLabel, BorderLayout.WEST);
//		PPanelB.add(clusters2, BorderLayout.CENTER);
//
//		PPanelB2.add(addpoint);
//		PPanelB2.add(removepoint);
//		PPanelB.add(PPanelB2, BorderLayout.EAST);
//		PPanelC.add(PPanelB);
//		PPanel.add(PPanelC, BorderLayout.CENTER);
//
//		SPanel.add(CPanel);
//		SPanel.add(PPanel);

		
		//combine Panes
		JPanel allPanes = new JPanel(new BorderLayout());
		allPanes.add(termsScrollPane,BorderLayout.LINE_START);
		allPanes.add(contextScrollPane,BorderLayout.CENTER);
		allPanes.add(graphPanel,BorderLayout.LINE_END);
		
		add(LPanel, BorderLayout.NORTH);
		
		add(allPanes, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);
		testingPurposes();
	}	
    
	private void testingPurposes() {
//		FileInputStream fis = new FileInputStream("objectFiles/tempTermsToIndex");
//		ObjectInputStream ois = new ObjectInputStream(fis);
//		Tagger.termsToIndex = (HashMap<String, WordAttributes>) ois.readObject();
//		ois.close();
//		fis.close();
//		Tagger.termsToIndex = 
//			
//		Tagger.termsToIndex = (HashMap<String, WordAttributes>) readFromMemory("objectFiles/tempTermsToIndex");
//		EVD.coordMatrix = (float[][]) readFromMemory("objectFiles/tempCoordMatrix");
//		TermSelectionStepPanelFrequency.idxArray=(ArrayList<String>) readFromMemory("objectFiles/tempIdxArray");
//		DocumentsStepPanel.Text=(String) readFromMemory("objectFiles/tempOriginalText");		
//		
	}

	private Object readFromMemory(String string) {
		try{
			FileInputStream fis = new FileInputStream(string);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			ois.close();
			fis.close();
			return obj;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == set) {
			try{
				graph.getGraphLayoutCache().removeCells(graph.getGraphLayoutCache().getCells(false,false,true,true));
				termsToIndex = Tagger.termsToIndex;
				coordMatrix = EVD.coordMatrix;
				idxArray = TermSelectionStepPanelFrequency.idxArray;
				originalText = DocumentsStepPanel.Text;		
			    
				com.JavaIndexer.processing.WSD wsd = 
			    		new com.JavaIndexer.processing.WSD(coordMatrix, termsToIndex, idxArray, originalText);
			    
			    String[] theTerms = new String[idxArray.size()];
			    for (int i=0; i<theTerms.length; i++)
			    	theTerms[i] = termsToIndex.get(idxArray.get(i)).getIndexTerm();
			    
			    terms.setListData(theTerms);
			}catch(Exception e){
				System.out.println("ERROR: " + e);
				e.printStackTrace();
			}
			
		}
		else if (event.getSource() == split){
			String oldWord = termsToIndex.get(idxArray.get(terms.getSelectedIndex())).getIndexTerm();
			String newWord = JOptionPane.showInputDialog("Enter new word");
			String newTag = JOptionPane.showInputDialog("Enter new tag");
			if (!termsToIndex.containsKey(newWord)){
				termsToIndex.put(newWord, new WordAttributes(newWord, oldWord, newTag));
			}
			Object[] selectedCells = graph.getSelectionCells();
			int[] instances = new int[selectedCells.length];
			for (int i=0; i<selectedCells.length; i++){
				DefaultGraphCell cell = (DefaultGraphCell) selectedCells[i];
				instances[i] = Integer.parseInt(cell.getUserObject().toString());
			}
			Arrays.sort(instances);
			for (int i=instances.length-1; i>=0; i--){
				ParticularWord term = termsToIndex.get(idxArray.get(terms.getSelectedIndex())).popInstance(instances[i]);
				termsToIndex.get(newWord).addInstance(term);
			}
			termsToIndex.get(newWord).makeIndexed();
			idxArray.add(newWord);
			Collections.sort(idxArray);
		}
		else if (event.getSource() == bestFit){
			XMinTF.setText("");
			XMaxTF.setText("");
			YMinTF.setText("");
			YMaxTF.setText("");
			generateGraph();
		}
		else if (event.getSource() == changeCoords){
			generateGraph();
		}
	}
	private String getString(int startIndex) {
		int endIndex = startIndex;
		for (int i=0; i<2; i++){
			startIndex = originalText.lastIndexOf(' ', startIndex-1);
		}
		for (int i=0; i<3; i++){
			endIndex = originalText.indexOf(' ', endIndex+1);
		}
		return originalText.substring(startIndex,endIndex);
	}
	
	private void generateGraph(){
		//remove previous graph info
		graph.getGraphLayoutCache().removeCells(graph.getGraphLayoutCache().getCells(false,false,true,true));
		
		int termIdx = terms.getSelectedIndex();
		int termCount = termsToIndex.get(idxArray.get(termIdx)).getCount();
		double XAxisPos, YAxisPos;
		
		//termCoords is all instances of word
		termCoords = new double[termCount][2];
		for (int i=0; i<termCount; i++){
			termCoords[i][0] = termsToIndex.get(idxArray.get(termIdx)).getWSDXValue(i);
			termCoords[i][1] = termsToIndex.get(idxArray.get(termIdx)).getWSDYValue(i);
		}
		if (XMinTF.getText().equals("") || XMaxTF.getText().equals("") || 
				YMinTF.getText().equals("") || YMaxTF.getText().equals("")){
			//find best coords
			termCoords = bestFitCoords(termCoords);
			XAxisPos = 0.5;
			YAxisPos = 0.5;
		}
		else {
			termCoords = boundedCoords(termCoords);
			XAxisPos = 1 - (-Double.parseDouble(YMinTF.getText()) / (Double.parseDouble(YMaxTF.getText())-Double.parseDouble(YMinTF.getText())));
			YAxisPos = -Double.parseDouble(XMinTF.getText()) / (Double.parseDouble(XMaxTF.getText())-Double.parseDouble(XMinTF.getText()));
		}
		

		DefaultEdge YAxis = new DefaultEdge();
		DefaultGraphCell temp = new DefaultGraphCell();
		GraphConstants.setBounds(temp.getAttributes(), new Rectangle2D.Double(400*YAxisPos,0,0,0));
		temp.add(new DefaultPort());
		YAxis.setSource(temp.getChildAt(0));
		
		temp = new DefaultGraphCell();
		GraphConstants.setBounds(temp.getAttributes(), new Rectangle2D.Double(400*YAxisPos,400,0,0));
		temp.add(new DefaultPort());
		YAxis.setTarget(temp.getChildAt(0));
		GraphConstants.setSelectable(YAxis.getAttributes(), false);		
		
		DefaultEdge XAxis = new DefaultEdge();
		temp = new DefaultGraphCell();
		GraphConstants.setBounds(temp.getAttributes(), new Rectangle2D.Double(0,400*XAxisPos,0,0));
		temp.add(new DefaultPort());
		XAxis.setSource(temp.getChildAt(0));
		
		temp = new DefaultGraphCell();
		GraphConstants.setBounds(temp.getAttributes(), new Rectangle2D.Double(400,400*XAxisPos,0,0));
		temp.add(new DefaultPort());
		XAxis.setTarget(temp.getChildAt(0));
	    GraphConstants.setSelectable(XAxis.getAttributes(), false);

		DefaultGraphCell[] cells = new DefaultGraphCell[termCount];
		for (int i=0; i<termCount; i++){
			cells[i] = new DefaultGraphCell(i);
		    GraphConstants.setBounds(cells[i].getAttributes(), new Rectangle2D.Double(termCoords[i][0],termCoords[i][1],10,10));
		    GraphConstants.setGradientColor(cells[i].getAttributes(), Color.red);
		    GraphConstants.setBackground(cells[i].getAttributes(), Color.red);
		    GraphConstants.setBorderColor(cells[i].getAttributes(), Color.black);
		    GraphConstants.setOpaque(cells[i].getAttributes(), true);
		}
		
	    graph.getGraphLayoutCache().insert(YAxis);
	    graph.getGraphLayoutCache().insert(XAxis);
	    graph.getGraphLayoutCache().insert(cells);
		graph.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e){
			}
			public void focusLost(FocusEvent e){
				if (check2){
					popup2.hide();
				}
			}
		});
	    graph.addMouseListener(new MouseAdapter(){
	        public void mouseClicked(MouseEvent me) {
					if (graph.getSelectionCells().length == 1){
						DefaultGraphCell tempCell = (DefaultGraphCell)graph.getSelectionCell();
						double XPopupCoord = termsToIndex.get(idxArray.get(terms.getSelectedIndex())).getWSDXValue(Integer.parseInt(tempCell.getUserObject().toString()));
						double YPopupCoord = termsToIndex.get(idxArray.get(terms.getSelectedIndex())).getWSDYValue(Integer.parseInt(tempCell.getUserObject().toString()));
						
	        			popupLabel2.setText("<HTML>" + Double.toString(XPopupCoord)+ "<BR>" + Double.toString(YPopupCoord) + "</HTML>");
	        			factory = PopupFactory.getSharedInstance();	
	        			popup2 = factory.getPopup(null, popupLabel2, me.getLocationOnScreen().x, me.getLocationOnScreen().y);
	        			popup2.show();
	        			check2 = true;
	        		}
	        		else if (check2) {
	        			popup2.hide();
	        		}
	        }
	    });
	    graph.addGraphSelectionListener(new GraphSelectionListener() {
	    	
	    	public void valueChanged(GraphSelectionEvent e){
	    		if (check2){
	    			popup2.hide();
	    		}
	    		Object[] objCells = graph.getSelectionCells();
	    		if (objCells.length ==0){
	    			context.removeSelectionInterval(0, context.getVisibleRowCount());
	    			return;
	    		}
	    			
	    		DefaultGraphCell[] tempCells = new DefaultGraphCell[objCells.length];
	    		for (int i=0; i<tempCells.length; i++){
	    			tempCells[i] = (DefaultGraphCell) objCells[i];
	    		}
	    		int tempIndices[] = new int [tempCells.length];
	    		for (int i=0; i<tempCells.length; i++){
	    			tempIndices[i] = Integer.parseInt(tempCells[i].getUserObject().toString());
	    		}
	    		context.setSelectedIndices(tempIndices);
	    		graphPanel.updateUI();
	    		if (tempIndices.length == 1){
	    			System.out.println(termCoords[tempIndices[0]][0]);
	    			System.out.println(termCoords[tempIndices[0]][1]);
	    			
	    		}
	    	}
	    });
		graph.updateUI();
		graphPanel.updateUI();	
	}	
	private String getSearchTerm(String wholeWord){
		return wholeWord.substring(wholeWord.indexOf("<searchTerm=")+12,wholeWord.indexOf("><tag="));
	}
	private String getTag(String wholeWord){
		return wholeWord.substring(wholeWord.indexOf("<tag=")+5,wholeWord.length()-1);
	}
	private double[][] bestFitCoords(double[][] termCoords){
		double largestX=0, largestY=0, XFactor, YFactor;
		//find largest X and Y value
		for (int i=0; i<termCoords.length; i++){
			if (Math.abs(termCoords[i][0]) > largestX){
				largestX = Math.abs(termCoords[i][0]);
			}
			if (Math.abs(termCoords[i][1]) > largestY){
				largestY = Math.abs(termCoords[i][1]);
			}
		}
		if (largestX != 0){
			XFactor = 200/(largestX);
		}else{
			XFactor = 0;
		}
		if (largestY != 0){
			YFactor = 200/(largestY);
		}else{
			YFactor = 0;
		}		
		
		for (int i=0; i<termCoords.length; i++){
			termCoords[i][0] = (termCoords[i][0] * XFactor) + 200;
			termCoords[i][1] = 200 - termCoords[i][1] * YFactor;
		}
		XMinTF.setText(Double.toString(-largestX));
		XMaxTF.setText(Double.toString(largestX));
		YMinTF.setText(Double.toString(-largestY));
		YMaxTF.setText(Double.toString(largestY));
		return termCoords;	
	}
	public double[][] boundedCoords(double[][] termCoords){
		double XOrigin = 400 * (-Double.parseDouble(XMinTF.getText()) / (Double.parseDouble(XMaxTF.getText())-Double.parseDouble(XMinTF.getText())));
		double YOrigin = 400 * (-Double.parseDouble(YMinTF.getText()) / (Double.parseDouble(YMaxTF.getText())-Double.parseDouble(YMinTF.getText())));
		double XDist = Double.parseDouble(XMaxTF.getText()) - Double.parseDouble(XMinTF.getText());
		double YDist = Double.parseDouble(YMaxTF.getText()) - Double.parseDouble(YMinTF.getText());
		
		for (int i=0; i<termCoords.length; i++){
			termCoords[i][0] = (termCoords[i][0]/XDist) * 400 + XOrigin;
			termCoords[i][1] = YOrigin - (termCoords[i][1]/YDist) * 400;
		}
		return termCoords;
	}
	private String getTheText(int termsIdx, int contextIdx) {
		int textIdx = termsToIndex.get(idxArray.get(termsIdx)).getStartIndex(contextIdx);
		StringBuffer finalString = new StringBuffer();
		finalString.append("<html>");
		int startIdx = textIdx-200;
		while (!Character.isWhitespace(originalText.charAt(startIdx))){
			startIdx--;
		}
		int endIdx = textIdx+200;
		while (!Character.isWhitespace(originalText.charAt(endIdx))){
			endIdx++;
		}
		int breakIdx=0;
		for (int i=startIdx; i<endIdx; i++){
			if (breakIdx > 40 && Character.isWhitespace(originalText.charAt(i))){
				finalString.append("<br />");
				breakIdx = 0;
			}
			else if (i == textIdx){
				finalString.append("<font color = red><b>" + termsToIndex.get(idxArray.get(termsIdx)).getIndexTerm() + "</b></font> ");
				breakIdx += termsToIndex.get(idxArray.get(termsIdx)).getIndexTerm().length();
				i += termsToIndex.get(idxArray.get(termsIdx)).getIndexTerm().length();
			}
			else {
				finalString.append(originalText.charAt(i));
				breakIdx++;
			}
		}
		return finalString.toString();
	}
}
