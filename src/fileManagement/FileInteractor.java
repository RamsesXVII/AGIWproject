package fileManagement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import relationIdentifier.RelationalIdentifier;

public class FileInteractor {

	public void deleteFile(String toDelete){
		try{
    		File file = new File(toDelete);
    		if(file.delete())
    			System.out.println(file.getName() + " is deleted!");
    		else
    			System.out.println("Delete operation is failed.");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	public Reader getFileReader(String absolutePath) throws UnsupportedEncodingException, FileNotFoundException {
		return new InputStreamReader(new FileInputStream(new File(absolutePath)), "UTF-8");
	}
	
	public void writeFile(String toWrite, String fileName) throws UnsupportedEncodingException, FileNotFoundException, IOException{
		try(FileWriter fw = new FileWriter(fileName, true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println(toWrite);
			} catch (IOException e) {
			    //exception handling left as an exercise for the reader
			}
	}

	
	public String writeFactsExtractedOnFile(String cleanPhrase, List<String> phraseEntitiesList,List<String> relationProv, List<String> entityListDependencies, List<String> entityListNsubj) throws UnsupportedEncodingException, FileNotFoundException, IOException {


		FileInteractor f = new FileInteractor();
		f.writeFile("Frase Iniziale: "+cleanPhrase, "phrasesExtracted");
		for (String entity : phraseEntitiesList) {
			f.writeFile(entity, "phrasesExtracted");
		}
		String relation = "";
		for (String word : relationProv) {
			relation = relation+word+" ";
		}
		f.writeFile("Frase relazionale: "+relation, "phrasesExtracted");
		RelationalIdentifier ri = new RelationalIdentifier();
		String fatto = "";
		int i = 1;
		for (String string : entityListDependencies) {
			fatto = "";
			if(!entityListNsubj.isEmpty())
				fatto = fatto+entityListNsubj.get(0)+" ";
			fatto = fatto+relation+" ";
			fatto = fatto+string;
			f.writeFile("Fatto "+i+": "+fatto, "phrasesExtracted");
			i++;
		}
		f.writeFile("\n", "phrasesExtracted");

		return relation;
	}

}
