package crux;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    public static String studentName = "Yihan Xu";
    public static String studentID = "47011405";
    public static String uciNetID = "yihanx2";
  
// SymbolTable Management ==========================
    private SymbolTable symbolTable;
    
    private void initSymbolTable()
    {
    	symbolTable = new SymbolTable();
        //throw new RuntimeException("implement this");
    }
    
    private void enterScope()
    {
    	symbolTable = new SymbolTable(symbolTable);
        //throw new RuntimeException("implement this");
    }
    
    private void exitScope()
    {
    	//exit current table 
    	symbolTable = symbolTable.parent;
        //throw new RuntimeException("implement this");
    }

    private Symbol tryResolveSymbol(Token ident)
    {
        assert(ident.is(Token.Kind.IDENTIFIER));
        String name = ident.lexeme();
        try {
            return symbolTable.lookup(name);
        } catch (SymbolNotFoundError e) {
            String message = reportResolveSymbolError(name, ident.lineNumber(), ident.charPosition());
            return new ErrorSymbol(message);
        }
    }

    private String reportResolveSymbolError(String name, int lineNum, int charPos)
    {
        String message = "ResolveSymbolError(" + lineNum + "," + charPos + ")[Could not find " + name + ".]";
        errorBuffer.append(message + "\n");
        errorBuffer.append(symbolTable.toString() + "\n");
        return message;
    }

    private Symbol tryDeclareSymbol(Token ident)
    {
        assert(ident.is(Token.Kind.IDENTIFIER));
        String name = ident.lexeme();
        try {
            return symbolTable.insert(name);
        } catch (RedeclarationError re) {
            String message = reportDeclareSymbolError(name, ident.lineNumber(), ident.charPosition());
            return new ErrorSymbol(message);
        }
    }

    private String reportDeclareSymbolError(String name, int lineNum, int charPos)
    {
        String message = "DeclareSymbolError(" + lineNum + "," + charPos + ")[" + name + " already exists.]";
        errorBuffer.append(message + "\n");
        errorBuffer.append(symbolTable.toString() + "\n");
        return message;
    }    

// Helper Methods ==========================================

    private Token expectRetrieve(Token.Kind kind)
    {
        Token tok = currentToken;
        if (accept(kind))
            return tok;
        String errorMessage = reportSyntaxError(kind);
        throw new QuitParseException(errorMessage);
        //return ErrorToken(errorMessage);
    }
        
    private Token expectRetrieve(NonTerminal nt)
    {
        Token tok = currentToken;
        if (accept(nt))
            return tok;
        String errorMessage = reportSyntaxError(nt);
        throw new QuitParseException(errorMessage);
        //return ErrorToken(errorMessage);
    }
//correct from last project
// Grammar Rule Reporting ==========================================
    private int parseTreeRecursionDepth = 0;
    private StringBuffer parseTreeBuffer = new StringBuffer();

    public void enterRule(NonTerminal nonTerminal) {
        String lineData = new String();
        for(int i = 0; i < parseTreeRecursionDepth; i++)
        {
            lineData += "  ";
        }
        lineData += nonTerminal.name();
        //System.out.println("descending " + lineData);
        parseTreeBuffer.append(lineData + "\n");
        parseTreeRecursionDepth++;
    }
    
    private void exitRule(NonTerminal nonTerminal)
    {
        parseTreeRecursionDepth--;
    }
    
    public String parseTreeReport()
    {
        return parseTreeBuffer.toString();
    }
  //correct from last project
