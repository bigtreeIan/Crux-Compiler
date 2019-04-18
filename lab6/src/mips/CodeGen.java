package mips;

import java.util.Arrays;
import java.util.regex.Pattern;

import ast.*;
import types.*;

public class CodeGen implements ast.CommandVisitor {
    private StringBuffer errorBuffer = new StringBuffer();
    private TypeChecker tc;
    private Program program;
    private ActivationRecord currentFunction;
    //often used register
    private String reg = "$t0";
    private String reg1 = "$t1";
    private String freg = "$f0";
    private String freg1 = "$f1";
    private String funcLabel;
    public boolean mainFunc;
	Type voidType = new types.VoidType();
	Type intType = new types.IntType();
	Type floatType = new types.FloatType();
	Type boolType = new types.BoolType();
    
    //check if the function is main;
    public boolean checkMainFunction(){
    	if("main" == "main"){
    		return true;
    	}
    	return false;
    }
    /*********************************************************************/
    public CodeGen(TypeChecker tc)
    {
        this.tc = tc;
        this.program = new Program();
        //give program a mutator that gives them a beta
        program.setCg(this);
    }
    
    public boolean hasError()
    {
        return errorBuffer.length() != 0;
    }
    
    public String errorReport()
    {
        return errorBuffer.toString();
    }

