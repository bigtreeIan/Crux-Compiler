package crux;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import crux.Token.Kind;


public class Scanner implements Iterable<Token> {
	
	
	public Iterator<Token> iterator(){
		return null;
	}
	
	
	public static String studentName = "Yihan Xu";
	public static String studentID = "47011405";
	public static String uciNetID = "yihanx2";
	
	private int lineNum;  // current line count
	private int charPos;  // character offset for current line
	private int nextChar; // contains the next char (-1 == EOF)
	private Reader input;
	
	Scanner(Reader reader){
		// TODO: initialize the Scanner
		lineNum = 1;
		charPos = 1;
		input = reader;
		
		//make the scanner hold first char
		try {
			nextChar = input.read();
		} 
		catch (IOException e){
            e.printStackTrace();
		}
	}
	
	// OPTIONAL: helper function for reading a single char from input
	//           can be used to catch and handle any IOExceptions,
	//           advance the charPos or lineNum, etc.
	private int readChar(){
		if(nextChar == '\n'){
			try{
				nextChar = input.read();
			}
			catch (IOException e){
	            e.printStackTrace();
			}
			charPos = 1;
			lineNum++;
		}
		else if(nextChar != -1){
			try{
				nextChar = input.read();
			}
			catch (IOException e){
	            e.printStackTrace();
			}
			charPos++;
		}
		return nextChar;
	}
	
	/* Invariants:
	 *  1. call assumes that nextChar is already holding an unread character
	 *  2. return leaves nextChar containing an untokenized character
	 */
	
