
public class Tokenizer2 {

    private int index = -1;
    private int iProgramLength = -1;
    private String sProgram = null;

    public Tokenizer2(String sProgram) {
        this.index = 0;
        this.sProgram = sProgram;
        this.iProgramLength = sProgram.length();
    }

    public static void main(String[] args) {
		String test = "begin \n " +
				"ID := 6;" +
				" ID2 := asdfas;" +
				" ID3:=4+2; " +
				"ID4 :=5-ID4; " +
				"ID5:=4**3; " +
				"ID6 := ID3 * ID2; " +
				"print ( ID4 + (4*ID5)) \n " +
				"end";

        Tokenizer2 tokenizer = new Tokenizer2(test);

        Token token = null;

		System.out.println("TESTING");

        while (tokenizer.hasNext()) {
        	//System.out.println("hasnext");
            token = tokenizer.next();
            System.out.println("token: " + token.getValue());
        }

    }

    public Token next() {
        Token token = null;
        int iTokenLength = -1;

        // char we're currently looking at
        char curr;
        String sToken = "";

		while (token == null && this.hasNext()) {
			//System.out.println("index = " + this.index);
			
			curr = this.sProgram.charAt(index);
			//System.out.println(curr);
			
			if (sToken.length() > 0) {
				if ((curr == '(') || (curr == ')') || (curr == ',') || (curr == ';') || (curr == '+') || (curr == '-') ||
					(curr == ':') || (curr == '=') || (curr == '*') || (curr == ' ') || (curr == '\n') || (curr == '\t')) {
					// we were building a token and came upon a reserved character which means the end of our token
					// so create the token but don't increment the index -- keep index here so can resume next time
					
					// create token from whatever string we've amassed
					token = new Token(sToken);
					// reset the token temp holder
					sToken = null;
				} else {
		        	// keep collecting chars, expecting them to become something wonderful
		        	sToken += String.valueOf(curr);
		        	this.index += 1;	        	
		        }
			} else {
				if (curr == '(' || curr == ')' || curr == ',' || curr == ';' || curr == '+' || curr == '-') {
					// if our current char is one of these by-themselves tokens, we've found our token
		            token = new Token(String.valueOf(curr));
		            this.index += 1;
		        } else if ((curr == ':') && (this.sProgram.charAt(index+1) == '=')) {
		        	// if we have a :, it must be followed by a =
		        	token = new Token(":=");
		        	this.index += 2;
		        } else if ((curr == '*') && (this.sProgram.charAt(index+1) == '*')) {
		        	// check for ** before just * so don't get them confused
		        	token = new Token("**");
		        	this.index += 2;
		        } else if (curr == '*') {
		        	// now check for * after **
		        	token = new Token("*");
		        	this.index += 1;
		        } else if ((curr == ' ') || (curr == '\n') || (curr == '\t')) {
		        	// ignore whitespace
		        	this.index += 1;
		        } else {
		        	// keep collecting chars, expecting them to become something wonderful
		        	sToken += String.valueOf(curr);
		        	this.index += 1;	        	
		        }
			}	        
	         
		}
		
		if (!this.hasNext() && sToken.length() > 0) {
			// create token from whatever string we've amassed
			token = new Token(sToken);
			// reset the token temp holder
			sToken = null;
		}
		
        return token;
    }

    public boolean hasNext() {
        if (this.index < this.iProgramLength) { return true; } else { return false; }
    }

}
