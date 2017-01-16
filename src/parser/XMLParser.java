package parser;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class XMLParser {
	private DocumentBuilderFactory dbf;
	private DocumentBuilder db;
	private InputSource is;
	
	public XMLParser(String XMLString){
	    this.dbf =DocumentBuilderFactory.newInstance();
	    
	    try {
			this.db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	    
	    this.is = new InputSource();
	    this.is.setCharacterStream(new StringReader(XMLString));
		}
	
	public NodeList getNodesByTag(String tag){
		Document doc;
		try {
			doc = this.db.parse(is);
		    return doc.getElementsByTagName("dep");		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
}