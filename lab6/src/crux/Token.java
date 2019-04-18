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
		
		TRUE("true"),
		FALSE("false"),
		
		IF("if"),
		ELSE("else"),
		WHILE("while"),
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
		EOF();
		
		private String default_lexeme;
		
		Kind()
		{
			default_lexeme = "";
		}
		
		Kind(String lexeme)
		{
			default_lexeme = lexeme;
		}
		
		public boolean matches(int c)
		{
			return default_lexeme.length() == 1
                && default_lexeme.charAt(0) == c;
		}
		
		public boolean matches(String lexeme)
		{
			return default_lexeme.equals(lexeme);
		}
		
		public boolean hasStaticLexeme()
		{
			return default_lexeme != "";
		}
	}
	
	private int lineNum;
	private int charPos;
	Kind kind;
	private String lexeme = "";

	public static Token Error(String description, int linePos, int charPos)
	{
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.ERROR;
		tok.lexeme = description;
		return tok;
	}
	
	public static Token EOF(int linePos, int charPos)
	{
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.EOF;
		return tok;
	}
	
	public static Token Identifier(String name, int linePos, int charPos)
	{
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.IDENTIFIER;
		tok.lexeme = name;
		return tok;
	}
	
	public static Token Integer(String value, int linePos, int charPos)
	{
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.INTEGER;
		tok.lexeme = value;
		return tok;
	}
		
	public static Token Float(String value, int linePos, int charPos)
	{
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.FLOAT;
		tok.lexeme = value;
		return tok;
	}
	
	private Token(int lineNum, int charPos)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
		
		// if we don't match anything, signal error
		this.kind = Kind.ERROR;
		this.lexeme = "No Lexeme Given";
	}
	
	public Token(String lexeme, int lineNum, int charPos)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
		
		for (Kind tok: Token.Kind.values()) {
			if (tok.matches(lexeme)) {
				this.kind = tok;
				return;
			}
		}
		
		// if we don't match anything, signal error
		this.kind = Kind.ERROR;
		this.lexeme = "Unrecognized lexeme: " + lexeme;
	}
	
	public int lineNumber()
	{
		return lineNum;
	}
	
	public int charPosition()
	{
		return charPos;
	}
	
	public String lexeme()
	{
		return kind.hasStaticLexeme() ? kind.default_lexeme : lexeme;
	}
	
	public String toString()
	{
		String str = kind.name();
		
		if (!kind.hasStaticLexeme())
			str += "(" + lexeme() + ")";
		
		str += "(";
		str += "lineNum:" + lineNum;
		str += ", ";
		str += "charPos:" + charPos;
		str += ")";
		
		return str;
	}
	
	/*
	public String javaString()
	{
		String str = "";
		
		if (is(Kind.IDENTIFIER))
			str += "new CruxToken.Identifier(\"" + lexeme +"\", ";
		else if (is(Kind.ERROR))
			str += "new CruxToken.Error(\"" + lexeme + "\", ";
		else if (is(Kind.INTEGER))
			str += "new CruxToken.Integer(\"" + lexeme + "\", ";
		else if (is(Kind.FLOAT))
			str += "new CruxToken.Float(\"" + lexeme + "\", ";
		else if (is(Kind.EOF))
			str += "new CruxToken.EOF(";
		else
			str += "new CruxToken(\"" + kind.default_lexeme + "\", ";
		
		// TODO: should java-escape the token.name() strings.
		str += lineNum + ", " + charPos + ");";
		
		return str;
	}
	*/
	
	public boolean is(Token.Kind kind)
	{
		return this.kind == kind;
	}
	
	public boolean equals(Object other)
	{
		if (!(other instanceof Token))
			return false;
		Token tok = (Token)other;
			
		return this.kind == tok.kind
		    && this.lexeme().equals(tok.lexeme())
		    && this.lineNum == tok.lineNum
		    && this.charPos == tok.charPos;
	}
	
	public Kind kind()
	{
		return kind;
	}
}
