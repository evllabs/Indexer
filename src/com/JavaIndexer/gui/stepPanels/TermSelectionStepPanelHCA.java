package com.JavaIndexer.gui.stepPanels;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent; //import java.awt.event.ActionListener;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.math.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreeNode;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;


import cern.colt.matrix.impl.SparseDoubleMatrix2D;

import com.JavaIndexer.generics.BinarySearchTree;
import com.JavaIndexer.generics.BinarySearchTreeNode;
import com.JavaIndexer.generics.Document;
import com.JavaIndexer.generics.WordAttributes;
import com.JavaIndexer.gui.generics.StepPanel;
import com.JavaIndexer.processing.EVD;
import com.JavaIndexer.processing.Tagger;

class TreeNodeEl implements Comparable<TreeNodeEl>{
	double x;
	double y;
	String str;
	double xDist;
	
	TreeNodeEl(String str,double x,double y){
		this.str = str;
		this.x = x;
		this.y=y;
	}
	
	public void setString(String str){
		this.str=str;
	}
	
	@Override
	public int compareTo(TreeNodeEl o) {
		return 0;
	}
	
	public String toString(){
		return this.str;
	}

	public void combineWith(TreeNodeEl el) {
		this.x= (this.x + el.x)/2;
		this.y= (this.y + el.y)/2;
	}
}

/**
 * 
 * @author Nicole Pernischova
 */


