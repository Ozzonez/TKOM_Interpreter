package com.company;
import com.lexer.Token;
import com.lexer.Lexer;
import com.lexer.TokenType;
import com.parser.Parser;
import com.parser.ParserException;
import com.parser.TreeNode;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
//        Lexer lexer = new Lexer("C:\\Users\\Dadej\\Desktop\\tkom_\\tkom\\src\\Program.txt", 1);
//
//        Token token;
//        do{
//            token = lexer.buildToken();
//            System.out.println(token.getType() + " " + token.getContent() +  " (" + token.getX() + "," + token.getY() + ")");
//        }while(token.getType() != TokenType.EOF);

       /*String xd = "";
       String compare = "0.";
       xd = xd + '0';
       xd = xd + '.';
       if(xd.equals("0."))
        System.out.println(compare);*/
        File file;
        String program = "DDDDDDDDDDDDDDDDD";
        file = new File("C:\\Users\\Dadej\\Desktop\\tk\\TKOM_Interpreter\\tkom\\src\\Program.txt");

        TreeNode p;
        Parser parser = new Parser(file);
        try {
            p = parser.program();
            if(p.equals(null))
                System.out.println("XD");
        }
        catch(ParserException e){
            System.out.println("XDDD nie dziala");
        }
    }
}

