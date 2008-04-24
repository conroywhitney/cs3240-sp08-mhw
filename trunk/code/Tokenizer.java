public class Tokenizer {

	public TokenList tokens = null;
	private int index;
	private String program = null;

	public Tokenizer(String sProgram) {
		this.program = sProgram;
		this.tokens = new TokenList();
		this.index = 0;
	}

	public void tokenize() {
		int currentChar = 0;
		char curr = this.program.charAt(currentChar);
		String token = "";

		while (currentChar < this.program.length()) {
			if (curr == '(' || curr == ')' || curr == ',' || curr == ';'
					|| curr == '+' || curr == '-') {
				if (token.length() > 0) {
					this.tokens.add(token);
					token = "";
				}
				String terminal = "" + curr;
				this.tokens.add(terminal);
			} else if (curr == ':') {
				if (token.length() > 0) {
					this.tokens.add(token);
				}

				token = "" + curr;
			} else if (curr == '=') {
				if (token.equals(":")) {
					token = token + "=";
					this.tokens.add(token);
					token = "";
				}

				else {
					if (token.length() > 0) {
						this.tokens.add(token);
						token = "";
					}
					String terminal = "" + curr;
					this.tokens.add(terminal);
				}
			} else if (curr == '*') {
				if (token.length() > 0) {
					this.tokens.add(token);
					token = "";
				}

				if (this.program.length() > currentChar + 1
						&& this.program.charAt(currentChar + 1) == '*') {
					token = "**";
					this.tokens.add(token);
					token = "";
					currentChar++;
				} else {
					token = "*";
					this.tokens.add(token);
					token = "";
				}

			} else if (curr == ' ' || curr == '\n' || curr == '\t') {
				if (token.length() > 0) {
					this.tokens.add(token);
					token = "";
				}
			} else {
				if (tokens.size() > 1 && (token.length() == 0)) {
					String temp = tokens.get(tokens.size() - 2).getValue();
					String temp2 = tokens.get(tokens.size() - 1).getValue();

					if ((temp.equals("+") || temp.equals("-")
							|| temp.equals("*") || temp.equals("**")
							|| temp.equals("(") || temp.equals(",")
							|| temp.equals(":=") || temp.equals(";"))
							&& (temp2.equals("+") || temp2.equals("-"))) {
						token = temp2 + curr;
						tokens.removeLast();
					} else {
						token = token + curr;
					}
				} else {
					token = token + curr;
				}

			}
			currentChar++;
			if (currentChar < this.program.length()) {
				curr = this.program.charAt(currentChar);
			} else {
				if (token.length() > 0) {
					this.tokens.add(token);
					token = "";
				}
			}
		}

	}

	public Token next() {
		// give them the token at the current index
		// this will NOT consume a token, so other rules can check this same
		// token
		return this.tokens.get(this.index);
	}

	public void consume() {
		// consuming a token that they already have in hand
		// simply increment our index so we don't consume that one again
		this.index++;
	}

	public boolean hasNext() {
		// no more tokens if index is as long as the tokens list (aka, at end)
		if (this.index < this.tokens.size()) {
			return true;
		} else {
			return false;
		}
	}
}
