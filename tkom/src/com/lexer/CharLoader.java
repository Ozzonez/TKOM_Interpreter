package com.lexer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CharLoader {
    Character buffer;
    File text;
    Scanner scanner;

    public CharLoader(String file) throws FileNotFoundException {
        text = new File(file);
        scanner = new Scanner(text);
        scanner.useDelimiter("");
    }

    public CharLoader(String message, int i)
    {
        scanner = new Scanner(message);
        scanner.useDelimiter("");
    }


    public char getNextSymbol(){
        if(scanner.hasNext()){
            buffer = scanner.next().charAt(0);
            return buffer;
        }else{
            return '\u001a';
        }

    }
}