	public Token next(){
		// TODO: implement this
		//check if the file in empty at first 
		if(nextChar == -1){
			return Token.EOF(lineNum, charPos);
		}
		//everytime call next() if it is begin of file or end of line, readChar.
		while(Character.isSpaceChar(nextChar) || 
				nextChar == '\n'){
			readChar();
		}
		if(nextChar == -1){
			return Token.EOF(lineNum, charPos);
		}
		
		//lexeme store the longest possible token
		StringBuilder lexeme = new StringBuilder();

		//check unique keyword case
		if(nextChar == '('){
			//store charpos and linenum to mark the initial position
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			return new Token(lexeme.toString(), lm, cp);
		}
		
		else if(nextChar == ')'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			return new Token(lexeme.toString(), lm, cp);
		}
		
		else if(nextChar == '{'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			return new Token(lexeme.toString(), lm, cp);
		}
		
		else if(nextChar == '}'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			return new Token(lexeme.toString(), lm, cp);
		}
		
		else if(nextChar == '['){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			return new Token(lexeme.toString(), lm, cp);
		}
		
		else if(nextChar == ']'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			return new Token(lexeme.toString(), lm, cp);
		}
		
		else if(nextChar == '+'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			return new Token(lexeme.toString(), lm, cp);
		}
		
		else if(nextChar == '-'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			return new Token(lexeme.toString(), lm, cp);
		}
		
		else if(nextChar == '*'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			return new Token(lexeme.toString(), lm, cp);
		}
		
		//if there is '/' followed by '/', it is comment and read until the end of line. '//' is not Token
		else if(nextChar == '/'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			//System.out.println("lexeme111: " + lexeme);
			readChar();
			if(nextChar != '/'){
				return new Token(lexeme.toString(), lm, cp);
			}
			else{
				readChar();
				while(nextChar != '\n'){
					//System.out.println("lexeme222: " + lexeme);
					//lexeme.setLength(0);
					readChar();
					if(nextChar == -1){
						return Token.EOF(lm, cp);
					}
				}
				return Token.COMMENT(lm, cp);
			}
		}
		
		//token may followed by another symbol,
		else if(nextChar == '>'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			if(nextChar == '='){
				lexeme.append((char)nextChar);
				readChar();
				return new Token(lexeme.toString(), lm, cp);
			}
			else{
				return new Token(lexeme.toString(), lm, cp);
			}
		}
		
		else if(nextChar == '<'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			if(nextChar == '='){
				lexeme.append((char)nextChar);
				readChar();
				return new Token(lexeme.toString(), lm, cp);
			}
			else{
				return new Token(lexeme.toString(), lm, cp);
			}
		}
		
		else if(nextChar == '!'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			if(nextChar == '='){
				lexeme.append((char)nextChar);
				readChar();
				return new Token(lexeme.toString(), lm, cp);
			}
			else{
				return new Token(lexeme.toString(), lm, cp);
			}
		}
		
		else if(nextChar == '='){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			if(nextChar == '='){
				lexeme.append((char)nextChar);
				readChar();
				return new Token(lexeme.toString(), lm, cp);
			}
			else{
				return new Token(lexeme.toString(), lm, cp);
			}
		}
		
		else if(nextChar == ','){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			return new Token(lexeme.toString(), lm, cp);
		}
		
		else if(nextChar == ';'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			return new Token(lexeme.toString(), lm, cp);
		}
		
		else if(nextChar == ':'){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			if(nextChar == ':'){
				lexeme.append((char)nextChar);
				readChar();
				return new Token(lexeme.toString(), lm, cp);
			}
			else{
				return new Token(lexeme.toString(), lm, cp);
			}
		}
		
		//check INT and FLOAT
		//check if the nextchar is digit
		else if(Character.isDigit(nextChar)){
			int cp = charPos;
			int lm = lineNum;
			lexeme.append((char)nextChar);
			readChar();
			//read as many digit as possible
			while(Character.isDigit(nextChar)){
				lexeme.append((char)nextChar);
				readChar();
			}
			//until there is more dot, it must be a float,
			if(nextChar == '.'){
				lexeme.append((char)nextChar);
				readChar();
				// checking if it is followed by more digit.
				while(Character.isDigit(nextChar)){
					lexeme.append((char)nextChar);
					readChar();
				}
				return Token.FLOAT(lexeme.toString(), lm, cp);
			}
			//if there is no dot it means this is a int
			else{
				return Token.INTEGER(lexeme.toString(), lm, cp);
			}
		}
		
		//Check keywords and identifier:
		//check if the first char match the identity of keywords or identifier
		else if(Character.getType(nextChar) == Character.LOWERCASE_LETTER || 
				Character.getType(nextChar) == Character.UPPERCASE_LETTER || 
				nextChar == '_'){
			lexeme.append((char)nextChar);
			int cp = charPos;
			int lm = lineNum;
			readChar();
			//if followed by many letter or digit, it can be identifier or keyword
			while(Character.getType(nextChar) == Character.LOWERCASE_LETTER || 
					Character.getType(nextChar) == Character.UPPERCASE_LETTER || 
					Character.isDigit(nextChar) ||
					nextChar == '_'){
				lexeme.append((char)nextChar);	
				readChar();
			}
			
			/*
			for (Kind i : Kind.values()){
				if(lexeme.toString().equals(i)){
					return new Token(lexeme.toString(), lm, cp);
				}
				else{
					return Token.IDENTIFIER(lexeme.toString(), lm, cp);
				}
			}
		}
		*/
			if(lexeme.toString().equals("and") || 
					lexeme.toString().equals("or") || 
					lexeme.toString().equals("not") ||
					lexeme.toString().equals("let") ||
					lexeme.toString().equals("var") ||
					lexeme.toString().equals("array") ||
					lexeme.toString().equals("func") ||
					lexeme.toString().equals("if") ||
					lexeme.toString().equals("else") ||
					lexeme.toString().equals("while") ||
					lexeme.toString().equals("true") ||
					lexeme.toString().equals("false") ||
					lexeme.toString().equals("return")){
				return new Token(lexeme.toString(), lm, cp);
			}
			else{
				return Token.IDENTIFIER(lexeme.toString(), lm, cp);
			}
		}
		
		//if nothing match, return token;
		//System.out.println("lexeme1: " + lexeme);
		//System.out.println("char: " + (char)nextChar);
		if(nextChar != '\n'){
			lexeme.append((char)nextChar);
		}
		//System.out.println("lexeme2: " + lexeme);
		int cp2 = charPos;
		int lm2 = lineNum;
		readChar();
		return new Token(lexeme.toString(), lm2, cp2);
		}
	}
	// OPTIONAL: any other methods that you find convenient for implementation or testing

