package types;

import java.util.HashMap;
import ast.*;

public class TypeChecker implements CommandVisitor {
    
    private HashMap<Command, Type> typeMap = new HashMap<Command, Type>();
    private StringBuffer errorBuffer = new StringBuffer();
    private crux.Symbol currentFunction = crux.Symbol.newError("FunctionReturnType not set.");

    private void reportError(int lineNum, int charPos, String message)
    {
        errorBuffer.append("TypeError(" + lineNum + "," + charPos + ")");
        errorBuffer.append("[" + message + "]" + "\n");
    }
        
    private Type currentReturnType()
    {
        return ((FuncType)(currentFunction.type())).returnType();
    }

    private void checkForBadArgument(FunctionDefinition node)
    {
        crux.Symbol func = node.symbol();
        TypeList args = ((FuncType)func.type()).arguments();
        int pos = 0;
        for (Type arg : args) {
            if (arg instanceof VoidType) {
                reportError(node.lineNumber(), node.charPosition(), 
                    "Function " + func.name() + " has a void argument in position " + pos + ".");
            } else if (arg instanceof ErrorType) {
                ErrorType erg = (ErrorType)arg;
                reportError(node.lineNumber(), node.charPosition(),
                    "Function " + func.name() + " has an error in argument in position " + pos + ": " + erg.getMessage());
            }
            pos++;
        }
    }

    private void checkMainSignature(FunctionDefinition node)
    {
        crux.Symbol func = node.symbol();
        if (!func.name().equals("main"))
            return;
            
        Type sig = new FuncType(new TypeList(), new VoidType());
        if (sig.equivalent(func.type()))
            return;
            
        reportError(node.lineNumber(), node.charPosition(),
            "Function main has invalid signature.");
    }
    
    private boolean allPathsReturn(Command node)
    {
        if (node instanceof Return)
            return true;
        if (node instanceof StatementList) {
            boolean result = false;
            StatementList list = (StatementList)node;
            for (Statement s : list)
                result = result || allPathsReturn((Command)s);
            return result;
        }
        if (node instanceof IfElseBranch) {
            IfElseBranch branch = (IfElseBranch)node;
            return allPathsReturn(branch.thenBlock()) && allPathsReturn(branch.elseBlock());
        }
        
        return false;
    }
    
    private void put(Command node, Type type)
    {
        if (type instanceof ErrorType) {
            reportError(node.lineNumber(), node.charPosition(),
               ((ErrorType)type).getMessage());
        }
        typeMap.put(node, type);
    }
    
    public Type getType(Command node)
    {
        return typeMap.get(node);
    }
    
    public boolean check(Command ast)
    {
        ast.accept(this);
        return !hasError();
    }
    
    public boolean hasError()
    {
        return errorBuffer.length() != 0;
    }
    
    public String errorReport()
    {
        return errorBuffer.toString();
    }

    @Override
    public void visit(ExpressionList node) {
        TypeList tlist = new TypeList();
        for (Expression e : node) {
            e.accept(this);
            tlist.append(typeMap.get(e));
        }
        put(node, tlist);
    }

    @Override
    public void visit(DeclarationList node) {
        for (Declaration d : node)
            d.accept(this);
    }

    @Override
    public void visit(StatementList node) {
        for (Statement s : node)
            s.accept(this);
    }

    @Override
    public void visit(AddressOf node) {
        put(node, new AddressType(node.symbol().type()));
    }

    @Override
    public void visit(LiteralBool node) {
        put(node, new BoolType());
    }

    @Override
    public void visit(LiteralFloat node) {
        put(node, new FloatType());
    }

    @Override
    public void visit(LiteralInt node) {
        put(node, new IntType());
    }

    @Override
    public void visit(VariableDeclaration node) {
        Type t = node.symbol().type();

        if (t.equivalent(new BoolType()))
          put(node, new VoidType());
        else if (t.equivalent(new IntType()))
          put(node, new VoidType());
        else if (t.equivalent(new FloatType()))
          put(node, new VoidType());
        else
          reportError(node.lineNumber(), node.charPosition(),
            "Variable " + node.symbol().name() + " has invalid type " + t + ".");
    }

    @Override
    public void visit(ArrayDeclaration node) {
        Type t = node.symbol().type();

        while (t instanceof ArrayType)
           t = ((ArrayType)t).base();
        
        if (t.equivalent(new BoolType()))
          put(node, new VoidType());
        else if (t.equivalent(new IntType()))
          put(node, new VoidType());
        else if (t.equivalent(new FloatType()))
          put(node, new VoidType());
        else
          reportError(node.lineNumber(), node.charPosition(),
            "Array " + node.symbol().name() + " has invalid base type " + t + ".");

        put(node, new VoidType());
    }

