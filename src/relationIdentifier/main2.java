package relationIdentifier;

import relationIdentifier.RelationalIdentifier;

public class main2 {

	public static void main(String[] args) {

		RelationalIdentifier ri= new RelationalIdentifier();

		System.out.println(ri.isRelational("is a supporter of many teams"));
		System.out.println(ri.isRelational("is a supporter of"));
		System.out.println(ri.isRelational("is a supporter of many teams such as"));
		System.out.println(ri.isRelational("married some famous women such as "));
		System.out.println(ri.isRelational("also employed the form in smaller vocal"));
		System.out.println(ri.isRelational("is marketed "));
		
	}
}