    private class CodeGenException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        public CodeGenException(String errorMessage) {
            super(errorMessage);
        }
    }
    
    public boolean generate(Command ast)
    {
        try {
            currentFunction = ActivationRecord.newGlobalFrame();
            ast.accept(this);
            return !hasError();
        } catch (CodeGenException e) {
            return false;
        }
    }
    
    public Program getProgram()
    {
        return program;
    }

    @Override
    public void visit(ExpressionList node) {
    	for(Expression expr : node){
    		expr.accept(this);
    	}
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(DeclarationList node) {
    	for(Declaration dl : node){
    		dl.accept(this);
    	}
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(StatementList node) {
    	for(Statement st : node){
    		st.accept(this);
    		if(st instanceof Call){
    			Type returnType = tc.getType((Call) st);
    			if(returnType.equivalent(voidType) == false &&
    					(returnType.equivalent(intType) || returnType.equivalent(boolType)) 
    			  ){
   					program.popInt(reg);
   				}
    			if(returnType.equivalent(voidType) == false &&
    					returnType.equivalent(floatType)){
    				program.popFloat(reg);
    			}
    		}
    	}
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(AddressOf node) {
    	crux.Symbol sb = node.symbol();
    	currentFunction.getAddress(program, reg, sb);
    	program.pushInt(reg);
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(LiteralBool node) {
    	ast.LiteralBool.Value boolValue = node.value();
    	if(boolValue == ast.LiteralBool.Value.FALSE){
    		program.appendInstruction("li $t0, 0");
    		program.pushInt(reg);
    	}
    	else if(boolValue == ast.LiteralBool.Value.TRUE){
    		program.appendInstruction("li $t0, 1");
    		program.pushInt(reg);
    	}
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(LiteralFloat node) {
    	program.appendInstruction("li.s $f0, " + node.value());
    	program.pushFloat(freg);
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(LiteralInt node) {
    	program.appendInstruction("li $t0, " + node.value());
    	program.pushInt(reg);
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(VariableDeclaration node) {
    	currentFunction.add(program, node);
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(ArrayDeclaration node) {
        currentFunction.add(program, node);
    	//throw new RuntimeException("Implement this");
    }
    
    @Override
    public void visit(FunctionDefinition node) {
    	//new AR that links to its parents
        currentFunction = new ActivationRecord(node, currentFunction);
        String funcName = node.symbol().name();
        //System.out.println(funcName);
        funcLabel = program.newLabel();
        //System.out.println(funcLabel);
        int insertAt;
        if (funcName.equals("main")) {
            insertAt = program.appendInstruction(node.function().name() + ":") + 1;
        }
        else{
            insertAt = program.appendInstruction("func."+ node.function().name() + ":") + 1;
        }
        node.body().accept(this);
        int frameSize = currentFunction.stackSize();
        // Insert a function prologue at position pos
        program.insertPrologue(insertAt, frameSize);
        program.appendInstruction(funcLabel + ":");
    	if((tc.getType(node) instanceof IntType || tc.getType(node) instanceof BoolType) && 
    			tc.getType(node).equivalent(voidType) == false){
    		program.popInt("$v0");
    	}
    	if(tc.getType(node) instanceof FloatType && tc.getType(node).equivalent(voidType) == false){
    		program.popFloat("$v0");
    	}
    	
    	if(funcName.equals("main")){
    		mainFunc = true;
    	}    	
    	mainFunc = false;
    	program.appendEpilogue(frameSize);
    	currentFunction = currentFunction.parent();
    
        //System.out.println(mainFunc);
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(Addition node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	if(tc.getType(node).equivalent(floatType)){
    		program.popFloat(freg);
    		program.popFloat(freg1);
    		program.appendInstruction("add.s $f2, $f1, $f0");
    		program.pushFloat("$f2");
    	}
    	else{
    		program.popInt(reg);
    		program.popInt(reg1);
    		program.appendInstruction("add $t2, $t1, $t0");
    		program.pushInt("$t2");
    	}
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(Subtraction node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	if(tc.getType(node).equivalent(floatType)){
    		program.popFloat(freg);
    		program.popFloat(freg1);
    		program.appendInstruction("sub.s $f2, $f1, $f0");
    		program.pushFloat("$f2");
    	}
    	else{
    		program.popInt(reg);
    		program.popInt(reg1);
    		program.appendInstruction("sub $t2, $t1, $t0");
    		program.pushInt("$t2");
    	}
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(Multiplication node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	if(tc.getType(node).equivalent(floatType)){
    		program.popFloat(freg);
    		program.popFloat(freg1);
    		program.appendInstruction("mul.s $f2, $f1, $f0");
    		program.pushFloat("$f2");
    	}
    	else{
    		program.popInt(reg);
    		program.popInt(reg1);
    		program.appendInstruction("mul $t2, $t1, $t0");
    		program.pushInt("$t2");
    	}
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(Division node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	if(tc.getType(node).equivalent(floatType)){
    		program.popFloat(freg);
    		program.popFloat(freg1);
    		program.appendInstruction("div.s $f2, $f1, $f0");
    		program.pushFloat("$f2");
    	}
    	else{
    		program.popInt(reg);
    		program.popInt(reg1);
    		program.appendInstruction("div $t2, $t1, $t0");
    		program.pushInt("$t2");
    	}
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(LogicalAnd node) {
    	Expression leftSide = node.leftSide();
    	leftSide.accept(this);
    	Expression rightSide = node.rightSide();
    	rightSide.accept(this);
    	program.popInt(reg);
    	program.popInt(reg1);
    	program.appendInstruction("and $t2, $t1, $t0");
    	//store the result register
    	program.pushInt("$t2");
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(LogicalOr node) {
    	Expression leftSide = node.leftSide();
    	leftSide.accept(this);
    	Expression rightSide = node.rightSide();
    	rightSide.accept(this);
    	program.popInt(reg);
    	program.popInt(reg1);
    	program.appendInstruction("or $t2, $t1, $t0");
    	//store the result register
    	program.pushInt("$t2");
        //throw new RuntimeException("Implement this");
    }
    
    @Override
    public void visit(LogicalNot node) {
    	String notLab = program.newLabel();
    	String nextLab = program.newLabel();
    	Expression not_body = node.expression();
    	not_body.accept(this);
    	program.popInt(reg);
    	program.appendInstruction("beqz $t0, " + notLab);
    	program.appendInstruction("li $t1, 0");
    	program.appendInstruction("b " + nextLab);
    	program.appendInstruction(notLab + ":");
    	program.appendInstruction("li $t1, 1");
    	program.appendInstruction(nextLab + ":");
    	program.pushInt(reg1);
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(Comparison node) {
    	//get two side and the operation
        throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(Dereference node) {
    	node.expression().accept(this);
    	program.popInt(reg);
    	if(tc.getType(node).equivalent(new IntType()) || tc.getType(node).equivalent(new BoolType())){
    		program.appendInstruction("lw $t1, 0($t0)");
    		program.pushInt(reg1);
    	}
    	else if(tc.getType(node).equivalent(new FloatType()) == true){
    		program.appendInstruction("lwc1 $f0, 0($t0)");
    		program.pushFloat(freg);
    	}
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(Index node) {
    	Expression nodeBase = node.base();
    	nodeBase.accept(this);
    	Expression nodeAmount = node.amount();
    	nodeAmount.accept(this);
    	program.popInt(reg);
    	program.popInt("$t2");
        //System.out.println(node);
        //System.out.println(tc.getType(node));
    	program.appendInstruction("li $t1, " + ActivationRecord.numBytes(tc.getType(node)));
        //System.out.println("111");
    	program.appendInstruction("mul $t3, $t0, $t1");
    	program.appendInstruction("add $t4, $t2, $t3");
    	program.pushInt("$t4");
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(Assignment node) {
    	node.destination().accept(this);
    	node.source().accept(this);
    	if(tc.getType(node).equivalent(floatType)){
    		program.popFloat(freg);
    		program.popInt(reg);
    		program.appendInstruction("swc1 $f0, 0($t0)");
    	}
    	else{
    		program.popInt(reg);
    		program.popInt(reg1);
    		program.appendInstruction("sw $t0, 0($t1)");
    	}
    	//throw new RuntimeException("Implement this");
    }

    private boolean checkPredefinedFunc(String funcName){
    	String[] predefinedFunc = new String[] {"readInt", "readFloat", "printBool", "printInt", "printFloat", "println"};
        //System.out.println(funcName + ": " + Arrays.asList(predefinedFunc).contains(funcName));
    	if(Arrays.asList(predefinedFunc).contains(funcName)){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    
    @Override
    public void visit(Call node) {
    	Type func = node.function().type();
    	ExpressionList args = node.arguments();
    	args.accept(this);
    	String funcName = node.function().name();
        //System.out.println(funcName);
		program.appendInstruction("jal func." + funcName);
    	if(node.arguments().size() > 0){
        	int argumentSize = 0;
    		for(Expression expr : args){
    			argumentSize += ActivationRecord.numBytes(tc.getType((Command) expr));
    		}
    		program.appendInstruction("addi $sp, $sp, " + argumentSize);
    	}
    	
    	if( ((FuncType) func).returnType().equivalent(voidType) == false ){
    		program.appendInstruction("subu $sp, $sp, 4");
    		program.appendInstruction("sw $v0, 0($sp)");
    	}
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(IfElseBranch node) {
    	String elseLab = program.newLabel();
    	String thenLab = program.newLabel();
    	node.condition().accept(this);
    	program.popInt("$t5");
    	program.appendInstruction("beqz $t5, " + elseLab);
    	node.thenBlock().accept(this);
    	program.appendInstruction("b " + thenLab);
    	program.appendInstruction(elseLab + ":");
    	node.elseBlock().accept(this);
    	program.appendInstruction(thenLab + ":");
       // throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(WhileLoop node) {
    	throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(Return node) {
    	node.argument().accept(this);
    	program.appendInstruction("b " + funcLabel);
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(ast.Error node) {
        String message = "CodeGen cannot compile a " + node;
        errorBuffer.append(message);
        throw new CodeGenException(message);
    }
}
