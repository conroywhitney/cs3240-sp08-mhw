
public class Parser {

	//private AbstractSyntaxTree AST;
	private Tokenizer tokenizer = null;
	
    public Parser(String sProgram) {
        this.tokenizer = new Tokenizer(sProgram);
        this.tokenizer.tokenize();
    }

    // <Micro-program > -> begin <statement-list> end
    public boolean microProgram() {
    	System.out.println("microProgram");
        return (match("begin") & statementList() & match("end"));
    }

    // <statement-list> -> <statement> <statement-list'>
    public boolean statementList() {
    	System.out.println("statementList");
		return (statement() & statementListPrime());
    }

    // <statement-list'> -> ; <statement-list>
    // <statement-list'> -> \epsilon
    public boolean statementListPrime() {
    	System.out.println("statementListPrime");
        if (match(";")) {
			return statementList();
        } else {
			return true;
        }
    }

    // <statement> ->  print ( <exp-list> )
    // <statement> ->  ID := <exp>
    public boolean statement() {
    	System.out.println("statement");
        if (match("print")) {
            return (match("(") & expList() & match(")"));
        } else {
            return (matchID() && match(":=") & exp());
        }
    }

    // <exp-list> -> <exp> <exp-list'>
    public boolean expList() {
    	System.out.println("expList");
        return (exp() & expListPrime());
    }

    // <exp-list'> -> <exp-list>, <exp>
    // <exp-list'> -> \epsilon
    public boolean expListPrime() {
    	System.out.println("expListPrime");
        if (match(",")) {
            return expList();
        } else {
        	// epsilon
            return true;
        }
    }

    // <exp> -> ( <exp> ) <exp'>
    // <exp> -> ID <exp'>
    // <exp> -> INTNUM <exp'>
    public boolean exp() {
    	System.out.println("exp");
    	// create node(null, "exp")
        if (match("(")) {
            return  (exp() & match(")") & expPrime());
        } else if (matchINTNUM()) {
            return expPrime();            
        } else if (matchID()) {
            return expPrime();
        } else {
            return false;
            // dump tree
            // destroy node
        }
    }

    // <exp'> -> <bin-op> <exp>
    // <exp'> -> \epsilon
    public boolean expPrime() {
    	System.out.println("expPrime");
        if (binOp()) {
            return exp();
        } else {
            return true;
        }
    }

    // <bin-op> ->  +
    // <bin-op> ->  -
    // <bin-op> ->  *
    // <bin-op> ->  **  
    public boolean binOp() {
    	System.out.println("binOp");
        return ((match("**")) || (match("*")) || (match("+")) || (match("-")));
    } 

    public boolean match(String s) {
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
    			System.out.println("fux'd");
    		}
    	}
    	return b;
    }

    public boolean matchID() {
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
    			System.out.println("fux'd");
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
    	for (int i = 1; i < arr.length; i++) {
    		if (!isInt(arr[i])) {
    			b = false;
    		}
    	}
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
