cs3240 Team 6 README
Phase 1

COMPILING
=========
All files in this folder need to be compiled. "javac *.java" will do this.

RUNNING
=======
The main file is "Main". There are two main ways of inputing a program.

Input from Stdin:

You will need to pipe in input to the program through System.in. To pipe in the contents of a file, you would run the program as "java Main < file".

Input from File:

If you want to have our program parse the file or your OS doesnt support piping, simply try "Java Main.java filename"

NOTE: For TA test 3 (stack overflow test) our program should be run with the java -Xss 1k option 


OPTIONS
=======

We have several options available for our program. They should be applied in one argument following a '-' such as java Main.java -dpas filename or java Main.java -s filename

Options MUST be preceded by a dash character '-'.

d: set debug mode on
s: prints the Max Stack Size
p: prints the parse tree
a: prints the abstract syntax tree

DEBUG MODE
==========

Debug mode allows for a Micro program to be stepped statement by statement. NOTE: Eclipse must be used to utilize this functionality. 
Simply type any character into the console and press enter to continue to the next statement. The current id table will print with corresponding values.

TEST FILES
==========
Our test cases are in the folder called "test". Running an example test case would be something like "java Main < test/test01.txt".
Tests provided by TAs are located in the "req" folder;

FILES
=====
Main.java -- our main program

Parser.java -- our recursive-decent parser. Grabs a list of tokens and determines which rules to follow
StatementList.java -- an iterative solution for statements to avoid stack overflow

Tokenizer.java -- our tokenizer. Recognizes and groups characters into tokens
Token.java -- an instance of a token
TokenList.java -- our own version of an ArrayList for Tokens

TreeNode.java -- a node in the AST; created by Parser during parsing
AbstractSyntaxTree.java -- a file for outputting an AST given nodes with children
Queue.java -- created to perform a bredth-first search on our AST for printing

Variable.java -- an object which has an id and a numeric value, used for evaluation
VariableList.java -- an ArrayList of Variables
