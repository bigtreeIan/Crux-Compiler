package crux;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class Scanner implements Iterable<Token> {
	public static String studentName = "TODO: YOUR NAME";
	public static String studentID = "TODO: Your 8-digit id";
	public static String uciNetID = "TODO: uci-net id";
	
	private int lineNum;  // current line count
	private int charPos;  // character offset for current line
	private int nextChar; // contains the next char (-1 == EOF)
	private Reader input;
	
	public Scanner(Reader reader)
	{
		lineNum = 1;
		charPos = 0;
		input = reader;
		nextChar = readChar();
	}

	public Token next()
	{
		Token tok = nextImpl();
		//System.out.println("Scanner returning: " + tok);
		return tok;
	}
	
	private int readChar()
	{
		int c = -1;
		try {
			c = input.read();
			charPos++;
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
		if (-1 == c) {
			try {
				input.close();
			} catch (IOException e) {
			}
		}
		
		return c;
	}
		
	private boolean atEOF()
	{
		return -1 == nextChar;
	}
	
	/* Invariants:
	 *  - readOne is called always once before return, nextChar not inspected afterward
	 */
	private Token nextImpl()
	{
		while (Character.isWhitespace(nextChar)) {
			if ('\n' == nextChar) {
				lineNum++;
				charPos = 0;
			}
			nextChar = readChar();
		}
		
		if (atEOF())
			return Token.EOF(lineNum, charPos);
		
		int pos = charPos;
		
		if (nextChar == '/') {
			nextChar = readChar();
			if (nextChar == '/') {
				while ((nextChar = readChar()) != '\n') {}
				return next();
			}
			return new Token("/", lineNum, pos);
		}
		
		else if (nextChar == '=') {
			nextChar = readChar();
			if (nextChar == '=') {
				nextChar = readChar();
				return new Token("==", lineNum, pos);
			}
			return new Token("=", lineNum, pos);
		}
		
		else if (nextChar == '<') {
			nextChar = readChar();
			if (nextChar == '=') {
				nextChar = readChar();
				return new Token("<=", lineNum, pos);
			}
			return new Token("<", lineNum, pos);
		}
		
		else if (nextChar == '>') {
			nextChar = readChar();
			if (nextChar == '=') {
				nextChar = readChar();
				return new Token(">=", lineNum, pos);
			}
			return new Token(">", lineNum, pos);
		}
		
		else if (nextChar == ':') {
			nextChar = readChar();
			if (nextChar == ':') {
				nextChar = readChar();
				return new Token("::", lineNum, pos);
			}
			return new Token(":", lineNum, pos);
		}
		
		else if (nextChar == '!') {
			nextChar = readChar();
			if (nextChar == '=') {
				nextChar = readChar();
				return new Token("!=", lineNum, pos);
			}
			return Token.Error("Unexpected character: "+nextChar, lineNum, pos);
		}
			
		else if (Character.isDigit(nextChar))
		{
			String num = "";
			
			while (Character.isDigit(nextChar)) {
				num += (char)nextChar;
				nextChar = readChar();
			}
			
			if (nextChar == '.') {
				num += (char)nextChar;
				nextChar = readChar();
				while (Character.isDigit(nextChar)) {
					num += (char)nextChar;
					nextChar = readChar();
				}
				return Token.Float(num, lineNum, pos);
			}
			
			return Token.Integer(num, lineNum, pos);
		}
		
		else if (Character.isLetter(nextChar) || nextChar == '_') {
			String ident = "";
			
			while (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
				ident += (char)nextChar;
				nextChar = readChar();
			}
			
			for (Token.Kind t : Token.Kind.values()) {
				if (t.matches(ident))
					return new Token(ident, lineNum, pos);
			}
			
			return Token.Identifier(ident, lineNum, pos);
		}
		
		else {
			String c = Character.toString((char)nextChar);
			
			for (Token.Kind t : Token.Kind.values()) {
				if (t.matches(nextChar)) {
					nextChar = readChar();
					return new Token(c, lineNum, pos);
				}
			}
			
			nextChar = readChar();
			return Token.Error("Unexpected character: "+c, lineNum, pos);
		}
	}

	@Override
	public Iterator<Token> iterator()
	{
		return new ScannerIterator(this);
	}
	
	private class ScannerIterator implements Iterator<Token>
	{
		Scanner scanner;
		
		public ScannerIterator(Scanner scanner) {
			this.scanner = scanner;
		}

		@Override
		public boolean hasNext() {
			return scanner.nextChar != -1;
		}

		@Override
		public Token next() {
			return scanner.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
