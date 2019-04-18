package types;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import ast.*;

public class TypeChecker implements CommandVisitor {

	private HashMap<Command, Type> typeMap;
	private StringBuffer errorBuffer;
	/*
	 * Useful error strings:
	 *
	 * used,"Function " + func.name() + " has a void argument in position " +
	 * pos + "." used,"Function " + func.name() +
	 * " has an error in argument in position " + pos + ": " +
	 * error.getMessage()
	 *
	 * used,"Function main has invalid signature."
	 *
	 * used,"Not all paths in function " + currentFunctionName +
	 * " have a return."
	 *
	 * used,"IfElseBranch requires bool condition not " + condType + "." used,
	 * "WhileLoop requires bool condition not " + condType + "."
	 *
	 * used,"Function " + currentFunctionName + " returns " + currentReturnType
	 * + " not " + retType + "."
	 *
	 * used,"Variable " + varName + " has invalid type " + varType + "." used,
	 * "Array " + arrayName + " has invalid base type " + baseType + "."
	 */

	public TypeChecker() {
		typeMap = new HashMap<Command, Type>();
		errorBuffer = new StringBuffer();
	}

	private void reportError(int lineNum, int charPos, String message) {
		errorBuffer.append("TypeError(" + lineNum + "," + charPos + ")");
		errorBuffer.append("[" + message + "]" + "\n");
	}

	private void put(Command node, Type type) {
		if (type instanceof ErrorType) {
			reportError(node.lineNumber(), node.charPosition(), ((ErrorType) type).getMessage());
		}
		typeMap.put(node, type);
	}

	public Type getType(Command node) {
		return typeMap.get(node);
	}

	public boolean check(Command ast) {
		ast.accept(this);
		return !hasError();
	}

	public boolean hasError() {
		return errorBuffer.length() != 0;
	}

	public String errorReport() {
		return errorBuffer.toString();
	}

	// help method:
	// get the two side of node
	// use actualreturntype to check the actual return type, used when check is
	// return node has same type
	private Type actualReturnType;
	// funcName represent the function that return actualreturntype
	private String funcName;
	// set the boolean value of a function signiture to be void to be false
	// initially.
	private boolean returnNotVoid;
	// try to help combo function together

	public enum Typecombo {
		Comparison, Addition, Subtraction, Multiplication, Division, LogicalAnd, LogicalOr, LogicalNot
	}

	private List<Type> Comparisonhelp(Comparison node) {
		// visit the node
		// get the type of left/right node of the parameter node
		Expression expr_left = node.leftSide();
		Expression expr_right = node.rightSide();
		expr_left.accept(this);
		expr_right.accept(this);
		Type left_type = getType((Command) expr_left);
		Type right_type = getType((Command) expr_right);
		List<Type> twoside = new ArrayList<Type>();
		twoside.add(left_type);
		twoside.add(right_type);
		return twoside;
	}

	private List<Type> Additionhelp(Addition node) {
		// visit the node
		Expression expr_left = node.leftSide();
		Expression expr_right = node.rightSide();
		expr_left.accept(this);
		expr_right.accept(this);
		Type left_type = getType((Command) expr_left);
		Type right_type = getType((Command) expr_right);
		List<Type> twoside = new ArrayList<Type>();
		twoside.add(left_type);
		twoside.add(right_type);
		return twoside;
	}

	private List<Type> Subtractionhelp(Subtraction node) {
		// visit the node
		Expression expr_left = node.leftSide();
		Expression expr_right = node.rightSide();
		expr_left.accept(this);
		expr_right.accept(this);
		Type left_type = getType((Command) expr_left);
		Type right_type = getType((Command) expr_right);
		List<Type> twoside = new ArrayList<Type>();
		twoside.add(left_type);
		twoside.add(right_type);
		return twoside;
	}

	private List<Type> Multiplicationhelp(Multiplication node) {
		// visit the node
		Expression expr_left = node.leftSide();
		Expression expr_right = node.rightSide();
		expr_left.accept(this);
		expr_right.accept(this);
		Type left_type = getType((Command) expr_left);
		Type right_type = getType((Command) expr_right);
		List<Type> twoside = new ArrayList<Type>();
		twoside.add(left_type);
		twoside.add(right_type);
		return twoside;
	}

	private List<Type> Divisionhelp(Division node) {
		// visit the node
		Expression expr_left = node.leftSide();
		Expression expr_right = node.rightSide();
		expr_left.accept(this);
		expr_right.accept(this);
		Type left_type = getType((Command) expr_left);
		Type right_type = getType((Command) expr_right);
		List<Type> twoside = new ArrayList<Type>();
		twoside.add(left_type);
		twoside.add(right_type);
		return twoside;
	}

	private List<Type> Andhelp(LogicalAnd node) {
		// visit the node
		Expression expr_left = node.leftSide();
		Expression expr_right = node.rightSide();
		expr_left.accept(this);
		expr_right.accept(this);
		Type left_type = getType((Command) expr_left);
		Type right_type = getType((Command) expr_right);
		List<Type> twoside = new ArrayList<Type>();
		twoside.add(left_type);
		twoside.add(right_type);
		return twoside;
	}

