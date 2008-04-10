
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
    	
    	TreeNode rootNode = new TreeNode(null, "microProgram");
    	
    	rootNode.addChild(match("begin", true));
                        
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
    		
        if (node.addChild(match(";"))) {
			node.addChild(statementList());
        } else {
        	System.out.println("statementListPrime was EPSILON -- consuming tokens till ; or end");
        	
        	String sCurrToken = this.tokenizer.next().getValue();
        	boolean bFirst = true;
        	while ((!sCurrToken.equals("end")) && (!sCurrToken.equals(";"))) {
        		if (bFirst) {
        			error(this.tokenizer.next());
        			bFirst = false;
        		}
        		this.tokenizer.consume();
        		if (this.tokenizer.hasNext()) {
        			sCurrToken = this.tokenizer.next().getValue();
        		} else {
        			break;
        		}
        	}
        	
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
    	
        if (node.addChild(match("print"))) {
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
        	error(this.tokenizer.next());
        	// TODO: do i need to consume ??
        	node = new TreeNode(this.tokenizer.next(), "error");
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
    	    	
        if (node.addChild(match(","))) {
            node.addChild(expList());
        } else {
        	System.out.println("expListPrime was EPSILON");
        	// epsilon
            node = null;
        }
        
        return node;
    }

    // <exp> -> ( <exp> ) <exp'>
    // <exp> -> ID <exp'>
    // <exp> -> INTNUM <exp'>
    public TreeNode exp() {
    	System.out.println("\nexp");
    	
    	TreeNode node = new TreeNode("exp");
    	    	
        if (node.addChild(match("("))) {
        	if (node.addChild(exp()) & node.addChild(match(")", true))) {
        		node.addChild(expPrime());
        	}
        } else if (node.addChild(matchINTNUM())) {
            return expPrime();            
        } else if (node.addChild(matchID())) {
            return expPrime();
        } else {
        	error(this.tokenizer.next());
        	// TODO: do i need to consume ???
            node = new TreeNode(this.tokenizer.next(), "error");
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
        	// because can go to epsilon, set to null
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
        
        return node;
    } 

	public TreeNode match(String s) {
		return match(s, false);
	}

    public TreeNode match(String s, boolean required) {
    	System.out.println("match: " + s);
    	
    	// this method will return a TreeNode (auto null)
    	TreeNode node = null;
    	
    	if (this.tokenizer.hasNext()) {
    		// get next token
    		Token t = this.tokenizer.next();
    		if (s.equals(t.getValue())) {
    			// if our token is what we expect to see
    			t.setType(Token.TokenType.KEYWORD);    	
    			// create a new KEYWORD token		
    			node = new TreeNode(t, "keyword");
    			// and consume the token from the tokenizer
				this.tokenizer.consume();
    			System.out.println("consum'd: " + t.getValue());
    		} else {
    			// not what we expected to see
    			if (required) {
    				// if this was REQUIRED, then give them an error token
    				error(t);
    				this.tokenizer.consume();
    				node = new TreeNode(t, "error");
    			}
    		}
    	}
    	
    	return node;
    }

	public TreeNode matchID() {
		return matchID(false);
	}
	
    public TreeNode matchID(boolean required) {
    	System.out.println("matchID");
    	
    	// this method will return a TreeNode (auto null)
    	TreeNode node = null;
    	
    	if (this.tokenizer.hasNext()) {
    		Token t = this.tokenizer.next();
    		if (this.isID(t.getValue())) {
    			t.setType(Token.TokenType.ID);
    			node = new TreeNode(t, "ID");
    			this.tokenizer.consume();
    			System.out.println("consum'd: " + t.getValue());
    		} else {
    			if (required) {
    				error(t);
    				node = new TreeNode(t, "error");
    			}
    		}
    	}
    	
    	return node;
    }

    public TreeNode matchINTNUM() {
    	System.out.println("matchINTNUM");
    	
    	TreeNode node = null;
    	
    	if (this.tokenizer.hasNext()) {
    		Token t = this.tokenizer.next();
    		if (this.isINTNUM(t.getValue())) {
    			t.setType(Token.TokenType.INTNUM);
    			node = new TreeNode(t, "ID");
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
    	return ((c >= '0') &&
    	(c <= '9'));
    }
    
    public boolean isAlpha(char c) {
    	return ( ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) );
    }

}
