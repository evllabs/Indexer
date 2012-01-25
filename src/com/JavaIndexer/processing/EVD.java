package com.JavaIndexer.processing;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import com.JavaIndexer.generics.WordAttributes;
import com.JavaIndexer.gui.stepPanels.TermSelectionStepPanelFrequency;

import Jama.*;

public class EVD {
	public StringBuffer EVDlist = new StringBuffer();
	public float[][] EVDCoords;
	public static float[][] coordMatrix;
	public static void main(String[] args) throws IOException {		
	}
	
	public EVD (float[][] cmSparse, HashMap<String, WordAttributes> termsToIndex) {
		try {
			WordAttributes[] t = termsToIndex.values().toArray(new WordAttributes[0]);
			doEVD(cmSparse, t);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doEVD(float[][] covarMatrix, WordAttributes[] termsToIndex/*BufferedWriter bw,FileWriter fw,int[] idxArray*/) throws Exception {                        	
	//Pass the covariance matrix to the SVD function
//		FileWriter fw = new FileWriter("objectFiles/EVD.txt");
//		BufferedWriter bw = new BufferedWriter(fw);
//		
//    	FileInputStream fis = new FileInputStream("objectFiles/idxArray.idx");
//		ObjectInputStream ois = new ObjectInputStream(fis);
//		int[] idxArray = (int[]) ois.readObject();
		
		ArrayList<String> idxArray = TermSelectionStepPanelFrequency.idxArray;
		EVDCoords = new float [idxArray.size()][covarMatrix[0].length];
		
		EVDlist.append('\n');
	    System.out.println("EVD");
		EVDlist.append("<<EVD>>");
		EVDlist.append('\n');
		System.out.println("Covar[0][0]=" + covarMatrix[0][0]);
		System.out.println("Matrix B");
	    Matrix B = new Matrix(covarMatrix);
	    //TODO uncomment this System.out.println("rank = " + B.rank());
	    System.out.println("Eigenvalue Decomp S1");
	    EigenvalueDecomposition S1 = new EigenvalueDecomposition(B);
	    System.out.println("Vector V");
	    Matrix eigenVectorMatrixV = S1.getV();
	    System.out.println("Vector D");
	    Matrix eigenValueMatrixD = S1.getD();
	    //get dimensions of matrix so I can print out results
	    int coldim = eigenVectorMatrixV.getColumnDimension();
	    int rowdim = eigenVectorMatrixV.getRowDimension();
	    System.out.println("coldim = " + coldim + ", rowdim = " + rowdim);
	    System.out.println(" ");
	    System.out.println("Eigenvector V");
	    EVDlist.append("<<Eigenvector V>>:");
	    EVDlist.append('\n');/*
	    for (int idxr = 0; idxr < rowdim; idxr++){
	    	for (int idxc = 0; idxc < coldim; idxc++){
	    		//System.out.print(eigenVectorMatrixV.get(idxr,idxc) + " ");
	    		/////EVDlist.append(eigenVectorMatrixV.get(idxr,idxc) + " ");
	    	}    
	    	//System.out.println();
	    	/////EVDlist.append('\n');
        	/////EVDlist.append("********************");
        	/////EVDlist.append('\n');
	    }
	    */
	    coldim = eigenValueMatrixD.getColumnDimension();
	    rowdim = eigenValueMatrixD.getRowDimension();
	    System.out.println("Eigenvalues D");
	    EVDlist.append('\n');
	    EVDlist.append("<<Eigenvalues D>>:");
	    EVDlist.append('\n');
	    float vectorD[] = new float [rowdim]; //row and col dimensions are same
	    float tempvectorD[] = new float [rowdim];
	    for (int idxr = 0; idxr < rowdim; idxr++){   		
	    		//create single vector of absolute values of eigenvalues        		
	    			vectorD[idxr] = Math.abs(eigenValueMatrixD.get(idxr,idxr));        			
	    			//System.out.println(vectorD[idxr]); 
	    			/////EVDlist.append(Double.toString(vectorD[idxr]));
	    	}     
	    tempvectorD = vectorD;
	    //using our single vector of eigenvalues - rank them from largest to smallest
	    Arrays.sort(tempvectorD); //Java uses merge sort here                
	    float vectorDreverse[] = new float[rowdim];
	    //Print out sorted array
	    //EVDlist.append('\n');
	    //System.out.println("Reverse-sorted Eigenvalues D");
	    //EVDlist.append("<<Reverse-sorted Eigenvalues D>>:");
	    //EVDlist.append('\n');
	    for (int idxr = 0; idxr < rowdim; idxr++){
	    	vectorDreverse[idxr] = vectorD[rowdim - idxr - 1];
	    	//System.out.println(vectorDreverse[idxr]); 
	    	/////EVDlist.append(Double.toString(vectorDreverse[idxr])); 
	    }
	    //find eigenvectors corresponding to these eigenvalues - the largest two are
	    //the primary axes:
	    //get positions of eigenvalues from largest to smallest (to locate corresponding
	    //eigenvector rows
	    float positions[] = new float [rowdim];	    
	    for (int idxRev = 0; idxRev < rowdim; idxRev++){
	    	for (int idxD = 0; idxD < rowdim; idxD++){
	    		if (vectorDreverse[idxRev] == vectorD[idxD]){
	    			//found position of nth largest eigenvalue
	    			positions[idxRev] = idxD;	    			
	    		}        	
	    	}
	    }
	    //convert values in covariance matrix to be of the same coordinate system
	    //by taking dot products of each row of covar matrix with each eigenvector
	    //to get single value for each coordinate in eigenvector space
	    //
	    //create coordinateMatrix where each row represents a term,
	    //each column is the coordinate for that term (1st col is x coord, 2nd is y, etc.)
	    coordMatrix = new float [rowdim][coldim];
	    float sum = 0, part = 0;
	    //System.out.println();
	    EVDlist.append('\n');
	    System.out.println("Coord Matrix");
	    EVDlist.append("<<Coord Matrix>>:");
	    EVDlist.append('\n');
	    System.out.println("rowdim=" + rowdim);
	    System.out.println("positions.length=" + positions.length);
	    for (int idxr = 0; idxr < rowdim; idxr++){        		    	
	    	//EVDlist.append('\n');
	    	//EVDlist.append(idxr + " row=");
	    	float aa, bb;
	    	for (int idx0 = 0; idx0 < positions.length; idx0++){	    		
	    		for (int idxc = 0; idxc < coldim; idxc++){        		    	
	    			//get row of covariance matrix, DOT with each eigenvector from largest to smallest
	    			//to get coordinates (cols)
	    			aa = covarMatrix[idxr][idxc];
	    			bb = eigenVectorMatrixV.get((int) positions[idx0],idxc);
	    			part = aa * bb;
	    			sum += part;        			
	    		}//end for
	    		coordMatrix[idxr][idx0] = sum;
	    		sum = 0;
	    		//if (idx0 == 0 | idx0 == 1){
	    			//System.out.print(coordMatrix[idxr][idx0] + " ");
	    			//EVDlist.append(coordMatrix[idxr][idx0] + "*");
	    		//}//end if
	    	}//end for	    	        	
        	//EVDlist.append('\n');
	    }//end for
	    //only write row of matrix if it matches row for a noun within our thresholds (use idxArray)
	    for (int i=0; i<coordMatrix.length; i++){
	    	termsToIndex[i].setVals(coordMatrix[i][0],coordMatrix[i][1]);
	    	termsToIndex[i].setCoordIndex(i);
	    }
		int idx=0;		
//		System.out.println("arraylength=" + idxArray.length);
		int arrLen = coordMatrix.length;
		for (String word : idxArray){
			EVDlist.append(Tagger.termsToIndex.get(word).getXVal() + Tagger.termsToIndex.get(word).getYVal());
			EVDCoords[idx][0] = Tagger.termsToIndex.get(word).getXVal();
			EVDCoords[idx][1] = Tagger.termsToIndex.get(word).getYVal();
			idx++;
		}
//	    for (int x = 0; x < idxArray.length; x++){
//	    	idx = idxArray[x];
//	    	//only output the first 250 columns - this is how many dimensions we'll use			
//	    	//due to limitations with Microsoft Excel - # columns (for now)
//	    	int end = Math.min(arrLen,250);	   
//	    	end = 10;
//	    	end = EVDCoords.length;
//	    	for (int y = 0; y < end; y++){
//	    		EVDlist.append(coordMatrix[idx][y] + "/");
//	    		EVDCoords[x][y] = coordMatrix[idx][y];
//	    	}
//	    	EVDlist.append('\n');
//	    }

	    /*
	    FileOutputStream fos = new FileOutputStream("objectFiles/coordMatrix.cm");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(coordMatrix);*/
		
		/*fos = new FileOutputStream("objectFiles/termsToIndex.tti");
		oos = new ObjectOutputStream(fos);
		oos.writeObject(termsToIndex);*/
		
	    EVDlist.append("********");
	    
	    //call Word Sense Disambiguation routine
	    //WSD.doWSD(coordMatrix, bw, fw);	    
	    //call Cluster Analysis routine
	    //HCA.doHCA(coordMatrix, bw, fw);
//	    fos.close();
//	    oos.close();
//	    bw.close();
//	    fw.close();	    
	    System.out.println("DONE! Please review your data output file.");
//		Scanner scan = new Scanner(new File("objectFiles/EVD.txt"));
//		while (scan.hasNextLine()){
//			EVDlist = EVDlist + scan.nextLine() + '\n';
//		}
	}//end doEVD
	
}//end EVD class
