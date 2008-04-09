import java.util.*;

public class Tokenizer {

	//private ArrayList<String> terminals;

    public Tokenizer() {

    }

    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer();
		String test = "begin \n " +
				"ID := 6;" +
				" ID2 := asdfas;" +
				" ID3:=4+2; " +
				"ID4 :=5-ID4; " +
				"ID5:=4**3; " +
				"ID6 := ID3 * ID2; " +
				"print ( ID4 + (4*ID5)) \n " +
				"end";
        ArrayList<String> s = tokenizer.tokenize(test);
        for(int i = 0; i < s.size(); i++)
		{
			System.out.println(s.get(i));
		}
    }
	
	public ArrayList<String> tokenize(String program) {
		ArrayList<String> tokens = new ArrayList<String>();
		int currentChar = 0;
		char curr = program.charAt(currentChar);
		String token = "";
		
		
		while(currentChar < program.length())
		{
		  if(curr == '(' || curr == ')' || curr == ',' || curr == ';' || curr == '+' || curr == '-')
		  {
			  if(token.length() > 0) 
			  	{ 
				  tokens.add(token); 
				  token = "";
			  	}
			  String terminal = "" + curr;
			  tokens.add(terminal);
		  }
		  else if(curr == ':')
		  {
			  if(token.length() > 0) 
			  	{ 
				  tokens.add(token);
			  	}
			  
			  token = "" + curr;
		  }
		  else if(curr == '=')
		  {
			  if(token.equals(":"))
			  {
				  token = token + "=";
				  tokens.add(token); 
				  token = "";
			  }
			  
			  else
			  {
				  if(token.length() > 0) 
				  { 
					tokens.add(token); 
					token = "";
				  }
				  String terminal = "" + curr;
				  tokens.add(terminal);
			  }
		  }
		  else if(curr == '*')
		  {
			  if(token.length() > 0) 
			  { 
				  tokens.add(token);
				  token = "";
			  }
			  
			  if(program.length() > currentChar + 1 && program.charAt(currentChar + 1) == '*')
			  {
				  token = "**";
				  tokens.add(token);
				  token = "";
				  currentChar++;	  
			  }
			  else
			  {
				  token = "*";
				  tokens.add(token);
				  token = "";
			  }
			  
		  }
		  else if(curr == ' ' || curr == '\n' || curr == '\t')
		  {
			  if(token.length() > 0) 
			  	{ 
				  tokens.add(token); 
				  token = "";
			  	}
		  }
		  else
		  {
			  token = token + curr;
		  }  
		  currentChar++;
		  if(currentChar < program.length())
		  {
			  curr = program.charAt(currentChar);
		  }
		  else
		  {
			  if(token.length() > 0) 
			  	{ 
				  tokens.add(token); 
				  token = "";
			  	}
		  }
		}
		
		
		
		return tokens;
	}
}
