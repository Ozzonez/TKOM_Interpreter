package com.company;
import com.interpreter.Interpreter;
import com.interpreter.InterpreterException;
import com.lexer.LexerException;
import com.lexer.Token;
import com.lexer.Lexer;
import com.lexer.TokenType;
import com.parser.Parser;
import com.parser.ParserException;
import com.parser.TreeNode;
import com.parser.TreeNodeSub;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, LexerException {
        File file;
        file = new File("C:\\Users\\Dadej\\Desktop\\tk\\TKOM_Interpreter\\tkom\\src\\Program.txt");

        TreeNode p;
        Parser parser = new Parser(file);
        try {
            p = parser.program();
            Interpreter interpreter = new Interpreter(p);
            TreeNode wynik = (TreeNode)interpreter.run();
            System.out.println("--------------");
            System.out.println((wynik));
        }
        catch(ParserException | LexerException | InterpreterException e){
            System.out.println(e.getMessage());
        }
    }
}

