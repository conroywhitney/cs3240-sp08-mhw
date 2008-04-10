
public class Parser {

	//private AbstractSyntaxTree AST;
	private Tokenizer tokenizer = null;
	
    public Parser(String sProgram) {
        this.tokenizer = new Tokenizer(sProgram);
        this.tokenizer.tokenize();
    }
    
    private void error(Token t) {
    	System.out.println("\t\tError on token: " + t.getValue());
    }

    // <Micro-program > -> begin <statement-list> end
    public boolean microProgram() {
    	System.out.println("\nmicroProgram");
    	boolean b = true;
        if (!match("begin", true)) {
        	b = false;
        }
        
        statementList();
        
        if (!match("end", true)) {
        	b = false;
        }
        return b;
    }

    // <statement-list> -> <statement> <statement-list'>
    public boolean statementList() {
    	System.out.println("\nstatementList");
		return (statement() & statementListPrime());
    }

    // <statement-list'> -> ; <statement-list>
    // <statement-list'> -> \epsilon
    public boolean statementListPrime() {
    	System.out.println("\nstatementListPrime");
        if (match(";")) {
			return statementList();
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
        		return statementListPrime();
        	} else {
				return true;        		
        	}
        }
    }

    // <statement> ->  print ( <exp-list> )
    // <statement> ->  ID := <exp>
    public boolean statement() {
    	System.out.println("\nstatement");
        if (match("print")) {
            return (match("(", true) && expList() && match(")", true));
        } else if (matchID()) {
        	return (match(":=", true) && exp());
        } else {
        	// error
        	error(this.tokenizer.next());
        	return false;
        }
    }

    // <exp-list> -> <exp> <exp-list'>
    public boolean expList() {
    	System.out.println("\nexpList");
        return (exp() & expListPrime());
    }

    // <exp-list'> -> <exp-list>, <exp>
    // <exp-list'> -> \epsilon
    public boolean expListPrime() {
    	System.out.println("\nexpListPrime");
        if (match(",")) {
            return expList();
        } else {
        	System.out.println("expListPrime was EPSILON");
        	// epsilon
            return true;
        }
    }

    // <exp> -> ( <exp> ) <exp'>
    // <exp> -> ID <exp'>
    // <exp> -> INTNUM <exp'>
    public boolean exp() {
    	System.out.println("\nexp");
    	// create node(null, "exp")
        if (match("(")) {
            return  (exp() & match(")", true) && expPrime());
        } else if (matchINTNUM()) {
            return expPrime();            
        } else if (matchID()) {
            return expPrime();
        } else {
        	error(this.tokenizer.next());
            return false;
            // dump tree
            // destroy node
        }
    }

    // <exp'> -> <bin-op> <exp>
    // <exp'> -> \epsilon
    public boolean expPrime() {
    	System.out.println("\nexpPrime");
        if (binOp()) {
            return exp();
        } else {
        	System.out.println("expPrime was EPSILON");
            return true;
        }
    }

    // <bin-op> ->  +
    // <bin-op> ->  -
    // <bin-op> ->  *
    // <bin-op> ->  **  
    public boolean binOp() {
    	System.out.println("\nbinOp");
        return ((match("**")) || (match("*")) || (match("+")) || (match("-")));
    } 

	public boolean match(String s) {
		return match(s, false);
	}

    public boolean match(String s, boolean required) {
    	System.out.println("match: " + s);
    	boolean b = false;
    	if (this.tokenizer.hasNext()) {
    		Token t = this.tokenizer.next();
    		if (s.equals(t.getValue())) {
    			t.setType(Token.TokenType.KEYWORD);
    			b = true;
    			this.tokenizer.consume();
    			System.out.println("consum'd: " + t.getValue());
    		} else {
    			if (required) {
    				error(t);
    				this.tokenizer.consume();
    			}
    		}
    	}
    	return b;
    }

	public boolean matchID() {
		return matchID(false);
	}
	
    public boolean matchID(boolean required) {
    	System.out.println("matchID");
    	boolean b = false;
    	if (this.tokenizer.hasNext()) {
    		Token t = this.tokenizer.next();
    		if (this.isID(t.getValue())) {
    			t.setType(Token.TokenType.ID);
    			b = true;
    			this.tokenizer.consume();
    			System.out.println("consum'd: " + t.getValue());
    		} else {
    			if (required) {
    				error(t);
    			}
    		}
    	}
    	return b;
    }

    public boolean matchINTNUM() {
    	System.out.println("matchINTNUM");
    	boolean b = false;
    	if (this.tokenizer.hasNext()) {
    		Token t = this.tokenizer.next();
    		if (this.isINTNUM(t.getValue())) {
    			t.setType(Token.TokenType.INTNUM);
    			b = true;
    			this.tokenizer.consume();
    			System.out.println("consum'd: " + t.getValue());
    		} else {
    			System.out.println("fux'd");
    		}
    	}
    	return b;
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
