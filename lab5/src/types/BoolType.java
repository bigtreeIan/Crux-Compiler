package types;

public class BoolType extends Type {
    
    public BoolType()
    {
        //throw new RuntimeException("implement operators");
    }
    
    @Override
    public String toString()
    {
        return "bool";
    }

    @Override
    public boolean equivalent(Type that)
    {
        if (that == null)
            return false;
        if (!(that instanceof BoolType))
            return false;
        
        return true;
    }
    
    @Override
    public Type and(Type that){
    	if(that instanceof BoolType == false){
    		return super.and(that);
    	}
    	else{
    		return new BoolType();
    	}
    }
    @Override
    public Type or(Type that){
    	if(that instanceof BoolType == false){
    		return super.or(that);
    	}
    	else{
    		return new BoolType();
    	}
    }
    @Override
    public Type not(){
    	/*
    	if(that instanceof BoolType == false){
    		return super.not();
    	}
    	*/
    	return new BoolType();
    }
    
    @Override
    public Type assign(Type that){
    	if(that instanceof BoolType == false){
    		return super.assign(that);
    	}
    	else{
    		return this;
    	}
    }
    
    
}    
