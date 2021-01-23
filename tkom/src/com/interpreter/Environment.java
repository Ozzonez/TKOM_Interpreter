package com.interpreter;
import com.lexer.TokenType;
import com.parser.TreeNode;
import com.parser.TreeNodeSub;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class Environment {
    //HashMap<String, TreeNodeSub.Variable> globals = new HashMap<String, TreeNodeSub.Variable>(); // mapa zmiennych globalnych - potrzebna?
    ArrayDeque<CallContext> callStack = new ArrayDeque<>(); // stos call contextow
    ArrayList<TreeNode> funcDefs;
    Object lastResult;

    public void setLastResult(TreeNode lastResult) {
        this.lastResult = lastResult;
    }

    public Object getLastResult() {
        return lastResult;
    }

    public ArrayList<TreeNode> getFuncDefs() {
        return funcDefs;
    }

    public Environment(ArrayList<TreeNode> funcDefs)
    {
        this.funcDefs = funcDefs;
    }

    public void makeBlockContext()
    {
        CallContext funcCallContext = new CallContext();
        callStack.push(funcCallContext);
    }
    public void deleteBlockContext()
    {
        callStack.pop();
    }
    public void addVarContext()
    {
        assert callStack.peek() != null;
        callStack.peek().addLocalVarContext();
    }



    //todo getVariable(name)
    //todo makeCall(...)
    //todo deleteCall
    //todo makeVariable(name, type)
    //todo makeBlockContext
    //todo deleteBlockContext(

}
