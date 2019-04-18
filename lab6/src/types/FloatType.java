package types;

public class FloatType extends Type {
	
	public FloatType()
	{
	}
	
	@Override
	public String toString()
	{
		return "float";
	}
	
	@Override
	public Type add(Type that)
	{
		if (!(that instanceof FloatType))
			return super.add(that);
		return new FloatType();
	}
	
	@Override
	public Type sub(Type that)
	{
		if (!(that instanceof FloatType))
			return super.sub(that);
		return new FloatType();
	}
	
	@Override
	public Type mul(Type that)
	{
		if (!(that instanceof FloatType))
			return super.mul(that);
		return new FloatType();
	}
	
	@Override
	public Type div(Type that)
	{
		if (!(that instanceof FloatType))
			return super.div(that);
		return new FloatType();
	}
		
	@Override
	public Type compare(Type that)
	{
		if (!(that instanceof FloatType))
			return super.compare(that);
		return new BoolType();
	}
	
	@Override
	public boolean equivalent(Type that)
	{
		if (that == null)
			return false;
		if (!(that instanceof FloatType))
			return false;
		return true;
	}
}