public class TermSelectionStepPanelHCA extends StepPanel implements
		ActionListener {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	// ---- Global Variables ----

	private JLabel btermLabel, bclusterLabel, termsLabel, documentLabel;
	private JButton setlimits,update;
	private JComboBox bterms, bcluster;
	private JTextArea document;
	private DefaultGraphCell slider;
	private JScrollPane documentsScrollPane, termsScrollPane, graphPane;
	private String[] descriptionBT = { "Squared Euclidean", "Manhattan",
			"Chebychev", "Power", "Percent Disagreement" };
	private String[] descriptionBC = { "Nearest Neighbor", "Farthest Neighbor",
			"Unweighted pair-group average", "Weighted pair-group average",
			"Unweighted pair-group centroid", "Ward" };
	private JGraph graph;
	private JSlider distanceSlider;
	private double horizScale;
	private int yOffset = 0;
	private HashMap<String, WordAttributes> termsToIndex;
	//private ArrayList<Float> temps = new ArrayList<Float>();
	/**
	 * Default constructor
	 */
	
	public TermSelectionStepPanelHCA() {
		super("HCA");
		setNextButtonText("Next");
		
		
		document = new JTextArea();
		documentLabel = new JLabel();
		termsLabel = new JLabel();
		distanceSlider = new JSlider(JSlider.HORIZONTAL);
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(new DefaultGraphModel(),	new	DefaultCellViewFactory());
		slider = new DefaultGraphCell();
		GraphConstants.setBounds(slider.getAttributes(), new Rectangle2D.Double(100,0,4,10000));
	    GraphConstants.setBorderColor(slider.getAttributes(), Color.black);
	    GraphConstants.setOpaque(slider.getAttributes(),true);
	    
	/*	temp.add(new DefaultPort());
		slider.setSource(temp.getChildAt(0));
		
		temp = new DefaultGraphCell();
		GraphConstants.setBounds(temp.getAttributes(), new Rectangle2D.Double(50,400,0,0));
		temp.add(new DefaultPort());
		slider.setTarget(temp.getChildAt(0));*/
	    //GraphConstants.setSelectable(slider.getAttributes(), false);
		
//		GraphConstants.setBounds(slider.getAttributes(), new Rectangle2D.Double(50,0,50,100000)); 
		
		graph = new JGraph(model, view);
		graphPane = new JScrollPane(graph);
		graphPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		graphPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//	    graphPane.setViewportBorder(new LineBorder(Color.RED));
//	    graphPane.getViewport().add(graph, BorderLayout.CENTER);
//	    graphPane.getViewport().add(distanceSlider, BorderLayout.NORTH);
		
		
		bterms = new JComboBox(descriptionBT);
		bcluster = new JComboBox(descriptionBC);
		btermLabel = new JLabel();
		bclusterLabel = new JLabel();
		setlimits = new JButton();
		update = new JButton();
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
		JPanel graphPanel = new JPanel();
		graphPanel.setLayout(new BorderLayout());
		graphPanel.add(distanceSlider, BorderLayout.NORTH);
		graphPanel.add(graphPane);
		DocPanel.add(graphPanel);

		JPanel DocPanel2 = new JPanel(new BorderLayout());

		JPanel DocPanel3 = new JPanel(new FlowLayout());
		btermLabel.setText("Between-terms distance measures: ");
		DocPanel3.add(btermLabel);
		bterms.setSelectedIndex(1);
		bterms.addActionListener(this);
		DocPanel3.add(bterms);
		bclusterLabel.setText("Between-clusters distance measures: ");
		DocPanel3.add(bclusterLabel);
		bcluster.setSelectedIndex(1);
		bcluster.addActionListener(this);
		DocPanel3.add(bcluster);

		setlimits.setText("Set");
		setlimits.addActionListener(this);
		update.setText("Update");
		update.addActionListener(this);
		DocPanel2.add(setlimits, BorderLayout.EAST);
		DocPanel2.add(update, BorderLayout.WEST);
		DocPanel2.add(DocPanel3, BorderLayout.CENTER);

		add(LPanel, BorderLayout.NORTH);
		add(DocPanel, BorderLayout.CENTER);
		add(DocPanel2, BorderLayout.PAGE_END);

	}

	private void testingPurposes() {
//		writeToMemory("objectFiles/tempTermsToIndex",Tagger.termsToIndex);
//		writeToMemory("objectFiles/tempCoordMatrix",EVD.coordMatrix);
//		writeToMemory("objectFiles/tempIdxArray",TermSelectionStepPanelFrequency.idxArray);
//		writeToMemory("objectFiles/tempOriginalText",DocumentsStepPanel.Text);		
	}

	private void writeToMemory(String string,
			Object obj) {
		try{
		FileOutputStream fos = new FileOutputStream(string);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == update) {
			Object[] temp = graph.getSelectionCells();
			String higherWord = JOptionPane.showInputDialog("What word would you like to cluster this under?");
			for (Object t : temp){
				DefaultGraphCell cell = (DefaultGraphCell) t;
				if (cell.toString().equals("")){
					continue;
				}
				termsToIndex.get(cell.toString()).setHigherTerm(higherWord);
			}
			double xValue = GraphConstants.getBounds(slider.getAttributes()).getCenterX();
//			splitUpWords(xValue,finalStartNode);
			
		}
		else if (event.getSource() == setlimits) {
			try{
				document.setText(DocumentsStepPanel.Text);
				testingPurposes();
				long startTime = System.currentTimeMillis();
				yOffset=0;
				ArrayList<String> idxArray = TermSelectionStepPanelFrequency.idxArray;
				termsToIndex = Tagger.termsToIndex;
			    
				graph.getGraphLayoutCache().removeCells(graph.getGraphLayoutCache().getCells(false,false,true,true));
				BinarySearchTree<TreeNodeEl> myTree = setUpTree(idxArray.toArray(new String[0]));
				double distance = getMaxDistance(myTree.getRoot());
				horizScale = 1000.0/distance;
				writeToGui(myTree.getRoot());
				graph.updateUI();
				graphPane.updateUI();
				//clusterTree = new JTree(new DefaultMutableTreeNode());
//				calculateHCA();

			   long timeElapsed = (System.currentTimeMillis() - startTime);
			   System.out.println("HCA: " + timeElapsed + " milliseconds");
			}catch(Exception e){
				document.setText("ERROR" + e);
				e.printStackTrace();
			}

		}
	}

	private double getMaxDistance(BinarySearchTreeNode<TreeNodeEl> root) {
		if (root.isLeaf()){
			return 0;
		}
		else{
			double distance1 = getDist(root.getEl(), root.getLeft().getEl()) + getMaxDistance(root.getLeft());
			double distance2 = getDist(root.getEl(), root.getRight().getEl()) + getMaxDistance(root.getRight());
			if (distance1 > distance2){
				return distance1;
			}
			else{
				return distance2;
			}
		}
	}

	private DefaultGraphCell writeToGui(BinarySearchTreeNode<TreeNodeEl> root) {
		if (!root.isLeaf()){		    
			DefaultGraphCell cell1 = writeToGui(root.getLeft());
			DefaultGraphCell cell2 = writeToGui(root.getRight());
			
			double y = (GraphConstants.getBounds(cell1.getAttributes()).getCenterY()
						+ GraphConstants.getBounds(cell2.getAttributes()).getCenterY())/2;
			double x = GraphConstants.getBounds(cell1.getAttributes()).getMaxX() + 
						getDist(root.getEl(),((TreeNodeEl)cell1.getUserObject())) * horizScale;  
			DefaultGraphCell cell = new DefaultGraphCell(root.getEl());
			GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(x,y, 0,0));
		    GraphConstants.setBorderColor(cell.getAttributes(), Color.black);
		    GraphConstants.setOpaque(cell.getAttributes(),true);
		    cell.add(new DefaultPort());
			
			addConnection(cell, cell1);
			addConnection(cell, cell2);
			return cell;
		}
		else{
			DefaultGraphCell cell = new DefaultGraphCell(root.getEl());
			GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(0, yOffset, 100, 20));
		    GraphConstants.setBorderColor(cell.getAttributes(), Color.black);
		    GraphConstants.setOpaque(cell.getAttributes(),true);
		    DefaultPort p = new DefaultPort();
		    GraphConstants.setOffset(p.getAttributes(), new Point2D.Double(GraphConstants.PERMILLE,GraphConstants.PERMILLE/2));
		    cell.add(p);
		    yOffset+= 30;
			graph.getGraphLayoutCache().insert(cell);
			return cell;
		}
		
	}
	private void addConnection(DefaultGraphCell cell1, DefaultGraphCell cell2) {
		double x1 = GraphConstants.getBounds(cell1.getAttributes()).getCenterX();
		double x2 = GraphConstants.getBounds(cell2.getAttributes()).getCenterX();
		double y1 = GraphConstants.getBounds(cell1.getAttributes()).getCenterY();
		double y2 = GraphConstants.getBounds(cell2.getAttributes()).getCenterY();
		
		DefaultGraphCell midpoint = new DefaultGraphCell();
		midpoint.add(new DefaultPort());
		GraphConstants.setBounds(midpoint.getAttributes(), new Rectangle2D.Double(x1, y2, 0, 0));
		
	    DefaultEdge vertical = new DefaultEdge();
	    vertical.setSource(cell1.getChildAt(0));
	    vertical.setTarget(midpoint.getChildAt(0));
		
	    DefaultEdge horizontal = new DefaultEdge();
	    horizontal.setSource(midpoint.getChildAt(0));
	    horizontal.setTarget(cell2.getChildAt(0));
		
		graph.getGraphLayoutCache().insert(vertical);
		graph.getGraphLayoutCache().insert(horizontal);
		graph.getGraphLayoutCache().insert(midpoint);	
	}

	private BinarySearchTree<TreeNodeEl> setUpTree(String[] idxArray) {
		ArrayList<BinarySearchTreeNode<TreeNodeEl>> nodes = new ArrayList<BinarySearchTreeNode<TreeNodeEl>>();
		for (int i=0; i<idxArray.length; i++){	//put coord info into arrayList
			double x = termsToIndex.get(idxArray[i]).getXVal();
			double y = termsToIndex.get(idxArray[i]).getYVal();
			nodes.add(new BinarySearchTreeNode<TreeNodeEl>(new TreeNodeEl(idxArray[i],x,y)));
		}
		combineHigherTerms(nodes);
//		for (int i=0; i<nodes.size(); i++){
//			nodes.get(i).getEl().setString(generateString(nodes.get(i).getEl().list));
//		}
		while (nodes.size() != 1){
			combineSmallest(nodes);
		}
		BinarySearchTree<TreeNodeEl> tree = new BinarySearchTree<TreeNodeEl>();
		tree.setAsRoot(nodes.get(0));
		return tree;
	}

