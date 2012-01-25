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
import java.util.*;

import cern.colt.matrix.impl.SparseFloatMatrix2D;

import com.JavaIndexer.generics.WordAttributes;

public class TFIDF {
	public static StringBuffer TFIDFlist = new StringBuffer();
	public static float[][] covarMatrix;
	public void main(String[] args) throws IOException {
	}
	public TFIDF (HashMap<String, WordAttributes> termsToIndex, int[][] po) {
		try {
			WordAttributes[] t = termsToIndex.values().toArray(new WordAttributes[0]);
			getTermFreq(t, po);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getTermFreq(WordAttributes[] termsToIndex,
			int[][] po/*, BufferedWriter bw, FileWriter fw,
			int[] idxArray*/) throws IOException {
		// construct n x m array, where n is # of sentences, m is # of terms to
		// index
		TFIDFlist.append("\n<<TFIDF>>\n");
		int numSentences = po.length;
		int numTermsToIndex = termsToIndex.length;
		TFIDFlist.append("Number of sentences = " + numSentences);
		TFIDFlist.append('\n');
		TFIDFlist.append("Number of terms to index = " + numTermsToIndex);
		TFIDFlist.append('\n');

		System.out.println("Number of paragraphs = " + numSentences);
		System.out.println("Number of terms to index = " + numTermsToIndex);

		// FIRST PASS: get # of occurences of each term in each sentence
		// relative to total terms in sentence
		//float[][] termByDocMatrix = new float[numSentences][numTermsToIndex];
		SparseFloatMatrix2D termByDocMatrix = new SparseFloatMatrix2D(numSentences, numTermsToIndex);
		int paragraphNum;
		float theCount;
		for (int a=0; a<numTermsToIndex; a++){
			for (int b=0; b<po.length; b++){
				theCount = 0;
				for (int c=0; c<termsToIndex[a].getCount(); c++){
					if (termsToIndex[a].getStartIndex(c) >= po[b][0] && termsToIndex[a].getStartIndex(c) <= po[b][1]){
						theCount++;
					}
				}
				if (po[b][2] == 0.0){			//TODO delete this
					po[b][2] = 1;
				}
				termByDocMatrix.setQuick(b,a,theCount/po[b][2]);
			}
		}
//		TFIDFlist.append("<<List of terms>>");
/*		for (int aa = 0; aa < numSentences; aa++) {
			for (int bb = 0; bb < numTermsToIndex; bb++) {
				// get bb-th term to index from termsToIndex array]
				theCount = 0;
				int length=0;
				for (int searchTermNum = 0; termsToIndex[bb].hasSearchTerm(searchTermNum); searchTermNum++){
					for (int i=paragraphOffsets[aa]+6; i < paragraphOffsets[aa+1]; i++){
						length = po.indexOf(' ', i);
						if (termsToIndex[bb].getSearchTerm(searchTermNum).compareTo(po.substring(i, i+length)) == 0)
					}
					
					for (int i=0; i<paragraphArrayList.get(aa).size(); i++){
						if (termsToIndex[bb].getSearchTerm(searchTermNum).compareTo(paragraphArrayList.get(aa).get(i).getSearchTerm(0)) == 0
							&& termsToIndex[bb].getTag(searchTermNum).compareTo(paragraphArrayList.get(aa).get(i).getTag(0)) == 0){
								theCount++;
						}
					}
				}
				termByDocMatrix.setQuick(aa,bb,theCount/paragraphArrayList.get(aa).size());
			}
		}
		*/
		TFIDFlist.append('\n');
		getTermWeight(termByDocMatrix, numTermsToIndex, numSentences/*,
				idxArray*/);
		
	}// end getTermFreq
	public static void getTermWeight(SparseFloatMatrix2D termByDocMatrix,
			int numTermsToIndex, int numSentences) throws IOException {
		// SECOND PASS: revise weightings based on # of sentences in which the
		// term occurs
		float[] IDFArray = new float[numTermsToIndex];
		int termSentenceCount = 0;
		for (int c = 0; c < numTermsToIndex; c++) {
			for (int d = 0; d < numSentences; d++) {
				if (termByDocMatrix.get(d,c) != new Float(0.0)) {
					// System.out.println("term="+termByDocMatrix[d][c]);
					termSentenceCount += 1;
				}
			}
			// 6/5/06 SCL - if termSentenceCount = 0, change to 1 (term must
			// occur at least once in the corpus!)
			if (termSentenceCount == 0) {
				termSentenceCount = 1;
			}
			// IDF = log (Num sentences / Num sentences containing term)
			// System.out.println("idx="+c+",numSent="+numSentences+",termSentCt="+termSentenceCount);
			IDFArray[c] = (float) Math.log((double)numSentences
					/ termSentenceCount); 
			termSentenceCount = 0; // reset for next term
		}
		for (int a1 = 0; a1 < numSentences; a1++) {
			for (int b1 = 0; b1 < numTermsToIndex; b1++) {
				// TF = termByDocMatrix[a1][b1];
				termByDocMatrix.setQuick(a1,b1,termByDocMatrix.get(a1,b1)*IDFArray[b1]); // TF x IDF
			}
		}
		getTermNorm(termByDocMatrix, numTermsToIndex, numSentences/*,
				idxArray*/);
	}// end getTermWeight

	public static void getTermNorm(SparseFloatMatrix2D termByDocMatrix,
			int numTermsToIndex, int numSentences/*, int[] idxArray*/) throws IOException {
		// THIRD PASS: Normalize the values in the matrix
		float[] NormArray = new float[numSentences];
		float val = 0;
		for (int e1 = 0; e1 < numSentences; e1++) {
			for (int f1 = 0; f1 < numTermsToIndex; f1++) {
				val = val + (float) Math.pow(termByDocMatrix.get(e1,f1), 2);
			}
			NormArray[e1] = (float) Math.sqrt(val);
			val = 0;
		}
		for (int a2 = 0; a2 < numSentences; a2++) {
			for (int b4 = 0; b4 < numTermsToIndex; b4++) {
				// Divide original TFIDF values by normalizing value
				if (NormArray[a2] != 0.0) {
					termByDocMatrix.setQuick(a2,b4,termByDocMatrix.get(a2,b4)/ NormArray[a2]); // TF x IDF
				} else {
					termByDocMatrix.setQuick(a2,b4,0);
				}
			}
		}

		// Print out Term-By-Document matrix
		int idx = 0, idx1 = 0;
		System.out.println("T-by-D Matrix:");
		TFIDFlist.append('\n');
		TFIDFlist.append("<<T-by-D Matrix>>:");
		TFIDFlist.append('\n');
		/*for (idx = 0; idx < numSentences; idx++) {
			for (idx1 = 0; idx1 < numTermsToIndex; idx1++) {
//				 System.out.print(termByDocMatrix[idx][idx1]+" ");
//				if (idx < 10){
//					if (idx1 < 10){
						//write out first ten terms and first ten documents
						TFIDFlist.append(termByDocMatrix.get(idx,idx1) + "/");
//					}
			//	}				
			}
			// System.out.println();
			TFIDFlist.append('\n');
			TFIDFlist.append("********************");
			TFIDFlist.append('\n');
		}*/

		getCovarMatrix(termByDocMatrix, numTermsToIndex, numSentences/*,
				idxArray*/);
	
	}// end getTermNorm

	public static void getCovarMatrix(SparseFloatMatrix2D termByDocMatrix,
			int numTermsToIndex, int numSentences) throws IOException {
		// Get mean vector and covariance matrix that correspond to TF-IDF
		// matrix
		// System.out.println();
		TFIDFlist.append('\n');
		// Mean Vector:
		float meanVector[] = new float[numTermsToIndex];
		System.out.println("Mean Vector = ");
		TFIDFlist.append("<<Mean Vector>>:");
		TFIDFlist.append('\n');
		for (int idx1 = 0; idx1 < numTermsToIndex; idx1++) {
			float sum = 0;
			for (int idx = 0; idx < numSentences; idx++) {
				sum += termByDocMatrix.get(idx,idx1); // termByDocMatrix
			}
			meanVector[idx1] = sum / (float) numSentences;
			// System.out.print(meanVector[idx1] + " ");
			//write out first ten values in mean vector
			if (idx1 < 10){
				TFIDFlist.append(meanVector[idx1] + " ");
			}
		}
		// System.out.println();
		TFIDFlist.append('\n');
		// Covariance Matrix:
		covarMatrix = new float[numTermsToIndex][numTermsToIndex];
		float sum, variance, sumA, sumB, covariance;
		for (int idx = 0; idx < numTermsToIndex; idx++) {
			for (int idx1 = 0; idx1 < numTermsToIndex; idx1++) {
				if (idx == idx1) { // on the diagonal - calculate variance
					// loop through each instance of term (numSentences)
					// for each one, subtract mean for that term (from
					// meanVector) and
					// square it.
					// sum each of these results together, finally divide by n-1
					sum = 0;
					for (int idx2 = 0; idx2 < numSentences; idx2++) {
						sum += Math.pow(
								(termByDocMatrix.getQuick(idx2,idx) - meanVector[idx]),
								2);
					}
					// float variance = sum / (numSentences - 1);
					variance = sum / numSentences; // use population
															// variance instead
															// of sample
															// variance
					covarMatrix[idx][idx1] =variance;
				}// end if
				else { // calculate covariance
					// loop through each instance of term (numSentences)
					// for each of the two terms, subtract mean for the term
					// (from meanVector)
					// and then multiply the two results together.
					// sum all of these products together, finally divide by n-1
					sum = 0;
					for (int idx2 = 0; idx2 < numSentences; idx2++) {
						sum += (termByDocMatrix.getQuick(idx2,idx) - meanVector[idx]) * 
								(termByDocMatrix.getQuick(idx2,idx1) - meanVector[idx1]);
						/*sumA = termByDocMatrix.getQuick(idx2,idx) - meanVector[idx];
						sumB = termByDocMatrix.getQuick(idx2,idx1) - meanVector[idx1];
						sum += sumA * sumB; */
					}
					// float covariance = sum / (numSentences - 1);
					covariance = sum / numSentences; // use population
															// variance instead
															// of sample
															// variance
					covarMatrix[idx][idx1] = covariance; 
				}// end else
			}// end for
		}// end for
		// print out covariance matrix
		TFIDFlist.append('\n');
		System.out.println("Covar Matrix = ");
		TFIDFlist.append("<<Covar Matrix>>:");
		TFIDFlist.append('\n');
		TFIDFlist.append("(Number of rows or cols: " + numTermsToIndex + ")");
		TFIDFlist.append('\n');
	/*	for (int idx = 0; idx < numTermsToIndex; idx++) {
			for (int idx1 = 0; idx1 < numTermsToIndex; idx1++) {
				// System.out.print(covarMatrix[idx][idx1]+" ");
			//	if (idx < 10){
				//	if (idx1 < 10){
						//write out first ten rows/columns
						TFIDFlist.append(covarMatrix[idx][idx1]+"/");
			//		}
			//	}
			}
			// System.out.println();
			TFIDFlist.append('\n');
			TFIDFlist.append("********************");
			TFIDFlist.append('\n');
		}*/
		/*
		FileOutputStream fos = new FileOutputStream("objectFiles/covarMatrix.cm");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(covarMatrix);
		oos.close();
		fos.close();*/
		
		
//		EVD.doEVD(covarMatrix, bw, fw/*, idxArray*/);

	}// end getCovarMatrix

	/*
	 * author Johan Knngrd, http://dev.kanngard.net param s - The source
	 * string param search - The substring to search for in 's' param replace -
	 * The string that will replace 'search' return The changed string
	 */
	public static String replaceSubString(String s, String search,
			String replace) {
		int p = 0;
		while (p < s.length() && (p = s.indexOf(search, p)) >= 0) {
			s = s.substring(0, p) + replace
					+ s.substring(p + search.length(), s.length());
			p += replace.length();
		}
		return s;
	}// end replaceSubString

	// Shelly C. Lukon
	// loop through each sentence (string) and count words (using spaces as
	// delimiters between words)
	public static int wordCount(String s) {
		int count = 0;
		char prevChar = ' ';
		for (int i = 0; i < s.length(); i++) {
			switch (s.charAt(i)) {
			case ' ':
				if (prevChar != ' ') {
					count += 1;
				}
				break;
			default:
				break;
			}
			prevChar = s.charAt(i);
		}
		return count;
	}// end wordCount

	private int countParagraphs(ArrayList<Integer> paragraphOffsetsAL, String originalText) {
		int count=1;
		int idx =0;
		while((idx = originalText.indexOf("<E-O-P>", idx)) != -1 ){
			paragraphOffsetsAL.add(idx++);
			count++;
		}
		return count;
	}
}
