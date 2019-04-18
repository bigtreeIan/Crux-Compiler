package types;
import java.util.List;

public class FuncType extends Type {
   
   private TypeList args;
   private Type ret;
   
   public FuncType(TypeList args, Type returnType)
   {
      //throw new RuntimeException("implement operators");
      this.args = args;
      this.ret = returnType;
   }
   
   public Type returnType()
   {
      return ret;
   }
   
   public TypeList arguments()
   {
      return args;
   }
   
   @Override
   public String toString()
   {
      return "func(" + args + "):" + ret;
   }

   @Override
   public boolean equivalent(Type that)
   {
      if (that == null)
         return false;
      if (!(that instanceof FuncType))
         return false;
      
      FuncType aType = (FuncType)that;
      return this.ret.equivalent(aType.ret) && this.args.equivalent(aType.args);
   }
   
   //supply the call operation to override the Type call operation
   @Override
   
   public Type call(Type that){
	   /*test
       System.out.println("test");
	   TypeList test = new TypeList();
	   Type inttype = new IntType();
	   test.append(inttype)
	   end test*/
	   //track argument typelist
	   if(that instanceof TypeList == false){
	       //System.out.println("functype1");
		   return super.call(that);
	   }

	   else{
	       //System.out.println("functype2");
		   if(((TypeList)that).equivalent(this.args) == false){
		       //System.out.println("functype2");
			   return super.call(that);
		   }
		   else{
			   return ret;
		   }
	   }
   }
   
   
   
   
}
