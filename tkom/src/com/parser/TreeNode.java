package com.parser;

import com.interpreter.INodeVisitor;
import com.interpreter.Interpreter;
import com.interpreter.InterpreterException;

public interface TreeNode {
    public void accept(Interpreter visitor) throws InterpreterException;
}
