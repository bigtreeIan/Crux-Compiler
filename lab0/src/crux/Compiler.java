package crux;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import crux.Token.Kind;

public class Compiler {
    public static String studentName = "Yihan Xu";
    public static String studentID = "47011405";
    public static String uciNetID = "yihanx2";
	
	public static void main(String[] args)
	{
        String sourceFile = args[0];
        Scanner s = null;

        try {
            s = new Scanner(new FileReader(sourceFile));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error accessing the source file: \"" + sourceFile + "\"");
            System.exit(-2);
        }

        Token t = s.next();
        
        while (t.kind != Kind.EOF) {
        	if(t.kind == Kind.COMMENT){
               	//System.out.println("1: "+t.kind);
        		t = s.next();
        	}
        	else{
               	//System.out.println("1: "+t.kind);
        		System.out.println(t);
        		t = s.next();
        	}
        }
        
       	System.out.println(t);
    }
}