	private List<Type> Orhelp(LogicalOr node) {
		// visit the node
		Expression expr_left = node.leftSide();
		Expression expr_right = node.rightSide();
		expr_left.accept(this);
		expr_right.accept(this);
		Type left_type = getType((Command) expr_left);
		Type right_type = getType((Command) expr_right);
		List<Type> twoside = new ArrayList<Type>();
		twoside.add(left_type);
		twoside.add(right_type);
		return twoside;
	}

	private Type visitnRetrieve(Visitable node) {
		// visit the node
		node.accept(this);
		// just get/return the type of node
		Type node_type = getType((Command) node);
		return node_type;
	}

	private void visitNode(Visitable node) {
		// visit the node
		node.accept(this);
		// just get/return the type of node
		// Type node_type = getType((Command) node);
	}

	/////////////////////
	// implement following
	@Override
	public void visit(ExpressionList node) {
		TypeList type_list = new TypeList();
		// for each expression in expressionlist, append it to typelist
		for (ast.Expression expr : node) {
			Type expr_type = visitnRetrieve(expr);
			// expression list only contain expression0
			type_list.append(expr_type);
		}
		put(node, type_list);
		/*
		 * for(int i = 0; i < node.size(); i++){ TypeList.append(node[i]); }
		 */
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(DeclarationList node) {
		for (ast.Declaration dec : node) {
			visitNode(dec);
		}
		// put(node, type_list);
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(StatementList node) {
		//set return to be needed
		returnNotVoid = true;
		// System.out.println("in stat");
		for (ast.Statement stat : node) {
			// System.out.println("stat:" + stat);
			visitNode(stat);
		}
		// System.out.println("out loop");

		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(AddressOf node) {
		crux.Symbol sym = node.symbol();
		Type node_type = sym.type();
		Type new_addr = new AddressType(node_type);
		put(node, new_addr);
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LiteralBool node) {
		// the predefined type, create a booltype and use put function to
		// check and append to hashmap
		Type booltype = new BoolType();
		put(node, booltype);
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LiteralFloat node) {
		// the predefined type, create a float and use put function to
		// check and append to hashmap
		Type floattype = new FloatType();
		put(node, floattype);
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LiteralInt node) {
		Type inttype = new IntType();
		put(node, inttype);
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(VariableDeclaration node) {
		crux.Symbol sym = node.symbol();
		Type var_type = sym.type();
		String var_name = sym.name();
		if (var_type instanceof VoidType) {
			Type error_type = new ErrorType("Variable " + var_name + " has invalid type " + var_type + ".");
			put(node, error_type);
		} else {
			put(node, var_type);
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(ArrayDeclaration node) {
		//System.out.println("array");
		crux.Symbol sym = node.symbol();
		Type array_type = sym.type();
		String array_name = sym.name();
		// get the base type of array, for checking if it is void
		Type base_type = ((ArrayType) array_type).base();
		if (base_type instanceof VoidType) {
			//System.out.println("type is void");
			Type error_type = new ErrorType("Variable " + array_name + " has invalid base type " + base_type + ".");
			put(node, error_type);
		} 
		else {
			//System.out.println("type is not void: " + base_type.toString());
			put(node, base_type);
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(FunctionDefinition node) {
		//set the return statement needed to be 0
		crux.Symbol sym_func = node.function();
		Type sym_type = sym_func.type();
		funcName = sym_func.name();
		StatementList stat_body = node.body();
		List<crux.Symbol> args_list = node.arguments();
		//System.out.println(args_list.size());
		// check if there is void type for argument(not allowed):
		// get the actual return type from FuncType, returnType method
		actualReturnType = ((FuncType) sym_type).returnType();
		// System.out.println(actualReturnType instanceof VoidType);
		if (funcName.equals("main") == false) {
			// System.out.println(funcName + " signiture is not main!");
			for (int position = 0; position < args_list.size(); position++) {
				crux.Symbol arg = args_list.get(position);
				Type arg_type = arg.type();
				// the arg type cannot be void
				if (arg_type instanceof VoidType) {
					Type error_type = new ErrorType(
							"Function " + sym_func.name() + " has a void argument in position " + position + ".");
					put(node, error_type);
					return;
				}
				// there cannot be invalid type in arg
				else if (arg_type instanceof ErrorType) {
					// errortype inheritate type, so convert arg_type to a
					// invalid errortype
					ErrorType invalid_type = (ErrorType) arg_type;
					Type error_type = new ErrorType("Function " + sym_func.name()
							+ " has an error in argument in position " + position + ": " + invalid_type.getMessage());
					put(node, error_type);
					return;
				}
			}
		}
		// check for the runtime constrain
		else {
			// System.out.println(funcName + " signiture is main!");
			// System.out.println(actualReturnType instanceof VoidType);
			if (actualReturnType instanceof VoidType == false) {
				// System.out.println("rt:" + actualReturnType);
				// if the returntype of main function is not a void type, put
				// error
				Type error_type = new ErrorType("Function main has invalid signature.");
				put(node, error_type);
				return;
			} 
		}

		// System.out.println("1");
		visit(stat_body);
		// System.out.println("2");
		// if the actual return type is a void type check if the function
		// signiture require a return.
		// System.out.println(actualReturnType);
		// System.out.println(returnVoid);
		//if actual return a non void but don't need to return:
		if ((actualReturnType instanceof VoidType == false) && returnNotVoid == true) {
			// System.out.println("2.return " + returnVoid);
			Type error_type1 = new ErrorType("Not all paths in function " + funcName + " have a return.");
			// System.out.println(1);
			put(node, error_type1);
			return;
		} else {
			// System.out.println("1.return" + returnVoid);
			put(node, actualReturnType);
			return;
		}
	}

	@Override
	public void visit(Comparison node) {
		List<Type> L_R = Comparisonhelp(node);
		put(node, L_R.get(0).compare(L_R.get(1)));
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Addition node) {
		List<Type> L_R = Additionhelp(node);
		put(node, L_R.get(0).add(L_R.get(1)));
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Subtraction node) {
		List<Type> L_R = Subtractionhelp(node);
		put(node, L_R.get(0).sub(L_R.get(1)));
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Multiplication node) {
		List<Type> L_R = Multiplicationhelp(node);
		put(node, L_R.get(0).mul(L_R.get(1)));
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Division node) {
		List<Type> L_R = Divisionhelp(node);
		put(node, L_R.get(0).div(L_R.get(1)));
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LogicalAnd node) {
		List<Type> L_R = Andhelp(node);
		put(node, L_R.get(0).and(L_R.get(1)));
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LogicalOr node) {
		List<Type> L_R = Orhelp(node);
		put(node, L_R.get(0).or(L_R.get(1)));
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LogicalNot node) {
		// System.out.println("visit not");
		put(node, visitnRetrieve(node.expression()).not());
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Dereference node) {
		// use expression method in expression class to get node expression
		Expression expr = node.expression();
		Type dere_type = visitnRetrieve(expr).deref();
		put(node, dere_type);
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Index node) {
		// use base method in index class to get base expression
		Expression expr_base = node.base();
		// use amount method in index to get amount of index expression
		Expression expr_amount = node.amount();
		// get base and amount type by self defined function
		Type base_type = visitnRetrieve(expr_base);
		Type amount_type = visitnRetrieve(expr_amount);
		put(node, base_type.index(amount_type));
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Assignment node) {
		//System.out.println("assign");
		Expression expr_source = node.source();
		Expression expr_dest = node.destination();
		Type source_type = visitnRetrieve(expr_source);
		Type dest_type = visitnRetrieve(expr_dest);
		put(node, dest_type.assign(source_type));
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Call node) {
		//System.out.println("call");
		crux.Symbol sym_func = node.function();
		Type func_type = sym_func.type();
		ExpressionList args_list = node.arguments();
		Type args_type = visitnRetrieve(args_list);
		Type call_type = func_type.call(args_type);
		put(node, call_type);
		//// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(IfElseBranch node) {
		boolean ifhavereturn;
		Type bool_type = new BoolType();
		Expression expr_cond = node.condition();
		Type cond_type = visitnRetrieve(expr_cond);
		if (cond_type.equivalent(bool_type) == false) {
			Type error_type = new ErrorType("IfElseBranch requires bool condition not " + cond_type + ".");
			put(node, error_type);
			return;
		} 
		else {
			// call visit statementlist
			returnNotVoid = true;
			StatementList stat_then = node.thenBlock();
			visit(stat_then);
			if(returnNotVoid == false){
				ifhavereturn = false;
			}
			else{
				//if return is needed
				ifhavereturn = true;
			}
			StatementList stat_else = node.elseBlock();
			visit(stat_else);
			//System.out.println("1: " + ifhavereturn);
			//System.out.println("2: " + elsehavereturn);
			//if if and else both have return (also their nest stat) then we don't need return 

			//if if and else both have return (also their nest stat) then we don't need return, check for the void return type 
			if(ifhavereturn == false && returnNotVoid == false){
				returnNotVoid = false;
			}
			else{
				returnNotVoid = true;
			}

			//visit(stat_then);
			//visit(stat_else);
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(WhileLoop node) {
		Type bool_type = new BoolType();
		Expression expr_cond = node.condition();
		Type cond_type = visitnRetrieve(expr_cond);
		StatementList stat_body = node.body();
		if (cond_type.equivalent(bool_type) == false) {
			Type error_type = new ErrorType("WhileLoop requires bool condition not " + cond_type + ".");
			put(node, error_type);
			return;
		} 
		else 
		{   
			visit(stat_body);
			returnNotVoid = true;
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Return node) {
		Expression expr_arg = node.argument();
		Type arg_type = visitnRetrieve(expr_arg);
		if (arg_type.equivalent(actualReturnType) == false) {
			Type error_type = new ErrorType(
					"Function " + funcName + " returns " + actualReturnType + " not " + arg_type + ".");
			put(node, error_type);
		} else {
			put(node, arg_type);
		}
		returnNotVoid = false;
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(ast.Error node) {
		put(node, new ErrorType(node.message()));
	}
}
