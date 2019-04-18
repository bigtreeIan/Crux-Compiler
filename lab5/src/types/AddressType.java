package types;

public class AddressType extends Type {
    
    private Type base;
    
    public AddressType(Type base)
    {
        //throw new RuntimeException("implement operators");
        this.base = base;
    }

    public Type base()
    {
        return base;
    }

    @Override
    public String toString()
    {
        return "Address(" + base + ")";
    }

    @Override
    public boolean equivalent(Type that) {
        if (that == null)
            return false;
        if (!(that instanceof AddressType))
            return false;
        
        AddressType aType = (AddressType)that;
        return this.base.equivalent(aType.base);
    }
    
    @Override
    public Type assign(Type that){
        if (that.equivalent(base) == false) {
            //System.out.println("address");
    		return super.assign(that);
    	}
    	else{
    		//check the base type not the whole type!!!
    		return base.assign(that);
    	}
    }
    
    @Override
    public Type deref(){
    	//check if dereference a Address Type
    	/*if(!(that instanceof AddressType)){
    		return super.deref();
    	}*/
    	//return new AddressType(base);
    	return base;
    }
    
    @Override
    public Type compare(Type that){
    	if(that instanceof AddressType == false){
    		return super.compare(that);
    	}
    	else{
    		return new AddressType(base);
    	}
    }
    
    //testcase013
    @Override
    public Type index(Type that){
    	if(that.equivalent(new IntType())){
    		//call the index in base type to handle all the rest :)
    		return new AddressType(base.index(that));
    	}
    	else{
    		return super.index(that);
    	}
    }
    
}
