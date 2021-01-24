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

    // to musza dodawać wszystkie functionBlocki
    public void addVarContext()
    {
        assert callStack.peek() != null;
        callStack.peek().addVarContext();
    }

    public void deleteVarContext()
    {
        assert callStack.peek() != null;
        callStack.peek().deleteVarContext();
    }

    // sprawdza czy istnieje i nadpisywać jeśli znalazła oraz rzucać błąd jeśli nie znalazła
    public void updateVarInCurrentBlockContext(TreeNode name, TreeNode value) throws InterpreterException {
        assert callStack.peek() != null;
        callStack.peek().updateVarInBlockContext(name, value);
    }

    //dodaj nową zmienną (z wartością lub nullem) do var contextu aktualnego -- widoczna tylko w aktualnej mapie i niższych pochodnych
    public void declareVarInCurrentScope(TreeNode name, TreeNode value) throws InterpreterException {
        assert callStack.peek() != null;
        callStack.peek().declareVarInCurrentScope(name, value);
    }


    //todo getVariable(name)
    //todo makeCall(...)
    //todo deleteCall
    //todo makeVariable(name, type)
    //todo makeBlockContext
    //todo deleteBlockContext(

}
