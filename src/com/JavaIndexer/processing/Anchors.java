package com.JavaIndexer.processing;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.String;


/**
 * @author nicole.pernischova
 *
 */
public class Anchors {
	
	public static void main (String[] args) throws Exception{
		
		//InputStreamReader isr = new InputStreamReader(System.in);
		//BufferedReader stdin = new BufferedReader(isr);
		
		//System.out.println("Please enter directory path and filename: ");
		String input = "C:\\Users\\nicole.pernischova\\Desktop\\Thesis\\thesis.tex"; //stdin.readLine();
		File fin = new File(input);
		FileReader fr = new FileReader(fin);
		BufferedReader br = new BufferedReader(fr);
		
		String text = "", c; // text: whitespace
							// c: character from file
		int x;
		
		while(true){		// while not end of file
			x = br.read();  // save to x
			
			if (x == -1)	// breaks if end of file
				break;
			
			c = new Character((char)x).toString(); // gets char from file and turns into string
			text = text + c;  
		}
			fr.close();	// close file
			br.close();
			
			
			//System.out.println("Please enter directory path and filename for the array: ");
			String input2 = "C:\\Users\\nicole.pernischova\\Desktop\\Ind.txt";  //stdin.readLine();
			// java.util.Collections.asList(Object[]);

			 int count = 500;
			 String[]  word = new  String[count];
			 String o = text;

			 String strLine;
			
			    // Open the file that is the first 
			    // command line parameter
			    FileInputStream fstream = new FileInputStream(input2);
			    // Get the object of DataInputStream
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader r = new BufferedReader(new InputStreamReader(in));
			    
			    //Read File Line By Line
			   int j = 0;
			    while ((strLine = r.readLine()) != null)   {
			      // Print the content on the console
			    	word[j] = strLine;
			      j++;
			    }
			    //Close the input stream
			    in.close();
			    

			  //String st = "(?i)";
			  //String backslash = "\\\\";//new java.util.Scanner(System.in).nextLine();
			  String replacement1 = "\\\\index{";
			  String replacement2 = "}";
			for(int i = 0; i < j; i++){
				String regex = word[i];
				String replacement = replacement1.concat(word[i]).concat(replacement2);
					//System.out.println(regex + " " + replacement);
				o = replaceWord(regex,o,replacement); 
				// = "(?i)";
			}
			



		// Produces output to selected file
		//isr = new InputStreamReader( System.in );
		//stdin = new BufferedReader( isr );
		//System.out.print("Please enter the path and filename for the indexed text: ");
	    String outputfile = "C:\\Users\\nicole.pernischova\\Desktop\\Thesis\\OUTPUT.tex";  //stdin.readLine();        
	    FileWriter fw = new FileWriter(outputfile, false);
        BufferedWriter bw = new BufferedWriter(fw);
       
        
        bw.write(o);  
        bw.close();
        fw.close();
        
        // Break point, done when code is finished transferring
         System.out.println("Done");
         		
	}	
	
	 private static String replaceWord(String regex,String text, String indexed){
		    Pattern pattern = Pattern.compile(regex);
		    Matcher matcher = pattern.matcher(text);
		    return matcher.replaceAll(indexed);
		  }

}
