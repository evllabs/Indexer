/*
package com.JavaIndexer.processing;
import java.io.*;
//import java.util.Enumeration;
//import java.util.Hashtable;
import java.util.Scanner;

public class TFIDF {
	public String TFIDFlist;
	public static void main(String[] args) throws IOException {
	}
	public TFIDF (String[][] termsToIndex, File outputFile) {
		try {
			getTermFreq(termsToIndex,outputFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getTermFreq(String[][] termsToIndex, File outputFile) throws Exception{
			//constructs a 2d array [terms][paragraphs]
		float[][] termByDocMatrix = new float [termsToIndex.length][getNumOfParagraphs(outputFile)];
		Scanner kb = new Scanner(outputFile).useDelimiter("");
//		int[] freqByParagraph = new int [termsToIndex.length];

		String temp = "";
		String word = "";
		com.JavaIndexer.processing.Frequency f = new com.JavaIndexer.processing.Frequency();
		
		for (int i=0; i<getNumOfParagraphs(outputFile); i++){
			while (kb.hasNext()){
					temp = kb.next();
					if (temp.charAt(0) == '\n'){
						for (int j=0; j<termsToIndex.length; j++){
							//loop through document to see if term to index exists
							if (word.length() > 0 && f.normalize(word).compareTo(termsToIndex[j][0]) == 0){
								termByDocMatrix[j][i]++;
							}
						}
						break;
					}
					else if (temp.charAt(0) == ' '){
						for (int j=0; j<termsToIndex.length; j++){
							//loop through document to see if term to index exists
							if (f.normalize(word).compareTo(termsToIndex[j][0]) == 0){
								termByDocMatrix[j][i]++;
							}
						}
						word = "";
					}
					else{
						word = word + temp;
					}			
			}
		}

		for (int i=0; i<termsToIndex.length; i++){
			for (int j=0; j<getNumOfParagraphs(outputFile); j++){
				if (termByDocMatrix[i][j] > 0){
					termByDocMatrix[i][j] = (float)Integer.parseInt(termsToIndex[i][1]) / termByDocMatrix[i][j];

				}
				else
					termByDocMatrix[i][j] = 0;
			
				termByDocMatrix[i][j] = Math.log(termByDocMatrix[i][j]);
				TFIDFlist = TFIDFlist + "term: " + termsToIndex[i][0] +  " paragraph: " + j +
										" num: " + termByDocMatrix[i][j] + '\n';
			}
		}

}

/*			for (int j=0; i<termsToIndex.length; j++){
					if(table.get(termsToIndex[j][0]) == null)
						table.put(termsToIndex[j][0],1);
							//if the word has not been places into the hash yet, place it with value 1
					else{
						table.put(termsToIndex[j][0], table.get(termsToIndex[j][0]) + 1);
							//otherwise, get the current value, and increment it
					}
				}//end if
			}
			
			for (int k = 0; k<termsToIndex.length; k++){
				termByDocMatrix[k][i] = 
				Enumeration<String> e = table.keys(); e.hasMoreElements()){
				}

				
			}
			}

		
			for(int j=0; j<termsToIndex.length; j++){
				termByDocMatrix[j][i] = table.get(termsToIndex[j][0]);
				System.out.print(termsToIndex[j][0] + table.get(termsToIndex[j][0]));
				}
				*/
/*
public static int getNumOfParagraphs(File inputFile) throws Exception{
	int count = 0;
	Scanner kb = new Scanner(inputFile).useDelimiter("");
	while (kb.hasNext()){
		if (kb.next().charAt(0) == '\n')
			count++;
	} //end while
	return count;
}
}
*/


package com.JavaIndexer.processing;
import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

import cern.colt.matrix.impl.SparseDoubleMatrix3D;

import com.JavaIndexer.generics.WordAttributes;

