package com.JavaIndexer.processing;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.JavaIndexer.generics.WordAttributes;

public class WSD {
	public float [][] distanceMatrix;
	public String output;
	
	public static void main(String[] args) {
	}
	public WSD(float[][] coordMatrix, HashMap<String, WordAttributes> termsToIndex, ArrayList<String> idxArray, String ot){
		try{
			distanceMatrix = new float[coordMatrix.length][coordMatrix[0].length];
			WordAttributes[] t = termsToIndex.values().toArray(new WordAttributes[0]);
			doWSD(coordMatrix,t,idxArray,ot);
		}catch(Exception e){
			
		}
	}
	
	private void doWSD(float[][] coordMatrix, WordAttributes[] termsToIndex, ArrayList<String> idxArray, String ot) throws IOException {
		//use coordMatrix to get avg encodings for each of terms surrounding an
		//ambiguous term
		//after disambiguating terms, append to coordMatrix
		
		//prompt user for distance cutoff value
		
		//prompt user for suffixes to use for separate clusters of disambiguated terms
		
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
//		int idxVal;
//		for (int i=0; i<idxArray.length; i++){
//			output += termsToIndex[idxArray[i]].getSearchTerm(0) + '\n';
//			idxVal = idxArray[i];
//			//coordLength = HCACoordLengths[idxVal];
//			xAvg = 0;
//			yAvg = 0;
//			xAvg = termsToIndex[idxVal].getAverageWSDXValue();
//			yAvg = termsToIndex[idxVal].getAverageWSDYValue();
//			output += "\t" + "xAvg: " + xAvg + "\n\t" + "yAvg: " + yAvg + "\n\t";
//			
//			for (int j=0; j<termsToIndex[idxVal].getCount(); j++){
//				output += termsToIndex[idxVal].getSearchTerm(0) + " point " + (j+1) + "\n\t\t";
//				output += "Dimension X: " + termsToIndex[idxVal].getWSDXValue(j) + "\n\t\t";
//				output += "Sum of Squares: " +  Math.pow(termsToIndex[idxVal].getWSDXValue(j) - xAvg, 2) + "\n\t\t";
//				output += "Dimension Y: " + termsToIndex[idxVal].getWSDYValue(j) + "\n\t\t";
//				output += "Sum of Squares: " +  Math.pow(termsToIndex[idxVal].getWSDYValue(j) - yAvg, 2) + "\n\t";
//			}
//			output += "\n";
//			
//		}
	
//		FileWriter fw = new FileWriter(new File("WSD.txt"));
//		BufferedWriter bw = new BufferedWriter(fw);
				/*
		int rowdim = coordMatrix.length;	
		int dimensions = coordMatrix[0].length;
//		float [][] distanceMatrix = new float [rowdim][rowdim];
		bw.write('\r');
		bw.write("<<Dist Matrix>>");
		bw.write('\r');
		System.out.println("Dist Matrix, rowdim=" + rowdim)
		for (int i = 0; i < rowdim; i++){
			//compare each term with each successive term
			for (int j = 0; j < rowdim; j++){
				//get coordinates for each, get difference between them
				//calculate distance, store this value in new matrix
				float ssq = 0;
				float diff = 0;
				float d = 0;
				for (int k = 0; k < dimensions; k++){						
					diff = Math.abs(coordMatrix[i][k] - coordMatrix[j][k]);
					ssq = ssq + (float) Math.pow(diff, 2);													
				}//end for (k)
				d = (float) Math.sqrt(ssq);	
				distanceMatrix[i][j] = d;
				//if (i < 10 & j < 10){
					bw.write(distanceMatrix[i][j] + "*");
				//}
			}//end for (j)
			//if (i < 10){ 
				bw.write('\r'); 
			//}
		}//end for (i)
		*/
//		bw.close();
//	    fw.close();    
	    System.out.println("DONE! Please review your data output file.");
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
