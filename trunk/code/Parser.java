
public class Parser {

	private Tokenizer tokenizer = null;
	
    public Parser(String sProgram) {
        this.tokenizer = new Tokenizer(sProgram);
        this.tokenizer.tokenize();
    }
    
    private void error(Token t) {
    	System.out.println("\t\tError on token: " + t.getValue());
    }

    // <Micro-program > -> begin <statement-list> end
    public TreeNode microProgram() {
    	System.out.println("\nmicroProgram");
    	
		// create original root node for Parse Tree
    	TreeNode rootNode = new TreeNode("microProgram");
    	
    	rootNode.addChild(match("begin", true));
        
		// recursively add statementList and its children             
        rootNode.addChild(statementList());
        
        rootNode.addChild(match("end", true));
        
        return rootNode;
    }

    // <statement-list> -> <statement> <statement-list'>
    public TreeNode statementList() {
    	System.out.println("\nstatementList");
    	    	
    	TreeNode node = new TreeNode("statementList");
    	
    	node.addChild(statement());
    	
    	node.addChild(statementListPrime());
    	
		return node;
    }

    // <statement-list'> -> ; <statement-list>
    // <statement-list'> -> \epsilon
    public TreeNode statementListPrime() {
    	System.out.println("\nstatementListPrime");
    	
    	TreeNode node = new TreeNode("statementListPrime");
    	
		// addChild returns TRUE if adding a child that is NOT NULL
		// match() returns NULL if unable to match
        if (node.addChild(match(";"))) {
			node.addChild(statementList());
        } else {
			// using epsilon rule which ALWAYS means an error
			// going to fast-forward to the end of the sentence: a ';' or 'end'

        	System.out.println("statementListPrime was EPSILON -- consuming tokens till ; or end");
        	
			// 
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
        
        return node;
    }

    // <statement> ->  print ( <exp-list> )
    // <statement> ->  ID := <exp>
    public TreeNode statement() {
    	System.out.println("\nstatement");
    	
    	TreeNode node = new TreeNode("statement");
    	
		// test to see if we can match "print", continuing that rule if we can
        if (node.addChild(match("print"))) {
			// if we've got "print", we REQUIRE "(", which is that second param
			// match() will return an error node if we cannot match "("
			// in which case addChild() will return FALSE
            if (node.addChild(match("(", true))) {
            	if (node.addChild(expList())) {
            		node.addChild(match(")", true));
            	}
            }
        } else if (node.addChild(matchID())) {
        	if (node.addChild(match(":=", true))) {
        		node.addChild(exp());
        	}
        } else {
        	// error
        	node = new TreeNode(this.tokenizer.next(), "error");
        	error(this.tokenizer.next());
        	// TODO: do i need to consume ??
			// TODO: what about fast-forwarding ??
        }
        
        return node;
    }

    // <exp-list> -> <exp> <exp-list'>
    public TreeNode expList() {
    	System.out.println("\nexpList");
    	
    	TreeNode node = new TreeNode("expList");

		node.addChild(exp());
		
		node.addChild(expListPrime());
    	
        return node;
    }

    // <exp-list'> -> <exp-list>, <exp>
    // <exp-list'> -> \epsilon
    public TreeNode expListPrime() {
    	System.out.println("\nexpListPrime");
    	
    	TreeNode node = new TreeNode("expListPrime");

		// if we cannot match ',' then we ASSUME we go to EPSILON    	    	
        if (node.addChild(match(","))) {
            node.addChild(expList());
        } else {
        	System.out.println("expListPrime was EPSILON");
        	// epsilon a.k.a. NULL node
            node = null;
        }
        
        return node;
    }

    // <exp> -> ( <exp> ) <exp'>
    // <exp> -> INTNUM <exp'>
    // <exp> -> ID <exp'>
    public TreeNode exp() {
    	System.out.println("\nexp");
    	
    	TreeNode node = new TreeNode("exp");
    	    	
        if (node.addChild(match("("))) {
			// this was originally a & b/t exp() and match()
        	if (node.addChild(exp())) {
				if (node.addChild(match(")", true))) {
	        		node.addChild(expPrime());
				}
        	}
        } else if (node.addChild(matchINTNUM())) {
            node.addChild(expPrime());
        } else if (node.addChild(matchID())) {
            node.addChild(expPrime());
        } else {
            node = new TreeNode(this.tokenizer.next(), "error");
        	error(this.tokenizer.next());
        	// TODO: do i need to consume ???
        }
        
        return node;
    }

    // <exp'> -> <bin-op> <exp>
    // <exp'> -> \epsilon
    public TreeNode expPrime() {
    	System.out.println("\nexpPrime");
    	
    	TreeNode node = new TreeNode("expPrime");
    	
        if (node.addChild(binOp())) {
            node.addChild(exp());
        } else {
        	// because can go to epsilon, set to NULL
        	node = null;
        	System.out.println("expPrime was EPSILON");
        }
        
        return node;
    }

    // <bin-op> ->  +
    // <bin-op> ->  -
    // <bin-op> ->  *
    // <bin-op> ->  **  
    public TreeNode binOp() {
    	System.out.println("\nbinOp");
    	
    	TreeNode node = new TreeNode("binOp");
    	
    	// short-circuit add them to node
        if (!node.hasChildren()) { node.addChild(match("**")); }
        if (!node.hasChildren()) { node.addChild(match("*")); }
        if (!node.hasChildren()) { node.addChild(match("+")); }
        if (!node.hasChildren()) { node.addChild(match("-")); }

		// if after so many attempts, no children, steralize him/her
		if (!node.hasChildren()) { node = null; }

		// TODO: can we somehow use this short-circuit add to perform the RHS of
		// supporting 5 * -5 ???? consuming two tokens at once ??
        
        return node;
    } 

	public TreeNode match(String s) {
		return match(s, false);
	}

	// this method will return a TreeNode with a KEYWORD token
	// or NULL if the strings don't match
    public TreeNode match(String s, boolean required) {
    	System.out.println("match: " + s);
    	
    	TreeNode node = null;
    	
    	if (this.tokenizer.hasNext()) {
    		// get next token
    		Token t = this.tokenizer.next();

    		if (s.equals(t.getValue())) {
    			// if our token's value is what we expect to see

				// ... let it forever be known it is a KEYWORD token (aka reserved characters)
    			t.setType(Token.TokenType.KEYWORD);

    			// create a new KEYWORD node for the tree (instead of it being NULL)		
    			node = new TreeNode(t, "keyword");

    			// consume the token from the tokenizer so that nobody sees this token again
				this.tokenizer.consume();

    			System.out.println("consum'd: " + t.getValue());
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
    	System.out.println("matchID");
    	
    	TreeNode node = null;
    	
    	if (this.tokenizer.hasNext()) {
			// look at next token in tokenizer
    		Token t = this.tokenizer.next();

    		if (this.isID(t.getValue())) {
				// matches our "regex" (aka, character parsing) for an ID

				// give the token type ID
    			t.setType(Token.TokenType.ID);

				// give the new node type ID, and the newfound ID token
    			node = new TreeNode(t, "ID");

				// consume token so nobody sees it again
    			this.tokenizer.consume();

    			System.out.println("consum'd: " + t.getValue());
    		} else {
				// this token did NOT match an ID -- either we were just fishing for options
				// or we were REQUIRED to find an ID token here
    			if (required) {
					// give them back an error node if they were so sure of finding an ID here
    				node = new TreeNode(t, "error");
    				error(t);
					// TODO: do we need to consume this now ?? methinks ...
					// TODO: how about till the end of sentence ??
					// TODO: or maybe the person trying to match this should figure that out ...
					// TODO: so maybe the addChild returning NULL for error nodes is not a good idea
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
    	System.out.println("matchINTNUM");
    	
    	TreeNode node = null;
    	
    	if (this.tokenizer.hasNext()) {
    		Token t = this.tokenizer.next();
    		if (this.isINTNUM(t.getValue())) {
    			t.setType(Token.TokenType.INTNUM);
    			node = new TreeNode(t, "INTNUM");
    			this.tokenizer.consume();
    			System.out.println("consum'd: " + t.getValue());
    		} else {
    			//System.out.println("fux'd");
    		}
    	}

    	return node;
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
    		if (!isInt(arr[i])) {
    			b = false;
    		}
    	}
    	
    	System.out.println("Checking if " + s + " is INTNUM: " + b);
    	
    	return b;
    }
    
    public boolean isInt(char c) {
    	return ((c >= '0') && (c <= '9'));
    }
    
    public boolean isAlpha(char c) {
    	return ( ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) );
    }

}
