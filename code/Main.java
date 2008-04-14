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
				sInput += sLine.trim() + "\n";
			}
			sInput = sInput.replace("nullbegin", "begin");
		} catch (IOException ioe) {
			System.out.println("IO Error");
			sInput = null;
		}

		if (sInput != null) {	
			Parser parser = new Parser(sInput);
		
			TreeNode parserNodes = parser.microProgram();
		
			AbstractSyntaxTree tree = new AbstractSyntaxTree(parserNodes);
			System.out.println(tree.getAST().toString());
		
		    System.out.println("Max Stack Size: " + parser.getMaxStackSize());
		}

	}

}
