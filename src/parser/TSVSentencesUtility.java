package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class TSVSentencesUtility {
	private TsvParserSettings settings;
	private TsvParser parser;

	public TSVSentencesUtility(){
		this.settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		this.parser= new TsvParser(settings);

	}

	private  Reader getFileReader(String absolutePath) throws UnsupportedEncodingException, FileNotFoundException {
		return new InputStreamReader(new FileInputStream(new File(absolutePath)), "UTF-8");
	}

	public List<String[]> getAllSentencesFromTSV(String pathToFile) throws UnsupportedEncodingException, FileNotFoundException{
		return  parser.parseAll(this.getFileReader(pathToFile));
	}

	/**
	 * removeNoWordContent ha in input una frase presa dal file TSV e ne restituisce una lista
	 * @param phrase: frase presa dal TSV
	 * @return result: lista di stringhe, in prima posizione abbiamo la frase pulita dalle parentesi tra
	 * le entità, nelle posizioni successive abbiamo le stringhe delle varie entità presenti es entità:
	 * [[Microsoft|m.04sv4]]
	 */
	public List<String> removeNoWordContent(String phrase){
		List<String> result = new ArrayList<>();
		result.add(phrase.replaceAll("\\[\\[","").replaceAll("\\|[[a-z]*[A-Z]*[0-9]*[.]*[_]*]*\\]\\]","").replaceAll("_", " "));
		int beginIndex = 0;
		int endIndex;
		while (phrase.indexOf("[[",beginIndex)!=-1){
			beginIndex = phrase.indexOf("[[",beginIndex);
			endIndex = phrase.indexOf("]]",beginIndex+1)+2;
			String entity = phrase.substring(beginIndex, endIndex);
			beginIndex = endIndex;
			result.add(entity);
		}
	
		return result;
	}
}
