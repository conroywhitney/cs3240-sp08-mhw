
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
						
		String simple_print = "begin ";
		simple_print += "print ( 4 ) \n ";
		simple_print += "end";
		
		String simple_set = "begin ";
		simple_set += "var1 := 6;";
		simple_set += "var2 := 4;";
		simple_set += "var3 := var1 + var2 ";
		simple_set += "end";

		String awesomeTest_fuckYeah = "begin ";
		awesomeTest_fuckYeah += "awesome := sauce;";
		awesomeTest_fuckYeah += "print(fuck, yeah);";
		awesomeTest_fuckYeah += "meaning_of_life_the_universe_and_everything := 42 ";
		awesomeTest_fuckYeah += "end";		
			
		String addTest = "begin ";
		addTest += "ID1 := 3 + 4 ";
		addTest += "end";
		
		String sTest = addTest;
		
		Parser parser = new Parser(sTest);
		
		TreeNode parserNodes = parser.microProgram();
		
		AbstractSyntaxTree tree = new AbstractSyntaxTree(parserNodes);
		System.out.println(tree.toString());
		System.out.println(tree.getAST().toString());
		
		//System.out.println(parser.isID("_some_Test2_"));

        System.out.println("Max Stack Size: " + parser.getMaxStackSize());

	}

}
