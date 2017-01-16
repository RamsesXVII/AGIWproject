package relationIdentifier;

import FSM.RelationalFilter;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class RelationalIdentifier {
	private	RelationalFilter rf;
	private MaxentTagger tagger;

	public RelationalIdentifier(){
		this.rf= new RelationalFilter();
		this.tagger = new MaxentTagger("models/english-left3words-distsim.tagger");

	}

	public boolean isRelational(String phrase){
		String tagged = tagger.tagString(phrase);

		String[] tagsArray = tagged.split(" ");  

		int i= 0;

		for ( String ss : tagsArray) {
			tagsArray[i]=ss.substring(ss.indexOf("_")+1);
			i++;
		}

		return rf.isRelational(tagsArray);

	}


}
