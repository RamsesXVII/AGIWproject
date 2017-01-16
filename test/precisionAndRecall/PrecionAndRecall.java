package precisionAndRecall;

import static org.junit.Assert.*;

import org.junit.Before;

import org.junit.Test;

import evaluatingUtility.PrecisionFalloutRecallGetter;

public class PrecionAndRecall {
	private String pathToOnly7ListFile="./test/precisionAndRecall/only7List.tsv";
	private String pathToOnly18NotListFile="./test/precisionAndRecall/only18NotList.tsv";
	
	private String pathToOnlyInitialDataset6List6NotListFile="./test/precisionAndRecall/initialDataset.tsv";
	private String pathToAccepted3List3NotListFile="./test/precisionAndRecall/3list3notlist.tsv";
	

	private PrecisionFalloutRecallGetter prGetterOnlyList;
	private PrecisionFalloutRecallGetter prGetterOnlyNotList;

	private PrecisionFalloutRecallGetter prGetterHalfList;


	@Before
	public void setUp() throws Exception {
		
		/*uso lo stesso file come dataset iniziale e frasi accettate in modo dtale da avere precion o pari a 1 o a 0 a seconda
		 * che il file contenga tutte liste o tutte non liste*/
		this.prGetterOnlyList= new PrecisionFalloutRecallGetter(pathToOnly7ListFile,pathToOnly7ListFile,pathToOnly7ListFile);
		this.prGetterOnlyNotList= new PrecisionFalloutRecallGetter(pathToOnly18NotListFile,pathToOnly18NotListFile,pathToOnly18NotListFile);
		
		/*dataset:file con 50% rilevanti-> accepted metà dei rilevanti*/
		this.prGetterHalfList= new PrecisionFalloutRecallGetter(pathToOnlyInitialDataset6List6NotListFile,pathToAccepted3List3NotListFile, null);
	
	}

	@Test
	public void PrecisionTest() {

		assertTrue(1.0== this.prGetterOnlyList.getPrecision());
		assertTrue(0.0== this.prGetterOnlyNotList.getPrecision());
		assertTrue(0.5==this.prGetterHalfList.getPrecision());
	
	}
	
	@Test
	public void RecallTest() {

		assertTrue(1.0== this.prGetterOnlyList.getRecall());
		assertTrue(0.0== this.prGetterOnlyNotList.getRecall());
		assertTrue(0.5==this.prGetterHalfList.getRecall());
	
	}	



}