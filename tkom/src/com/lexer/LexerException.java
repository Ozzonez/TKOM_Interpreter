package com.lexer;

public class LexerException extends Exception{
    public LexerException(String msg)
    {
        super(msg);
    }

    @Override
    public String toString() {
        return this.getMessage();
    }
}
