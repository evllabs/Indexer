package com.JavaIndexer.processing;

import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.WordLemmaTag;
import edu.stanford.nlp.ling.WordTag;
import edu.stanford.nlp.process.Morphology;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ProgressMonitor;

import com.JavaIndexer.generics.WordAttributes;
import com.JavaIndexer.gui.stepPanels.DocumentsStepPanel;
import com.puppycrawl.tools.checkstyle.checks.indentation.WhileHandler;

public class Tagger implements Serializable{
	private static final long serialVersionUID = 1L;
	public String displayName() {
		return "Tagger";
	}
	
	public String tooltipText() {
		return "For tagging already read test file.";
	}

	public boolean showInGUI() {
		return false;
	}
	public static HashMap<String,WordAttributes> termsToIndex;
	/**
	 * The tagged text
	 */
	public static String procText;
	/**
	 * the untagged text
	 */

	public static String Text;
	public static int[][] paragraphOffsets;

	
	/**
	 * Default constructor 
	 */
	public Tagger() {
		procText = "";
		Text = "";
	}

	/**
	 * Constructor given a text
	 * 
	 * @param text The text to be tagged
	 * @param pm 
	 * 		
	 */
	public Tagger(String text) {
		this.Text = text;
		try {
			tag(text);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param text The text to be tagged
	 */
	private void tag(String text){      
        //Uses Stanford word processor
        //Stanford POS Tagger, v. 1.6 - 28 Sept, 2008
        //Copyright (c) 2002-2008 The Board of Trustees of
        //The Leland Stanford Junior University. All Rights Reserved.

        //Original tagger author: Kristina Toutanova
        //Code contributions: Christopher Manning, Dan Klein, William Morgan, Huihsin Tseng, Anna Rafferty
        //Current release prepared by: Christopher Manning
        
        // Open Model
		
		String myFile = "bidirectional-wsj-0-18.tagger";
		try {
			//read originalText string object and put into origText
			String origText = text;
			
			termsToIndex = new HashMap<String, WordAttributes>();	
			
			String regex = "[\\s&&[^\\n]]*\\n[\\s&&[^\\n]]*\\n\\s*";
			String[] paragraphs = origText.split(regex);	//splits into paragraphs
			
			StringBuffer taggedDoc = new StringBuffer();		//entire tagged Doc 
//			MaxentTagger myTagger = new MaxentTagger("bidirectional-wsj-0-18.tagger");	//initialize Maxent Tagger
			MaxentTagger myTagger = new MaxentTagger(myFile);	//initialize Maxent Tagger
			paragraphOffsets = new int [paragraphs.length][3];	//[][0] = start; [][1] = end; [][2] is length 
			
			int offset = -1;
			for (int i=0; i<paragraphs.length; i++){
				if (paragraphs[i].isEmpty()){
					System.out.println("Paragraph is Empty"); //TODO remove error correction
				}
				else{
					String taggedItem = myTagger.tagString(paragraphs[i]);
					taggedDoc.append(taggedItem);
					taggedDoc.append("\r"); //taggedDoc will be paragraphs separated by \r
					
					//get start index of paragraph
					paragraphOffsets[i][0] = origText.indexOf(paragraphs[i],offset+1);
					
					//get end index of paragraph
					paragraphOffsets[i][1] = paragraphOffsets[i][0] + paragraphs[i].length();

					processSentence(termsToIndex,paragraphs[i],taggedItem,origText.indexOf(paragraphs[i],offset+1));
					offset = paragraphOffsets[i][0];
				}
			}
			
			
			//separateFunction(termsToIndex);
//			WordAttributes[] termsToIndex = new WordAttributes[termsToIndexHash.size()];
//			int i = 0;
//			for (WordAttributes e : termsToIndexHash.values()){
//				termsToIndex[i++] = e;
//			}
//			Arrays.sort(termsToIndex, new Comparator(){
//
//				public int compare(Object arg0, Object arg1) {
//				//	return new Integer(((WordAttributes)arg0).getCount()).compareTo(((WordAttributes)arg1).getCount()); 
//					return ((WordAttributes)arg0).getIndexTerm().compareTo(((WordAttributes)arg1).getIndexTerm());
//				}
//			});
//			
//			for (WordAttributes word : termsToIndexHash.values()){
//				System.out.println(word.getStartIndex(0) + " " + word.getEndIndex(0));
//				
//			}
			procText = taggedDoc.toString();
			
//			System.out.println("Tagger is complete after: " + secsElapsed + " milliseconds");
		}
		catch (Exception e){
			System.out.println("ERROR: "); e.printStackTrace();
		}
		
	}

	/**
	 * Processes the sentence into termsToIndex
	 * @param termsToIndex The HashMap that is used throughout the program 
	 * @param str	the original untagged string
	 * @param taggedStr	the Tagged string
	 * @param paragraphOffset	the paragraph's offset in the document
	 */
	private void processSentence(HashMap<String, WordAttributes> termsToIndex,
			String str, String taggedStr, int paragraphOffset) {
		//split into individual words
		String[] splitTaggedWords = taggedStr.split("\\s");
		int endIndex = 0;
		//loop through each word
		for (int i=0; i<splitTaggedWords.length; i++){
			if (shouldSkip(splitTaggedWords[i])){
				continue;
			}
			int[] temp = putWordIntoHash(termsToIndex,splitTaggedWords,str, i, endIndex, paragraphOffset);
			i = temp[0];
			endIndex = temp[1];
		}		
}

	private int[] putWordIntoHash(HashMap<String, WordAttributes> termsToIndex, String[] splitTaggedWords, String str, int i, int endIndex, int po) {
		
		String wordText = splitTaggedWords[i].substring(0, splitTaggedWords[i].lastIndexOf('/'));
		String wordTag = splitTaggedWords[i].substring(splitTaggedWords[i].lastIndexOf('/')+1);
//		String wordLemma = Morphology.lemmatizeStatic(new WordTag(wordText,wordTag)).lemma();
		String wordLemma = wordText;
		int startIdx = getIndex(str, wordText, endIndex);
		
		
		if (!wordTag.startsWith("NN")){
			updateHash(termsToIndex,new WordAttributes(wordLemma,wordText,wordTag,startIdx+po,startIdx+wordText.length()+po));
			return new int[] {i,startIdx+wordText.length()};
			
		}
		else{
			String[] tempWord = new String[2];
			int endIdx = startIdx + wordText.length();
			String prevTag = wordTag; //we need to remember previous tag
			for (;i<splitTaggedWords.length-1; i++){
				tempWord[0] = splitTaggedWords[i+1].substring(0, splitTaggedWords[i+1].lastIndexOf('/'));
				tempWord[1] = splitTaggedWords[i+1].substring(splitTaggedWords[i+1].lastIndexOf('/')+1);
				
				if (tempWord[1].equalsIgnoreCase(",")){
					updateHash(termsToIndex,new WordAttributes(wordLemma,wordText,wordTag,startIdx+po,endIdx+po));
					return new int[] {i,endIdx};
				}
				if (shouldSkip(splitTaggedWords[i+1])){
					continue;
				}
				if (prevTag.startsWith("NNP")){
					if (tempWord[1].startsWith("NN")){
						wordText += " " + tempWord[0];
						wordTag = "NNC";
						wordLemma = wordText;
					}
					else{
						updateHash(termsToIndex,new WordAttributes(wordLemma,wordText,wordTag,startIdx+po,endIdx+po));
						return new int[] {i,endIdx};
					}
				}
				else{
					if (tempWord[1].startsWith("NN") && !tempWord[1].startsWith("NNP")){
						wordText += " " + tempWord[0];
						wordTag= "NNC";
						wordLemma = wordText;
					}
					else{
						updateHash(termsToIndex,new WordAttributes(wordLemma,wordText,wordTag,startIdx+po,endIdx+po));
						return new int[] {i,endIdx};
					}
				}
				endIdx = getIndex(str, tempWord[0], endIdx) + tempWord[0].length();
				prevTag = tempWord[1];
			}
			updateHash(termsToIndex, new WordAttributes(wordLemma,wordText,wordTag,startIdx+po,endIdx+po));
			return new int[] {i, endIdx};
		}
	}

	private int getIndex(String str, String curTaggedVal, int endIndex) {
		ArrayList<String> vals = new ArrayList<String>();
		vals.add(curTaggedVal);
		if (curTaggedVal.indexOf("color") != -1){
			vals.add(curTaggedVal.replaceAll("color", "colour"));			
		}
		if (curTaggedVal.indexOf("labor") != -1){
			vals.add(curTaggedVal.replaceAll("labor", "labour"));			
		}
		int min = Integer.MAX_VALUE;
		for (int i=0; i<vals.size(); i++){
			int tempIndex = str.indexOf(vals.get(i), endIndex);
			if (tempIndex != -1 && tempIndex < min){
				min = tempIndex;
			}
		}
		return min;
	}

	/**
	 * @param str
	 * @return true if tag is CD, -LRB-, -RRB-, POS
	 */
	private boolean shouldSkip(String str) {
		if (str.equalsIgnoreCase("")){
			return true;
		}
		if (str.substring(str.lastIndexOf('/')+1).equalsIgnoreCase("CD")){
			return true;
		}
		if (str.substring(str.lastIndexOf('/')+1).equalsIgnoreCase("-LRB-")){
			return true;
		}
		if (str.substring(str.lastIndexOf('/')+1).equalsIgnoreCase("-RRB-")){
			return true;
		}
		if (str.substring(str.lastIndexOf('/')+1).equalsIgnoreCase("POS")){
			return true;
		}
		
		
		str = str.substring(0,str.lastIndexOf("/"));
		if (str.matches("\\d+(st|rd|nd|th)")){
			return true;
		}
		if (str.matches("\\W*")){
			return true;
		}
		if (str.matches("\\d*")){
			return true;
		}
		return false;
	}

	private String[] splitWords(String str) {
		ArrayList<String> splitWordsList = new ArrayList<String>(Arrays.asList(str.split("\\s")));
		for (int i=0; i<splitWordsList.size(); i++){
			String word = removePunc(splitWordsList.get(i));
			if (word.endsWith("'s") || word.endsWith("'re") || word.endsWith("'t")){
				splitWordsList.remove(i);
				int delim = word.lastIndexOf("'");
				splitWordsList.add(i,word.substring(delim));
				splitWordsList.add(i,word.substring(0, delim));
				i++;
			}
			else {
				if (word.equalsIgnoreCase("")){
					splitWordsList.remove(i);
					i--;
					continue;
				}
				splitWordsList.set(i, removePunc(word));
			}
		}
		return splitWordsList.toArray(new String[0]);
	}

	private boolean isPunc(String str) {
		return str.matches("\\W") || str.equalsIgnoreCase("-LRB-") || str.equalsIgnoreCase("-RRB-");
	}

	private void addSeriesToHash(HashMap<String, WordAttributes> termsToIndex,
			ArrayList<WordAttributes> series, String str, int paragraphOffset) {
		for (int i=0; i<series.size(); i++){
			if (isPunc(series.get(i).getIndexTerm())){
				if (series.get(i).getIndexTerm().charAt(0) == '.' && !(i > 0 && i <series.size()-1 && series.get(i-1).getTag(0).startsWith("NNP") && 
										series.get(i+1).getTag(0).startsWith("NNP"))){
				}
				else{
					addSeriesToHash(termsToIndex, new ArrayList<WordAttributes>(series.subList(0, i)), str, paragraphOffset);
					series = new ArrayList<WordAttributes>(series.subList(i+1, series.size()));
					i=-1;
				}
			}
		}
		series = removePuncs(series);
		if (series.isEmpty()){
			return;
		}
		String lemma = "";
		for (int i=0; i<series.size(); i++){
			if (isPunc(series.get(i).getIndexTerm())){
				continue;
			}
			lemma += series.get(i).getIndexTerm() + " ";
		}
		lemma = lemma.substring(0, lemma.length()-1);
		int startIdx = series.get(0).getStartIndex(0);
		int endIdx = series.get(series.size()-1).getEndIndex(0);
		if (endIdx-paragraphOffset >= str.length() || (endIdx-paragraphOffset) - (startIdx-paragraphOffset) <0 ||
						startIdx - paragraphOffset < 0 ){
			System.out.println("ERROR!");
		}
		String substring = str.substring(startIdx-paragraphOffset, endIdx-paragraphOffset);
		String tag = "NNC";
		if (series.size() == 1){
			tag = series.get(0).getTag(0);
		}
		WordAttributes combWord = new WordAttributes(lemma, substring, tag, startIdx, endIdx);
		updateHash(termsToIndex,combWord);
		
	}

	private ArrayList<WordAttributes> removePuncs(
			ArrayList<WordAttributes> series) {
		for (int i=0; i<series.size(); i++){
			if (isPunc(series.get(i).getIndexTerm())){
				series.remove(i);
				i--;
			}
		}
		return series;
	}

	private void updateHash(HashMap<String, WordAttributes> termsToIndex,
			WordAttributes combWord) {
		String key = combWord.getIndexTerm();
		if (termsToIndex.containsKey(key)){
			termsToIndex.get(key).addSearchTerm(combWord.getSearchTerm(0), combWord.getTag(0));
			termsToIndex.get(key).addOffset(combWord.getStartIndex(0),combWord.getEndIndex(0));
		}
		else{
			termsToIndex.put(key, combWord);
		}
	}
	
	private String removePunc(String s){
		if (s.length()<=0){
			return s;
		}
		while (s.length() > 0 && !Character.isLetter(s.charAt(0)) && !Character.isDigit(s.charAt(0))){
			s=s.substring(1);
		}
		while (s.length() > 0 && !Character.isLetter(s.charAt(s.length()-1)) && !Character.isDigit(s.charAt(s.length()-1))){
			s=s.substring(0,s.length()-1);
		}
	return s;
	}
	private void separateFunction(HashMap<String, WordAttributes> termsToIndex, String fileName) throws IOException {
		String[] strings = termsToIndex.keySet().toArray(new String[0]);
	
		File file = new File("/home/kevyn/Desktop/sampleTexts/modTexts");
		File temps[] = file.listFiles();
		Arrays.sort(temps);
		for (int i=0; i<temps.length; i++){
			if (!temps[i].getName().startsWith(fileName)){
				continue;
			}
			int totalCount = 0;
			File temp = new File("/home/kevyn/Desktop/sampleTexts/compIndexes/" + 
					temps[i].getName());
//			temp.createNewFile();
			FileWriter fw = new FileWriter(temp);
			BufferedWriter bw = new BufferedWriter(fw);
			Scanner scan = new Scanner(temps[i]);
			if (!scan.hasNextLine()){ 
				System.out.println("ERROR!!!");
			}
			while (scan.hasNextLine()){
				String word = scan.nextLine();
				WordAttributes theWord = termsToIndex.get(word);
				if (theWord == null){
					bw.write(word + ": not Found!");
				}
				else {
					bw.write(word + ": count= " + theWord.getCount() + " tags= " + getTags(theWord));
				}
				bw.newLine();
			}
			bw.close();
			fw.close();
			
		}
		FileWriter fw = new FileWriter(new File("/home/kevyn/Desktop/sampleTexts/compIndexes/" + fileName + "Hash.txt"));
		BufferedWriter bw = new BufferedWriter(fw);
		Arrays.sort(strings);			
		for (int i=0; i<strings.length; i++){
			bw.write(strings[i]);
			bw.newLine();
		}
		bw.close();
		fw.close();

	}

	private String getTags(WordAttributes theWord) {
		ArrayList<String> tags = new ArrayList<String>();
		Loop:
		for (int i=0; i<theWord.getCount(); i++){
			for (int j=0; j<tags.size(); j++){
				if (theWord.getTag(i).equalsIgnoreCase(tags.get(j))){
					continue Loop;
				}
			}
			tags.add(theWord.getTag(i));
		}
		
		return tags.toString();
	}
}
