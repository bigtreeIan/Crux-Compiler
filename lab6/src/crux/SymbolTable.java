package crux;

import java.util.Vector;

public class SymbolTable {
	
	private SymbolTable parent;
	private Vector<Symbol> table;
	private int depth;

	public SymbolTable()
	{
		this.table = new Vector<Symbol>();
		this.parent = null;
		this.depth = 0;
	}
	
	public SymbolTable(SymbolTable parent)
	{
		this.table = new Vector<Symbol>();
		this.parent = parent;
		this.depth = parent.depth + 1;
	}
	
	public Symbol lookup(String name) throws SymbolNotFoundError
	{
		// option 1: Symbols are hashable (hash function of name and type)
		/*
		ArrayList<Symbol> matches = new ArrayList<Symbol>();
		for (Symbol s : table) {
			if (s.name().equals(name))
				matches.append(s);
		}
		if (matches.isEmpty())
			return parent.lookup(name);
		return matches;
		*/
			
		// option 2: can only store one symbol per name
	    //           no function overloading
		//           can't have variable and function by same name
		Symbol sym = get(name);
		if (sym == null && parent != null)
			return parent.lookup(name);
		else if (sym == null) // && parent == null
			throw new SymbolNotFoundError(name);
		else
			return sym;
	}
	
	private Symbol get(String name)
	{
		for (Symbol sym : table)
			if (sym.name().equals(name))
				return sym;
		return null;
	}
	
	/*
	public Symbol lookup(String name, List<type.Type> args)
	{
		Symbol sym = table.get(name);
		if (sym == null)
			return parent.lookup(name);
		
		return sym;
	}
	*/
	
	public Symbol insert(String name) throws RedeclarationError
	{
		assert(name != null);
		assert(name != "");
		
		if (contains(name))
			throw new RedeclarationError(get(name));
		Symbol sym = new Symbol(name);
		table.add(sym);
		return sym;
	}
	
	private boolean contains(String name)
	{
		return null != get(name);
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		if (parent != null)
			sb.append(parent.toString());
		
		String indent = new String();
		for (int i = 0; i < depth; i++) {
			indent += "  ";
		}
		
		for (Symbol s : table)
		{
			sb.append(indent + s.toString() + "\n");
		}
		return sb.toString();
	}
	
	public SymbolTable parentTable()
	{
		return parent;
	}
}

class SymbolNotFoundError extends Error
{
	private static final long serialVersionUID = 1L;
	private String name;
	
	SymbolNotFoundError(String name)
	{
		this.name = name;
	}
	
	public String name()
	{
		return name;
	}
}

class RedeclarationError extends Error
{
	private static final long serialVersionUID = 1L;

	public RedeclarationError(Symbol sym)
	{
		super("Symbol " + sym + " being redeclared.");
	}
}
