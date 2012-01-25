package com.JavaIndexer.processing;
import java.io.*;

import java.util.*;

import com.JavaIndexer.generics.WordAttributes;

public class Frequency {
	public int[] idxArray;
	private WordAttributes[] termsToIndex = null;
	public Frequency(){
	}
	public Frequency(WordAttributes[] taggedOriginal, boolean[] posSelected, boolean[] freqSelected) {
		try {
			getFrequency(taggedOriginal, posSelected, freqSelected);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getFrequency(WordAttributes[] termsToIndex, boolean[] posSelected, boolean[] freqSelected) throws IOException, ClassNotFoundException {
		// read in a text file/*
		double freqPercent;
		int totalCount=0;
		String tag;
		ArrayList<Integer> idxArrayList = new ArrayList<Integer>();

		
		for (int i=0; i<termsToIndex.length; i++){
			totalCount += termsToIndex[i].getCount();
		}
		for (int i =0; i<termsToIndex.length; i++){
			WordAttributes item = termsToIndex[i];
			freqPercent = (double) item.getCount() / (double) totalCount;
			tag = item.getTag(0);
			
			if (posSelected[0] == true && tag.startsWith("N")){
				idxArrayList.add(i);
				continue;
			}
			if (posSelected[1] == true && tag.startsWith("VB")){
				idxArrayList.add(i);
				continue;
			}
			if (posSelected[2] == true && tag.startsWith("RB")){
				idxArrayList.add(i);
				continue;
			}
			if (posSelected[3] == true && tag.startsWith("JJ")){
				idxArrayList.add(i);
				continue;
			}

		}
		idxArray = new int[idxArrayList.size()];
		for (int i=0; i<idxArray.length; i++){
			idxArray[i] = (int) idxArrayList.get(i);
		}
	}/*
	private void getOnlyUsableTerms(
			Hashtable<WordAttributes, Integer> table,
			int lf, int uf) throws IOException, ClassNotFoundException {
		termsToIndex = new WordAttributes [table.size()];
		idxArray = new int [table.size()];
		int i = 0;
		int tempCount;
		WordAttributes element;
		for (Enumeration<WordAttributes> e = table.keys() ; e.hasMoreElements() ;) {
			//create an Enumeration of Strings and loop until it runs out of elements
			element = e.nextElement();
			tempCount = table.get(element);
			if (element.getTag(0).startsWith("N") && tempCount >= lf && tempCount <= uf){
				termsToIndex[i] = element;
				termsToIndex[i].setCount(tempCount);
				idxArray[i] = i;
				termsToIndex[i].setTag("");
				i++;
			}
		}
		termsToIndex = Arrays.copyOf(termsToIndex, i);
		idxArray = Arrays.copyOf(idxArray, i);
		updateIt();
	}
	private void getOnlyNouns(Hashtable<WordAttributes, Integer> table,
			int lf, int uf) throws IOException, ClassNotFoundException {
		boolean changedTerm = false;
		termsToIndex = new WordAttributes [table.size()];
		idxArray = new int [table.size()];
		int i = 0, idxVal = 0, tempCount = 0;
		WordAttributes element;
		for (Enumeration<WordAttributes> e = table.keys() ; e.hasMoreElements() ;) {
			//create an Enumeration of Strings and loop until it runs out of elements
			changedTerm = false;
			element = e.nextElement();
			tempCount = table.get(element);
			for (int j=0; termsToIndex[j] != null; j++){
				if (termsToIndex[j].getIndexTerm().compareTo(element.getIndexTerm()) == 0 &&
						element.getTag(0).startsWith("N")){
					termsToIndex[j].setCount(termsToIndex[j].getCount() + tempCount);
					changedTerm = true;
				}
			}
			if (!changedTerm){
				if (element.getTag(0).startsWith("N")){
					termsToIndex[i] = element;
					termsToIndex[i].setCount(table.get(element));
					i++;
				}
			}
		}
		for (int j=0; termsToIndex[j] != null; j++){
			if (termsToIndex[j].getCount() >= lf && termsToIndex[j].getCount() <= uf){
				idxArray[idxVal] = j;
				idxVal++;
			}
			termsToIndex[j].setTag("GNN"); //GNN stands for General NouN
		}
		termsToIndex = Arrays.copyOf(termsToIndex, i);
		idxArray = Arrays.copyOf(idxArray, idxVal);
		updateIt();
	}
	private void getOnlyWords(Hashtable<WordAttributes, Integer> table,
			int lf, int uf) throws IOException, ClassNotFoundException {
		boolean changedTerm = false;
		termsToIndex = new WordAttributes [table.size()];
		idxArray = new int [table.size()];
		int i = 0, idxVal = 0, tempCount = 0;
		WordAttributes element;
		for (Enumeration<WordAttributes> e = table.keys() ; e.hasMoreElements() ;) {
			changedTerm = false;
			//create an Enumeration of Strings and loop until it runs out of elements
			element = e.nextElement();
			tempCount = table.get(element);
			for (int j=0; termsToIndex[j] != null; j++){
				if (termsToIndex[j].getIndexTerm().compareTo(element.getIndexTerm()) == 0){
					termsToIndex[j].setCount(termsToIndex[j].getCount() + tempCount);
					changedTerm = true;
				}
			}
			if (!changedTerm){
				termsToIndex[i] = element;
				termsToIndex[i].setCount(table.get(element));
				if (element.getTag(0).startsWith("N") && tempCount >= lf && tempCount <= uf){
					idxArray[idxVal] = i;
					idxVal++;
				}
				termsToIndex[i].setTag("");
				i++;
			}
		}
		termsToIndex = Arrays.copyOf(termsToIndex, i);
		idxArray = Arrays.copyOf(idxArray, idxVal);
		updateIt();
	}
	private void getWordsAndUsages(
			Hashtable<WordAttributes, Integer> table,
			int lf, int uf) {
		termsToIndex = new WordAttributes [table.size()];
		idxArray = new int [table.size()];
		int i = 0, idxVal = 0, tempCount = 0;
		WordAttributes element;
		for (Enumeration<WordAttributes> e = table.keys() ; e.hasMoreElements() ;) {
			//create an Enumeration of Strings and loop until it runs out of elements
			element = e.nextElement();
			tempCount = table.get(element);
			termsToIndex[i] = element;
			termsToIndex[i].setCount(table.get(element));
			if (element.getTag(0).startsWith("N") && tempCount >= lf && tempCount <= uf){
				idxArray[idxVal] = i;
				idxVal++;
			}
			i++;
		}
		termsToIndex = Arrays.copyOf(termsToIndex, i);
		idxArray = Arrays.copyOf(idxArray, idxVal);
	}
	private void updateIt() throws IOException, ClassNotFoundException{
		FileInputStream fis = new FileInputStream("objectFiles/paragraphArrayList.pal");
		ObjectInputStream ois = new ObjectInputStream(fis);
		List<ArrayList<WordAttributes>> pal = (List<ArrayList<WordAttributes>>) ois.readObject();
		for (int i=0; i<pal.size(); i++){
			for (int j=0; j<pal.get(i).size(); j++){
				if (pal.get(i).get(j).getTag(0).startsWith("N")){
					pal.get(i).get(j).setTag("GNN");
				}
			}
		}
		
		FileOutputStream fos = new FileOutputStream("objectFiles/paragraphArrayList.pal");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(pal);

		fis = new FileInputStream("objectFiles/taggedOriginal.to");
		ois = new ObjectInputStream(fis);
		WordAttributes[] wa = (WordAttributes[]) ois.readObject();
		for (int i=0; i<wa.length; i++){
			if (wa[i].getTag(0).startsWith("N")){
				wa[i].setTag("GNN");
			}
		}
		fos = new FileOutputStream("objectFiles/taggedOriginal.to");
		oos = new ObjectOutputStream(fos);
		oos.writeObject(wa);
		
		fis.close();
		ois.close();
		fos.close();
		oos.close();
	}*/
}
