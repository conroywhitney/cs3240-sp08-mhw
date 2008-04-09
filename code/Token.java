public class Token {

    public enum TokenType { KEYWORD, ID, INTNUM, INVALID }

    private String sValue;
    private TokenType type;

    public Token(String sValue) {
        this.sValue = sValue;
    }

    public String getValue() {
        return this.sValue;
    }
    
    public TokenType getType() {
    	return this.type;
    }
    
    public void setType(TokenType type) {
    	this.type = type;
    }

    public int length() {
        return this.sValue.length();
    }

}
