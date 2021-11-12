To run my program in IntelliJ:
 1. Open IntelliJ.
 2. Choose one of the configurations listed (code_1.txt, code_2.txt, code_3.txt, gui)

To run my program as a standalone application:
 1. Open the terminal.
 2. Navigate to from this directory to 'out/artifacts/project_jar/'.
 3. Type in the command "java -jar project.jar <arguments>"

NOTES:
 - This project was compiled in Java 14, and certain DLL files utilised by the program's version of JavaFX require that its JRE be 64-bit. As such, I have included a
   copy of OpenJRE 15.0.2 (64-bit) for temporary transfer to the Java Environment Path. It is contained within this directory's 'jdk' folder.
 - This program contains a number of different tests for separate parts of the system. Different codes must be entered in after the 'project.jar' to change the
   program's behaviour. These are as below:
	
	ARGUMENT_1	ARGUMENT_2				PURPOSE
	
	gui							Runs the graphical interface.

	file		the name of a file contained in 'code'	Runs the interpreter's analysis sequence through the text of the file supplied as the second argument.

	ln							Prints out a list of states that composed the NFA’s for three separate tokens.

	pl							Prints out the results of a series of lexical analyses to test the regular expressions of
								important tokens.

	lp							Tests how the lexer handles token precedence by checking the difference between keywords and other
								text.

	eg							Creates a list of pairs of pointed grammars, some of which are equal and some of which 
								aren’t. Tests each pair in the list to check that they are being compared properly.

	cl							Tests the output of the closure function that the parser uses to create new states in its DFA.

	rd							Tests the behavior of the reduce action when applied to an artificial parsing session’s stack
								and input.

	al							Tests how a strip of text-based code is lexically analysed by the main language’s lexer.

	tl							Tests how a strip of text-based code is lexically analysed by a test lexer.

	ac							Tests how the main language’s parser handles constructing its parsing table.

	at							Tests how long it takes for the main language’s parser to construct its table.

	ot							Tests how long it takes for a small test language’s parser to construct its table.

	1p							Tests how the lexer handles token precedence by checking the difference between keywords and
								other text.

	2p							Tests how a different test language parses through a series of already analysed tokens.

	ap		an integer from 1 to 9			Lexically analyses a strip of text-based code, and tests how the main language parses through it.
								This code strip is determined by the second integer parameter.

	op							Lexically analyses a strip of text-based code, and tests how a parser specifically dealing with
								operations parses through it.

	va							Tests the string values of a series of Value concise tokens and compares them to their execution
								results.

	lk							Tests how a lookahead (LR(1)) parser handles creating lookahead tokens for its DFA.
