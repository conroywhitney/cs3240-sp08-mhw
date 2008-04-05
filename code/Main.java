import java.util.*;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String test = "begin \n " +
				"ID := 6;" +
				" ID2 := asdfas;" +
				" ID3:=4+2; " +
				"ID4 :=5-ID4; " +
				"ID5:=4**3; " +
				"ID6 := ID3 * ID2; " +
				"print ( ID4 + (4*ID5)) \n " +
				"end";
		
		Tokenizer t = new Tokenizer();
		ArrayList<String> s = t.tokenize(test);
		
		for(int i = 0; i < s.size(); i++)
		{
			System.out.println(s.get(i));
		}
				

	}

}