    @Override
    public void visit(FunctionDefinition node) {
        put(node, new VoidType());
        crux.Symbol rememberFunction = currentFunction;
        currentFunction = node.symbol();
        checkForBadArgument(node);
        checkMainSignature(node);

        node.body().accept(this);
        if (!(currentReturnType() instanceof VoidType) && !allPathsReturn(node.body())) {
            reportError(node.lineNumber(), node.charPosition(),
                "Not all paths in function " + currentFunction.name() + " have a return.");
        }
        currentFunction = rememberFunction;
    }

    @Override
    public void visit(Comparison node) {
        node.leftSide().accept(this);
        Type leftType = typeMap.get(node.leftSide());
        node.rightSide().accept(this);
        Type rightType = typeMap.get(node.rightSide());
        put(node, leftType.compare(rightType));
    }
    
    @Override
    public void visit(Addition node) {
        node.leftSide().accept(this);
        Type leftType = typeMap.get(node.leftSide());
        node.rightSide().accept(this);
        Type rightType = typeMap.get(node.rightSide());
        put(node, leftType.add(rightType));
    }
    
    @Override
    public void visit(Subtraction node) {
        node.leftSide().accept(this);
        Type leftType = typeMap.get(node.leftSide());
        node.rightSide().accept(this);
        Type rightType = typeMap.get(node.rightSide());
        put(node, leftType.sub(rightType));
    }
    
    @Override
    public void visit(Multiplication node) {
        node.leftSide().accept(this);
        Type leftType = typeMap.get(node.leftSide());
        node.rightSide().accept(this);
        Type rightType = typeMap.get(node.rightSide());
        put(node, leftType.mul(rightType));
    }
    
    @Override
    public void visit(Division node) {
        node.leftSide().accept(this);
        Type leftType = typeMap.get(node.leftSide());
        node.rightSide().accept(this);
        Type rightType = typeMap.get(node.rightSide());
        put(node, leftType.div(rightType));
    }
    
    @Override
    public void visit(LogicalAnd node) {
        node.leftSide().accept(this);
        Type leftType = typeMap.get(node.leftSide());
        node.rightSide().accept(this);
        Type rightType = typeMap.get(node.rightSide());
        put(node, leftType.and(rightType));
    }

    @Override
    public void visit(LogicalOr node) {
        node.leftSide().accept(this);
        Type leftType = typeMap.get(node.leftSide());
        node.rightSide().accept(this);
        Type rightType = typeMap.get(node.rightSide());
        put(node, leftType.or(rightType));
    }

    @Override
    public void visit(LogicalNot node) {
        node.expression().accept(this);
        Type exprType = typeMap.get(node.expression());
        put(node, exprType.not());
    }
    
    @Override
    public void visit(Dereference node) {
        node.expression().accept(this);
        Type exprType = typeMap.get(node.expression());
        put(node, exprType.deref());
    }

    @Override
    public void visit(Index node) {
        node.base().accept(this);
        Type baseType = typeMap.get(node.base());
        node.amount().accept(this);
        Type amountType = typeMap.get(node.amount());
        put(node,  baseType.index(amountType));
    }

    @Override
    public void visit(Assignment node) {
        node.destination().accept(this);
        Type destType = typeMap.get(node.destination());
        node.source().accept(this);
        Type srcType = typeMap.get(node.source());
        put(node, destType.assign(srcType));
    }

    @Override
    public void visit(Call node) {
        node.arguments().accept(this);
        Type args = typeMap.get(node.arguments());
        put(node, node.function().type().call(args));
    }

    @Override
    public void visit(IfElseBranch node) {
        assert(false);
        node.condition().accept(this);
        Type condType = typeMap.get(node.condition());
        node.thenBlock().accept(this);
        node.elseBlock().accept(this);
        
        if (!(condType instanceof BoolType))
            put(node, new ErrorType("IfElseBranch requires bool condition not " + condType + "."));
        else
            put(node, new VoidType());
    }

    @Override
    public void visit(WhileLoop node) {
        node.condition().accept(this);
        Type condType = typeMap.get(node.condition());
        node.body().accept(this);
        
        if (!(condType instanceof BoolType))
            put(node, new ErrorType("WhileLoop requires bool condition not " + condType + "."));
        else
            put(node, new VoidType());    
    }

    @Override
    public void visit(Return node) {
        node.argument().accept(this);
        Type retType = typeMap.get(node.argument());
        
        if (!retType.equivalent(currentReturnType()))
            put(node, new ErrorType("Function " + currentFunction.name() + " returns " + currentReturnType() + " not " + retType + "."));
        else
            put(node, new VoidType());
    }

    @Override
    public void visit(ast.Error node) {
        put(node, new ErrorType(node.message()));
    }
}
