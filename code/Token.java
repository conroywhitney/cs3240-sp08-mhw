public class Token {

    public enum TokenType { KEYWORD, ID, INTNUM }

    private String sValue;
    private TokenType type;

    public Token(String sValue, TokenType type) {
        this.sValue = sValue;
        this.type = type;
    }

    public String getValue() {
        return this.sValue;
    }

    public int length() {
        return this.sValue.length();
    }

}
