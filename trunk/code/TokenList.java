
public class TokenList {

	private Token[] tokens;
	private int index;
	
    public TokenList() {
    	this.tokens = new Token[0];
    }
    
    public void add(Token t) {
		Token[] temp = new Token[this.tokens.length + 1];
		for (int i = 0; i < this.tokens.length; i++) {
			temp[i] = this.tokens[i];
		}    	
		
		temp[this.tokens.length] = t;
		this.tokens = temp;
    }
    
    public void add(String sToken) {
    	this.add(new Token(sToken));
    }
    
    public Token get(int i) {
    	return this.tokens[i];
    }
    
    public int size() {
    	return this.tokens.length;
    }

}