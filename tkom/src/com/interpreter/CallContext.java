package com.interpreter;
import com.lexer.TokenType;
import com.parser.TreeNode;
import com.parser.TreeNodeSub;
import com.sun.source.tree.Tree;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class CallContext {
    ArrayList<HashMap<String, TreeNode>> localVariablesStack = new ArrayList<>();


    public void addVarContext()
    {
        HashMap<String, TreeNode> newVarContext = new HashMap<String, TreeNode>();
        localVariablesStack.add(newVarContext);
    }

    public void deleteVarContext()
    {
        if(localVariablesStack.size() != 0)
            localVariablesStack.remove(localVariablesStack.size() - 1);
    }

    public void updateVarInBlockContext(TreeNode name, TreeNode value) throws InterpreterException {
        TreeNode tmp = null;
        for(int i = 1; i <= localVariablesStack.size(); i++) {
            if ((tmp = localVariablesStack.get(localVariablesStack.size() - i).get(((TreeNodeSub.Variable) (name)).getName().getContent())) != null) {
                localVariablesStack.get(localVariablesStack.size() - i).replace(((TreeNodeSub.Variable) (name)).getName().getContent(), value);
                break;
            }
        }
        if(tmp == null) throw new InterpreterException("Variable " + ((TreeNodeSub.Variable)name).getName().getContent() + " is not declared in this scope");
    }

    public void declareVarInCurrentScope(TreeNode name, TreeNode value) throws InterpreterException {
        if(value != null) {
            if(localVariablesStack.get(localVariablesStack.size() - 1).get(((TreeNodeSub.Variable) (name)).getName().getContent()) != null) throw new InterpreterException("Variable already declared in this scope" + ((TreeNodeSub.Variable)name).getName().getLine());
            localVariablesStack.get(localVariablesStack.size() - 1).put(((TreeNodeSub.Variable) name).getName().getContent(), value);
        }
        else
        localVariablesStack.get(localVariablesStack.size() - 1).put(((TreeNodeSub.Variable)name).getName().getContent(), new TreeNodeSub.Num());
    }

    public TreeNode getVarValue(TreeNode name) throws InterpreterException {
        TreeNode tmp = null;
        for(int i = 1; i <= localVariablesStack.size(); i++) {
            if ((tmp = localVariablesStack.get(localVariablesStack.size() - i).get(((TreeNodeSub.Variable) (name)).getName().getContent())) != null) {
                if(tmp instanceof TreeNodeSub.Num)
                    if(((TreeNodeSub.Num) tmp).getValue() == null) throw new InterpreterException("Variable not initialized!" + ((TreeNodeSub.Variable)name).getName().getLine());
                return tmp;
            }
        }
        throw new InterpreterException("Variable " + ((TreeNodeSub.Variable)name).getName().getContent() + " is not declared in this scope");
    }
}