public class HCA {
	public String output = "";
	public void main(String[] args) throws IOException {
	}
 	public HCA (float[][] coordMatrix, WordAttributes[] termsToIndex, int[] idxArray, String ot) {
		try {
			doHCA(coordMatrix, termsToIndex, idxArray, ot);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//put in EVDCoords
	public void doHCA(float[][] coordMatrix, WordAttributes[] termsToIndex, int[] idxArray, String ot){
		int highestCount = 0;
		for (int i=0; i<termsToIndex.length; i++){
			if (termsToIndex[i].getCount() > highestCount)
				highestCount = termsToIndex[i].getCount();
		}
		//SparseDoubleMatrix3D HCACoords = new SparseDoubleMatrix3D(termsToIndex.length,highestCount,3); 		//value[][][0] will be startIdx in original text
		//int HCACoordLengths[] = new int[termsToIndex.length];												//value[][][1] will be endIdx of text
		//String theTerm, theTag, tempTerm, tempTag;								   						//value[][][2] will be x coord
		float xAvg, yAvg, xSum, ySum, xVal, yVal;															//value[][][3] will be y coord
		String temp;
		int startIdx, endIdx;
		for (int i=0; i<termsToIndex.length; i++){
			for (int j=0; j<termsToIndex[i].getCount(); j++){
				endIdx = getBounds(ot,termsToIndex[i].getStartIndex(j))-1;
				xVal = 0;
				yVal = 0;
				for (int k=0; k<5; k++){
					endIdx++;
					startIdx = endIdx;
					endIdx = ot.indexOf(' ', endIdx);
					for (int l=0; l<termsToIndex.length; l++){
						if (termsToIndex[l].areIndexes(startIdx,endIdx)){	//determine where the term is in termsToIndex
							xVal = xVal + termsToIndex[l].getXVal();
							yVal = yVal + termsToIndex[l].getYVal();
							break;
						}
					}
				}
				termsToIndex[i].setWSDXValue(j,xVal/5);
				termsToIndex[i].setWSDYValue(j,yVal/5);
			}
		}
		int idxVal;
		for (int i=0; i<idxArray.length; i++){
			output += termsToIndex[idxArray[i]].getSearchTerm(0) + '\n';
			idxVal = idxArray[i];
			//coordLength = HCACoordLengths[idxVal];
			xAvg = 0;
			yAvg = 0;
			xAvg = termsToIndex[idxVal].getAverageWSDXValue();
			yAvg = termsToIndex[idxVal].getAverageWSDYValue();
			output += "\t" + "xAvg: " + xAvg + "\n\t" + "yAvg: " + yAvg + "\n\t";
			
			for (int j=0; j<termsToIndex[idxVal].getCount(); j++){
				output += termsToIndex[idxVal].getSearchTerm(0) + " point " + (j+1) + "\n\t\t";
				output += "Dimension X: " + termsToIndex[idxVal].getWSDXValue(j) + "\n\t\t";
				output += "Sum of Squares: " +  Math.pow(termsToIndex[idxVal].getWSDXValue(j) - xAvg, 2) + "\n\t\t";
				output += "Dimension Y: " + termsToIndex[idxVal].getWSDYValue(j) + "\n\t\t";
				output += "Sum of Squares: " +  Math.pow(termsToIndex[idxVal].getWSDYValue(j) - yAvg, 2) + "\n\t";
			}
			output += "\n";
			
		}
		try {
		FileOutputStream fos = new FileOutputStream("objectFiles/termsToIndex.tti");
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(termsToIndex);
	    
	    oos.close();
	    fos.close();
		}catch (Exception e){
			System.out.println("Error: " + e);
		}
	}
	public String getSearchTerm(String wholeWord){
		//startIdx = termsToIndex[i][0].indexOf("<searchTerm=");
		//endIdx = termsToIndex[i][0].indexOf("><tag=");
		return wholeWord.substring(wholeWord.indexOf("<searchTerm=")+12,wholeWord.indexOf("><tag="));
	}
	public String getTag(String wholeWord){
		return wholeWord.substring(wholeWord.indexOf("<tag=")+5,wholeWord.length()-1);
	}
	public int[] getBounds(int k, WordAttributes[]taggedOriginal){
		int[] bounds = new int[2];

		if (k == 0 || taggedOriginal[k-1].getIndexTerm().compareTo("\r") == 0)
			bounds[0] = k;
		else if (k == 1 || taggedOriginal[k-2].getIndexTerm().compareTo("\r") == 0)
			bounds[0] = k-1;
		else
			bounds[0] = k-2;

		
		if (k == taggedOriginal.length-1 || taggedOriginal[k+1].getIndexTerm().compareTo("\r") == 0)
			bounds[1] = k;
		else if (k == taggedOriginal.length-2 || taggedOriginal[k+2].getIndexTerm().compareTo("\r") == 0)
			bounds[1] = k+1;
		else
			bounds[1] = k+2;
		
		return bounds;
	}
	private int getBounds(String ot, int startIndex) {
		int start = startIndex;
		
		for (int i=0; i<2; i++){
			start = ot.lastIndexOf(' ', start);
		}
		return start+1;
	}
	private String getWord(String ot, int index, int count){
		int start = index;
		for (int i=0; i<count; i++){
			start = ot.indexOf(' ', start);
		}
		return ot.substring(start, ot.indexOf(' ', start));
	}
}

