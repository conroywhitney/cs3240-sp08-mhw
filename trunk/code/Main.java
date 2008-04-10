
public class Main {

	public static void main(String[] args) {
	
		String daniel_complicated = "begin \n " +
				"ID := 6;" +
				"ID2 := asdfas;" +
				"ID3:= 4+2; " +
				"ID4 :=5-ID4; " +
				"ID5:=4**3; " +
				"ID6 := ID3 * ID2; " +
				"print ( ID4 + (4*ID5)) \n " +
				"end";
						
		String simplePrint = "begin ";
		simplePrint += "print ( 4 ) \n ";
		simplePrint += "end";
		
		String simpleSet = "begin ";
		simpleSet += "var1 := 6;";
		simpleSet += "var2 := 4;";
		simpleSet += "var3 := var1 + var2 ";
		simpleSet += "end";

		String awesomeSauce = "begin ";
		awesomeSauce += "awesome := sauce;";
		awesomeSauce += "print(heck, yeah);";
		awesomeSauce += "meaning_of_life_the_universe_and_everything := 42 ";
		awesomeSauce += "end";		
			
		String addTest = "begin ";
		addTest += "ID1 := 3 * 4 ";
		addTest += "end";
		
		String signedIntTest = "begin ";
		signedIntTest += "ID1 := 3 * -4 ";
		signedIntTest += "end";
		
		String sTest = signedIntTest;
		
		Parser parser = new Parser(sTest);
		
		TreeNode parserNodes = parser.microProgram();
		
		AbstractSyntaxTree tree = new AbstractSyntaxTree(parserNodes);
		System.out.println(tree.toString());
		System.out.println(tree.getAST().toString());
		
		//System.out.println(parser.isID("_some_Test2_"));

        System.out.println("Max Stack Size: " + parser.getMaxStackSize());

	}

}
