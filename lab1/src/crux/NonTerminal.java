package crux;
import java.util.HashSet;
import java.util.Set;

public enum NonTerminal {
    
    // TODO: mention that we are not modeling the empty string
    // TODO: mention that we are not doing a first set for every line in the grammar
    //       some lines have already been handled by the CruxScanner
    
	
    DESIGNATOR(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
            add(Token.Kind.IDENTIFIER);
        }}),
    
    TYPE(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.IDENTIFIER);
        }}),
    LITERAL(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.INTEGER);
        	add(Token.Kind.FLOAT);
        	add(Token.Kind.TRUE);
        	add(Token.Kind.FALSE);
        }}),
    CALL_EXPRESSION(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.CALL);
        }}),
    OP0(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.GREATER_EQUAL);
        	add(Token.Kind.LESSER_EQUAL);
        	add(Token.Kind.NOT_EQUAL);
        	add(Token.Kind.EQUAL);
        	add(Token.Kind.GREATER_THAN);
        	add(Token.Kind.LESS_THAN);
       }}),
    OP1(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.ADD);
        	add(Token.Kind.SUB);
        	add(Token.Kind.OR);
       }}),
    OP2(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.MUL);
        	add(Token.Kind.DIV);
        	add(Token.Kind.AND);
       }}),
    EXPRESSION3(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.NOT);
        	add(Token.Kind.OPEN_PAREN);
        	//add(Token.Kind.CLOSE_PAREN);
        	addAll(NonTerminal.DESIGNATOR.firstSet);
        	addAll(NonTerminal.CALL_EXPRESSION.firstSet);
        	addAll(NonTerminal.LITERAL.firstSet);
       }}),
    EXPRESSION2(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(NonTerminal.EXPRESSION3.firstSet);
        }}),
    EXPRESSION1(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(NonTerminal.EXPRESSION2.firstSet);
        }}),
    EXPRESSION0(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(NonTerminal.EXPRESSION1.firstSet);
        }}),
    EXPRESSION_LIST(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(NonTerminal.EXPRESSION0.firstSet);
        }}),
    PARAMETER(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.IDENTIFIER);
        	//add(Token.Kind.COMMA);
        	//addAll(TYPE.firstSet);
        }}),
    PARAMETER_LIST(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(NonTerminal.PARAMETER.firstSet);
        }}),
    VARIABLE_DECLARATION(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.VAR);
        }}),
    ARRAY_DECLARATION(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.ARRAY);
        }}),
    FUNCTION_DEFINITION(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.FUNC);
        }}),
    DECLARATION(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(NonTerminal.VARIABLE_DECLARATION.firstSet);
        	addAll(NonTerminal.ARRAY_DECLARATION.firstSet);
        	addAll(NonTerminal.FUNCTION_DEFINITION.firstSet);
        }}),
    DECLARATION_LIST(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(NonTerminal.DECLARATION.firstSet);
        }}),    
    ASSIGNMENT_STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.LET);
        }}),
    CALL_STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(NonTerminal.CALL_EXPRESSION.firstSet);
        }}),
    IF_STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.IF);
        }}),
    WHILE_STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.WHILE);
        }}),
    RETURN_STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.RETURN);
        }}),
    STATEMENT_BLOCK(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.OPEN_BRACE);
        }}),
    STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
			addAll(NonTerminal.VARIABLE_DECLARATION.firstSet);
        	addAll(NonTerminal.CALL_STATEMENT.firstSet);
        	addAll(NonTerminal.ASSIGNMENT_STATEMENT.firstSet);
        	addAll(NonTerminal.IF_STATEMENT.firstSet);
        	addAll(NonTerminal.WHILE_STATEMENT.firstSet);
        	addAll(NonTerminal.RETURN_STATEMENT.firstSet);
        }}),
    
    STATEMENT_LIST(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(NonTerminal.STATEMENT.firstSet);
        }}),
    
    PROGRAM(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(NonTerminal.DECLARATION_LIST.firstSet);
        }});
           
    public final HashSet<Token.Kind> firstSet = new HashSet<Token.Kind>();

    NonTerminal(HashSet<Token.Kind> t)
    {
        firstSet.addAll(t);
    }
    
    public final Set<Token.Kind> firstSet()
    {
        return firstSet;
    }
}
