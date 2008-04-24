import java.io.*;

public class Main {

	public static void main(String[] args) {

		BufferedReader br = null;  
		boolean options = false;  //the user as used any options
		boolean debug = false;    //debug mode has been turned on
		boolean printParse = false; //the parse tree will be printed
		boolean printAST = false; //the abstract syntax tree will be printed
		boolean printMaxStack = false; //the max stack size will be printed
        
		//Checks to see if any options have been used
		if (args.length > 0 && args[0].charAt(0) == '-') {
			if (args[0].length() > 1) {
				for (int i = 1; i < args[0].length(); i++) {
					char c = args[0].charAt(i);
					if (c == 'd') {
						debug = true;
						options = true;
					} else if (c == 'p') {
						printParse = true;
						options = true;
					} else if (c == 'a') {
						printAST = true;
						options = true;
					} else if (c == 's') {
						printMaxStack = true;
						options = true;
					}

				}
			}
		}
        
		//if arguments are given, and a filename is provided the parse the file, else parse from stdin
		if ((args.length > 0 && !options) || (args.length > 1 && options)) {
			try {
				int index = 0;

				if (options) {
					index = 1;
				}
				br = new BufferedReader(new FileReader(args[index]));
			} catch (IOException ioe) {
				System.out.println("IO Error: File Cannot be Read");
			}
		} else {
			br = new BufferedReader(new InputStreamReader(System.in));
		}
		
        //read the input line by line, parser will parse sInput character by character
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
            //if input exists, the parser will parse the input and produce a parse tree, ast, and evaluate the input
			if (sInput != null) {

				Parser parser = new Parser(sInput);
				TreeNode parserNodes = parser.microProgram();

				AbstractSyntaxTree tree = new AbstractSyntaxTree(parserNodes);
				AbstractSyntaxTree ast = tree.getAST();

				if (printParse) {
					System.out
							.println("**************PARSE TREE*********************");
					System.out.println(tree.toString());
					System.out
							.println("***********END PARSE TREE********************");
				}
				if (printAST) {
					System.out
							.println("**************AST*********************");
					System.out.println(ast.toString());
					System.out
							.println("**************END AST*********************");
				}
				if (printMaxStack) {
					System.out.println("MAX STACK SIZE: "
							+ parser.getMaxStackSize());
				}

                if (!parserNodes.hasError()) {
				    ast.evaluate(debug); //this evaluates the Micro program
                } else {
                    System.out.println("\nThere was an error during parsing. The program will not be evaluated.\n");
                }
			}
		} else {
			System.out.println("IO Error");
		}
	}
}
