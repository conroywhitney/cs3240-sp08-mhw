
public class Parser {

	private Tokenizer tokenizer = null;
	
	private int iMaxStack = 0;
	private int iCurrStack = 0;

	private TreeNode globalRootNode = null;
	
	private VariableList variableList = null;
    private String lastVariableOnLHS = "";
	
	private boolean bRequireDeclaration = false;
		
    public Parser(String sProgram) {
        this.tokenizer = new Tokenizer(sProgram);
        this.tokenizer.tokenize();
        this.variableList = new VariableList();
    }
    
    private void error(Token t) {
    	System.out.println("=======================\nError on token: " + t.getValue() + "\n=======================");
		////System.out.println("Tree So Far: ");
		//AbstractSyntaxTree tree = new AbstractSyntaxTree(globalRootNode);
		////System.out.println(tree.getAST().toString());
    }

    // <Micro-program > -> begin <statement-list> end
    public TreeNode microProgram() {
    	//System.out.println("\nmicroProgram");
    	incrementStackCounter();
    	
		// create original root node for Parse Tree
    	globalRootNode = new TreeNode("microProgram");
    	
        // always need to match "begin"
    	globalRootNode.addChild(match("begin", true));
        
		// Because of the TA's test3 (big recursion), we need to find
		// a better way to create our TreeNodes

		// Create an ArrayList of statements
		StatementList arrStatements = new StatementList();

		// Keep track of a statementList Node
		TreeNode statementListNode = null;
		// And of a statementListPrime Node
		TreeNode statementListPrimeNode = null;

		// This is to check if we were able to find statementListPrime (or if end of program)
		boolean bHasStatementListPrime = false;

		do {
			bHasStatementListPrime = false;

			// Create the new statementList Node like usual
			// Adding a statement Node to it
			statementListNode = new TreeNode("statementList");
			statementListNode.addChild(statement());
			arrStatements.add(statementListNode);

			// But instead of calling statementListPrime recurisvely
			// Check to see if we can create our own statementListPrime node
			// Adding the ";" node to it
			statementListPrimeNode = new TreeNode("statementListPrime");
			if (statementListPrimeNode.addChild(match(";"))) {
				bHasStatementListPrime = true;
				arrStatements.add(statementListPrimeNode);
			}
			
			// If we had a statementListPrime, it wasn't end of prog so do again
		} while (bHasStatementListPrime);

		// Now for the cheeky part -- we have an ArrayList of
		// TreeNodes but they're not parents of each other yet
		
		// Step through the ArrayList backwards
		// Add the last (the child) to the second-to-last (the parent)
		// Overwrite the parent, and erase the child from the ArrayList
		for (int i = arrStatements.size() - 2; i >= 0; i--) {
			TreeNode lastNode = arrStatements.get(i+1);
			TreeNode parentNode = arrStatements.get(i);
			parentNode.addChild(lastNode);
			arrStatements.set(i, parentNode);
			arrStatements.set(i+1, null);
		}

		// Now we are down to one TreeNode in the ArrayList
		// And it has all the necessary children added to it
		// Add this to the Global Root Node like usual
		globalRootNode.addChild(arrStatements.get(0));
        
        // always need to match "end"
        globalRootNode.addChild(match("end", true));
        
        decrementStackCounter();
        return globalRootNode;
    }

/*
    // <statement-list> -> <statement> <statement-list'>
    public TreeNode statementList() {
    	//System.out.println("\nstatementList");
    	incrementStackCounter();
    	    	
    	TreeNode node = new TreeNode("statementList");
    	
		node.addChild(statement());
    	
    	node.addChild(statementListPrime());
    	
    	decrementStackCounter();
		return node;
    }

    // <statement-list'> -> ; <statement-list>
    // <statement-list'> -> \epsilon
    public TreeNode statementListPrime() {
    	//System.out.println("\nstatementListPrime");
    	incrementStackCounter();
    	
    	TreeNode node = new TreeNode("statementListPrime");
    	
		// addChild returns TRUE if adding a child that is NOT NULL
		// match() returns NULL if unable to match
        if (node.addChild(match(";"))) {
			node.addChild(statementList());
        } else {
			// using epsilon rule which ALWAYS means an error
			// going to fast-forward to the end of the sentence: a ';' or 'end'
        	
        	String sCurrToken = this.tokenizer.next().getValue();
        	boolean bFirst = true;
        	while ((!sCurrToken.equals("end")) && (!sCurrToken.equals(";"))) {
        		if (bFirst) {
					// only throw an error for the first problem token
					// the other tokens in the sentence are just collatoral damage
        			error(this.tokenizer.next());
        			bFirst = false;
        		}
        		this.tokenizer.consume();
        		if (this.tokenizer.hasNext()) {
        			sCurrToken = this.tokenizer.next().getValue();
        		} else {
					// if we've not found "end" but we're at the end, get outa this loop (h4x)
        			break;
        		}
        	}
        	
			// if we found a ';' and not 'end', move onto next sentence
			// TODO: where is an error node actually created in this method ???
        	if (sCurrToken.equals(";")) {
        		node = statementListPrime();
        	} else {
				node = null;        		
        	}
        }
        
        decrementStackCounter();
        return node;
    }
*/

