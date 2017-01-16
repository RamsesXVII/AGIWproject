package phrasesExpansion;

import java.io.*;

import extractor.FactsListExtractor;
import fileManagement.FileInteractor;
import parser.TSVSentencesUtility;
import prefilter.ParallelFilter;
import splitter.PhraseSplitter;

import java.util.List;

public class mainPhrasesAnalysis {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		//creo due file Accepted.tsv e Refused.tsv
		String pathToInputFile="evaluationcorpus.tsv";
		boolean useMetrics=false; //le frasi del file di input devono essere etichettate!!! (tab Y otab N)
		ParallelFilter pf = new ParallelFilter();
		pf.filterSentences(pathToInputFile,useMetrics);
		
		FactsListExtractor fe= new FactsListExtractor();
		fe.do_extractFacts();
		splitter();
	}

	private static void splitter() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		TSVSentencesUtility t = new TSVSentencesUtility();
		List<String[]> allRows = t.getAllSentencesFromTSV("evaluationcorpus.tsv");

		FileInteractor f = new FileInteractor();

		PhraseSplitter splitter = new PhraseSplitter();
		for(String[] phrase : allRows){
			System.out.println("_______");
			f.writeFile("_______", "splitted.txt");
			List<String> phraseSplitted = splitter.splitPhrase(phrase[3]);
			System.out.println(phrase[3]);
			f.writeFile(phrase[3], "splitted.txt");

			for (String string : phraseSplitted) {
				System.out.println(string);
				f.writeFile(string, "splitted.txt");
			}
		}
	}
}
