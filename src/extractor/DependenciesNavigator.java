package extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DependenciesNavigator {
	private NodeList nodes;
	
	public DependenciesNavigator(NodeList nodes) {
		this.nodes=nodes;
	}
	/**
	 * governor2dependents restituisce una mappa per una specifica dipendenza dove la chiave è 
	 * un governor mentre i valori aono una lista di dependent di quel governor
	 * @param nodes: lista delle dipendenze in formato XML
	 * @param depType: dipendenza che si vuole cercare
	 * @return governor2dependents: mappa per una specifica dipendenza dove la chiave è 
	 * un governor mentre i valori aono una lista di dependent di quel governor
	 */
	public Map<Element, List<Element>> governor2dependents(String depType){
		Map<Element, List<Element>> governor2dependents = new HashMap<>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			Element currentElement = (Element) nodes.item(i);
			String dep_type = currentNode.getAttributes().getNamedItem("type").getNodeValue();
			if(dep_type.contains(depType)){
				Element governor = (Element) currentElement.getElementsByTagName("governor").item(0);
				Element support = null;
				boolean isPresent = false;
				for (Element el : governor2dependents.keySet())
					if (el.getTextContent().equals(governor.getTextContent())
							&& el.getAttributes().getNamedItem("idx").getNodeValue().equals(governor.getAttributes().getNamedItem("idx").getNodeValue())){
						isPresent=true;
						support = el;
					}
				if(isPresent)
					governor2dependents.get(support).add((Element) currentElement.getElementsByTagName("dependent").item(0));
				else {
					List<Element> nsubjDepGov = new ArrayList<>();
					nsubjDepGov.add((Element) currentElement.getElementsByTagName("dependent").item(0));
					governor2dependents.put(governor, nsubjDepGov);
				}
			}
		}
		return governor2dependents;
	}
	/**
	 * lowerPositionDependent ritorna la posizione minore di un governor e delle sue dipendendenze (di un certo tipo)
	 * @param nodes: lista delle dipendenze in formato XML
	 * @param governor: governor di partenza
	 * @param depType: dipendenza che si vuole analizzare
	 * @return finalPosition: la posizione minore di un governor e delle sue dipendendenze (di un certo tipo)
	 */
	public int lowerPositionDependent(Element governor, String depType) {
		//		System.out.println("KKKK"+governor.getTextContent());
		int finalPosition = Integer.parseInt(governor.getAttributes().getNamedItem("idx").getNodeValue());
		Map<Element, List<Element>> governor2dependentsByCompound = this.governor2dependents(depType);
		for (Element el : governor2dependentsByCompound.keySet()) {
			if (el.getTextContent().equals(governor.getTextContent())
					&& el.getAttributes().getNamedItem("idx").getNodeValue().equals(governor.getAttributes().getNamedItem("idx").getNodeValue())){
				finalPosition = lowerPosition(governor2dependentsByCompound.get(el));
			}
		}
		return finalPosition;
	}
	/**
	 * 
	 * @param nodes
	 * @param position
	 * @return
	 */
	public Element getGovernorByPosition(int position) {
		Element element = null;
		for (int i = 0; i < nodes.getLength(); i++) {
			Element currentElement = (Element) nodes.item(i);
			Element governor = (Element) currentElement.getElementsByTagName("governor").item(0);
			int elementPosition = Integer.parseInt(governor.getAttributes().getNamedItem("idx").getNodeValue());
			if (elementPosition==position)
				element = governor;
		}
		return element;
	}
	/**
	 * 
	 * @param nodes
	 * @param position
	 * @return
	 */
	public Element getElementByPosition(int position) {
		Element element = null;
		for (int i = 0; i < nodes.getLength(); i++) {
			Element currentElement = (Element) nodes.item(i);
			Element governor = (Element) currentElement.getElementsByTagName("governor").item(0);
			int governorPosition = Integer.parseInt(governor.getAttributes().getNamedItem("idx").getNodeValue());
			Element dependent = (Element) currentElement.getElementsByTagName("dependent").item(0);
			int dependentPosition = Integer.parseInt(dependent.getAttributes().getNamedItem("idx").getNodeValue());
			if (governorPosition==position)
				element = governor;
			else if (dependentPosition==position)
				element = dependent;
		}
		return element;
	}
	/**
	 * lowerPosition ritorna la posizione minore tra una lista di Element
	 * @param elements: lista degli Element in formato XML
	 * @return lowerPosition: posizione minore tra una lista di Element
	 */
	public int lowerPosition(List<Element> elements) {
		int finalPosition = Integer.parseInt(elements.get(0).getAttributes().getNamedItem("idx").getNodeValue());
		for (Element element : elements) {
			int elementPosition = Integer.parseInt(element.getAttributes().getNamedItem("idx").getNodeValue());
			if (elementPosition<finalPosition) {
				finalPosition = elementPosition;
			}
		}
		return finalPosition;
	}

	/**
	 * 
	 * @param nodes
	 * @param node
	 * @return
	 */
	public List<Element> getAllPreviousElements(Element node) {
		List<Element> previousElements = new LinkedList<>();
		int inputPosition = Integer.parseInt(node.getAttributes().getNamedItem("idx").getNodeValue());

		for (int i = 0; i < nodes.getLength(); i++) {
			Element currentElement = (Element) nodes.item(i);

			Element previousElementDependent = (Element) currentElement.getElementsByTagName("dependent").item(0);
			Element previousElementGovernor = (Element) currentElement.getElementsByTagName("governor").item(0);

			int elementdepPosition = Integer.parseInt(previousElementDependent.getAttributes().getNamedItem("idx").getNodeValue());
			int elementgovPosition = Integer.parseInt(previousElementGovernor.getAttributes().getNamedItem("idx").getNodeValue());
			boolean sameNodeGovernor = inputPosition==elementgovPosition;
			boolean sameNodeDependent = inputPosition==elementdepPosition;

			if(sameNodeGovernor && (inputPosition>elementdepPosition)){
				previousElements.add(previousElementDependent);
			}
			if(sameNodeDependent && (inputPosition>elementgovPosition)){
				previousElements.add(previousElementGovernor);
			}
		}
		return previousElements;
	}

	/**
	 * 
	 * @param nodes
	 * @param node
	 * @return
	 */
	public List<Element> getPreviousElementsPointedByNode(Element node) {
		List<Element> previousElements = new LinkedList<>();
		int inputPosition = Integer.parseInt(node.getAttributes().getNamedItem("idx").getNodeValue());

		for (int i = 0; i < nodes.getLength(); i++) {
			Element currentElement = (Element) nodes.item(i);

			Element previousElementGovernor = (Element) currentElement.getElementsByTagName("governor").item(0);
			Element previousElementDependent = (Element) currentElement.getElementsByTagName("dependent").item(0);

			int previousPosition = Integer.parseInt(previousElementDependent.getAttributes().getNamedItem("idx").getNodeValue());
			int elementgovPosition = Integer.parseInt(previousElementGovernor.getAttributes().getNamedItem("idx").getNodeValue());

			boolean sameNode = ((inputPosition==elementgovPosition) && (previousElementGovernor.getTextContent().equals(node.getTextContent())));

			if(sameNode && (inputPosition>previousPosition))
				previousElements.add(previousElementDependent);
		}

		return previousElements;
	}
	/**
	 * 
	 * @param nodes
	 * @param node
	 * @return
	 */
	public List<Element> getNextElements(Element node){
		List<Element> nextElements = new LinkedList<>();
		int inputPosition = Integer.parseInt(node.getAttributes().getNamedItem("idx").getNodeValue());

		for (int i = 0; i < nodes.getLength(); i++) {
			Element currentElement = (Element) nodes.item(i);

			Element nextElementGovernor = (Element) currentElement.getElementsByTagName("governor").item(0);
			Element nextElementDependent = (Element) currentElement.getElementsByTagName("dependent").item(0);

			int nextPosition = Integer.parseInt(nextElementDependent.getAttributes().getNamedItem("idx").getNodeValue());
			int nodePosition = Integer.parseInt(node.getAttributes().getNamedItem("idx").getNodeValue());
			int elementgovPosition = Integer.parseInt(nextElementGovernor.getAttributes().getNamedItem("idx").getNodeValue());

			boolean sameNode = ((nodePosition==elementgovPosition) && (nextElementGovernor.getTextContent().equals(node.getTextContent())));

			if(sameNode && (inputPosition<nextPosition))
				nextElements.add(nextElementDependent);
		}

		return nextElements;
	}
	/**
	 * startPositionNsubj ritorna dato un certo elemento(parola) la posizione del suo soggetto
	 * @param nodes: lista delle dipendenze in formato XML
	 * @param finalElement: elemento di partenza da dove cercare il suo soggetto(nsubj)
	 * @return startPosition: posizione del soggetto
	 */
	public int startPositionNsubj(Element finalElement) {
		int startPosition = 0;
		int supPosition = 0;
		for (Element el : getAllPreviousElements(finalElement)) {
			Map<Element, List<Element>> governor2dependentsByNsubj = governor2dependents("nsubj");
			for (Element gov : governor2dependentsByNsubj.keySet()) {

				if (finalElement.getTextContent().equals(gov.getTextContent())
						&& finalElement.getAttributes().getNamedItem("idx").getNodeValue().equals(gov.getAttributes().getNamedItem("idx").getNodeValue())){
					for (Element dep : governor2dependentsByNsubj.get(gov)) {
						supPosition = Integer.parseInt(dep.getAttributes().getNamedItem("idx").getNodeValue());
						if (supPosition>startPosition)
							startPosition = supPosition;
					}
				}
				else if (el.getTextContent().equals(gov.getTextContent())
						&& el.getAttributes().getNamedItem("idx").getNodeValue().equals(gov.getAttributes().getNamedItem("idx").getNodeValue())){
					for (Element dep : governor2dependentsByNsubj.get(gov)) {
						supPosition = Integer.parseInt(dep.getAttributes().getNamedItem("idx").getNodeValue());
						if (supPosition>startPosition)
							startPosition = supPosition;
					}
				}
				else{
					startPosition = startPositionNsubj(el);
				}
			}
		}
		return startPosition;
	}
	/**
	 * depTypeNmodList restituisce una lista delle tipologie di nmod presenti nella frase
	 * @param nodes: lista delle dipendenze in formato XML
	 * @return depType: lista delle tipologie di nmod presenti nella frase
	 */

	
	/*
	
	 * dependenciesFinder restituisce una lista per una specifica dipendenza dove ogni elemento è una coppia (dep,gov) 
	 * @param nodes: lista delle dipendenze in formato XML
	 * @param depType: dipendenza che si vuole cercare
	 * @return dependenciesList: lista per una specifica dipendenza dove ogni elemento è una coppia (dep,gov)
	 
	public List<List<Element>> dependenciesFinder(String depType){
		List<List<Element>> dependenciesList = new LinkedList<>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			Element currentElement = (Element) nodes.item(i);
			String dep_type = currentNode.getAttributes().getNamedItem("type").getNodeValue();
			if(dep_type.contains(depType)){
				List<Element> dependencieDepGov = new ArrayList<>();
				dependencieDepGov.add((Element) currentElement.getElementsByTagName("dependent").item(0));
				dependencieDepGov.add((Element) currentElement.getElementsByTagName("governor").item(0));
				dependenciesList.add(dependencieDepGov);
			}
		}
		return dependenciesList;
	} */
	 
}
