package com.interpreter;
import com.lexer.TokenType;
import com.parser.TreeNode;
import com.parser.TreeNodeSub;
import com.sun.source.tree.Tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class Environment {
    //HashMap<String, TreeNodeSub.Variable> globals = new HashMap<String, TreeNodeSub.Variable>(); // mapa zmiennych globalnych - potrzebna?
    ArrayDeque<CallContext> callStack = new ArrayDeque<>(); // stos call contextow
    ArrayList<TreeNode> funcDefs;
    Object lastResult;
    Object lastResultVar;
    ArrayList<TreeNode> parameters;//tablica obiektów typu Variable - po przypisaniu parametersValue gotowa do dodania jako variables do current var context
    ArrayList<TreeNode> parametersValues; // tablica obiektów z wartościami poszczególnych parameters

    public ArrayList<TreeNode> getParametersValues() {
        return parametersValues;
    }

    public void setParametersValues(ArrayList<TreeNode> parametersValues) {
        this.parametersValues = parametersValues;
    }

    public ArrayList<TreeNode> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<TreeNode> parameters) {
        this.parameters = parameters;
    }

    public void setLastResult(TreeNode lastResult) { //todo tutaj OBJECT
        this.lastResult = lastResult;
    }

    public Object getLastResult() {
        return lastResult;
    }

    public void setLastResultVar(TreeNode lastResultVar) {
        this.lastResultVar = lastResultVar;
    }

    public Object getLastResultVar() {
        return lastResultVar;
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

    //zwraca unit, stringvar, albo num
    public TreeNode getVarValue(TreeNode name) throws InterpreterException {
        return callStack.peek().getVarValue(name);
    }


    //todo getVariable(name)
    //todo makeCall(...)
    //todo deleteCall
    //todo makeVariable(name, type)
    //todo makeBlockContext
    //todo deleteBlockContext(

}
