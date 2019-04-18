package crux;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class SymbolTable {
	//create parent scope of the current table
    protected SymbolTable parent;
    //create a map that contain current symbol, symbol name and type is symbol
    private Map<String, Symbol> currenttable;
    //the depth of the scope
    private int depth = 0;
    //for the pre-define function
    public static String[] pre_defined_func = {
    		"readInt", "readFloat", "printBool", "printInt", "printFloat", "println"
    };
    public SymbolTable(){
    	currenttable = new LinkedHashMap<String, Symbol>();
    	//loop through the pre-defined function array, create symbol for each of them
    	for(int i = 0; i < pre_defined_func.length; i++){
    		Symbol pre_defined_symbol = new Symbol(pre_defined_func[i]);
    		currenttable.put(pre_defined_func[i], pre_defined_symbol);
    	}
        //throw new RuntimeException("implement this");
    }
    
    public SymbolTable(SymbolTable parent)
    {
    	depth = parent.depth + 1;
    	this.parent = parent;
    	currenttable = new LinkedHashMap<String, Symbol>();
        //throw new RuntimeException("implement this");
    }
    
    public Symbol lookup(String name) throws SymbolNotFoundError
    {
    	Symbol target_symbol = currenttable.get(name);
    	//base-case, if find the symbol, just return the value
    	if(target_symbol != null){
    		return target_symbol;
    	}
    	//recursivly call lookup
    	else{
    		//if did not find symbol and there is no upper scope, throw error
    		if(parent == null){
    			throw new SymbolNotFoundError(name);
    		}
    		//else recursively call lookup
    		else{
    			target_symbol = parent.lookup(name);
    		}
    		return target_symbol;
    	}
    	//throw new RuntimeException("implement this");
    	}    
    
    //insert, track if there is redeclaration by keep all the known symbol
    public Symbol insert(String name) throws RedeclarationError
    {
    	Symbol check_symbol = currenttable.get(name);
    	//if not null, the symbol is already known
    	if(check_symbol != null){
    		throw new RedeclarationError(check_symbol);
    	}
    	//add to the know list
    	else{
    		check_symbol = new Symbol(name);
    		currenttable.put(name, check_symbol);
    		return check_symbol;
    	}
        //throw new RuntimeException("implement this");
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if (parent != null){
            sb.append(parent.toString());
        }
        
        String indent = new String();
        for (int i = 0; i < depth; i++) {
            indent += "  ";
        }
        
        for (Symbol s : currenttable.values()){
            sb.append(indent + s.toString() + "\n");
        }
        return sb.toString();
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