//	private String generateString(ArrayList<Integer> list) {
//		return termsToIndex[list.get(0)].getIndexTerm();
//	}

	private void combineHigherTerms(ArrayList<BinarySearchTreeNode<TreeNodeEl>> nodes) {
		for (int i=0; i<nodes.size(); i++){
			if (!termsToIndex.containsKey(nodes.get(i).getEl().str) || !termsToIndex.get(nodes.get(i).getEl().str).hasHigherTerm()){
				continue;
			}
			String str = getHighestTerm(nodes.get(i).getEl().str); 
				
			//remove all nodes with this highest term, and combine coords
			double xTotal = 0;
			double yTotal = 0;
			int count = 0;
			for (int j=0; j<nodes.size();){
				if (str.equals(getHighestTerm(nodes.get(j).getEl().str))){
					xTotal += nodes.get(j).getEl().x;
					yTotal += nodes.get(j).getEl().y;
					count++;
					nodes.remove(j);
				}
				else{
					j++;
				}
			}
			nodes.add(new BinarySearchTreeNode<TreeNodeEl>(new TreeNodeEl(str,xTotal/count,yTotal/count)));
		}
	}

	private String getHighestTerm(String str) {
		if (!termsToIndex.containsKey(str) || !termsToIndex.get(str).hasHigherTerm()){
			return str;
		}
		return getHighestTerm(termsToIndex.get(str).getHigherTerm());
	}

	private void combineSmallest(ArrayList<BinarySearchTreeNode<TreeNodeEl>> nodes) {
		double smallestDistance = Double.MAX_VALUE;
		int[] idxes = new int[2];
		for (int i=0; i<nodes.size(); i++){
			for (int j=0; j<nodes.size(); j++){
				if (i == j){
					continue;
				}
				double dist = getDist(nodes.get(i).getEl(),nodes.get(j).getEl());
				if (dist < smallestDistance){
					smallestDistance = dist;
					//ensure that [0] is always larger
					idxes[0] = (i>j) ? i : j;
					idxes[1] = (i>j) ? j : i;
				}
			}
		}
		double newX = (nodes.get(idxes[0]).getEl().x + nodes.get(idxes[1]).getEl().x)/2;
		double newY = (nodes.get(idxes[0]).getEl().y + nodes.get(idxes[1]).getEl().y)/2;
		TreeNodeEl newEl = new TreeNodeEl("",newX,newY);
		BinarySearchTreeNode<TreeNodeEl> newNode = new BinarySearchTreeNode<TreeNodeEl>(newEl, nodes.get(idxes[0]), nodes.get(idxes[1]));
		//remove old nodes and add new nodes
//		System.out.println(nodes.get(idxes[0]).getEl() + "/" + nodes.get(idxes[1]).getEl());
		nodes.remove(idxes[0]);
		nodes.remove(idxes[1]);
		nodes.add(newNode);
	}

	private double getDist(TreeNodeEl node1, TreeNodeEl node2) {
		return Math.sqrt(Math.pow(node1.x - node2.x,2) +		
				Math.pow(node1.y - node2.y,2));
	}

	private void splitUpWords(double xValue, DefaultGraphCell tempNode) {
		if (tempNode.isLeaf())
				return;
		if (GraphConstants.getBounds(tempNode.getAttributes()).getCenterX() < xValue){
			outputWords(tempNode);
			return;
		}
		splitUpWords(xValue,(DefaultGraphCell)tempNode.getChildAt(0));
		splitUpWords(xValue,(DefaultGraphCell)tempNode.getChildAt(1));

	}

	private void outputWords(DefaultGraphCell tempNode) {
		if (tempNode.isLeaf()){
			System.out.println(tempNode.getUserObject());
			return;
		}
		outputWords((DefaultGraphCell)tempNode.getChildAt(0));
		outputWords((DefaultGraphCell)tempNode.getChildAt(1));		
	}

	private void calculateHCA() {
		graph.getGraphLayoutCache().removeCells(graph.getGraphLayoutCache().getCells(false,false,true,true));
		
//		createStack();
		graph.updateUI();
		graphPane.updateUI();
	//	drawGraph();
				/*
				if (nodes[i].isRoot() && nodes[j].isRoot()){
					nodes[i].add(node)
				}
					if (clusters[j][1] == 0.0 || temp < clusters[j][0]){
						if (clusters[j][1] != 0.0){
							clusters[(int)clusters[j][0]][0] = (float) 0.0;
							clusters[(int)clusters[j][0]][1] = (float) 0.0;
						}
						clusters[i][1] = (float)temp;
						clusters[j][1] = (float)temp;
						clusters[i][0] = j;
						clusters[j][0] = i;
					}
				}
			}
		}
		//get Number of Clusters
		for (int i=0; i<clusters.length; i++){
			if (clusters[i][1] != 0.0){
				numOfClusters++;
			}
		}
		numOfClusters = numOfClusters/2;
		
		//Reinitialize "clusters" with Centroid distance measure
		for (int i=0; i<clusters.length; i++){
			if (clusters[i][1] != 0.0){
				
			}
		}
		
		
		int tempNumOfClusters = 0;
		
		*/
	}
