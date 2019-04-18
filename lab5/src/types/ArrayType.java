package types;

public class ArrayType extends Type {
    
    private Type base;
    private int extent;
    
    public ArrayType(int extent, Type base)
    {
        //throw new RuntimeException("implement operators");
        this.extent = extent;
        this.base = base;
    }
    
    public int extent()
    {
        return extent;
    }
    
    public Type base()
    {
        return base;
    }
    
    @Override
    public String toString()
    {
        return "array[" + extent + "," + base + "]";
    }
    
    @Override
    public boolean equivalent(Type that)
    {
    	try{
        if (that == null)
            return false;
        if (!(that instanceof IntType))
            return false;
        ArrayType aType = (ArrayType)that;
        return this.extent == aType.extent && base.equivalent(aType.base);}
    	//why there is a crush that happens in the given code ??!! why T_T
    	catch(ClassCastException c){
    		return false;
    	}
    }

    @Override
    public Type assign(Type that){
    	/*
    	//if the assign source is not arraytype, return errortype
    	if(that instanceof ArrayType == false){
    		return super.assign(that);
    	}
    	else{
    		return new ArrayType(extent, base);
    	}	*/
    	return base.assign(that);
    }
    
    @Override
    public Type index(Type that){
    	//check if the index is a int, if not return errortype
    	if (that.equivalent(new IntType()) == false)
    	{
    		return super.index(that);
    	}
    	else{
    		return base;
    	}
    }
    
    
    
}
