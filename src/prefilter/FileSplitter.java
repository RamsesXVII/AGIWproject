package prefilter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
 * Splitta un file in più file, tenendo conto del numero di thread che verranno creati
 */
public class FileSplitter {
	private String pathToFileTosplit;
	private String fileName;
	private int numbersOfFileToCreate;

	public FileSplitter(String pathToFileToSplit){
		this.pathToFileTosplit=pathToFileToSplit;

		this.numbersOfFileToCreate=Runtime.getRuntime().availableProcessors();
		
		this.fileName=pathToFileToSplit.substring(0, pathToFileToSplit.length()-4);//elimina estensione
		
	}

	/*
	 * I nomi dei file creati (contengono e frasi che verranno analizzate dal prefiltro sono nella forma 
	 * sentencesx.tsv con x=1,..,nmaxthread
	 */
	public void splitFile(){

		try{  

			// Reading file and getting no. of files to be generated  
			String inputfile = this.pathToFileTosplit; //  Source File Name.  

			int fileLinesCount = 0;  

			fileLinesCount=this.getNumberOfLines(inputfile)+1;

			System.out.println("Lines in the file: " + fileLinesCount);     // Displays no. of lines in the input file.  

			int nol = fileLinesCount/this.numbersOfFileToCreate; //  No. of lines to be split and saved in each output file.  

			double temp = (fileLinesCount/nol);  
			int temp1=(int)temp;  
			int nof=0;  

			if(temp1==temp)  
			{  
				nof=temp1;  
			}  
			else  
			{  
				nof=temp1+1;  
			}  

			System.out.println("No. of files to be generated :"+nof); 
			// Displays no. of files to be generated.  

			//---------------------------------------------------------------------------------------------------------  

			// Actual splitting of file into smaller files  

			FileInputStream fstream = new FileInputStream(inputfile); DataInputStream in = new DataInputStream(fstream);  

			BufferedReader br = new BufferedReader(new InputStreamReader(in)); String strLine;  

			for (int j=1;j<=nof;j++)  
			{  
				FileWriter fstream1 = new FileWriter(this.fileName+j+".tsv");     // Destination File Location  
				BufferedWriter out = new BufferedWriter(fstream1);   
				for (int i=1;i<=nol;i++)  
				{  
					strLine = br.readLine();   
					if (strLine!= null)  
					{  
						out.write(strLine);   
						if(i!=nol)  
						{  
							out.newLine();  
						}  
					}  
				}  
				out.close();  
			}  

			in.close();  
		}catch (Exception e)  
		{  
			System.err.println("Error: " + e.getMessage());  
		}  

	}

	private int getNumberOfLines(String inputfile) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(inputfile));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}}
