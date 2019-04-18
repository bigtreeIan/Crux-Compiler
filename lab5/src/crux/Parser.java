package crux;

import java.util.ArrayList;
import java.util.List;
import crux.*;

import ast.Command;
import types.*;

public class Parser {
    public static String studentName = "Yihan Xu";
    public static String studentID = "47011405";
    public static String uciNetID = "yihanx2";
        
	//Parser ==========================================
	private Scanner scanner;
	private Token currentToken;
	public Parser(Scanner scanner)
	{
		//implement this:
		this.scanner = scanner;
		currentToken = scanner.next();
	}
	
	public ast.Command parse()
	{
	    initSymbolTable();
	    try {
	        return program();
	    } catch (QuitParseException q) {
	        return new ast.Error(lineNumber(), charPosition(), "Could not complete parsing.");
	    }
	}
	
	//SymbolTable Management ==========================
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
	
	//Helper Methods ==========================================
	
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
	//Grammar Rule Reporting ==========================================
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
	//Error Reporting ==========================================
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
	 
	  
	// Typing System ===================================
	 private Type tryResolveType(String typeStr)
	 {
		 return Type.getBaseType(typeStr);
	 }
	 
	//correct from last project
	//edit if-statement while-statement func-definition
	//edit designator call-expression 
	//edit parameter var-declaration array-declaration function-definition
	// Grammar Rules =====================================================
	    // literal := INTEGER | FLOAT | TRUE | FALSE .
	    public ast.Expression literal()
	    {
	    	enterRule(NonTerminal.LITERAL);
	    	//set the default value of literal which is a expression to null;
	    	ast.Expression literal_exp = null;
	    	//check if the Token is INTERGER, FLOAT, TRUE, FALSE.
	    	if(have(Token.Kind.INTEGER)){
	    		//if the next Token is INTEGER, Then just use expect to move forward.
	    		Token tok_integer = expectRetrieve(Token.Kind.INTEGER);
	    		//if token is integer, set expression to be LiteralInteger
	        	literal_exp = Command.newLiteral(tok_integer);
	            exitRule(NonTerminal.LITERAL);
	    	}
	    	else if(have(Token.Kind.FLOAT)){
	    		Token tok_float = expectRetrieve(Token.Kind.FLOAT);
	    		//if token is integer, set expression to be LiteralFloat
	    		literal_exp = Command.newLiteral(tok_float);
	            exitRule(NonTerminal.LITERAL);
	    	}
	    	else if(have(Token.Kind.TRUE)){
	    		Token tok_true = expectRetrieve(Token.Kind.TRUE);
	    		//if token is integer, set expression to be LiteralTrue
	    		literal_exp = Command.newLiteral(tok_true);
	            exitRule(NonTerminal.LITERAL);
	    	}
	    	else if(have(Token.Kind.FALSE)){
	    		Token tok_false = expectRetrieve(Token.Kind.FALSE);
	    		//if token is integer, set expression to be LiteralFalse
	    		literal_exp = Command.newLiteral(tok_false);
	            exitRule(NonTerminal.LITERAL);
	    	}
	    	//if not these four, report ERROR, because literal must followed by the four above
	    	else{
	    		reportSyntaxError(NonTerminal.LITERAL);
	            exitRule(NonTerminal.LITERAL);
	    	}
	    	return literal_exp;
	    }
	    