    // <statement> ->  print ( <exp-list> )
    // <statement> ->  ID := <exp>
    public TreeNode statement() {
    	//System.out.println("\nstatement");
    	incrementStackCounter();
    	
    	TreeNode node = new TreeNode("statement");
    	
		// test to see if we can match "print", continuing that rule if we can
        if (node.addChild(match("print"))) {
			// if we've got "print", we REQUIRE "(", which is that second param
			// match() will return an error node if we cannot match "("
			// in which case addChild() will return FALSE
            node.addChild(match("(", true));

           	// we are inside a print statement now
           	// we need to make sure that all IDs used are declared already
           	this.bRequireDeclaration = true;
            	
          	node.addChild(expList());

            node.addChild(match(")", true));
            		
           	// we are out of the print statement now so we can lax our guard
           	this.bRequireDeclaration = false;

        } else if (node.addChild(matchID())) {
            node.addChild(match(":=", true));
        	
            // we are on the right-side of an assignment operator now
        	// so we need to make sure that all IDs are declared already
       		this.bRequireDeclaration = true;
        		
       		node.addChild(exp());

            // so see if this node has errors while trying to set RHS
            // if not, go ahead and add this variable to the list
            if (!node.hasError()) {
                // also, add this to our list of known variables
	    		this.declare(this.lastVariableOnLHS);
            }

	    			
        		
       		// we are done with the assignment now so we can lax our guard
       		this.bRequireDeclaration = false;

        } else {
        	// this cannot be null so this is an error
            node = new TreeNode("error");

        }

        if (node.hasError()) {
            node = new TreeNode("error");
        }
        
        decrementStackCounter();
        return node;
    }

    // <exp-list> -> <exp> <exp-list'>
    public TreeNode expList() {
    	//System.out.println("\nexpList");
    	incrementStackCounter();
    	
    	TreeNode node = new TreeNode("expList");

		node.addChild(exp());

		node.addChild(expListPrime());			
    	
    	decrementStackCounter();
        return node;
    }

    // <exp-list'> -> , <exp-list>
    // <exp-list'> -> \epsilon
    public TreeNode expListPrime() {
    	//System.out.println("\nexpListPrime");
    	incrementStackCounter();
    	
    	TreeNode node = new TreeNode("expListPrime");

		// if we cannot match ',' then we ASSUME we go to EPSILON    	    	
        if (node.addChild(match(","))) {
            node.addChild(expList());
        } else {
        	// epsilon a.k.a. NULL node
            node = null;
        }
        
        decrementStackCounter();
        return node;
    }

    // <exp> -> <addexp> <addexp'>
    public TreeNode exp() {
    	//System.out.println("\nexp");
    	incrementStackCounter();
    	
    	TreeNode node = new TreeNode("exp");
    	
        node.addChild(addexp());

		node.addChild(addexpPrime());        	
        
        decrementStackCounter();
        return node;
    }

    // <addexp> -> <mulexp> <mulexp'>
    public TreeNode addexp() {
    	//System.out.println("\naddexp");
    	incrementStackCounter();
    	
    	TreeNode node = new TreeNode("addexp");

        node.addChild(mulexp());

		node.addChild(mulexpPrime());        	

        decrementStackCounter();
        return node;
    }

    // <addexp'> -> + <exp>
    // <addexp'> -> - <exp>
    // <addexp'> -> \epsilon
    public TreeNode addexpPrime() {
    	//System.out.println("\naddexpPrime");
    	incrementStackCounter();
    	
    	TreeNode node = new TreeNode("addexpPrime");

        if (node.addChild(match("+"))) {
            node.addChild(exp());
        } else if (node.addChild(match("-"))) {
            node.addChild(exp());
        } else {
            node = null;
        }

        decrementStackCounter();
        return node;
    }

