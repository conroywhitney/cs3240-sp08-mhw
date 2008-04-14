
public class TreeNode {
	
	private String label;
	private Token token;
	private TreeNode[] children;
	
	public TreeNode(Token token, String label)
	{
		this.label = label;
		this.token = token;
		children = new TreeNode[0];
	}
	
	public TreeNode(String label) {
		this(null, label);
	}
		
	public boolean addChild(TreeNode node)
	{
		if(node != null)
		{
			TreeNode[] temp = new TreeNode[children.length + 1];		
			for(int i = 0; i < children.length; i ++)
			{
				temp[i] = children[i];
			}		
			temp[children.length] = node;		
			children = temp;

			// decide what to return based on node type		
			if (node.getLabel().equals("error"))
			{
				// we added the error node, but we should stop the production of any other nodes
				return false;
			}
			else
			{
				return true;
			}
		}
		return false;
	}
	
	public void addLeftChild(TreeNode node)
	{
		if(node != null)
		{
		TreeNode[] temp = new TreeNode[children.length + 1];		
		for(int i = 1; i < children.length + 1; i++)
		{
			temp[i] = children[i - 1];
		}		
		temp[0] = node;		
		children = temp;
		}
	}
	
	public String toString()
	{
		String s = "( " + getDisplay() + " ";
		
		for(int i = 0; i < getChildren().length; i++)
		{
			s += getChildren()[i].getDisplay() + " ";
		}
		
		s += ") \n";
		
		return s;
	}
	
	
	public TreeNode getAST()
	{
		
		TreeNode root = null;
		
		if(label.equals("microProgram"))
		{
			root = children[2];  // end
			root.addChild(children[0]); // begin
			root.addChild(children[1].getAST()); // ast of statementList
		}
		else if(label.equals("statementList"))
		{
			if(children.length == 2) //if statementListPrime != null
			{
				root = children[1].getAST(); // ast of statementListPrime
				root.addLeftChild(children[0].getAST());//ast of statement
			}
			else
			{
				root = children[0].getAST(); // ast of statement
			}
		}
		else if(label.equals("statementListPrime"))
		{
			if(children.length == 2) //if statementListPrime != null
			{
				root = children[0]; // ';'
				root.addChild(children[1].getAST()); // ast of statementList
			}
		}
		else if(label.equals("statement"))
		{
			if(children[0].getLabel().equals("keyword")) // if "print"
			{
				root = children[0];   // "print"
				root.addChild(children[1].getAST()); // ast of expList 
			}
			else
			{
				// <statement>  -->  ID := <exp>
				root = children[1]; //  ':='
				root.addChild(children[0]); // ID
				root.addChild(children[2].getAST()); // ast of exp
			}
		}
		else if(label.equals("expList"))
		{
			if(children.length == 2)
			{
				root = children[1].getAST();
				root.addLeftChild(children[0].getAST());
			}
			else	// exp-list' to epsilon
			{	
				root = children[0].getAST();
			}
		}
		else if(label.equals("expListPrime"))
		{
			if(children.length > 0)
			{
				root = children[0];
				root.addChild(children[1].getAST());
			}
		}
		else if(label.equals("exp"))
		{
			if(children.length == 2)
			{
				//<exp> -->  ID <exp’>
				//<exp> -->  INTNUM <exp’>
				root = children[1].getAST();
				root.addLeftChild(children[0]); // ID or INTNUM
			}
			else if (children.length == 4)
			{
				// <exp>  -->  ( <exp> ) <exp’>
				// TODO: are we sure it's not 1, 3 ?
				root = children[3].getAST();
				root.addLeftChild(children[1].getAST());				
			}
			else		
			{
				// This is when expPrime has gone to EPSILON
				// aka -- the right-side of an := 
				root = children[0];
			}
		}
		else if(label.equals("expPrime"))
		{
			if(children.length == 2)
			{
				root = children[0];
				root.addChild(children[1].getAST());
			}
		}
		else if(label.equals("error"))
		{
			root = this;
		}
		
		return root;
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
