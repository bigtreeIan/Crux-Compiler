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
    	types.TypeList typeList = new types.TypeList();
    	currenttable = new LinkedHashMap<String, Symbol>();
    	//loop through the pre-defined function array, create symbol for each of them
    	/*for(int i = 0; i < pre_defined_func.length; i++){
    		Symbol pre_defined_symbol = new Symbol(pre_defined_func[i]);
    		//pre_defined_symbol.setType(new FuncType(new TypeList(), new pre_defined_func[i].));
    		currenttable.put(pre_defined_func[i], pre_defined_symbol);
    	}*/
    	//readint
    	Symbol pre_def_ri = new Symbol(pre_defined_func[0]);
    	pre_def_ri.setType(new types.FuncType(typeList, new types.IntType()));
    	currenttable.put(pre_defined_func[0], pre_def_ri);
    	//readfloat
    	Symbol pre_def_rf = new Symbol(pre_defined_func[1]);
    	pre_def_rf.setType(new types.FuncType(typeList, new types.FloatType()));
    	currenttable.put(pre_defined_func[1], pre_def_rf);
    	//printBOOL
    	Symbol pre_def_pb = new Symbol(pre_defined_func[2]);
    	typeList = new types.TypeList();
    	typeList.append(new types.BoolType());
    	pre_def_pb.setType(new types.FuncType(typeList, new types.VoidType()));
    	currenttable.put(pre_defined_func[2], pre_def_pb);
    	
    	//printint
    	Symbol pre_def_pi = new Symbol(pre_defined_func[3]);
    	typeList = new types.TypeList();
    	types.Type inty = new types.IntType();
    	typeList.append(inty);
		//System.out.println("printInt: " + new types.IntType());
    	pre_def_pi.setType(new types.FuncType(typeList, new types.VoidType()));
    	currenttable.put(pre_defined_func[3], pre_def_pi);
    	
    	//printfloat
    	Symbol pre_def_pf = new Symbol(pre_defined_func[4]);
    	typeList = new types.TypeList();
    	typeList.append(new types.FloatType());
    	pre_def_pf.setType(new types.FuncType(typeList, new types.VoidType()));
    	currenttable.put(pre_defined_func[4], pre_def_pf);
    	//println
    	Symbol pre_def_pl = new Symbol(pre_defined_func[5]);
    	typeList = new types.TypeList();
    	pre_def_pl.setType(new types.FuncType(typeList, new types.VoidType()));
    	currenttable.put(pre_defined_func[5], pre_def_pl);
    	
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
