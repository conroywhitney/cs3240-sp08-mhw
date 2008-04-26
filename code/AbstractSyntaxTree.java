import java.util.Scanner;

public class AbstractSyntaxTree {

	TreeNode root;

	/*
	 * This class is a basic tree and also serves as a parse tree as well as an
	 * AST Constructor takes in a TreeNode which becomes the root node
	 */
	public AbstractSyntaxTree(TreeNode root) {
		this.root = root;
	}

	/*
	 * The method converts the current parse tree and returns its abstract
	 * syntax tree
	 */
	public AbstractSyntaxTree getAST() {
		return new AbstractSyntaxTree(root.getAST());
	}

	/*
	 * This method evaluates the AST from the bottom up debug: if this true, the
	 * tree will only evaluate one statement at a time and wait for user input
	 * before continuing
	 */
	public void evaluate(boolean debug) {
		VariableList identifiers = new VariableList();

		if (debug) {
			TreeNode curr = root.getChildren()[1].evalNext(identifiers);
			System.out.println(identifiers.toString());
			Scanner scan = new Scanner(System.in);

			while ((curr != null)) {
				while (!scan.hasNext()) {
				} // Wait until user inputs any line before continuing
				scan.next();
							
				curr = curr.evalNext(identifiers);
				System.out.println(identifiers.toString());
			}
		} else {
			root.evaluate(identifiers);
		}
	}

	/*
	 * return the string out put of the entire tree
	 */
	public String toString() {
		if (root == null) {
			return "No root node";
		}

		String s = "Tree:\n";
		Queue q = new Queue();
		q.addNode(root);

		while (!q.isEmpty()) {
			TreeNode node = q.pop();
			for (int i = 0; i < node.getChildren().length; i++) {
				q.addNode(node.getChildren()[i]);
			}
			s += node.toString();
		}
		return s;
	}
}