	    // designator := IDENTIFIER { "[" expression0 "]" } .
	    public ast.Expression designator()
	    {
	        enterRule(NonTerminal.DESIGNATOR);
	        //check, and if there is id retrieve it
	        Token tok_identifier = expectRetrieve(Token.Kind.IDENTIFIER);
	        Symbol sym_identifier = tryResolveSymbol(tok_identifier);
	        int ln_num = tok_identifier.lineNumber();
	        int char_pos = tok_identifier.charPosition();
	        //first set the base line number and char position
	        //The occurrence of an identifier as part of an expression 
	        //(represents the address of that symbol).
	        ast.Expression exp_base = new ast.AddressOf(ln_num, char_pos, sym_identifier);
	        //expect(Token.Kind.IDENTIFIER);
	        //because of {} means is zero or more, check if there is [,  
	        while (have(Token.Kind.OPEN_BRACKET)) {
	        	expect(Token.Kind.OPEN_BRACKET);
	        	int lineNum = currentToken.lineNumber();
	        	//System.out.println("line:" + lineNum);
	        	int charPos = currentToken.charPosition();
	        	//System.out.println("charpos:" + charPos);
	        	ast.Expression exp_amount = expression0();
	        	//obviously, the id is followed by other expression, set the linenum and charPos;
	        	exp_base = new ast.Index(lineNum, charPos, exp_base, exp_amount);
	            expect(Token.Kind.CLOSE_BRACKET);
	        }
	        exitRule(NonTerminal.DESIGNATOR);
	        return exp_base;
	    }
	    
	    // program := declaration-list EOF .
	    public ast.DeclarationList program()
	    {
	    	enterRule(NonTerminal.PROGRAM);
	    	ast.DeclarationList dl = declaration_list();
	    	expect(Token.Kind.EOF);
	    	exitRule(NonTerminal.PROGRAM);
	    	return dl;
	    }
	    
	    // type := IDENTIFIER .
	    //RETURN TYPE from type()
	    public Type type(){
	    	enterRule(NonTerminal.TYPE);
	    	//Token IDENTIFIER = expectRetrieve(Token.Kind.IDENTIFIER);
	        //tryResolveSymbol(IDENTIFIER);
	    	Token id_tok= expectRetrieve(Token.Kind.IDENTIFIER);
	    	//get the string of the id token and retrieve the type
	    	Type id_type = tryResolveType(id_tok.lexeme());
	    	exitRule(NonTerminal.TYPE);
	    	return id_type;
	    }
	    
	    // op0 := ">=" | "<=" | "!=" | "==" | ">" | "<" .
	    public Token op0(){
	    	enterRule(NonTerminal.OP0);
	    	Token tok_op0 = null;
	    	//check which symbol is it, if not fall in to any one of the below, report an error 
	    	//check, if it is, move forward using expect
	    	if(have(Token.Kind.GREATER_EQUAL)){
	    		tok_op0 = expectRetrieve(Token.Kind.GREATER_EQUAL);
	        	exitRule(NonTerminal.OP0);
	        	return tok_op0;
	    	}
	    	else if(have(Token.Kind.LESSER_EQUAL)){
	    		tok_op0 = expectRetrieve(Token.Kind.LESSER_EQUAL);
	        	exitRule(NonTerminal.OP0);
	        	return tok_op0;
	    	}
	    	else if(have(Token.Kind.NOT_EQUAL)){
	    		tok_op0 = expectRetrieve(Token.Kind.NOT_EQUAL);
	        	exitRule(NonTerminal.OP0);
	        	return tok_op0;
	    	}
	    	else if(have(Token.Kind.EQUAL)){
	    		tok_op0 = expectRetrieve(Token.Kind.EQUAL);
	        	exitRule(NonTerminal.OP0);
	        	return tok_op0;
	    	}
	    	else if(have(Token.Kind.GREATER_THAN)){
	    		tok_op0 = expectRetrieve(Token.Kind.GREATER_THAN);
	        	exitRule(NonTerminal.OP0);
	        	return tok_op0;
	    	}
	    	else if(have(Token.Kind.LESS_THAN)){
	    		tok_op0 = expectRetrieve(Token.Kind.LESS_THAN);
	        	exitRule(NonTerminal.OP0);
	        	return tok_op0;
	    	}
	    	else{
	    		reportSyntaxError(NonTerminal.OP0);
	        	exitRule(NonTerminal.OP0);
	    	}    
	    	return tok_op0;
	    }
	    
