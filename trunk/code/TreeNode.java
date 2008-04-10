
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
		for(int i = 1; i < children.length; i ++)
		{
			temp[i] = children[i];
		}		
		temp[0] = node;		
		children = temp;
		}
	}
	
	public String toString()
	{
		String s = "( " + getLabel() + " ";
		
		for(int i = 0; i < getChildren().length; i++)
		{
			System.out.println(getChildren()[i]);
			s += getChildren()[i].getLabel() + " ";
		}
		
		s += ") \n";
		/*
		for(int i = 0; i < getChildren().length; i++)
		{
			s += children[i].toString();
		}
		*/
		
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
				root.addChild(children[0].getAST());
			}
			else
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
			if(children.length == 3)
			{
				root = children[1].getAST();
			}
			else if (children.length == 4)
			{
				root = children[3].getAST();
				root.addChild(children[1].getAST());				
			}
			else
			{
				root = children[0].getAST();
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
