
public class Parser {

    public Parser(String sProgram) {
        Tokenizer tokenizer = new Tokenizer(sProgram);
    }

    // <Micro-program > -> begin <statement-list> end
    public void microProgram() {
        match("begin");
        statementList();
        match("end");
    }

    // <statement-list> -> <statement> <statement-list’>
    public void statementList() {
        statement();
        statementListPrime();
    }

    // <statement-list’> -> ; <statement-list>
    // <statement-list’> -> ε
    public void statementListPrime() {
        if (match(";")) {
           statementList();
        } else {
            // nothing
        }
    }

    // <statement> ->  print ( <exp-list> )
    // <statement> ->  ID := <exp>
    public void statement() {
        if (match("print")) {
            match("(");
            expList();
            match(")");
        } else {
            matchID();
            match(":=");
            exp();
        }
    }

    // <exp-list> -> <exp> <exp-list’>
    public void expList() {
        exp();
        expListPrime();
    }

    // <exp-list’> -> <exp-list>, <exp>
    // <exp-list’> -> ε
    public void expListPrime() {
        if (expList()) {
            match(",");
            exp();
        } else {
            // nothing
        }
    }

    // <exp> -> ( <exp> ) <exp’>
    // <exp> ->  ID <exp’>
    // <exp> -> INTNUM <exp’>
    public void exp() {
        if (match("(")) {
            exp();
            match(")");
            expPrime();
        } else if (matchID()) {
            expPrime();
        } else if (matchINTNUM()) {
            expPrime();
        } else {
            return false;
        }
    }

    // <exp’> -> <bin-op> <exp>
    // <exp’> -> ε
    public void expPrime() {
        if (binOp()) {
            exp();
        } else {
            // nothing
        }
    }

    // <bin-op> ->  +
    // <bin-op> ->  -
    // <bin-op> ->  *
    // <bin-op> ->  **  
    public void binOp() {
        if (match("+")) {

        } else if (match("-")) {

        } else if (match("*")) {

        } else if (match("**")) {

        }
    } 

    public boolean match(String s) {
        
        // get token from input stream (or array or whatever)
        // check it with string class to make sure it looks the same
        // increment index
        // return result if matches
    }

    public boolean matchID() {
        // get token from array list
        // check to make sure of type ID
        // increment index
        // return result if matches
    }

    public boolean matchINTNUM() {
        // get token from array list
        // check to make sure of type INTNUM
        // increment index
        // return result if matches
    }

}
