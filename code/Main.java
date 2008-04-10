
public class Main {

	public static void main(String[] args) {
		
		String test = "begin \n " +
				"ID := 6;" +
				" ID2 := asdfas;" +
				" ID3:=4+2; " +
				"ID4 :=5-ID4; " +
				"ID5:=4**3; " +
				"ID6 := ID3 * ID2; " +
				"print ( ID4 + (4*ID5)) \n " +
				"end";
				
/*		
		String test = "begin ";
		test += "var1 := 6;";
		test += "var2 := 4;";
		test += "var3 := var1 + var2 ";
		test += "end";
*/		
		
		Parser parser = new Parser(test);
		
		parser.microProgram();

        // create parser
        // interpret parser output ??
        // get and display tree ??		

	}

}
