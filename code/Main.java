import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {

		String sInput = null;
		String sLine = null;

		InputStreamReader isr = new InputStreamReader( System.in );
		BufferedReader br = new BufferedReader( isr );

		try {
			while ((sLine = br.readLine()) != null) {
				System.out.println(sLine);
				sInput += sLine.trim() + "\n";
			}
		} catch (IOException ioe) {
			System.out.println("IO Error");
			sInput = null;
		}

		//sInput = "begin awesome := sauce; print(heck, yeah); meaning_of_life := 42 end";
/*
		sInput = "begin ";
		sInput += "ID := 6; ";
		sInput += "  ID2 := asdfas; ";
		sInput += "ID3:= 4+2; ";
		sInput += "ID4 :=5-ID4; ";
		sInput += "ID5:=4**3; ";
		sInput += "ID6 := ID3 * ID2; ";
		sInput += "print ( ID4 + (4*ID5)) ";
		sInput += "end";
*/

		if (sInput != null) {	
			Parser parser = new Parser(sInput);
		
			TreeNode parserNodes = parser.microProgram();
		
			AbstractSyntaxTree tree = new AbstractSyntaxTree(parserNodes);
			//System.out.println(tree.toString());
			System.out.println(tree.getAST().toString());
		
		    System.out.println("Max Stack Size: " + parser.getMaxStackSize());
		}

	}

}