	    // op1 := "+" | "-" | "or" .
	    public Token op1(){
	    	enterRule(NonTerminal.OP1);
	    	Token tok_op1 = null;
	    	if(have(Token.Kind.ADD)){
	    		tok_op1 = expectRetrieve(Token.Kind.ADD);
	        	exitRule(NonTerminal.OP1);
	        	return tok_op1;
	    	}
	    	else if(have(Token.Kind.SUB)){
	    		tok_op1 = expectRetrieve(Token.Kind.SUB);
	        	exitRule(NonTerminal.OP1);
	        	return tok_op1;
	    	}
	    	else if(have(Token.Kind.OR)){
	    		tok_op1 = expectRetrieve(Token.Kind.OR);
	        	exitRule(NonTerminal.OP1);
	        	return tok_op1;
	    	}
	    	else{
	    		reportSyntaxError(NonTerminal.OP1);
	        	exitRule(NonTerminal.OP1);
	    	}
	    	return tok_op1;
	    }
	    
	    //op2 := "*" | "/" | "and" .
	    public Token op2(){
	    	enterRule(NonTerminal.OP2);
	    	Token tok_op2 = null;
	    	if(have(Token.Kind.MUL)){
	    		tok_op2 = expectRetrieve(Token.Kind.MUL);
	        	exitRule(NonTerminal.OP2);
	        	return tok_op2;
	    	}
	    	else if(have(Token.Kind.DIV)){
	    		tok_op2 = expectRetrieve(Token.Kind.DIV);
	        	exitRule(NonTerminal.OP2);
	        	return tok_op2;
	    	}
	    	else if(have(Token.Kind.AND)){
	    		tok_op2 = expectRetrieve(Token.Kind.AND);
	        	exitRule(NonTerminal.OP2);
	        	return tok_op2;
	    	}
	    	else{
	    		reportSyntaxError(NonTerminal.OP2);
	        	exitRule(NonTerminal.OP2);
	    	}    
	    	return tok_op2;
	    }
	    
	    ///expression3 := "not" expression3 | "(" expression0 ")" | designator | call-expression | literal .
	    //write this first
	    public ast.Expression expression3(){
	    	enterRule(NonTerminal.EXPRESSION3);
	    	ast.Expression expression = null;
			int line_num = currentToken.lineNumber();
			int char_pos = currentToken.charPosition();
	    	//check if expression3 is which one.
	    	if(have(Token.Kind.NOT)){
	    		//check and retrieve the token not
	    		Token tok_not = expectRetrieve(Token.Kind.NOT);
	    		int lineNum = tok_not.lineNumber();
	    		int charPos = tok_not.charPosition();
	    		ast.Expression expr3 = expression3();
	    		//expression is not xxx
	    		//expr3 = new ast.Command.newExpression(expr3, tok_not, expr3);
	    		expression = Command.newExpression(expr3, tok_not, null);
	        	exitRule(NonTerminal.EXPRESSION3);
	        	return expression;
	        }
	    	else if(have(Token.Kind.OPEN_PAREN)){
	    		expect(Token.Kind.OPEN_PAREN);
	    		expression = expression0();
	    		expect(Token.Kind.CLOSE_PAREN);
	        	exitRule(NonTerminal.EXPRESSION3);
	        	return expression;
	    	}
	    	else if(have(NonTerminal.DESIGNATOR)){
	    		ast.Expression expr_designator = designator();
	    		expression = new ast.Dereference(line_num, char_pos, expr_designator);
	        	exitRule(NonTerminal.EXPRESSION3);
	        	return expression;
	    	}
	    	else if(have(NonTerminal.CALL_EXPRESSION)){
	    		expression = call_expression();
	        	exitRule(NonTerminal.EXPRESSION3);
	        	return expression;
	    	}
	    	else if(have(NonTerminal.LITERAL)){
	    		expression = literal();
	        	exitRule(NonTerminal.EXPRESSION3);
	        	return expression;
	    	}
	    	else{
	    		reportSyntaxError(NonTerminal.EXPRESSION3);
	        	exitRule(NonTerminal.EXPRESSION3);
	    	}
	    	return expression;
	    }
	    
