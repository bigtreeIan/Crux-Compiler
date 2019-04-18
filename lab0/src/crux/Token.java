package crux;

public class Token {
	
	public static enum Kind {
		AND("and"),
		OR("or"),
		NOT("not"),
		LET("let"),
		VAR("var"),
		ARRAY("array"),
		FUNC("func"),
		IF("if"),
		ELSE("else"),
		WHILE("while"),
		TRUE("true"),
		FALSE("false"),
		RETURN("return"),
		
		OPEN_PAREN("("),
		CLOSE_PAREN(")"),
		OPEN_BRACE("{"),
		CLOSE_BRACE("}"),
		OPEN_BRACKET("["),
		CLOSE_BRACKET("]"),
		ADD("+"),
		SUB("-"),
		MUL("*"),
		DIV("/"),
		GREATER_EQUAL(">="),
		LESSER_EQUAL("<="),
		NOT_EQUAL("!="),
		EQUAL("=="),
		GREATER_THAN(">"),
		LESS_THAN("<"),
		ASSIGN("="),
		COMMA(","),
		SEMICOLON(";"),
		COLON(":"),
		CALL("::"),
		
		IDENTIFIER(),
		INTEGER(),
		FLOAT(),
		ERROR(),
		COMMENT(),
		EOF();
		
		
		// TODO: complete the list of possible tokens
		
		private String default_lexeme;
		
		Kind()
		{
			default_lexeme = "";
		}
		
		Kind(String lexeme)
		{
			default_lexeme = lexeme;
		}
		
		public boolean hasStaticLexeme()
		{
			return default_lexeme != null;
		}
		
		// OPTIONAL: if you wish to also make convenience functions, feel free
		//           for example, boolean matches(String lexeme)
		//           can report whether a Token.Kind has the given lexeme
	}

	//private static final Kind  = null;
	
	private int lineNum;
	private int charPos;
	Kind kind;
	private String lexeme = "";
	
	
	// OPTIONAL: implement factory functions for some tokens, as you see fit
	          
	//Unique case:
	public static Token COMMENT(int linePos, int charPos){
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.COMMENT;
		return tok;
	}
	
	public static Token EOF(int linePos, int charPos){
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.EOF;
		return tok;
	}
	
	public static Token IDENTIFIER(String name, int linePos, int charPos){
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.IDENTIFIER;
		tok.lexeme = name;
		return tok;
	}
	
	public static Token INTEGER(String num, int linePos, int charPos){
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.INTEGER;
		tok.lexeme = num;
		return tok;
	}
	
	public static Token FLOAT(String numfloat, int linePos, int charPos){
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.FLOAT;
		tok.lexeme = numfloat;
		return tok;
	}
	
	private Token(int lineNum, int charPos){
		this.lineNum = lineNum;
		this.charPos = charPos;
		// if we don't match anything, signal error
		this.kind = Kind.ERROR;
		this.lexeme = "No Lexeme Given";
	}
	
	//Keyword:
	public Token(String lexeme, int lineNum, int charPos)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
		//if not match any Kind, there is a error;
		// if we don't match anything, signal error
		this.kind = Kind.ERROR;
		this.lexeme = "Unexpected character: " + lexeme;
		
		// TODO: based on the given lexeme determine and set the actual kind
		
		//check if the lexeme match keyword
		for (Kind unique : Kind.values()){
			//System.out.println("lexeme: " + lexeme);
			//System.out.println("unique: " + unique);
			if(lexeme.equals(unique.default_lexeme)){
				//System.out.println("if");
				this.kind = unique;
				this.lexeme = lexeme;
				break;
			}
		}
		/*
		//check if the lexeme match unique case
		for(int i = 0; i < lexeme.length(); i++){
			if(!Character.isDigit(lexeme.charAt(i))){
				
			}
		}
		*/
	}
	
	public int lineNumber()
	{
		return lineNum;
	}
	
	public int charPosition()
	{
		return charPos;
	}
	
	// Return the lexeme representing or held by this token
	public String lexeme(){
		// TODO: implement
		if(kind.hasStaticLexeme()){
			return kind.default_lexeme;
		}
		return lexeme;
	}
	
	public String toString(){
		// TODO: implement this
		
		StringBuilder printout = new StringBuilder();
		//System.out.println("111: " + kind.name());
		printout.append(kind.name());

		//when the error occurs:
		if(this.kind == Kind.ERROR){
			printout.append("(");
			printout.append(this.lexeme);
			printout.append(")");
		}
		//when it is unique case
		else if(this.kind == Kind.INTEGER || 
				this.kind == Kind.FLOAT){
			printout.append("(");
			printout.append(this.lexeme);
			printout.append(")");
		}
		else if(this.kind == Kind.IDENTIFIER){
			printout.append("(");
			printout.append(this.lexeme);
			printout.append(")");
		}
		
		//if it is comment, don't print
		else if(this.kind == Kind.COMMENT){
			return "";
		}
		//add the line number and char position:
		
		printout.append("(lineNum:");
		printout.append(lineNum);
		printout.append(", ");
		printout.append("charPos:");
		printout.append(charPos);
		printout.append(")");
		return printout.toString();
	}
	
	
	
	
	
	// OPTIONAL: function to query a token about its kind
	//           boolean is(Token.Kind kind)
	
	// OPTIONAL: add any additional helper or convenience methods
	//           that you find make for a clean design

}
