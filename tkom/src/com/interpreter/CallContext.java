package com.interpreter;

import com.lexer.TokenType;
import com.parser.TreeNode;
import com.parser.TreeNodeSub;
import com.sun.source.tree.Tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class CallContext {
    //HashMap<TokenType, TreeNodeSub.Variable> variables = new HashMap<TokenType, TreeNodeSub.Variable>(); // zmienna - token Name + obiekt typu Variable
    ArrayList<HashMap<String, TreeNode>> localVariablesStack = new ArrayList<>(); // ewentualnie mapa variables może być na szczycie tego stosu
    //todo do arrayLista dodawać Token czy po prostu nazwę variable czyli Token x.getContent()
    //pojedynczy rekord na stosie to mapa variabli w tym scopie
    //czy potrzebne jeszcze pola typu token name, ActivationType typ - loop, if, program, itp

    public void addVarContext()
    {
        HashMap<String, TreeNode> newVarContext = new HashMap<String, TreeNode>();
        localVariablesStack.add(newVarContext);
    }

    public void deleteVarContext()
    {
        localVariablesStack.remove(localVariablesStack.size() - 1);
    }

    // value - num, stringvar, unit | name to Variable - wyciągnąć z niego variable.getName.getcontent -- kiedy aktualizujemy wartość var, zmieniamy wskazanie jego pola value na nowy obiekt num, string lub unit
    public void updateVarInBlockContext(TreeNode name, TreeNode value) throws InterpreterException {
        TreeNode tmp = null; // wskazanie na variable
        for(int i = 1; i <= localVariablesStack.size(); i++) {
            if ((tmp = localVariablesStack.get(localVariablesStack.size() - i).get(((TreeNodeSub.Variable) (name)).getName().getContent())) != null) {
                localVariablesStack.get(localVariablesStack.size() - i).replace(((TreeNodeSub.Variable) (name)).getName().getContent(), value);
                break;
            }
        }
        if(tmp == null) throw new InterpreterException("Variable is not declared in this scope");
    }

    //todo MAP< STRING , NUM ABLO STRING ALBO UNIT > TO JEST KLASA VARIABLE CZYLI DO
    public void declareVarInCurrentScope(TreeNode name, TreeNode value) throws InterpreterException {
        if(value != null)
            localVariablesStack.get(localVariablesStack.size() - 1).put(((TreeNodeSub.Variable)name).getName().getContent(), value);
        else
        localVariablesStack.get(localVariablesStack.size() - 1).put(((TreeNodeSub.Variable)name).getName().getContent(), new TreeNodeSub.Num());
    }

    //TODO SPRAWDZAĆ CZY ZMIENNNA ZAININICJOWANA; ZMIENNA NIEZAINICJOWANA TO TAKA KTÓREJ TREENODE VALUE W HASHMAPIE jest równy NUM.getValue ==null
    public TreeNode getVarValue(TreeNode name) throws InterpreterException {
        TreeNode tmp = null; // wskazanie na wartość variable
        for(int i = 1; i <= localVariablesStack.size(); i++) {
            if ((tmp = localVariablesStack.get(localVariablesStack.size() - i).get(((TreeNodeSub.Variable) (name)).getName().getContent())) != null) {
                if(tmp instanceof TreeNodeSub.Num)
                    if(((TreeNodeSub.Num) tmp).getValue() == null) throw new InterpreterException("Variable not initialized!"); //todo info o linii
                return tmp;
            }
        }

        throw new InterpreterException("Variable is not declared in this scope");
    }

}