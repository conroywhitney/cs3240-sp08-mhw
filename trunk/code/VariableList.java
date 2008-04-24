import java.math.*;

public class VariableList {

	private Variable[] variables;

	public VariableList() {
		this.variables = new Variable[0];
	}

	private void add(Variable t) {
		Variable[] temp = new Variable[this.variables.length + 1];
		for (int i = 0; i < this.variables.length; i++) {
			temp[i] = this.variables[i];
		}

		temp[this.variables.length] = t;
		this.variables = temp;
	}

	public void removeLast() {
		if (variables.length > 0) {
			Variable[] temp = new Variable[variables.length - 1];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = variables[i];
			}
			variables = temp;
		}
	}

	public Variable get(int i) {
		return this.variables[i];
	}

	public BigInteger getVal(String id) {
		int index = 0;
		for (int i = 0; i < size(); i++) {
			if (variables[i].getIdentifier().equals(id)) {
				index = i;
				break;
			}
		}

		return variables[index].getValue();

	}

	public int size() {
		return this.variables.length;
	}

	public boolean hasVariable(String id) {
		boolean found = false;
		for (int i = 0; i < size(); i++) {
			if (variables[i].getIdentifier().equals(id)) {
				found = true;
			}
		}

		return found;
	}

	public void update(Variable v) {
		boolean found = false;
		for (int i = 0; i < size(); i++) {
			if (variables[i].getIdentifier().equals(v.getIdentifier())) {
				variables[i] = v;
				found = true;
			}
		}

		if (found == false) {
			add(v);
		}
	}

	public String toString() {
		String s = "***************Variables**********************";
		for (int i = 0; i < variables.length; i++) {
			s += "\n" + variables[i].getIdentifier() + "= "
					+ variables[i].getValue();
		}

		s += "\n***********************************************";
		return s;

	}

}
