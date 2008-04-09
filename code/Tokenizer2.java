
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

        while (tokenizer.hasNext()) {
            token = tokenizer.next(); 
            System.out.println(token.getValue());
        }

    }

    public Token next() {
        Token token = null;
        int iTokenLength = -1;

        // char we're currently looking at
        char curr = this.sProgram.charAt(index);

        // if our current char is one of these by-themselves tokens, we've found our token
        if (curr == '(' || curr == ')' || curr == ',' || curr == ';' || curr == '+' || curr == '-') {
            token = new Token(String.valueOf(curr), Token.TokenType.KEYWORD);
        }

        iTokenLength = token.length();
        index += iTokenLength;
        return token;
    }

    public boolean hasNext() {
        return (index < iProgramLength);
    }

}