	    //expression2 := expression3 { op2 expression3 } .
	    public ast.Expression expression2(){
	    	enterRule(NonTerminal.EXPRESSION2);
	    	ast.Expression expression = expression3();
	    	while(have(NonTerminal.OP2)){
	    		//while there is operator, set it to be the leftside parameter of newexpression
	    		Token tok_op = op2();
	    		//expression is the rightside of new expression
	    		ast.Expression expr_rightside = expression3();
	    		expression = ast.Command.newExpression(expression, tok_op, expr_rightside);
	    	}
	    	exitRule(NonTerminal.EXPRESSION2);
	    	return expression;
	    }
	    
	    //expression1 := expression2 { op1  expression2 } .
	    public ast.Expression expression1(){
	    	enterRule(NonTerminal.EXPRESSION1);
	    	ast.Expression expression = expression2();
	    	//checking if followed by op1 {} is 0 or more
	    	while(have(NonTerminal.OP1)){
	    		//while there is operator, set it to be the leftside parameter of new expression
	    		Token tok_op = op1();
	    		ast.Expression expr_rightside = expression2();
	    		expression = ast.Command.newExpression(expression, tok_op, expr_rightside);
	    	}
	    	exitRule(NonTerminal.EXPRESSION1);
	    	return expression;
	    }
	    
	    ///expression0 := expression1 [ op0 expression1 ] .
	    public ast.Expression expression0(){
	    	enterRule(NonTerminal.EXPRESSION0);
	    	ast.Expression expr_leftside = expression1();
	    	//checking if expression1 is followed by OP0, [] means o or 1
	    	if(have(NonTerminal.OP0)){
	    		//expect OP0 in order to forward to next Token
	    		//while there is operator, set it to be the leftside parameter of new expression
	    		Token tok_op = op0();
	    		ast.Expression expr_rightside = expression1();
	    		expr_leftside = ast.Command.newExpression(expr_leftside, tok_op, expr_rightside);
	    	}
	    	exitRule(NonTerminal.EXPRESSION0);
	    	return expr_leftside;
	    }
	    
	    ///call-expression := "::" IDENTIFIER "(" expression-list ")" .
	    public ast.Call call_expression(){
	    	enterRule(NonTerminal.CALL_EXPRESSION);
	    	Token tok_call = expectRetrieve(Token.Kind.CALL);
	    	Token tok_identifier = expectRetrieve(Token.Kind.IDENTIFIER);
	    	int lineNum = tok_call.lineNumber();
	    	int charPos = tok_call.charPosition();
	        Symbol sb = tryResolveSymbol(tok_identifier);
	    	//expect(Token.Kind.IDENTIFIER);
	    	expect(Token.Kind.OPEN_PAREN);
	    	ast.ExpressionList expr_call_arg = expression_list();
	    	ast.Call astcall = new ast.Call(lineNum, charPos, sb, expr_call_arg);
	    	expect(Token.Kind.CLOSE_PAREN);
	    	exitRule(NonTerminal.CALL_EXPRESSION);
	    	return astcall;
	    	
	    }
	    
	    ///expression-list := [ expression0 { "," expression0 } ] .
	    public ast.ExpressionList expression_list(){
	    	enterRule(NonTerminal.EXPRESSION_LIST);
	    	int lineNum = currentToken.lineNumber();
	    	int charPos = currentToken.charPosition();
	    	//create a expression_list with current line number and char position
	    	ast.ExpressionList expr_list = new ast.ExpressionList(lineNum, charPos);
	    	if(have(NonTerminal.EXPRESSION0)){
	    		ast.Expression expr = expression0();
	    		expr_list.add(expr);
	    		while(have(Token.Kind.COMMA)){
	    			Token tok_comma = expectRetrieve(Token.Kind.COMMA);
	    			expr = expression0();
	    			expr_list.add(expr);
	    		}
	    	}
	    	exitRule(NonTerminal.EXPRESSION_LIST);
	    	return expr_list;
	    }
	    
