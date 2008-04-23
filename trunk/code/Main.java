import java.io.*;

public class Main {

	public static void main(String[] args) {

		BufferedReader br = null;

		if (args.length > 0) {
			System.out.println(args[0]);
			
			try {
				FileReader fr = new FileReader(args[0]);
				br = new BufferedReader(fr);
			} catch (IOException ioe) {
				System.out.println("IO Error");
			}
		} else {

			InputStreamReader isr = new InputStreamReader(System.in);
			br = new BufferedReader(isr);
		}

		if (br != null) {
			String sInput = null;
			String sLine = null;
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
				//System.out.println(tree.toString());
				AbstractSyntaxTree ast = tree.getAST();
				System.out.println(ast.toString());

				System.out.println("Max Stack Size: "
						+ parser.getMaxStackSize());
				
				System.out.println("Evaluation: " + ast.evaluate());
			}
		} else {
			System.out.println("IO Error");
		}
	}

}
