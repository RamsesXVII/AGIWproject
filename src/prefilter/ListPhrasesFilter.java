package prefilter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Callable;

import parser.TSVSentencesUtility;

/*
 * Classe che prese le frasi del file input le divide in probabili frasi lista inserendole in un file 
 * acceptedx.tsv
 */
public class ListPhrasesFilter implements Callable<Integer> {
	private String pathToSentencesFile;
	private String pathToAcceptedSentencesFile;
	private String pathToRefusedSentencesFile;

	private PhraseScoreGetter scoreGetter;

	private TSVSentencesUtility tSVSentencesUtility;

	private boolean metricsUtilityOn;


	public ListPhrasesFilter(String pathToSentencesFile,String pathToAcceptedSentencesFile, String pathToRefusedSentencesFile, boolean metricsUtilityOn){
		this.setPathToSentencesFile(pathToSentencesFile);
		this.setPathToAcceptedSentencesFile(pathToAcceptedSentencesFile);
		this.setPathToRefusedSentencesFile(pathToRefusedSentencesFile);

		this.scoreGetter= new PhraseScoreGetter();

		this.tSVSentencesUtility=new TSVSentencesUtility();

		this.metricsUtilityOn=metricsUtilityOn;
	}

	@Override
	public Integer call() throws Exception {
		try {

			PrintWriter acceptedWriter = new PrintWriter(this.pathToAcceptedSentencesFile, "UTF-8");
			PrintWriter refusedWriter = new PrintWriter(this.pathToRefusedSentencesFile, "UTF-8");

			List<String[]> allRows = this.tSVSentencesUtility.getAllSentencesFromTSV(this.pathToSentencesFile);

			double threashold=this.scoreGetter.getThreashold();

			for(String[] TSVsentence : allRows) {

				try
				{
					double score=scoreGetter.getScore(TSVsentence[3]);

					if(score>threashold)
						this.writeOnAccepted(acceptedWriter,TSVsentence,score);
					else
						this.writeOnRefused(refusedWriter,TSVsentence,score);

				}catch(NullPointerException e){
					System.out.println(" riga vuota");}

			} 

			acceptedWriter.flush();
			acceptedWriter.close();

			refusedWriter.flush();
			refusedWriter.close();

		}catch (UnsupportedEncodingException e) {
			System.out.println("errore");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("errore path");
			e.printStackTrace();

		}

		return 1;

	}

	private void writeOnRefused(PrintWriter refusedWriter, String[] TSVsentence, double score) {
		if(this.metricsUtilityOn)
			refusedWriter.println(TSVsentence[0]+"\t"+TSVsentence[1]+"\t"+TSVsentence[2]+"\t"+TSVsentence[3]+"\t"+TSVsentence[4]+"\t"+score);
		else
			refusedWriter.println(TSVsentence[0]+"\t"+TSVsentence[1]+"\t"+TSVsentence[2]+"\t"+TSVsentence[3]+"\t");



	}

	private void writeOnAccepted(PrintWriter acceptedWriter, String[] TSVsentence, double score) {
		if(this.metricsUtilityOn)
			acceptedWriter.println(TSVsentence[0]+"\t"+TSVsentence[1]+"\t"+TSVsentence[2]+"\t"+TSVsentence[3]+"\t"+TSVsentence[4]+"\t"+score);
		else
			acceptedWriter.println(TSVsentence[0]+"\t"+TSVsentence[1]+"\t"+TSVsentence[2]+"\t"+TSVsentence[3]+"\t");



	}

	public String getPathToSentencesFile() {
		return pathToSentencesFile;
	}

	public void setPathToSentencesFile(String pathToSentencesFile) {
		this.pathToSentencesFile = pathToSentencesFile;
	}

	public String getPathToAcceptedSentencesFile() {
		return pathToAcceptedSentencesFile;
	}

	public void setPathToAcceptedSentencesFile(String pathToAcceptedSentencesFile) {
		this.pathToAcceptedSentencesFile = pathToAcceptedSentencesFile;
	}

	public String getPathToRefusedSentencesFile() {
		return pathToRefusedSentencesFile;
	}

	public void setPathToRefusedSentencesFile(String pathToRefusedSentencesFile) {
		this.pathToRefusedSentencesFile = pathToRefusedSentencesFile;
	}



}