	    //parameter := IDENTIFIER ":" type .
	    public Symbol parameter(){
	    	enterRule(NonTerminal.PARAMETER);
	    	Token tok_identifier = expectRetrieve(Token.Kind.IDENTIFIER);
	    	Symbol sb = tryDeclareSymbol(tok_identifier);
	    	expect(Token.Kind.COLON);
	    	//get the identifier type
	    	Type type = type();
	    	//set the type of symbol to be this type
	    	sb.setType(type);
	    	exitRule(NonTerminal.PARAMETER);
	    	return sb;
	    }

	    //parameter-list := [ parameter { "," parameter } ] .
	    public List<Symbol> parameter_list(){
	    	List<Symbol> sb_list = new ArrayList<Symbol>();
	    	enterRule(NonTerminal.PARAMETER_LIST);
	    	if(have(NonTerminal.PARAMETER)){
	    		Symbol sb = parameter();
	    		sb_list.add(sb);
	    		while(have(Token.Kind.COMMA)){
	    			expect(Token.Kind.COMMA);
	    			sb = parameter();
	    			sb_list.add(sb);
	    		}
	    	}
	    	exitRule(NonTerminal.PARAMETER_LIST);
	    	return sb_list;
	    }
	    
	    ///variable-declaration := "var" IDENTIFIER ":" type ";" .
	    public ast.VariableDeclaration variable_declaration(){
	    	enterRule(NonTerminal.VARIABLE_DECLARATION);
	    	Token tok_var = expectRetrieve(Token.Kind.VAR);
	    	int lineNum = tok_var.lineNumber();
	    	int charPos = tok_var.charPosition();
	    	Token tok_identifier = expectRetrieve(Token.Kind.IDENTIFIER);
	    	int lineNum1 = tok_identifier.lineNumber();
	    	int charPos1 = tok_identifier.charPosition();
	    	Symbol sb = tryDeclareSymbol(tok_identifier);
	    	ast.VariableDeclaration var = new ast.VariableDeclaration(lineNum, charPos, sb);
	    	//expect(Token.Kind.IDENTIFIER);
	    	expect(Token.Kind.COLON);
	    	Type var_type = type();
	    	sb.setType(var_type);
	    	expect(Token.Kind.SEMICOLON);
	    	exitRule(NonTerminal.VARIABLE_DECLARATION);
	    	return var;
	    }
	    
	    ///array-declaration := "array" IDENTIFIER ":" type "[" INTEGER "]" { "[" INTEGER "]" } ";" .
	    public ast.ArrayDeclaration array_declaration(){
	    	enterRule(NonTerminal.ARRAY_DECLARATION);
	    	Token tok_array = expectRetrieve(Token.Kind.ARRAY);
	    	int lineNum = tok_array.lineNumber();
	    	int charPos = tok_array.charPosition();
	    	Token tok_identifier = expectRetrieve(Token.Kind.IDENTIFIER);
	    	int lineNum1 = tok_identifier.lineNumber();
	    	int charPos1 = tok_identifier.charPosition();
	    	Symbol sb = tryDeclareSymbol(tok_identifier);
	    	ast.ArrayDeclaration array_dec = new ast.ArrayDeclaration(lineNum, charPos, sb);
	    	//expect(Token.Kind.IDENTIFIER);
	    	expect(Token.Kind.COLON);
	    	Type array_basetype = type();
	    	expect(Token.Kind.OPEN_BRACKET);
	    	//expect(Token.Kind.INTEGER);
	    	//get the extent of the array;
	    	Token token_array_extent = expectRetrieve(Token.Kind.INTEGER);
	    	//convert the token array_extent to string
	    	String s_array_extent = token_array_extent.lexeme();
	    	//convert the string to int
	    	int int_array_extent = Integer.valueOf(s_array_extent);
            //System.out.println(int_array_extent);
	    	Type sb_type = new ArrayType(int_array_extent, array_basetype);
	    	sb.setType(sb_type);
	    	expect(Token.Kind.CLOSE_BRACKET);
	    	while(have(Token.Kind.OPEN_BRACKET)){
	    		expect(Token.Kind.OPEN_BRACKET);
	    		expect(Token.Kind.INTEGER);
	    		expect(Token.Kind.CLOSE_BRACKET);
	    	}
	    	expect(Token.Kind.SEMICOLON);
	    	exitRule(NonTerminal.ARRAY_DECLARATION);
	    	return array_dec;
	    }
	    
