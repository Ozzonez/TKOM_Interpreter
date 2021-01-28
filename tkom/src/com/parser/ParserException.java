package com.parser;

public class ParserException extends Exception{
    public ParserException(String msg)
    {
        super(msg);
    }

    @Override
    public String toString() {
        return this.getMessage();
    }
}
