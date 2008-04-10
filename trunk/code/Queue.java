
public class Queue {
	
	private TreeNode[] nodes;
	
	public Queue()
	{
		nodes = new TreeNode[0];
	}
	
	public void addNode(TreeNode node)
	{
		TreeNode[] temp = new TreeNode[nodes.length + 1];		
		for(int i = 0; i < nodes.length; i ++)
		{
			temp[i] = nodes[i];
		}		
		temp[nodes.length] = node;		
		nodes = temp;
	}
	
	public boolean isEmpty()
	{
		if(nodes.length == 0)
		{
			return true;
		}
		
		return false;
	}
	
	public TreeNode pop()
	{
		if(!isEmpty())
		{
			TreeNode node = nodes[0];
			
			TreeNode[] temp = new TreeNode[nodes.length - 1];
			
			for(int i = 0; i < temp.length; i++)
			{
				temp[i] = nodes[i + 1];
			}
			
			nodes = temp;
			
			return node;
		}
		else
		{
			return null;
		}
	}

}