	    ///function-definition := "func" IDENTIFIER "(" parameter-list ")" ":" type statement-block .
	    public ast.FunctionDefinition function_definition(){
    		//System.out.println("1");	    	
	    	enterRule(NonTerminal.FUNCTION_DEFINITION);
	    	//SymbolTable sb_table = new SymbolTable();
	    	Token tok_func = expectRetrieve(Token.Kind.FUNC);
	    	int lineNum = tok_func.lineNumber();
	    	int charPos = tok_func.charPosition();
	    	Token tok_identifier = expectRetrieve(Token.Kind.IDENTIFIER);
	    	int lineNum1 = tok_identifier.lineNumber();
	    	int charPos1 = tok_identifier.charPosition();
	    	Symbol sb_func = tryDeclareSymbol(tok_identifier);
	    	//expect(Token.Kind.IDENTIFIER);
	    	expect(Token.Kind.OPEN_PAREN);
	    	enterScope();
	    	List<Symbol> sb_list_args = parameter_list();
	    	//get the TypeList from parameter_list():
	    	TypeList type_list = new types.TypeList();  
	    	//for each symbol in the list, get its type and add to the type list
    		//System.out.println("sb_list:" + sb_list_args.size());	
    		//System.out.println("sb_func:" + sb_func.name());	    	
	    	for(int i = 0; i < sb_list_args.size(); i++){
	    		type_list.append((sb_list_args.get(i)).type());
	    		//System.out.println("for loop" + type_list);	    	
	    	}		

	    	expect(Token.Kind.CLOSE_PAREN);
	    	expect(Token.Kind.COLON);
	    	Type return_type = type();
	    	ast.StatementList st_list_body = statement_block();
	    	Type sb_type = new FuncType(type_list, return_type);
	    	sb_func.setType(sb_type);
	    	ast.FunctionDefinition func_def = new ast.FunctionDefinition(lineNum, charPos, sb_func, sb_list_args, st_list_body);
	    	//Symbol sb_new = sb_table.insert(sb_func.name());
	    	//sb_new.setType(sb_type);
	    	
	    	exitScope();
	    	exitRule(NonTerminal.FUNCTION_DEFINITION);
	    	return func_def;
	    }
	    
	    ///declaration := variable-declaration | array-declaration | function-definition .
	    public ast.Declaration declaration(){
	    	enterRule(NonTerminal.DECLARATION);
	    	ast.Declaration dec = null;
	    	if(have(NonTerminal.VARIABLE_DECLARATION)){
	    		//expect(NonTerminal.VARIABLE_DECLARATION);
	    		dec = variable_declaration();
	        	exitRule(NonTerminal.DECLARATION);
	    		return dec;
	    	}
	    	else if(have(NonTerminal.ARRAY_DECLARATION)){
	    		//expect(NonTerminal.ARRAY_DECLARATION);
	    		dec = array_declaration();
	        	exitRule(NonTerminal.DECLARATION);
	    		return dec;
	    	}
	    	else if(have(NonTerminal.FUNCTION_DEFINITION)){
	    		//expect(NonTerminal.FUNCTION_DEFINITION);
	    		dec = function_definition();
	        	exitRule(NonTerminal.DECLARATION);
	    		return dec;
	    	}
	    	else{
	    		reportSyntaxError(NonTerminal.DECLARATION);
	        	exitRule(NonTerminal.DECLARATION);
	    	}
			return dec;
	    }
	    
