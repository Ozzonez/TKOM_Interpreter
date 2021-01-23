package com.parser;

import com.interpreter.INodeVisitor;
import com.interpreter.Interpreter;

public interface TreeNode {
    public void accept(Interpreter visitor);
}
