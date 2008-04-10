
public class AbstractSyntaxTree {
	
	TreeNode root;
	
	public AbstractSyntaxTree(TreeNode root)
	{
		this.root = root;  
	}
	
	public String toString()
	{
		if(root == null)
		{
			return "No root node";
		}
		
		String s = "Tree:\n";
		
		Queue q = new Queue();
		
		q.addNode(root);
		
		while(!q.isEmpty())
		{
			TreeNode node = q.pop();
			
			for(int i = 0; i < node.getChildren().length; i++)
			{
				q.addNode(node.getChildren()[i]);
			}
			
			s += node.toString();
		}
		/*
		String s = " (" + root.getLabel() + " ";
		
		for(int i = 0; i < root.getChildren().length; i++)
		{
			s += root.getChildren()[i].getLabel() + " ";
		}
		
		s += ") \n";
		
		for(int i = 0; i < root.getChildren().length; i++)
		{
			s += root.toString();
		}
		*/
		
		//String s = root.toString();
		return s;		
	}
	
	public AbstractSyntaxTree getAST()
	{
		return new AbstractSyntaxTree(root.getAST());
	}
}
