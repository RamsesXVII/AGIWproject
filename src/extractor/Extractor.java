package extractor;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import parser.XMLParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import edu.stanford.nlp.util.PropertiesUtils;
import fileManagement.FileInteractor;


public class Extractor {
	private StanfordCoreNLP pipeline;

	public Extractor() {
		this.pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties("annotators", "tokenize, ssplit, pos, lemma, ner, parse"));

	}

	public List<String> extractFacts(List<String> cleanPhraseAndEntitesList) {
		Map<String,List<List<String>>> relationsMap=new HashMap<>();
		List<String> relations=new ArrayList<>();

		List<String> phraseEntitiesList = new ArrayList<>();
		for (int i = 1; i<cleanPhraseAndEntitesList.size() ;i++) 
			phraseEntitiesList.add(cleanPhraseAndEntitesList.get(i));

		String cleanPhrase = cleanPhraseAndEntitesList.get(0);
		DependenciesExpert dependenciesExpert=new DependenciesExpert(cleanPhrase,pipeline);
		String phraseEnhancedDependenciesXML=dependenciesExpert.getEnhancedDependenciesOfSingleSentence();

		try {

			XMLParser x=new XMLParser(phraseEnhancedDependenciesXML);
			NodeList nodes = x.getNodesByTag("dep");
			DependenciesNavigator dependenciesNavigator = new DependenciesNavigator(nodes);

			int finalPosition = 0;
			Element finalElement = null;
			Set<String> typeNmod = dependenciesExpert.getTypesOfNmod(nodes);
			for (String type : typeNmod) {
				Map<Element, List<Element>> governor2dependentsByNmod = dependenciesNavigator.governor2dependents(type);

				// ogni governor di un nmod va analizzato come frase
				for (Element governor : governor2dependentsByNmod.keySet()) {
					List<String> dependentsNmodName = new ArrayList<>();
					// per ora aggiunge solo un soggetto
					List<String> dependentsNsubjName = new ArrayList<>();
					List<Element> dependents = governor2dependentsByNmod.get(governor);
					if(dependents.size()>1){
						for(Element e : dependents)
							dependentsNmodName.add(e.getTextContent());

						finalPosition = dependenciesNavigator.lowerPosition(dependents);
						finalElement = dependenciesNavigator.getGovernorByPosition(finalPosition);

						if (finalElement!=null)
							finalPosition = dependenciesNavigator.lowerPositionDependent(finalElement, "compound");

						int startPosition = dependenciesNavigator.startPositionNsubj(governor);

						if(startPosition!=0)
							dependentsNsubjName.add(dependenciesNavigator.getElementByPosition(startPosition).getTextContent());


						dependenciesExpert= new DependenciesExpert(dependentsNmodName,phraseEntitiesList,dependentsNsubjName,startPosition,finalPosition,nodes);

						List<String> entityDependenciesList = dependenciesExpert.getEntityDependenciesList();
						List<String> entityNsubjList = dependenciesExpert.getEntityNsubjList();
						List<String> relationEntities = dependenciesExpert.getRelationEntities();

						FileInteractor fileInteractor= new FileInteractor();
						String relation=fileInteractor.writeFactsExtractedOnFile(cleanPhrase,phraseEntitiesList,relationEntities,entityDependenciesList,entityNsubjList);

						List<List<String>> subjDep = new ArrayList<>();
						relationsMap.put(relation, subjDep);
						relations.add(relation);

					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return relations;
	}
}