	    ///declaration-list := { declaration } .
	    public ast.DeclarationList declaration_list(){
	    	enterRule(NonTerminal.DECLARATION_LIST);
	    	int lineNum = currentToken.lineNumber();
	    	int charPos = currentToken.charPosition();
	    	ast.DeclarationList dec_list = new ast.DeclarationList(lineNum, charPos);
	    	while(have(NonTerminal.DECLARATION)){
	    		//while there is more declaration in the statement, add them all
	    		ast.Declaration dec = declaration();
	    		dec_list.add(dec);
	    	}
	    	exitRule(NonTerminal.DECLARATION_LIST);
	    	return dec_list;
	    }
	    
	    ///assignment-statement := "let" designator "=" expression0 ";" .
	    public ast.Assignment assignment_statement(){
	    	enterRule(NonTerminal.ASSIGNMENT_STATEMENT);
	    	Token tok_let = expectRetrieve(Token.Kind.LET);
	    	int lineNum = tok_let.lineNumber();
	    	int charPos = tok_let.charPosition();
	    	ast.Expression expr_dest = designator();
	    	expect(Token.Kind.ASSIGN);
	    	ast.Expression expr_source = expression0();
	    	ast.Assignment assignment = new ast.Assignment(lineNum, charPos, expr_dest, expr_source);
	    	expect(Token.Kind.SEMICOLON);
	    	exitRule(NonTerminal.ASSIGNMENT_STATEMENT);
	    	return assignment;
	    }
	    
	    ///call-statement := call-expression ";" .
	    public ast.Call call_statement(){
	    	enterRule(NonTerminal.CALL_STATEMENT);
	    	ast.Call call = call_expression();
	    	expect(Token.Kind.SEMICOLON);
	    	exitRule(NonTerminal.CALL_STATEMENT);
	    	return call;
	    }
	    ///statement := variable-declaration | 
	    //array-declaration |
	    //call-statement | 
	    //assignment-statement | 
	    //if-statement | 
	    //while-statement | 
	    //return-statement .
	    public ast.Statement statement(){
	    	enterRule(NonTerminal.STATEMENT);
	    	ast.Statement st = null;
	    	if(have(NonTerminal.VARIABLE_DECLARATION)){
	    		st = variable_declaration();
	        	exitRule(NonTerminal.STATEMENT);
	        	return st;
	    	}
	    	else if(have(NonTerminal.ARRAY_DECLARATION)){
	    		st = array_declaration();
	    		exitRule(NonTerminal.STATEMENT);
	    		return st;
	    	}
	    	else if(have(NonTerminal.CALL_STATEMENT)){
	    		st = call_statement();
	        	exitRule(NonTerminal.STATEMENT);
	        	return st;
	    	}
	    	else if(have(NonTerminal.ASSIGNMENT_STATEMENT)){
	    		st = assignment_statement();
	        	exitRule(NonTerminal.STATEMENT);
	        	return st;
	    	}
	    	else if(have(NonTerminal.IF_STATEMENT)){
	    		st = if_statement();
	        	exitRule(NonTerminal.STATEMENT);
	        	return st;
	    	}
	    	else if(have(NonTerminal.WHILE_STATEMENT)){
	    		st = while_statement();
	        	exitRule(NonTerminal.STATEMENT);
	        	return st;
	    	}
	    	else if(have(NonTerminal.RETURN_STATEMENT)){
	    		st = return_statement();
	        	exitRule(NonTerminal.STATEMENT);
	        	return st;
	    	}
	    	else{
	    		reportSyntaxError(NonTerminal.STATEMENT);
	        	exitRule(NonTerminal.STATEMENT);
	    	}
	    	return st;
	    }

