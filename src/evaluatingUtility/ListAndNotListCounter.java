package evaluatingUtility;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import parser.TSVSentencesUtility;

public class ListAndNotListCounter {

	private String pathToFile;

	public ListAndNotListCounter(String pathToFile){
		this.pathToFile=pathToFile;

	}


	public double getListCount(){

		int listCount=0;

		TSVSentencesUtility t= new TSVSentencesUtility();
		List<String[]> allRows;
		
		try {

			allRows = t.getAllSentencesFromTSV(this.pathToFile);

			for(String[] TSVsentence : allRows) {
				if(TSVsentence[4].contains("Y"))
					listCount++;
			}

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1 ) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch(NullPointerException e1 ) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return (double)listCount;
	}

	public int getNotListCount(){

		int notListCount=0;

		TSVSentencesUtility t= new TSVSentencesUtility();

		List<String[]> allRows;
		try {

			allRows = t.getAllSentencesFromTSV(this.pathToFile);

			for(String[] TSVsentence : allRows) {

				if(TSVsentence[4].contains("N"))
					notListCount++;
			}

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1 ) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch(NullPointerException e1 ) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return notListCount;
	}

	public void setPathToFile(String pathToFile){
		this.pathToFile=pathToFile;
	}


}
