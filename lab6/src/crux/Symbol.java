package crux;

import types.Type;
import types.ErrorType;

public class Symbol {
	
	private String name;
	private Type type;

	public Symbol(String name) {
		this.name = name;
		this.type = new ErrorType("Type not set.");
	}
	
	public String name()
	{
		return this.name;
	}
	
	public void setType(Type type)
	{
		this.type = type;
	}
	
	public Type type()
	{
		return type;
	}

	public String toString()
	{
		return "Symbol(" + name + ":" + type + ")";
	}

	public static Symbol newError(String message) {
		return new ErrorSymbol(message);
	}
}

class ErrorSymbol extends Symbol
{
	public ErrorSymbol(String message)
	{
		super(message);
	}
}