    // <mulexp> -> <powexp> <powexp'>
    public TreeNode mulexp() {
    	//System.out.println("\nmulexp");
    	incrementStackCounter();
    	
    	TreeNode node = new TreeNode("mulexp");

        node.addChild(powexp());

		node.addChild(powexpPrime());        	

        decrementStackCounter();
        return node;
    }

    // <mulexp'> -> * <addexp>
    // <mulexp'> -> \epsilon
    public TreeNode mulexpPrime() {
    	//System.out.println("\nmulexpPrime");
    	incrementStackCounter();
    	
    	TreeNode node = new TreeNode("mulexpPrime");

        if (node.addChild(match("*"))) {
            node.addChild(addexp());
        } else {
            node = null;
        }

        decrementStackCounter();
        return node;
    }

    // <powexp> -> ( <exp> )
    // <powexp> -> ID
    // <powexp> -> INTNUM
    public TreeNode powexp() {
    	//System.out.println("\npowexp");
    	incrementStackCounter();
    	
    	TreeNode node = new TreeNode("powexp");

        if (node.addChild(match("("))) {
            node.addChild(exp());
            node.addChild(match(")", true));
        } else if (node.addChild(matchID())) {
            // OK
        } else if (node.addChild(matchINTNUM())) {
            // OK
        } else {
            node = new TreeNode("error");
        }

        decrementStackCounter();
        return node;
    }

    // <powexp'> -> ** <mulexp>
    // <powexp'> -> \epsilon
    public TreeNode powexpPrime() {
    	//System.out.println("\npowexpPrime");
    	incrementStackCounter();
    	
    	TreeNode node = new TreeNode("powexpPrime");

        if (node.addChild(match("**"))) {
            node.addChild(mulexp());
        } else {
            node = null;
        }

        decrementStackCounter();
        return node;
    }

	public TreeNode match(String s) {
		return match(s, false);
	}

	// this method will return a TreeNode with a KEYWORD token
	// or NULL if the strings don't match
    public TreeNode match(String s, boolean required) {
    	//System.out.println("match: " + s);
    	
    	TreeNode node = null;
    	
    	if (this.tokenizer.hasNext()) {
    		// get next token
    		Token t = this.tokenizer.next();

    		if (s.equals(t.getValue())) {
    			// if our token's value is what we expect to see

                //System.out.println("\tconsume'd: " + s);

				// ... let it forever be known it is a KEYWORD token (aka reserved characters)
    			t.setType(Token.TokenType.KEYWORD);

    			// create a new KEYWORD node for the tree (instead of it being NULL)		
    			node = new TreeNode(t, "keyword");

    			// consume the token from the tokenizer so that nobody sees this token again
				this.tokenizer.consume();

    		} else {
    			// our token's value was not what we expected to see
				// either this was simply a test (like "herm, should i use the rule that starts with...")
				// or it was REQUIRED, meaning this token NEEDS to have been found here
    			if (required) {
    				// in such a REQUIRED case, give them back an error node w/ the affixed token
    				node = new TreeNode(t, "error");
					// throw an error message
    				error(t);
					// and consume the token so nobody sees it evar again
    				this.tokenizer.consume();
    			}
    		}
    	}
    	
    	return node;
    }
	
