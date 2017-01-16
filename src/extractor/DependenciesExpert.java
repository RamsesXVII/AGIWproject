package extractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class DependenciesExpert {
	private List<String> dependentsNmodName;
	private List<String> phraseEntitiesList;
	private List<String> dependentsNsubjName;
	private int startPosition;
	private int finalPosition;
	private NodeList nodes;
	
	private String cleanPhrase;
	private StanfordCoreNLP pipeline;

	public DependenciesExpert(List<String> dependentsNmodName, List<String> phraseEntitiesList,
			List<String> dependentsNsubjName, int startPosition, int finalPosition,
			NodeList nodes) {
		this.dependentsNmodName=dependentsNmodName;
		this.phraseEntitiesList=phraseEntitiesList;
		this.dependentsNsubjName=dependentsNsubjName;
		this.startPosition=startPosition;
		this.finalPosition=finalPosition;
		this.nodes=nodes;
	}
	
	public DependenciesExpert(String cleanPhrase, StanfordCoreNLP pipeline) {
		this.cleanPhrase=cleanPhrase;
		this.pipeline=pipeline;
	}

	public List<String> getEntityDependenciesList() {
		List<String> entityListDep = new ArrayList<>();
		for (String nameDepNmod : dependentsNmodName) {
			for (String nameEntity : phraseEntitiesList){
				if (nameEntity.contains(nameDepNmod))
					entityListDep.add(nameEntity);
			}
		}
		return entityListDep;
	}

	public List<String> getEntityNsubjList() {
		List<String> entityListNsubj = new ArrayList<>();
		for (String nameDepNsubj : dependentsNsubjName) {
			for (String nameEntity : phraseEntitiesList){
				if (nameEntity.contains(nameDepNsubj))
					entityListNsubj.add(nameEntity);
			}
		}
		return entityListNsubj;
	}
	
	public List<String> getRelationEntities() {
		List<String> relationEntities = new ArrayList<>();
		for(int i = startPosition+1; i<finalPosition; i++){
			for (int j = 0; j < nodes.getLength(); j++) {
				Element currentElement = (Element) nodes.item(j);
				if(i==Integer.parseInt(currentElement.getElementsByTagName("governor").item(0).getAttributes().getNamedItem("idx").getNodeValue())){
					relationEntities.add(currentElement.getElementsByTagName("governor").item(0).getTextContent());
					break;
				}
				else if(i==Integer.parseInt(currentElement.getElementsByTagName("dependent").item(0).getAttributes().getNamedItem("idx").getNodeValue())){
					relationEntities.add(currentElement.getElementsByTagName("dependent").item(0).getTextContent());
					break;
				}
			}
		}
		return relationEntities;
	}

	public String getEnhancedDependenciesOfSingleSentence() {

		String phraseEnhancedDependenciesXML=null;
		Annotation document =new Annotation(cleanPhrase);
		pipeline.annotate(document);

		List<CoreMap> singlePhraseCoreMap =document.get(SentencesAnnotation.class);

		for(CoreMap sentence: singlePhraseCoreMap) {
			phraseEnhancedDependenciesXML= sentence.get(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class).toString(SemanticGraph.OutputFormat.XML);
		}
		return phraseEnhancedDependenciesXML;
	}
	
	public Set<String> getTypesOfNmod(NodeList nodes) {
		Set<String> depType = new HashSet<>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			String dep_type = currentNode.getAttributes().getNamedItem("type").getNodeValue();
			if (dep_type.contains("nmod")){
				depType.add(dep_type);
			}
		}
		return depType;
	}
	
}
