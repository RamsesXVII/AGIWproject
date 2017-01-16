package prefilter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import evaluatingUtility.ListAndNotListCounter;
import evaluatingUtility.PrecisionFalloutRecallGetter;
import parser.TSVSentencesUtility;
/**
 * classe che si occupa del riconoscimento delle frasi che probabilmente sono liste
 */
public class ParallelFilter{

	private int NCPU;

	private ExecutorService pool;
	private ExecutorCompletionService<Integer> ecs;

	public ParallelFilter() {

		this.NCPU=Runtime.getRuntime().availableProcessors();

		this.pool= Executors.newFixedThreadPool(NCPU);

		this.ecs=new ExecutorCompletionService<Integer>(pool);
	}
	/**
	 * Questo metodo restitusce genera un File Accepted tsv che contiene tutte le frasi che verranno passate alla libreria di Stanford
	 * È un metodo parallelo, i vari thread lavoreranno su file diversi e alla fine il risultato verrò ricomposto
	 * Se metrics utility on -> il file in input deve esseere etichettato con Y o N nel 4o campo tab
	 * @param pathToFile
	 */
	public void filterSentences(String pathToFile,boolean metricsUtilityOn) { 
		File f1 = new File("Accepted.tsv"); //mi assicuro che non esistano file già essitenti
		f1.delete();
		f1= new File("Refused.tsv");
		f1.delete();

		FileSplitter fs = new FileSplitter(pathToFile); //divide il file in tanti file quanti sono i thread
		fs.splitFile();

		String fileroot=pathToFile.substring(0, pathToFile.length()-4); //elimina l'estensione


		for(int i=0;i<this.NCPU;i++)
			this.ecs.submit(new ListPhrasesFilter(fileroot+(i+1)+".tsv", "accepted"+(i+1)+".tsv", "refused"+(i+1)+".tsv",metricsUtilityOn));

		int sum=0;

		try {
			for(int i=0; i<this.NCPU;i++){
				sum+=this.ecs.take().get();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		System.out.println("inizio merge");
		for(int i=0; i<this.NCPU;i++)
			this.mergeOutput(i,metricsUtilityOn);

		this.pool.shutdown();

		this.deleteThreadCreatedFiles(fileroot);
		
		if(metricsUtilityOn)
			   this.printMetrics(pathToFile);


	}

	/*
	 * calcola i valori di precision recall e fallout
	 */
	private void printMetrics(String pathToFile) {
		PrecisionFalloutRecallGetter prg= new PrecisionFalloutRecallGetter(pathToFile,"Accepted.tsv", "Refused.tsv");
		  
		  System.out.println("Precision "+ prg.getPrecision());
		  System.out.println("Recall "+ prg.getRecall());
		  System.out.println("Fallout "+ prg.getFallout());

		  
		  ListAndNotListCounter lsc= new ListAndNotListCounter(pathToFile);
		  
		  System.out.println("On a file with");
		  System.out.println(lsc.getListCount()+"list phrases");
		  System.out.println(lsc.getNotListCount()+"not list phrases");
		
	}
	/**
	 * il medoto cancella i file temporanei creati dai vari thread
	 * @param fileroot
	 */
	private void deleteThreadCreatedFiles(String fileroot) {
		/*cancello i file temporanei creati dai vari thread*/
		for(int i=0; i<this.NCPU;i++){
			File f= new File(fileroot+(i+1)+".tsv");
			f.delete();

			f=new File("accepted"+(i+1)+".tsv");
			f.delete();

			f=new File("refused"+(i+1)+".tsv");
			f.delete();

		}

	}

	/**
	 * Ogni thread ha creato un file acceptedx.tsv e un refusedx.tsv
	 * questo metodo fa un merge di questi file generando un unico Accepted e un unico Refused.tsv
	 */
	private void mergeOutput(int i,boolean metricsUtilityOn) {

		try {

			BufferedWriter out = new BufferedWriter(new FileWriter("Accepted.tsv",true));
			TSVSentencesUtility tex= new TSVSentencesUtility();

			List<String[]> allRowsAccepted = tex.getAllSentencesFromTSV("accepted"+(i+1)+".tsv");

			for(String[] TSVsentence : allRowsAccepted) 
				this.writeOnFile(out,TSVsentence,metricsUtilityOn);

			out.close();

			out = new BufferedWriter(new FileWriter("Refused.tsv",true));
			List<String[]> allRowsRefused =tex.getAllSentencesFromTSV("refused"+(i+1)+".tsv");

			for(String[] TSVsentence : allRowsRefused)
				this.writeOnFile(out,TSVsentence,metricsUtilityOn);

			out.close();

		}catch (UnsupportedEncodingException e) {
			System.out.println("errore");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("errore path");
			e.printStackTrace();
		}

	}
	private void writeOnFile(BufferedWriter out, String[] TSVsentence, boolean metricsUtilityOn) throws IOException {
		if(metricsUtilityOn)
			out.write(TSVsentence[0]+"\t"+TSVsentence[1]+"\t"+TSVsentence[2]+"\t"+TSVsentence[3]+"\t"+TSVsentence[4]+"\t"+TSVsentence[5]+"\n");//TODO in versione definitiva cancellare TSV[4]
		else
			out.write(TSVsentence[0]+"\t"+TSVsentence[1]+"\t"+TSVsentence[2]+"\t"+TSVsentence[3]+"\n");
	}


}