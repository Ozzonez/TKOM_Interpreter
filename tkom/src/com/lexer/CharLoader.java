package com.lexer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CharLoader {
    Character buffer;
    File text;
    Scanner scanner;

    public CharLoader(File file) throws FileNotFoundException {
        scanner = new Scanner(file);
        scanner.useDelimiter("");
    }

    public CharLoader(String message)
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
