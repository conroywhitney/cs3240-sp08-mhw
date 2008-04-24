public class TokenList {

	private Token[] tokens;

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

	public void removeLast() {
		if (tokens.length > 0) {
			Token[] temp = new Token[tokens.length - 1];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = tokens[i];
			}
			tokens = temp;
		}
	}

	public Token get(int i) {
		return this.tokens[i];
	}

	public int size() {
		return this.tokens.length;
	}

}
