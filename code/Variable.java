import java.math.*;
public class Variable {
	
	private String identifier;
	private BigInteger value;
	
	public Variable(String id, BigInteger val)
	{
		identifier = id;
		value = val;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public BigInteger getValue() {
		return value;
	}

	public void setValue(BigInteger value) {
		this.value = value;
	}

}