	    ///statement-list := { statement } .
	    public ast.StatementList statement_list(){
	    	enterRule(NonTerminal.STATEMENT_LIST);
	    	int lineNum = currentToken.lineNumber();
	    	int charPos = currentToken.charPosition();
	    	ast.StatementList st_list = new ast.StatementList(lineNum, charPos);
	    	while(have(NonTerminal.STATEMENT)){
	    		ast.Statement st = statement();
	    		st_list.add(st);
	    	}
	    	exitRule(NonTerminal.STATEMENT_LIST);
	    	return st_list;
	    }
	    
	    ///statement-block := "{" statement-list "}" .
	    public ast.StatementList statement_block(){
	    	enterRule(NonTerminal.STATEMENT_BLOCK);
	    	//Token tok = expectRetrieve(Token.Kind.OPEN_BRACE);
	    	expect(Token.Kind.OPEN_BRACE);
	    	ast.StatementList st_list = statement_list();
	    	expect(Token.Kind.CLOSE_BRACE);
	    	exitRule(NonTerminal.STATEMENT_BLOCK);
	    	return st_list;
	    }   
	    
	    ///if-statement := "if" expression0 statement-block [ "else" statement-block ] .
	    public ast.IfElseBranch if_statement(){
	    	enterRule(NonTerminal.IF_STATEMENT);
	    	Token tok_if = expectRetrieve(Token.Kind.IF);
	    	int lineNum = tok_if.lineNumber();
	    	int charPos = tok_if.charPosition();
	    	ast.Expression expr_condition = expression0();
	    	enterScope();
	    	ast.StatementList st_if = statement_block();
	    	ast.StatementList st_else = null;
	    	exitScope();
	    	if(have(Token.Kind.ELSE)){
	    		enterScope();
	    		/*
	    		int lineNum1 = currentToken.lineNumber();
	    		int charPos1 = currentToken.charPosition();
	    		st_else = new ast.StatementList(lineNum1, charPos1);
	    		*/
	    		Token tok_else = expectRetrieve(Token.Kind.ELSE);
	    		st_else = statement_block();
	    		exitScope();
	    	}
	    	else{
	    		int lineNum1 = currentToken.lineNumber();
	    		int charPos1 = currentToken.charPosition();
	    		st_else = new ast.StatementList(lineNum1, charPos1);
	    	}
	    	ast.IfElseBranch if_else = new ast.IfElseBranch(lineNum, charPos, expr_condition, st_if, st_else);
	    	exitRule(NonTerminal.IF_STATEMENT);
	    	return if_else;
	    }
	    
	    ///while-statement := "while" expression0 statement-block .
	    public ast.WhileLoop while_statement(){
	    	enterRule(NonTerminal.WHILE_STATEMENT);
	    	Token tok_while = expectRetrieve(Token.Kind.WHILE);
	    	int lineNum = tok_while.lineNumber();
	    	int charPos = tok_while.charPosition();
	    	ast.Expression condition = expression0();
	    	enterScope();
	    	ast.StatementList st_list_body = statement_block();
	    	ast.WhileLoop while_loop = new ast.WhileLoop(lineNum, charPos, condition, st_list_body);
	    	exitRule(NonTerminal.WHILE_STATEMENT);
	    	exitScope();
	    	return while_loop;
	    }
	    
	    ///return-statement := "return" expression0 ";" .
	    public ast.Return return_statement(){
	    	enterRule(NonTerminal.RETURN_STATEMENT);
	    	Token tok_return = expectRetrieve(Token.Kind.RETURN);
	    	int lineNum = tok_return.lineNumber();
	    	int charPos = tok_return.charPosition();
	    	ast.Expression expr_arg = expression0();
	    	ast.Return return_st = new ast.Return(lineNum, charPos, expr_arg);
	    	expect(Token.Kind.SEMICOLON);
	    	exitRule(NonTerminal.RETURN_STATEMENT);
	    	return return_st;
	    }
	         
	/*
	    public void program()
	    {
	        throw new RuntimeException("implement symbol table into grammar rules");
	    }
	*/
	}



	 
