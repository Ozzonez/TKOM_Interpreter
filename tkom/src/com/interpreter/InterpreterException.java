package com.interpreter;

public class InterpreterException extends Exception{
    public InterpreterException(String msg)
    {
        super(msg);
    }

    @Override
    public String toString() {
        return this.getMessage();
    }
}