	// this function looks at a token and sees if it could be an ID
	// if so, it returns an ID node to be added to the parse tree
    public TreeNode matchID(boolean required) {
    	//System.out.println("matchID");
    	
    	TreeNode node = null;
    	
    	if (this.tokenizer.hasNext()) {
			// look at next token in tokenizer
    		Token t = this.tokenizer.next();

    		if (this.isID(t.getValue())) {
				// matches our "regex" (aka, character parsing) for an ID
				
				// if we are within an assignment statement or a print statement
				// we must make sure that this ID has already been declared
				if ((this.bRequireDeclaration) && (!this.isIDDeclared(t.getValue()))) {
					// we are using this ID in an illegal way
					// we must turn this node into an error node
					// and throw an error
					node = new TreeNode(t, "error");
					error(t);
					
					// consume token so nobody sees it again
	    			this.tokenizer.consume();	
				} else {
					// we are using this ID in a legal way
					//System.out.println("\tconsume'd: " + t.getValue());

					// give the token type ID
	    			t.setType(Token.TokenType.ID);
	
					// give the new node type ID, and the newfound ID token
	    			node = new TreeNode(t, "ID");

                    // if not requiring declaration, this means we are trying to DECLARE the variable
                    // in which case this was the last variable we tried to declare
                    // remember this so we can try to stick it in the variable list after correct RHS eval
                    if (!this.bRequireDeclaration) {
                        this.lastVariableOnLHS = t.getValue();
                    }

                    // going to declare this ID after we already know that the RHS has evaluated correctly
                    // else we don't even try to put it in the list
	
					// consume token so nobody sees it again
	    			this.tokenizer.consume();				
				}                
    		} else {
				// this token did NOT match an ID -- either we were just fishing for options
				// or we were REQUIRED to find an ID token here
    			if (required) {
					// give them back an error node if they were so sure of finding an ID here
    				node = new TreeNode(t, "error");
    				error(t);
                    // and consume the token so nobody sees it evar again
    				this.tokenizer.consume();
    			}
    		}
    	}
    	
    	return node;
    }

	// Overload for non-required
	public TreeNode matchID() {
		return matchID(false);
	}

    public TreeNode matchINTNUM() {
    	//System.out.println("matchINTNUM");
    	
    	TreeNode node = null;
    	
    	if (this.tokenizer.hasNext()) {
    		Token t = this.tokenizer.next();
    		if (this.isINTNUM(t.getValue())) {
                //System.out.println("\tconsume'd: " + t.getValue());
    			t.setType(Token.TokenType.INTNUM);
    			node = new TreeNode(t, "INTNUM");
    			this.tokenizer.consume();
    		}
    	}

    	return node;
    }
    
    private void declare(String sIDName) {
    	this.variableList.update(new Variable(sIDName));	
    }
    
    private boolean isIDDeclared(String sIDName) {
    	return this.variableList.hasVariable(sIDName);
    }
    
    public boolean isID(String s) {
    	char[] arr = s.toCharArray();
    	boolean b = true;
    	
    	if (!isAlpha(arr[0])) {
    		// if not start with an alpha
    		if (!((arr[0] == '_') && (arr.length > 0) && (isAlpha(arr[1])))) {
    			// if not start with an underscore followed by an alpha
    			b = false;
    		} 
    	}
    	
    	if (b) {
    		for (int i = 1; i < arr.length - 1; i++) {
    			if ((!isAlpha(arr[i])) && (!isInt(arr[i]))) {
    				if (arr[i] == '_') {
    					if (arr[i+1] == '_') {
    						b = false;
    					}
    				} else {
						b = false;    					
    				}	
    			}
    		}
    		
    		// ends either with an alpha or an int (not an _)
    		if ((!isAlpha(arr[arr.length - 1])) && (!isInt(arr[arr.length - 1])) || (arr[arr.length - 1] == '_'))   {
    			b = false;
    		}
    	}
    	return b;
    }
    
    public boolean isINTNUM(String s) {
    	char[] arr = s.toCharArray();
    	boolean b = true;
    	    	
    	// cannot start with a 0
    	if (arr[0] == '0') {
    		b = false;
    	}

		// all characters have to be integers    	
    	for (int i = 0; i < arr.length; i++) {
			// if first char, can have - or +
    		if ((i==0) && !((isInt(arr[i])) || (arr[i] == '-') || (arr[i] == '+'))) {
    			b = false;
    		}

			// all other chars have to be ints
			if ((i > 0) && !isInt(arr[i])) {
				//System.out.println(i + ": " + arr[i] + "; !isInt");
    			b = false;
    		}

    	}
    	
    	return b;
    }
    
    public boolean isInt(char c) {
    	return ((c >= '0') && (c <= '9'));
    }
    
    public boolean isAlpha(char c) {
    	return ( ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) );
    }
    
    // used to keep track of maximum production-rule stack size
    private void incrementStackCounter() {
    	this.iCurrStack += 1;
    	this.iMaxStack = (this.iCurrStack > this.iMaxStack) ? this.iCurrStack : this.iMaxStack;
    }
    // used to keep track of maximum production-rule stack size
    private void decrementStackCounter() {
    	this.iCurrStack -= 1;
    }
    
    public int getMaxStackSize() {
    	return this.iMaxStack;
    }

}
