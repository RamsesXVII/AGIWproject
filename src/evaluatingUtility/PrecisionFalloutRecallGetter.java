package evaluatingUtility;
/*
 * precision fallout e recall per il prefiltro
 */
public class PrecisionFalloutRecallGetter {
	private String pathToAcceptedFile;
	private String pathToRefusedFile;   //??
	private String pathToInitialDataset;
	private ListAndNotListCounter lcounter;

	public PrecisionFalloutRecallGetter(String pathToInitialDataset,String pathToAccepted, String pathToRefused){
		this.pathToAcceptedFile=pathToAccepted;
		this.pathToRefusedFile=pathToRefused;
		this.pathToInitialDataset=pathToInitialDataset;
		this.lcounter= new ListAndNotListCounter(pathToInitialDataset);
	}

	public double getPrecision(){
		this.lcounter.setPathToFile(pathToInitialDataset);


		this.lcounter.setPathToFile(pathToAcceptedFile);

		double listPhrasesRetrievedCount= this.lcounter.getListCount();
		double totalPhraseRetrievedCount=this.lcounter.getNotListCount()+listPhrasesRetrievedCount;

		if(totalPhraseRetrievedCount==0||listPhrasesRetrievedCount==0)
			return 0;

		return listPhrasesRetrievedCount/totalPhraseRetrievedCount;

	}


	public double getRecall(){
		this.lcounter.setPathToFile(pathToInitialDataset);

		double listPhrasesTotalCount=this.lcounter.getListCount();

		this.lcounter.setPathToFile(pathToAcceptedFile);

		double listPhrasesRetrievedCount= this.lcounter.getListCount();

		if(listPhrasesTotalCount==0||listPhrasesRetrievedCount==0)
			return 0;

		return listPhrasesRetrievedCount/listPhrasesTotalCount;

	}
	
	public double getFallout(){
		this.lcounter.setPathToFile(pathToInitialDataset);

		double notListPhrasesTotalCount=this.lcounter.getNotListCount();

		this.lcounter.setPathToFile(pathToAcceptedFile);

		double notListPhrasesRetrievedCount= this.lcounter.getListCount();

		if(notListPhrasesRetrievedCount==0||notListPhrasesTotalCount==0)
			return 0;

		return notListPhrasesRetrievedCount/notListPhrasesTotalCount;

	}
}
