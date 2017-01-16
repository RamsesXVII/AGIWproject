package extractor;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import parser.TSVSentencesUtility;

public class FactsListExtractor {
	private List<List<String>>result;
	private TSVSentencesUtility tSVutility;
	private List<String[]> allRowsOfFile;
	
	public FactsListExtractor() throws UnsupportedEncodingException, FileNotFoundException{
		this.result=new LinkedList<List<String>>();
		this.tSVutility = new TSVSentencesUtility();
		this.allRowsOfFile = tSVutility.getAllSentencesFromTSV("Accepted.tsv");
	}
	
	public void do_extractFacts(){
		Extractor eb = new Extractor();
		
		for(String[] phrase : allRowsOfFile){
			try {
				List<String> cleanPhraseAndEntitesList = tSVutility.removeNoWordContent(phrase[3]);
				List<String> ar = eb.extractFacts(cleanPhraseAndEntitesList);
				result.add(ar);

			}
			catch(Exception e){
				e.printStackTrace();
			}
	}}

}