// Error Reporting ==========================================
    private StringBuffer errorBuffer = new StringBuffer();
    
    private String reportSyntaxError(NonTerminal nt)
    {
        String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[Expected a token from " + nt.name() + " but got " + currentToken.kind() + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }
     
    private String reportSyntaxError(Token.Kind kind)
    {
        String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[Expected " + kind + " but got " + currentToken.kind() + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }
    
    public String errorReport()
    {
        return errorBuffer.toString();
    }
    
    public boolean hasError()
    {
        return errorBuffer.length() != 0;
    }
    
    private class QuitParseException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        public QuitParseException(String errorMessage) {
            super(errorMessage);
        }
    }
    
    private int lineNumber()
    {
        return currentToken.lineNumber();
    }
    
    private int charPosition()
    {
        return currentToken.charPosition();
    }
  //correct from last project   
 // Helper Methods ==========================================
    private boolean have(Token.Kind kind)
    {
    	//return currentToken.toString() == kind.toString();
    	return currentToken.is(kind);
    }
    
    private boolean have(NonTerminal nt)
    {
    	
        return nt.firstSet().contains(currentToken.kind());
    }

    private boolean accept(Token.Kind kind)
    {
        if (have(kind)) {
            currentToken = scanner.next();
            return true;
        }
        return false;
    }    
    
    private boolean accept(NonTerminal nt)
    {
        if (have(nt)) {
            currentToken = scanner.next();
            return true;
        }
        return false;
    }
   
    private boolean expect(Token.Kind kind)
    {
        if (accept(kind))
            return true;
        String errorMessage = reportSyntaxError(kind);
        throw new QuitParseException(errorMessage);
        //return false;
    }
        
    private boolean expect(NonTerminal nt)
    {
        if (accept(nt))
            return true;
        String errorMessage = reportSyntaxError(nt);
        throw new QuitParseException(errorMessage);
        //return false;
    }
    
//correct from last project
//edit if-statement while-statement func-definition
//edit designator call-expression 
//edit parameter var-declaration array-declaration function-definition
// Grammar Rules =====================================================
    
    // literal := INTEGER | FLOAT | TRUE | FALSE .
    public void literal()
    {
    	//implement this:
    	enterRule(NonTerminal.LITERAL);
    	//check if the Token is INTERGER, FLOAT, TRUE, FALSE.
    	if(have(Token.Kind.INTEGER)){
    		//if the next Token is INTEGER, Then just use expect to move forward.
    		expect(Token.Kind.INTEGER);
    	}
    	else if(have(Token.Kind.FLOAT)){
    		//same as above
    		expect(Token.Kind.FLOAT);
    	}
    	else if(have(Token.Kind.TRUE)){
    		expect(Token.Kind.TRUE);
    	}
    	else if(have(Token.Kind.FALSE)){
    		expect(Token.Kind.FALSE);
    	}
    	//if not these four, report ERROR, because literal must followed by the four above
    	else{
    		reportSyntaxError(NonTerminal.LITERAL);
    	}
        exitRule(NonTerminal.LITERAL);
    }
    
    // designator := IDENTIFIER { "[" expression0 "]" } .
    public void designator()
    {
        enterRule(NonTerminal.DESIGNATOR);
        //EDIT:
        Token IDENTIFIER = expectRetrieve(Token.Kind.IDENTIFIER);
        tryResolveSymbol(IDENTIFIER);
        //expect(Token.Kind.IDENTIFIER);
        // because of {} means is zero or more, check if there is [,  
        while (have(Token.Kind.OPEN_BRACKET)) {
        	expect(Token.Kind.OPEN_BRACKET);
            expression0();
            expect(Token.Kind.CLOSE_BRACKET);
        }
        exitRule(NonTerminal.DESIGNATOR);
    }
    
    // program := declaration-list EOF .
    public void program()
    {
    	enterRule(NonTerminal.PROGRAM);
    	declaration_list();
    	expect(Token.Kind.EOF);
    	exitRule(NonTerminal.PROGRAM);
    }
    
    // type := IDENTIFIER .
    public void type(){
    	enterRule(NonTerminal.TYPE);
    	//EDIT:xxxxxx
    	//Token IDENTIFIER = expectRetrieve(Token.Kind.IDENTIFIER);
        //tryResolveSymbol(IDENTIFIER);
    	expect(Token.Kind.IDENTIFIER);
    	exitRule(NonTerminal.TYPE);
    }
    
    // op0 := ">=" | "<=" | "!=" | "==" | ">" | "<" .
    public void op0(){
    	enterRule(NonTerminal.OP0);
    	//check which symbol is it, if not fall in to any one of the below, report an error 
    	//check, if it is, move forward using expect
    	if(have(Token.Kind.GREATER_EQUAL)){
    		expect(Token.Kind.GREATER_EQUAL);
    	}
    	else if(have(Token.Kind.LESSER_EQUAL)){
    		expect(Token.Kind.LESSER_EQUAL);
    	}
    	else if(have(Token.Kind.NOT_EQUAL)){
    		expect(Token.Kind.NOT_EQUAL);
    	}
    	else if(have(Token.Kind.EQUAL)){
    		expect(Token.Kind.EQUAL);
    	}
    	else if(have(Token.Kind.GREATER_THAN)){
    		expect(Token.Kind.GREATER_THAN);
    	}
    	else if(have(Token.Kind.LESS_THAN)){
    		expect(Token.Kind.LESS_THAN);
    	}
    	else{
    		reportSyntaxError(NonTerminal.OP0);
    	}    	
    	exitRule(NonTerminal.OP0);
    }
    
    // op1 := "+" | "-" | "or" .
    public void op1(){
    	enterRule(NonTerminal.OP1);
    	if(have(Token.Kind.ADD)){
    		expect(Token.Kind.ADD);
    	}
    	else if(have(Token.Kind.SUB)){
    		expect(Token.Kind.SUB);
    	}
    	else if(have(Token.Kind.OR)){
    		expect(Token.Kind.OR);
    	}
    	else{
    		reportSyntaxError(NonTerminal.OP1);
    	}    	
    	exitRule(NonTerminal.OP1);
    }
    
    //op2 := "*" | "/" | "and" .
    public void op2(){
    	enterRule(NonTerminal.OP2);
    	if(have(Token.Kind.MUL)){
    		expect(Token.Kind.MUL);
    	}
    	else if(have(Token.Kind.DIV)){
    		expect(Token.Kind.DIV);
    	}
    	else if(have(Token.Kind.AND)){
    		expect(Token.Kind.AND);
    	}
    	else{
    		reportSyntaxError(NonTerminal.OP2);
    	}    	
    	exitRule(NonTerminal.OP2);
    }
    
    ///expression0 := expression1 [ op0 expression1 ] .
    public void expression0(){
    	enterRule(NonTerminal.EXPRESSION0);
    	expression1();
    	//checking if expression1 is followed by OP0, [] means o or 1
    	if(have(NonTerminal.OP0)){
    		//expect OP0 in order to forward to next Token
    		op0();
    		expression1();
    	}
    	exitRule(NonTerminal.EXPRESSION0);
    }
    
    //expression1 := expression2 { op1  expression2 } .
    public void expression1(){
    	enterRule(NonTerminal.EXPRESSION1);
    	expression2();
    	//checking if followed by op1 {} is 0 or more
    	while(have(NonTerminal.OP1)){
    		op1();
    		expression2();
    	}
    	exitRule(NonTerminal.EXPRESSION1);
    }
    
    //expression2 := expression3 { op2 expression3 } .
    public void expression2(){
    	enterRule(NonTerminal.EXPRESSION2);
    	expression3();
    	while(have(NonTerminal.OP2)){
    		op2();
    		expression3();
    	}
    	exitRule(NonTerminal.EXPRESSION2);
    }
    
    ///expression3 := "not" expression3 | "(" expression0 ")" | designator | call-expression | literal .
    public void expression3(){
    	enterRule(NonTerminal.EXPRESSION3);
    	//check if expression3 is which one.
    	if(have(Token.Kind.NOT)){
    		expect(Token.Kind.NOT);
    		expression3();
    	}
    	else if(have(Token.Kind.OPEN_PAREN)){
    		expect(Token.Kind.OPEN_PAREN);
    		expression0();
    		expect(Token.Kind.CLOSE_PAREN);
    	}
    	else if(have(NonTerminal.DESIGNATOR)){
    		designator();
    	}
    	else if(have(NonTerminal.CALL_EXPRESSION)){
    		call_expression();
    	}
    	else if(have(NonTerminal.LITERAL)){
    		literal();
    	}
    	else{
    		reportSyntaxError(NonTerminal.EXPRESSION3);
    	}
    	exitRule(NonTerminal.EXPRESSION3);
    }
    
    ///call-expression := "::" IDENTIFIER "(" expression-list ")" .
    public void call_expression(){
    	enterRule(NonTerminal.CALL_EXPRESSION);
    	expect(Token.Kind.CALL);
    	Token IDENTIFIER = expectRetrieve(Token.Kind.IDENTIFIER);
        tryResolveSymbol(IDENTIFIER);
    	//expect(Token.Kind.IDENTIFIER);
    	expect(Token.Kind.OPEN_PAREN);
    	expression_list();
    	expect(Token.Kind.CLOSE_PAREN);
    	exitRule(NonTerminal.CALL_EXPRESSION);
    }
    
    ///expression-list := [ expression0 { "," expression0 } ] .
    public void expression_list(){
    	enterRule(NonTerminal.EXPRESSION_LIST);
    	if(have(NonTerminal.EXPRESSION0)){
    		expression0();
    		while(have(Token.Kind.COMMA)){
    			expect(Token.Kind.COMMA);
    			expression0();
    		}
    	}
    	exitRule(NonTerminal.EXPRESSION_LIST);
    }
    
    //parameter := IDENTIFIER ":" type .
    public void parameter(){
    	enterRule(NonTerminal.PARAMETER);
    	Token IDENTIFIER = expectRetrieve(Token.Kind.IDENTIFIER);
    	tryDeclareSymbol(IDENTIFIER);
    	//expect(Token.Kind.IDENTIFIER);
    	expect(Token.Kind.COLON);
    	type();
    	exitRule(NonTerminal.PARAMETER);
    }

    //parameter-list := [ parameter { "," parameter } ] .
    public void parameter_list(){
    	enterRule(NonTerminal.PARAMETER_LIST);
    	if(have(NonTerminal.PARAMETER)){
    		parameter();
    		while(have(Token.Kind.COMMA)){
    			expect(Token.Kind.COMMA);
    			parameter();
    		}
    	}
    	exitRule(NonTerminal.PARAMETER_LIST);
    }
    
    ///variable-declaration := "var" IDENTIFIER ":" type ";" .
    public void variable_declaration(){
    	enterRule(NonTerminal.VARIABLE_DECLARATION);
    	expect(Token.Kind.VAR);
    	Token INENTIFIER = expectRetrieve(Token.Kind.IDENTIFIER);
    	tryDeclareSymbol(INENTIFIER);
    	//expect(Token.Kind.IDENTIFIER);
    	expect(Token.Kind.COLON);
    	type();
    	expect(Token.Kind.SEMICOLON);
    	exitRule(NonTerminal.VARIABLE_DECLARATION);
    }
    
    ///array-declaration := "array" IDENTIFIER ":" type "[" INTEGER "]" { "[" INTEGER "]" } ";" .
    public void array_declaration(){
    	enterRule(NonTerminal.ARRAY_DECLARATION);
    	expect(Token.Kind.ARRAY);
    	Token INENTIFIER = expectRetrieve(Token.Kind.IDENTIFIER);
    	tryDeclareSymbol(INENTIFIER);
    	//expect(Token.Kind.IDENTIFIER);
    	expect(Token.Kind.COLON);
    	type();
    	expect(Token.Kind.OPEN_BRACKET);
    	expect(Token.Kind.INTEGER);
    	expect(Token.Kind.CLOSE_BRACKET);
    	while(have(Token.Kind.OPEN_BRACKET)){
    		expect(Token.Kind.OPEN_BRACKET);
    		expect(Token.Kind.INTEGER);
    		expect(Token.Kind.CLOSE_BRACKET);
    	}
    	expect(Token.Kind.SEMICOLON);
    	exitRule(NonTerminal.ARRAY_DECLARATION);
    }
    
    ///function-definition := "func" IDENTIFIER "(" parameter-list ")" ":" type statement-block .
    public void function_definition(){
    	enterRule(NonTerminal.FUNCTION_DEFINITION);
    	expect(Token.Kind.FUNC);
    	Token IDENTIFIER = expectRetrieve(Token.Kind.IDENTIFIER);
    	tryDeclareSymbol(IDENTIFIER);
    	//expect(Token.Kind.IDENTIFIER);
    	expect(Token.Kind.OPEN_PAREN);
    	enterScope();
    	parameter_list();
    	expect(Token.Kind.CLOSE_PAREN);
    	expect(Token.Kind.COLON);
    	type();
    	statement_block();
    	exitScope();
    	exitRule(NonTerminal.FUNCTION_DEFINITION);
    }
    
    ///declaration := variable-declaration | array-declaration | function-definition .
    public void declaration(){
    	enterRule(NonTerminal.DECLARATION);
    	if(have(NonTerminal.VARIABLE_DECLARATION)){
    		//expect(NonTerminal.VARIABLE_DECLARATION);
    		variable_declaration();
    	}
    	else if(have(NonTerminal.ARRAY_DECLARATION)){
    		//expect(NonTerminal.ARRAY_DECLARATION);
    		array_declaration();
    	}
    	else if(have(NonTerminal.FUNCTION_DEFINITION)){
    		//expect(NonTerminal.FUNCTION_DEFINITION);
    		function_definition();
    	}
    	else{
    		reportSyntaxError(NonTerminal.DECLARATION);
    	}
    	exitRule(NonTerminal.DECLARATION);
    }
    
    ///declaration-list := { declaration } .
    public void declaration_list(){
    	enterRule(NonTerminal.DECLARATION_LIST);
    	while(have(NonTerminal.DECLARATION)){
    		declaration();
    	}
    	exitRule(NonTerminal.DECLARATION_LIST);
    }
    
    ///assignment-statement := "let" designator "=" expression0 ";" .
    public void assignment_statement(){
    	enterRule(NonTerminal.ASSIGNMENT_STATEMENT);
    	expect(Token.Kind.LET);
    	designator();
    	expect(Token.Kind.ASSIGN);
    	expression0();
    	expect(Token.Kind.SEMICOLON);
    	exitRule(NonTerminal.ASSIGNMENT_STATEMENT);
    }
    
    ///call-statement := call-expression ";" .
    public void call_statement(){
    	enterRule(NonTerminal.CALL_STATEMENT);
    	call_expression();
    	expect(Token.Kind.SEMICOLON);
    	exitRule(NonTerminal.CALL_STATEMENT);
    }
    
    ///if-statement := "if" expression0 statement-block [ "else" statement-block ] .
    public void if_statement(){
    	enterRule(NonTerminal.IF_STATEMENT);
    	expect(Token.Kind.IF);
    	expression0();
    	enterScope();
    	statement_block();
    	exitScope();
    	if(have(Token.Kind.ELSE)){
    		enterScope();
    		expect(Token.Kind.ELSE);
    		statement_block();
    		exitScope();
    	}
    	exitRule(NonTerminal.IF_STATEMENT);
    }
    
    ///while-statement := "while" expression0 statement-block .
    public void while_statement(){
    	enterRule(NonTerminal.WHILE_STATEMENT);
    	expect(Token.Kind.WHILE);
    	expression0();
    	enterScope();
    	statement_block();
    	exitRule(NonTerminal.WHILE_STATEMENT);
    	exitScope();
    }
    
    ///return-statement := "return" expression0 ";" .
    public void return_statement(){
    	enterRule(NonTerminal.RETURN_STATEMENT);
    	expect(Token.Kind.RETURN);
    	expression0();
    	expect(Token.Kind.SEMICOLON);
    	exitRule(NonTerminal.RETURN_STATEMENT);
    }
    
    ///statement := variable-declaration | call-statement | assignment-statement | if-statement | while-statement | return-statement .
    public void statement(){
    	enterRule(NonTerminal.STATEMENT);
    	if(have(NonTerminal.VARIABLE_DECLARATION)){
    		variable_declaration();
    	}
    	else if(have(NonTerminal.CALL_STATEMENT)){
    		call_statement();
    	}
    	else if(have(NonTerminal.ASSIGNMENT_STATEMENT)){
    		assignment_statement();
    	}
    	else if(have(NonTerminal.IF_STATEMENT)){
    		if_statement();
    	}
    	else if(have(NonTerminal.WHILE_STATEMENT)){
    		while_statement();
    	}
    	else if(have(NonTerminal.RETURN_STATEMENT)){
    		return_statement();
    	}
    	else{
    		reportSyntaxError(NonTerminal.STATEMENT);
    	}
    	exitRule(NonTerminal.STATEMENT);
    }

    ///statement-list := { statement } .
    public void statement_list(){
    	enterRule(NonTerminal.STATEMENT_LIST);
    	while(have(NonTerminal.STATEMENT)){
    		statement();
    	}
    	exitRule(NonTerminal.STATEMENT_LIST);
    }
    
    ///statement-block := "{" statement-list "}" .
    public void statement_block(){
    	enterRule(NonTerminal.STATEMENT_BLOCK);
    	expect(Token.Kind.OPEN_BRACE);
    	statement_list();
    	expect(Token.Kind.CLOSE_BRACE);
    	exitRule(NonTerminal.STATEMENT_BLOCK);
    }         
// Parser ==========================================
    private Scanner scanner;
    private Token currentToken;
    public Parser(Scanner scanner)
    {
    	//implement this:
    	this.scanner = scanner;
    	currentToken = scanner.next();
    }
    public void parse()
    {
        initSymbolTable();
        try {
            program();
        } catch (QuitParseException q) {
            errorBuffer.append("SyntaxError(" + lineNumber() + "," + charPosition() + ")");
            errorBuffer.append("[Could not complete parsing.]");
        }
    }
    
/*
    public void program()
    {
        throw new RuntimeException("implement symbol table into grammar rules");
    }
*/
}


