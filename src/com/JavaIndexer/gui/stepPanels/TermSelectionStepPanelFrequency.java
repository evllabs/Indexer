package com.JavaIndexer.gui.stepPanels;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Arrays;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent; //import java.awt.event.ActionListener;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import com.JavaIndexer.generics.Document;
import com.JavaIndexer.generics.WordAttributes;
import com.JavaIndexer.gui.JavaIndexerGUI;
import com.JavaIndexer.gui.generics.StepPanel;
import com.JavaIndexer.processing.Tagger;

import edu.stanford.nlp.util.ArrayUtils;

/**
 * 
 * @author nicole.pernischova
 */
public class TermSelectionStepPanelFrequency extends StepPanel implements
		ActionListener {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	// ---- Global Variables ----

	private String origStr;
	private JButton collapseButton, set, update;
	private JList terms;
	private JScrollPane termsScrollPane;
	private JPanel filterPanel, termInstancesPanel, freqPanel, posPanel, checkBoxesPanel;
	private HashMap<String,WordAttributes> termsToIndex;
	public static ArrayList<String> idxArray;
	private JTabbedPane tabPane;
	private com.JavaIndexer.processing.Frequency f;
	private JCheckBox nounBox, verbBox, adjBox, advBox;
	private JRadioButton[] freqs;
	private DefaultListModel termsListModel, instanceListModel;
	private JSplitPane wholePane,rightPane;
	private JPanel leftPanel,leftCenterPanel,centerPanel,rightPanel;
	private Vector<String> selectedTerms = new Vector<String>();
	private String originalText;
	private int backChars=100, forwardChars=100, totalCount;
	private JComboBox instanceBox;
	private Vector<String> instanceList;
//	public static int[] idxArray;
	private JScrollPane boxes;
	private ButtonGroup group;
	/**
	 * Default constructor.
	 */
	
	private class myModel extends DefaultListSelectionModel{
		myModel(){
			super();
		}
		private static final long serialVersionUID = 5565591280993034064L;
	    @Override
	    public void setSelectionInterval(int index0, int index1) {
	        if(super.isSelectedIndex(index0)) {
	            super.removeSelectionInterval(index0, index1);
	        }
	        else {
	            super.addSelectionInterval(index0, index1);
	        }
	    }
	}
		
		
	
	public TermSelectionStepPanelFrequency() {
		super("Frequency");
		setNextButtonText("Next");
		setLayout(new GridLayout(1,2));
		
		idxArray = new ArrayList<String>();
		
		//left side: word list
		termsListModel = new DefaultListModel();
		terms = new JList(termsListModel);
		terms.setSelectionModel(new myModel());/*
		terms.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {	
				for (int i=arg0.getFirstIndex(); i<=arg0.getLastIndex(); i++){
					if (terms.isSelectedIndex(i) && !idxArray.contains(i)){
						for (int j=0; j<idxArray.size(); j++){
							if (i < idxArray.get(j)){
								idxArray.add(j, i);
								instanceList.add(j, termsToIndex[i].getIndexTerm());
							}
						}
						if (idxArray.isEmpty() || i > idxArray.get(idxArray.size()-1)){
							idxArray.add(i);
							instanceList.add(termsToIndex[i].getIndexTerm());
						}
					}
					else if (!terms.isSelectedIndex(i) && idxArray.contains(i)){
						instanceList.remove(termsToIndex[i].getIndexTerm());
						idxArray.remove(new Integer(i));
					}
					
				}/*
				System.out.print(arg0.getFirstIndex() + " " + arg0.getLastIndex() + "\n");
				if (terms.isSelectedIndex(arg0.getFirstIndex())){
					if (!idxArray.contains(arg0.getFirstIndex())){
						idxArray.add(new Integer(arg0.getFirstIndex()));
						instanceList.add(termsToIndex[arg0.getFirstIndex()].getIndexTerm());
					}
				}
				else {
					idxArray.remove(new Integer(arg0.getFirstIndex()));
					instanceList.remove(termsToIndex[arg0.getFirstIndex()].getIndexTerm());
				}
				instanceBox.updateUI();
			}
			
		});*/
		termsScrollPane = new JScrollPane(terms);
		
		//right side: tabbed pane of filters and term instances
		tabPane = new JTabbedPane();
		filterPanel = new JPanel();	
		filterPanel.setLayout(new GridLayout(2,2));
		
		//pos Panel
		posPanel = new JPanel();
		posPanel.setLayout(new GridLayout(4,1));
		posPanel.setBorder(BorderFactory.createTitledBorder("Part Of Speech"));
		nounBox = new JCheckBox("Noun");
		verbBox = new JCheckBox("Verb");
		adjBox = new JCheckBox("Adjective");
		advBox = new JCheckBox("Adverb");
		
		posPanel.add(nounBox);
		posPanel.add(verbBox);
		posPanel.add(adjBox);
		posPanel.add(advBox);
		
		//freq Panel
		freqPanel = new JPanel();
		freqPanel.setLayout(new GridLayout(6,1));
		freqPanel.setBorder(BorderFactory.createTitledBorder("Frequency"));
		freqs = new JRadioButton[6];
		freqs[0] = new JRadioButton("<10");
		freqs[1] = new JRadioButton("<1");
		freqs[2] = new JRadioButton("<0.1");
		freqs[3] = new JRadioButton("<0.01");
		freqs[4] = new JRadioButton("<0.001");
		freqs[5] = new JRadioButton("<0.0001");
		
		group = new ButtonGroup();
		group.add(freqs[0]);
		group.add(freqs[1]);
		group.add(freqs[2]);
		group.add(freqs[3]);
		group.add(freqs[4]);
		group.add(freqs[5]);
		
		freqPanel.add(freqs[0]);
		freqPanel.add(freqs[1]);
		freqPanel.add(freqs[2]);
		freqPanel.add(freqs[3]);
		freqPanel.add(freqs[4]);
		freqPanel.add(freqs[5]);
		
//		filterPanel.setPreferredSize(new Dimension(100,400));
		filterPanel.add(posPanel);
		filterPanel.add(freqPanel);
		filterPanel.add(new JLabel(""));	//for sizing purposes
		
		//buttonPanel
		set = new JButton("set");
		set.addActionListener(this);
		update = new JButton("update");
		update.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(8,2));
		buttonPanel.add(new JLabel(""));		//for sizing purposes
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(set);
		buttonPanel.add(update);
		filterPanel.add(buttonPanel);
		
		checkBoxesPanel = new JPanel();
		checkBoxesPanel.setLayout(new GridLayout(0,1));
		boxes = new JScrollPane(checkBoxesPanel);
		boxes.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		termInstancesPanel = new JPanel();
		termInstancesPanel.setLayout(new BorderLayout());
		instanceList = new Vector<String>();
		instanceBox = new JComboBox(instanceList);
		instanceBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkBoxesPanel.removeAll();
				JComboBox box = (JComboBox) arg0.getSource();
				WordAttributes word = termsToIndex.get((String)box.getSelectedItem());
				for (int i=0; i< word.getCount(); i++){
					boolean selected = word.isIndexed(i);
					JCheckBox j = new JCheckBox(getSomeText(word,i,30),selected);
					j.setPreferredSize(new Dimension(10,200));
					j.setName(word.getIndexTerm() + i);
					j.addItemListener(new ItemListener(){
						@Override
						public void itemStateChanged(ItemEvent arg0) {
							JCheckBox j = (JCheckBox) arg0.getItemSelectable();
							String name = j.getName();
							int idx = -1;
							for (int i=name.length()-1; i>=0; i--){
								if (!Character.isDigit(name.charAt(i))){
									idx = i+1;
									break;
								}
							}
							String word = name.substring(0,idx);
							int num = Integer.parseInt(name.substring(idx,name.length()));
							termsToIndex.get(word).setIndexed(num, j.isSelected());							
							
						}
						
					});
					checkBoxesPanel.add(j);
				}
				checkBoxesPanel.updateUI();
			}
			
		});
		termInstancesPanel.add(instanceBox,BorderLayout.NORTH);
		termInstancesPanel.add(boxes, BorderLayout.CENTER);
		
		tabPane.add(filterPanel, "Filter");
		tabPane.add(termInstancesPanel, "Instances");
		
		add(termsScrollPane);
		add(tabPane);
	}
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == collapseButton) {
			wholePane.setDividerLocation(0);
		}
		else if (event.getSource() == set) {
			termsListModel.removeAllElements();
			
			//load termsToIndex and origStr
			try {
				termsToIndex = Tagger.termsToIndex;
				origStr = DocumentsStepPanel.Text;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			//load and sort the terms
			ArrayList<WordAttributes> termValues = new ArrayList<WordAttributes>(termsToIndex.values());
			Collections.sort(termValues);
			
			for (WordAttributes item : termValues){
				termsListModel.addElement(item.getIndexTerm());
				totalCount += item.getCount();
			}
			
			for (int i=0; i<termsToIndex.size(); i++){
				if (nounBox.isSelected() && termValues.get(i).getTag(0).startsWith("NN")){
					if (checkFreq(termValues.get(i).getCount())){
						terms.addSelectionInterval(i, i);
					}
				}
				else if (verbBox.isSelected() && termValues.get(i).getTag(0).startsWith("VB")){
					if (checkFreq(termValues.get(i).getCount())){
						terms.addSelectionInterval(i, i);
					}
				}
				else if (adjBox.isSelected() && termValues.get(i).getTag(0).startsWith("JJ")){
					if (checkFreq(termValues.get(i).getCount())){
						terms.addSelectionInterval(i, i);
					}
				}
				else if (advBox.isSelected() && termValues.get(i).getTag(0).startsWith("RB")){
					if (checkFreq(termValues.get(i).getCount())){
						terms.addSelectionInterval(i, i);
					}
				}
			}
			update();
		}
		else if (event.getSource() == update){
			update();
		}
		else if (event.getSource().getClass().getSimpleName().equals("JCheckBox")){
			JCheckBox temp = (JCheckBox) event.getSource();
//			termsToIndex[idxArray[(instanceBox.getSelectedIndex())]].setIndexed(Integer.parseInt(temp.getName()),temp.isSelected());
		}
	}
	private void update() {
		idxArray = new ArrayList<String>(terms.getSelectedValues().length);
		instanceList.removeAllElements();
		for (Object obj : terms.getSelectedValues()){
			idxArray.add((String) obj);
			instanceList.add((String)obj);
		}
	}

	private String getSomeText(WordAttributes word, int i, int count) {
		int startIdx = word.getStartIndex(i);
		System.out.println("Start");
		for (int j=0; j<count; j++){
			startIdx = origStr.lastIndexOf(" ",startIdx);
			if (startIdx == -1){
				startIdx = 0;
				break;
			}
			startIdx--;
		}
		System.out.println("End");
		int endIdx = word.getStartIndex(i);
		for (int j=0; j<count; j++){
			endIdx = origStr.indexOf(" ",endIdx);
			if (endIdx == -1){
				endIdx = origStr.length();
				break;
			}
			endIdx++;
		}
		String subString = "<html>"; 
		subString += origStr.substring(startIdx,word.getStartIndex(i));
		subString += "<font color = \"RED\">";
		subString += origStr.substring(word.getStartIndex(i),word.getEndIndex(i));
		subString += "</font>";
		subString += origStr.substring(word.getEndIndex(i),endIdx);
		subString += "</html>";
		
		return subString;
		
//		int start = (word.getStartIndex(i)-count > 0) ? word.getStartIndex(i)-count : 0;
//		int end = (word.getEndIndex(i)+count < origStr.length()) ? word.getStartIndex(i)+count : origStr.length();
//		
//		for (int j=start+30; j<end; j+=30){
//			
//		}
//		return "<html>..." + origStr.substring(start,end) + "...</html>";
	}
	private boolean checkFreq(int i) {
		for (Enumeration e = group.getElements(); e.hasMoreElements();){
			JRadioButton radioButton = (JRadioButton) e.nextElement();
			if (radioButton.isSelected()){
				double a = Double.parseDouble(radioButton.getText().substring(1));
				if (i/(double)totalCount < a){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
	}
//	public void setIt() throws IOException{
//		termsListModel.clear();
//		selectedTerms.clear();
//		terms.removeSelectionInterval(0, terms.getComponentCount());
//		boolean highlight = false;
//		for (int i=0; i<termsToIndex.length; i++){
//			highlight = false;
//			termsListModel.add(termsListModel.getSize(), termsToIndex[i].getIndexTerm());
//			for (int j=0; j<idxArray.length; j++){
//				if (idxArray[j] == i){
//					highlight = true;
//				}
//			}
//			if (highlight){
//				selectedTerms.add(termsToIndex[i].getIndexTerm());
//				terms.addSelectionInterval(i, i);
//				System.out.println(i);
//			}	
//		}
//		FileOutputStream fos = new FileOutputStream("objectFiles/idxArray.idx");
//		ObjectOutputStream oos = new ObjectOutputStream(fos);
//		oos.writeObject(idxArray);
//		
//	}
}


