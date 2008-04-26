import java.math.*;

public class StatementList {

	private TreeNode[] nodes;

	public StatementList() {
		this.nodes = new TreeNode[0];
	}

	public void add(TreeNode t) {
		TreeNode[] temp = new TreeNode[this.nodes.length + 1];
		for (int i = 0; i < this.nodes.length; i++) {
			temp[i] = this.nodes[i];
		}

		temp[this.nodes.length] = t;
		this.nodes = temp;
	}

	public TreeNode get(int i) {
		return this.nodes[i];
	}

	public void set(int i, TreeNode t) {
		this.nodes[i] = t;
	}

	public int size() {
		return this.nodes.length;
	}

}
