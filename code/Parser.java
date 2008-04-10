
public class Parser {

	//private AbstractSyntaxTree AST;
	private Tokenizer tokenizer = null;
	
    public Parser(String sProgram) {
        this.tokenizer = new Tokenizer(sProgram);
        this.tokenizer.tokenize();
    }

    // <Micro-program > -> begin <statement-list> end
    public boolean microProgram() {
        return (match("begin") & statementList() & match("end"));
    }

    // <statement-list> -> <statement> <statement-list'>
    public boolean statementList() {
		return (statement() & statementListPrime());
    }

    // <statement-list'> -> ; <statement-list>
    // <statement-list'> -> \epsilon
    public boolean statementListPrime() {
        if (match(";")) {
			return statementList();
        } else {
			return true;
        }
    }

    // <statement> ->  print ( <exp-list> )
    // <statement> ->  ID := <exp>
    public boolean statement() {
        if (match("print")) {
            return (match("(") & expList() & match(")"));
        } else {
            return (matchID() & match(":=") & exp());
        }
    }

    // <exp-list> -> <exp> <exp-list'>
    public boolean expList() {
        return (exp() & expListPrime());
    }

    // <exp-list'> -> <exp-list>, <exp>
    // <exp-list'> -> \epsilon
    public boolean expListPrime() {
        if (expList()) {
            return (match(",") & exp());
        } else {
        	// rollback
        	// epsilon
            return true;
        }
    }

    // <exp> -> ( <exp> ) <exp'>
    // <exp> ->  ID <exp'>
    // <exp> -> INTNUM <exp'>
    public boolean exp() {
        if (match("(")) {
            return  (exp() & match(")") & expPrime());
        } else if (matchID()) {
            return expPrime();
        } else if (matchINTNUM()) {
            return expPrime();
        } else {
            return false;
        }
    }

    // <exp'> -> <bin-op> <exp>
    // <exp'> -> \epsilon
    public boolean expPrime() {
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
        return ((match("**")) || (match("*")) || (match("+")) || (match("-")));
    } 

    public boolean match(String s) {
    	System.out.println("Trying to match: " + s);
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
    	System.out.println("Trying to match ID");
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
    	System.out.println("Trying to match INTNUM");
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
    
    private boolean isID(String s) {
    	return true;
    }
    
    private boolean isINTNUM(String s) {
    	return true;
    }

}
