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
    ArrayList<HashMap<String, TreeNodeSub.Variable>> localVariablesStack = new ArrayList<>(); // ewentualnie mapa variables może być na szczycie tego stosu
    //todo do arrayLista dodawać Token czy po prostu nazwę variable czyli Token x.getContent()
    //pojedynczy rekord na stosie to mapa variabli w tym scopie
    //czy potrzebne jeszcze pola typu token name, ActivationType typ - loop, if, program, itp

    //todo funckje: getVar, addVar - dodaje do koncowej mapy arraylista nowy element

    public void addVarContext()
    {
        HashMap<String, TreeNodeSub.Variable> newVarContext = new HashMap<String, TreeNodeSub.Variable>();
        localVariablesStack.add(newVarContext);
    }

    public void deleteVarContext()
    {
        localVariablesStack.remove(localVariablesStack.size() - 1);
    }

    //todo oooooooooooo co chodzi
    public void addVariable(TreeNode var)
    {
        TreeNodeSub.Variable x = (TreeNodeSub.Variable)var;
        localVariablesStack.get(localVariablesStack.size() - 1).put(x.getName().getContent(), (TreeNodeSub.Variable)var);
    }

    public void updateVarInBlockContext(TreeNode name, TreeNode value) throws InterpreterException {
        TreeNodeSub.Variable tmp = null; // wskazanie na variable
        for(int i = 1; i <= localVariablesStack.size(); i++) {
            if ((tmp = localVariablesStack.get(localVariablesStack.size() - i).get(((TreeNodeSub.Variable) (name)).getName().getContent())) != null) {
                break;
            }
        }
        if(tmp == null) throw new InterpreterException("Variable is not declared in this scope");

        tmp.setValue(value);
    }

    //dodać ją po prostu do najnowszej mapy - ostatnia na liście
    public void declareVarInCurrentScope(TreeNode name, TreeNode value) throws InterpreterException {
        localVariablesStack.get(localVariablesStack.size() - 1).put(((TreeNodeSub.Variable)name).getName().getContent(), (TreeNodeSub.Variable)value);
    }

}