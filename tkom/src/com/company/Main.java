package com.company;
import com.lexer.Token;
import com.lexer.Lexer;
import com.lexer.TokenType;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Lexer lexer = new Lexer("C:\\Users\\Dadej\\Desktop\\tkom_\\tkom\\src\\Program.txt", 1);

        Token token;
        do{
            token = lexer.buildToken();
            System.out.println(token.getType() + " " + token.getContent() +  " (" + token.getX() + "," + token.getY() + ")");
        }while(token.getType() != TokenType.EOF);

       /*String xd = "";
       String compare = "0.";
       xd = xd + '0';
       xd = xd + '.';
       if(xd.equals("0."))
        System.out.println(compare);*/

    }
    }

