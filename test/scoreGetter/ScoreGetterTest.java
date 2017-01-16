package scoreGetter;

import static org.junit.Assert.*;

import org.junit.Before;

import org.junit.Test;

import prefilter.PhraseScoreGetter;



public class ScoreGetterTest {

	private PhraseScoreGetter scoreGetter;
		
	private String withListIdentifierSuchAs;
	private String withListIdentifierIncluding;
	private String withListIdentifierIE;
	private String withListIdentifierLike;
	private String withListIdentifierEG;
	private String withListIdentifierForExample;
	
	private String with1Colon;
	private String with2Colon;
	private String withoutColon;

	private String withoutListIdentifier;
	
	private String withoutThreeEntitiesPattern;
	private String withThreeEntitiesPattern;
	private String withThreeEntitiesPattern1;

	private String withListQuantifier;
	private String withoutListQuantifier;
	
	private String withNumberPattern;
	private String withNumberPattern1;

	private String withoutNumberPattern;
	
	private String withColonFollowedByListPattern;



	@Before
	public void setUp() throws Exception {

		this.scoreGetter= new PhraseScoreGetter();
		
		this.withListIdentifierSuchAs= "mario met some girls such as [[Maria|m.0bkzt]] and [[Giulia|m.0bkzt]]";
		this.withListIdentifierIncluding= "mario met some girls including [[Maria|m.0bkzt]] and [[Giulia|m.0bkzt]]";
		this.withListIdentifierIE= "mario met some girls, i.e. [[Maria|m.0bkzt]] and [[Giulia|m.0bkzt]]";
		this.withListIdentifierLike= "mario met some girls, like [[Maria|m.0bkzt]] and [[Giulia|m.0bkzt]]";
		this.withListIdentifierEG= "mario met some girls, e.g. [[Maria|m.0bkzt]] and [[Giulia|m.0bkzt]]";
		this.withListIdentifierForExample= "mario met some girls, for example [[Maria|m.0bkzt]] and [[Giulia|m.0bkzt]]";

		this.withoutListIdentifier="mario met Maria and Giulia";
		
		this.with1Colon="mario met some girls:Matia and Giulia";
		this.with2Colon="mario met some girls:Maria and Giulia: the daughter of Pippo";
		this.withoutColon="mario met some girls, for example Maria and Giulia";
		
		this.withThreeEntitiesPattern= "[[Christiaan_Huygens|m.0bkzt]] was programmed to "
				+ "[[Transmission_(telecommunications)|m.02w40n]] telemetry and scientific"
				+ " data to the Cassini orbiter for relay to [[Earth|m.02j71]] using two redundant"
				+ " [[S-band|m.02pbwv]] radio systems , referred to as Channel A and B , or Chain A and B. "
				+ "Channel A was the sole path for an experiment to measure wind speeds by studying tiny"
				+ " frequency changes caused by [[Christiaan_Huygens|m.0bkzt]] 's motion ." ;
		
		this.withThreeEntitiesPattern1="[[Obama|m.03rlt]] and [[Trump|m.12231]] is a supporter of Lakers , [[Cavalliers|m.03rlt]] and Bulls.";

		this.withoutThreeEntitiesPattern="Irvin worked on the [[Space_probe|m.02q54c9]] 's descent "
				+ "control sub-system under contract to [[Martin-Baker_Space_Systems|m.022kbb]] .";
		
		this.withListQuantifier="Mario won some prizes";
		this.withoutListQuantifier="Mario won 2 prizes";
		
		this.withNumberPattern="Mario married two women: Maria and Chiara";
		this.withNumberPattern1="Mario married 6 women: Claudia,Giulia;Cristina,Roberta,Francesca,Paola";

		this.withoutNumberPattern="Mario is six years old";
		
		this.withColonFollowedByListPattern="Mario married 2 women: Claudia , Paola and [[Maria|m.02q54c9]] .";
	}

	@Test
	public void ContainStringIDtest() {

		assertEquals(1,this.scoreGetter.containsListIdFollwedByEntity(withListIdentifierSuchAs));
		assertEquals(1,this.scoreGetter.containsListIdFollwedByEntity(withListIdentifierIncluding));
		assertEquals(1,this.scoreGetter.containsListIdFollwedByEntity(withListIdentifierIE));
		assertEquals(1,this.scoreGetter.containsListIdFollwedByEntity(withListIdentifierLike));
		assertEquals(1,this.scoreGetter.containsListIdFollwedByEntity(withListIdentifierEG));
		assertEquals(1,this.scoreGetter.containsListIdFollwedByEntity(withListIdentifierForExample));

		assertEquals(0,this.scoreGetter.containsListIdFollwedByEntity(withoutListIdentifier));
	}
	
	@Test
	public void colonsCountTest() {

		assertEquals(1,this.scoreGetter.containsColon(with1Colon));
		assertEquals(1,this.scoreGetter.containsColon(with2Colon));

		assertEquals(0,this.scoreGetter.containsColon(withoutColon));
	}
	
	@Test
	public void containsAtLeastThreeEntitiesPatternTest() {

		assertEquals(1,this.scoreGetter.containsListOfThreeElementsPattern(this.withThreeEntitiesPattern1));
		assertEquals(1,this.scoreGetter.containsListOfThreeElementsPattern(this.withThreeEntitiesPattern));
		assertEquals(0,this.scoreGetter.containsListOfThreeElementsPattern(this.withoutThreeEntitiesPattern));
	}
	
	@Test
	public void containsListQuantifiersTest() {

		assertEquals(1,this.scoreGetter.containsQuantifiers(this.withListQuantifier));
		assertEquals(0,this.scoreGetter.containsQuantifiers(this.withoutListQuantifier));
	}
	
	@Test
	public void containsNumberPatternTest() {

		assertEquals(1,this.scoreGetter.containsNumberCommaPattern(this.withNumberPattern));
		assertEquals(1,this.scoreGetter.containsNumberCommaPattern(this.withNumberPattern1));
		assertEquals(0,this.scoreGetter.containsNumberCommaPattern(this.withoutNumberPattern));
	}
	
	@Test
	public void getNumberOfEntitiesTest() {

		assertEquals(5,this.scoreGetter.getEntityCount(this.withThreeEntitiesPattern));
		assertEquals(2,this.scoreGetter.getEntityCount(this.withoutThreeEntitiesPattern));
	}
	
	
	@Test
	public void containsColonFollowedByListPattern(){
		assertEquals(1, this.scoreGetter.containsColonFollowedByListPattern(this.withColonFollowedByListPattern));
		assertEquals(0,this.scoreGetter.containsColonFollowedByListPattern(this.withListIdentifierSuchAs));
		
	}
	
	
	
	
}