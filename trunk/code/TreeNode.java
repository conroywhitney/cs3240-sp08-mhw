import java.math.*;

public class TreeNode {

	private String label;
	private Token token;
	private TreeNode[] children;

	public TreeNode(Token token, String label) {
		this.label = label;
		this.token = token;
		children = new TreeNode[0];
	}

	public TreeNode(String label) {
		this(null, label);
	}

	public boolean addChild(TreeNode node) {
		if (node != null) {
			TreeNode[] temp = new TreeNode[children.length + 1];
			for (int i = 0; i < children.length; i++) {
				temp[i] = children[i];
			}
			temp[children.length] = node;
			children = temp;

			// decide what to return based on node type
			if (node.getLabel().equals("error")) {
				// we added the error node, but we should stop the production of
				// any other nodes
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public void addLeftChild(TreeNode node) {
		if (node != null) {
			TreeNode[] temp = new TreeNode[children.length + 1];
			for (int i = 1; i < children.length + 1; i++) {
				temp[i] = children[i - 1];
			}
			temp[0] = node;
			children = temp;
		}
	}

	public String toString() {
		String s = "( " + getDisplay() + " ";

		for (int i = 0; i < getChildren().length; i++) {
			s += getChildren()[i].getDisplay() + " ";
		}

		s += ") \n";

		return s;
	}

	public TreeNode getAST() {

		TreeNode root = null;

		if (label.equals("microProgram")) {
			root = children[2]; // end
			root.addChild(children[0]); // begin
			root.addChild(children[1].getAST()); // ast of statementList
		} else if (label.equals("statementList")) {
			if (children.length == 2) // if statementListPrime != null
			{
				root = children[1].getAST(); // ast of statementListPrime
				root.addLeftChild(children[0].getAST());// ast of statement
			} else {
				root = children[0].getAST(); // ast of statement
			}
		} else if (label.equals("statementListPrime")) {
			if (children.length == 2) // if statementListPrime != null
			{
				root = children[0]; // ';'
				root.addChild(children[1].getAST()); // ast of statementList
			}
		} else if (label.equals("statement")) {
			if (children[0].getLabel().equals("keyword")) // if "print"
			{
				root = children[0]; // "print"
				root.addChild(children[2].getAST()); // ast of expList
			} else {
				// <statement> --> ID := <exp>
				root = children[1]; // ':='
				root.addChild(children[0]); // ID
				root.addChild(children[2].getAST());
			}
		} else if (label.equals("expList")) {
			if (children.length == 2) {
				root = children[1].getAST();
				root.addLeftChild(children[0].getAST());
			} else // exp-list' to epsilon
			{
				root = children[0].getAST();
			}
		} else if (label.equals("expListPrime")) {
			if (children.length > 0) {
				root = children[0];
				root.addChild(children[1].getAST());
			}
		} else if (label.equals("exp")) {

			if (children.length == 2) {
				// <exp> --> ID <exp’>
				// <exp> --> INTNUM <exp’>
				root = children[1].getAST();

				root.addLeftChild(children[0].getAST()); // ID or INTNUM
			} else {
				// This is when expPrime has gone to EPSILON
				// aka -- the right-side of an :=

				root = children[0].getAST();
			}
		} else if (label.equals("addexp")) {
			if (children.length == 2) {
				// <exp> --> ID <exp’>
				// <exp> --> INTNUM <exp’>
				root = children[1].getAST();
				root.addLeftChild(children[0].getAST()); // ID or INTNUM
			} else {

				// This is when expPrime has gone to EPSILON
				// aka -- the right-side of an :=
				root = children[0].getAST();
			}
		} else if (label.equals("addexpPrime")) {
			if (children.length == 2) {
				root = children[0];
				root.addChild(children[1].getAST());
			}
		} else if (label.equals("mulexp")) {
			if (children.length == 2) {
				// <exp> --> ID <exp’>
				// <exp> --> INTNUM <exp’>
				root = children[1].getAST();
				root.addLeftChild(children[0].getAST()); // ID or INTNUM
			} else {
				// This is when expPrime has gone to EPSILON
				// aka -- the right-side of an :=
				root = children[0].getAST();

			}
		} else if (label.equals("mulexpPrime")) {
			if (children.length == 2) {
				root = children[0];
				root.addChild(children[1].getAST());
			}
		} else if (label.equals("powexp")) {
			if (children.length == 3) {
				root = children[1].getAST();
			} else {
				root = children[0];
			}
		} else if (label.equals("powexpPrime")) {
			if (children.length == 2) {
				root = children[0];
				root.addChild(children[1].getAST());
			}
		} else if (label.equals("error")) {
			root = this;
		}

		return root;
	}
	
	public TreeNode evalNext(VariableList idents)
	{
		System.out.println(token.getValue());
		if(token.getValue().equals(";"))
		{
			children[0].evaluate(idents);
			return children[1];
		}
		else
		{
			evaluate(idents);
			return null;
		}
	}

	public Token evaluate(VariableList idents) {
		Token t = token;

        String sValue = token.getValue();

        //System.out.println("TOKEN: " + sValue);
    
		if (sValue.equals("+")) {
			if (children.length == 2) {
				t = new Token(new BigInteger(children[0].evaluate(idents).getValue())
						.add(new BigInteger(children[1].evaluate(idents).getValue()))
						.toString());
			}
		}
		else if (sValue.equals("-")) {
			if (children.length == 2) {
				t = new Token(new BigInteger(children[0].evaluate(idents).getValue())
						.subtract(new BigInteger(children[1].evaluate(idents).getValue()))
						.toString());
			}
		}
		else if (sValue.equals("*")) {
			if (children.length == 2) {
				t = new Token(new BigInteger(children[0].evaluate(idents).getValue())
						.multiply(new BigInteger(children[1].evaluate(idents).getValue()))
						.toString());
			}
		}
		else if (sValue.equals("**")) {
			if (children.length == 2) {
				BigInteger base = new BigInteger(children[0].evaluate(idents).getValue());
				BigInteger exponent = new BigInteger(children[1].evaluate(idents).getValue());
				t = new Token(power(base, exponent));
			}
		}
		else if (sValue.equals(":=")) {
			t = children[1].evaluate(idents);
            // set value in AL
			//System.out.println(idents);
            //String sVarName = children[0].evaluate(idents).getValue();
            //System.out.println("Assigning '" + children[0].getToken().getValue() + "' to '" + t.getValue() + "'");
            
            idents.update(new Variable(children[0].getToken().getValue(), new BigInteger(t.getValue())));
            //System.out.println(idents);
		}
		else if (t.getType() == Token.TokenType.ID)
		{
			String s = "0";
			if(idents.hasVariable(t.getValue()))
			{
				s = idents.getVal(t.getValue()).toString();
			} 
			t = new Token(s);
		}
		else if (sValue.equals(";")) {
			children[0].evaluate(idents);		
            children[1].evaluate(idents);
		}
		else if (sValue.equals("end")) {
			t = children[1].evaluate(idents);
		}
        else if (sValue.equals("print")) {
            children[0].print(idents);
            t = null;
        }
        else if (sValue.equals(",")) {
            t = null;
        }
        
		
		return t;
	}

    public void print(VariableList idents) {
        String sValue = token.getValue();

        if (sValue.equals(",")) {
            children[0].print(idents);
            children[1].print(idents);
        } else if (token.getType() == Token.TokenType.ID) {
            System.out.println(idents.getVal(token.getValue()));
        } else { 
            System.out.println(this.evaluate(idents).getValue());
        }
    }
    
    public String power(BigInteger base, BigInteger exponent)
    {
    	String s = "";
    	
    	if(base.intValue() == -1 && exponent.intValue() == -1)
    	{
    		s = "-1";
    	}
    	else if((base.intValue() == 0 && exponent.intValue() != 0) || (exponent.intValue() < 0 && base.intValue() != 1)) //exponent negative or 0^n where n !=0
    	{
    		s = "0";
    	}
    	else if(exponent.intValue() == 0 || (base.intValue() == 1 && exponent.intValue() == -1)) //exponent 0
    	{
    		s = "1";
    	}
    	else //base^positive
    	{
    		s = base.pow(exponent.intValue()).toString();
    	}
    	
    	return s;
    }

	public String getLabel() {
		return label;
	}

	// the name to be displayed when printint out
	// for things without token, want label
	// for things with token, want token value
	private String getDisplay() {
		if (this.token == null) {
			return this.getLabel();
		} else {
			return this.token.getValue();
		}
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public TreeNode[] getChildren() {
		return children;
	}

	public boolean hasChildren() {
		return (children.length > 0);
	}

	public void setChildren(TreeNode[] children) {
		this.children = children;
	}

}