/*
	private void createStack() {
		horizScale = 1000;
		offset = 0;
		double temp,min = 0.0;
		float[] centroid = new float[3]; //[0] is X; [1] is Y; [2] is sum of squares;
		int[] distance = new int[2];
		numberOfNodes = idxArray.length;
		ArrayList<Object> tti = new ArrayList<Object>();
		for (int i=0; i<idxArray.length; i++){
			tti.add(termsToIndex[idxArray[i]]);
		}
		cells = new ArrayList<DefaultGraphCell>();
		iterator = 0;
		while(true){
			min = 0.0;			//min will find the lowest distance between any two points
			distance[0] = 0;	//this will be one value which is the lowest distance 
			distance[1] = 0;	//this will be the other
			for (int i=0; i<tti.size()-1; i++){
				for (int j=i+1; j<tti.size(); j++){
					float[] iCoords;
					float[] jCoords;
					if (!tti.get(i).toString().startsWith("<indexterm=")){
						iCoords = (float[])tti.get(i);
					}
					else  {
						iCoords = totalCoords[((WordAttributes)tti.get(i)).getCoordIndex()].clone();
					}
					
					if (!tti.get(j).toString().startsWith("<indexterm=")){
						jCoords = (float[])tti.get(j);
					}
					else {
						jCoords = totalCoords[((WordAttributes)tti.get(j)).getCoordIndex()].clone();
					}
					temp = Math.sqrt(Math.pow(iCoords[0] - jCoords[0],2) +		
									Math.pow(iCoords[1] - jCoords[1],2));			//takes the squareroot of the distances between i and j
					if (temp == 0.0){
						distance[0] = i;					
						distance[1] = j;
						centroid[0] = iCoords[0];
						centroid[1] = iCoords[1];
						centroid[2] = 0;
						break;
					}
					if (min == 0.0 || temp < min){			//if this is not the new min, make it so
						distance[0] = i;					
						distance[1] = j;
						min = temp;
						centroid[0] = (iCoords[0] + jCoords[0])/2;
						centroid[1] = (iCoords[1] + jCoords[1])/2;
						centroid[2] = (float)temp;						
					}
				}
			}
			if (tti.size() <= 1){
				break;
			}
			DefaultGraphCell tmp = new DefaultGraphCell(centroid.clone());
			if (!tti.get(distance[1]).toString().startsWith("<indexterm=")){
				int cell2=-1;
				for (int i=0; i<cells.size(); i++){
					if (Arrays.equals((float[])(cells.get(i).getUserObject()),(float[])(tti.get(distance[1])))){
						cell2=i; break;
					}
				}
				tmp.insert(cells.get(cell2), 0);
				cells.remove(cell2);
			}
			else{
				tmp.insert(new DefaultGraphCell(tti.get(distance[1])), 0);
			}
			if (!tti.get(distance[0]).toString().startsWith("<indexterm=")){ // && !tti.get(distance[1]).toString().startsWith("<indexterm=")){
				int cell1=-1;
				for (int i=0; i<cells.size(); i++){
					if (Arrays.equals((float[])(cells.get(i).getUserObject()),(float[])(tti.get(distance[0])))){
						cell1=i; break;
					}
				}
				tmp.insert(cells.get(cell1), 0);
				cells.remove(cell1);
			}
			else{
				tmp.insert(new DefaultGraphCell(tti.get(distance[0])), 0);
			}
			
			cells.add(tmp);
				
			tti.remove(distance[1]);
			tti.remove(distance[0]);
			tti.add(centroid.clone());
			numberOfNodes++;
		}
		float[] temp1 = (float[]) ((DefaultGraphCell)cells.get(0).getRoot()).getUserObject();
		horizScale /= temp1[2];
		finalCells = new DefaultGraphCell[numberOfNodes];
		iterator = 0;			
		
		finalStartNode = duplicateTree(cells.get(0));
		
		drawGraph((DefaultGraphCell) cells.get(0).getRoot());
		graph.getGraphLayoutCache().insert(finalCells);
		graph.getGraphLayoutCache().insert(slider);
	}

	private DefaultGraphCell duplicateTree(DefaultGraphCell defaultGraphCell) {
		if (defaultGraphCell.isLeaf())
			return (DefaultGraphCell)defaultGraphCell.clone();
		DefaultGraphCell temp = new DefaultGraphCell();
		temp.add(duplicateTree((DefaultGraphCell)defaultGraphCell.getChildAt(0)));
		temp.add(duplicateTree((DefaultGraphCell)defaultGraphCell.getChildAt(1)));
		
		return temp;
	}

	private void drawGraph(DefaultGraphCell treeNode){
		if (treeNode.isLeaf()){
			createSingleCell((DefaultGraphCell) treeNode);
			return;
		}
		drawGraph((DefaultGraphCell)treeNode.getChildAt(0));
		drawGraph((DefaultGraphCell)treeNode.getChildAt(1));
		createConnection(treeNode,(DefaultGraphCell)treeNode.getChildAt(0), (DefaultGraphCell) treeNode.getChildAt(1));
		
		finalCells[iterator++] = (DefaultGraphCell) treeNode.getChildAt(0);
		treeNode.remove(0);
		finalCells[iterator++] = (DefaultGraphCell) treeNode.getChildAt(0);
		treeNode.remove(0);
		if (treeNode.isRoot()){
			finalCells[iterator++] = treeNode;
		}

		
	}
	
	private void createConnection(DefaultGraphCell cell, DefaultGraphCell child1,
			DefaultGraphCell child2) {
		float[] temp = (float[])(cell.getUserObject());
		int xdir = (int) (temp[2]*horizScale);
		int y1 = (int) GraphConstants.getBounds(child1.getAttributes()).getCenterY();
		int y2 = (int )GraphConstants.getBounds(child2.getAttributes()).getCenterY();
		int ydir = (y1+y2)/2;
		
		GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(xdir+100, ydir, 1, 1));
	    GraphConstants.setBorderColor(cell.getAttributes(), Color.black);
	    cell.add(new DefaultPort());
	    
	    DefaultGraphCell tempCell = new DefaultGraphCell();
	    tempCell.add(new DefaultPort());
	    GraphConstants.setBounds(tempCell.getAttributes(), new Rectangle2D.Double(xdir+100, y1, 1,1));
	    DefaultEdge myEdge = new DefaultEdge();
		myEdge.setSource(child1.getChildAt(0));
		myEdge.setTarget(tempCell);
		graph.getGraphLayoutCache().insert(myEdge);
	    myEdge = new DefaultEdge();
		myEdge.setSource(tempCell);
		myEdge.setTarget(cell);
		graph.getGraphLayoutCache().insert(myEdge);
		
		
		tempCell = new DefaultGraphCell();
	    tempCell.add(new DefaultPort());
	    GraphConstants.setBounds(tempCell.getAttributes(), new Rectangle2D.Double(xdir+100, y2, 1,1));
	    myEdge = new DefaultEdge();
		myEdge.setSource(child2.getChildAt(0));
		myEdge.setTarget(tempCell);
		graph.getGraphLayoutCache().insert(myEdge);
	    myEdge = new DefaultEdge();
		myEdge.setSource(tempCell);
		myEdge.setTarget(cell);
		graph.getGraphLayoutCache().insert(myEdge);
	}

	private void createSingleCell(DefaultGraphCell cell) {
		GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(0, offset, 100, 20));
	    GraphConstants.setBorderColor(cell.getAttributes(), Color.black);
	    GraphConstants.setOpaque(cell.getAttributes(),true);
	    cell.add(new DefaultPort());
	    cell.setUserObject(((WordAttributes)cell.getUserObject()).getIndexTerm());
	    offset+=50;
	}*/
}
