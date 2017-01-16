package precisionAndRecall;

import static org.junit.Assert.*;

import org.junit.Before;

import org.junit.Test;

import evaluatingUtility.ListAndNotListCounter;

public class ListAndNotListCountTest {

	private ListAndNotListCounter lCounterOnlyList;
	private ListAndNotListCounter lCounterOnlyNotList;
	private ListAndNotListCounter listAndNotListCounter;

	
	@Before
	public void setUp() throws Exception {
		this.lCounterOnlyList= new ListAndNotListCounter("./test/precisionAndRecall/only7List.tsv");
		this.lCounterOnlyNotList= new ListAndNotListCounter("./test/precisionAndRecall/only18NotList.tsv");
		this.listAndNotListCounter= new ListAndNotListCounter("./test/precisionAndRecall/6List6NotList.tsv");
	}

	@Test
	public void ContainStringIDtest() {

		assertTrue(7==this.lCounterOnlyList.getListCount());
		assertTrue(18==this.lCounterOnlyNotList.getNotListCount());
		assertTrue(6==this.listAndNotListCounter.getListCount());
		assertTrue(6== this.listAndNotListCounter.getNotListCount());

	}		
}