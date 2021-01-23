package com.interpreter;

import com.parser.TreeNode;
import com.parser.TreeNodeSub;

public class Interpreter{
    Environment env;

    public void getFunc(String name)
    {
        TreeNode temporaryFunctionDef = new TreeNodeSub.Variable(TokenNode );
        //todo implement searching
        if(not found)
            throw Exception jakis
        return temporaryFunctionDef;
    }

    public void visit(TreeNode x) // przeciazony visit - usunac na koniec
    {

    }

    public void visit(TreeNodeSub.Program program)
    {
        env = new Environment(program.getFunctions());
        //jakis visit co co do maina przesunie nas itepe i tede
    }

    public void visit(TreeNodeSub.FunctionDef fd)
    {
        fd.getFunctionBlock().accept(this);
    }

    public void visit(TreeNodeSub.FunctionCall fd)// przekazac argumenty z funcCalla do zmiennych lokalnych w nowym callstacku
    {
        env.makeBlockContext();
        env.addVarContext();
        ((TreeNodeSub.FunctionDef)(getFunc(fd.getName().getContent()))).getParameters();
        for(TreeNode i : ((TreeNodeSub.Parameters)((TreeNodeSub.FunctionDef)(getFunc(fd.getName().getContent()))).getParameters()).getParameters()){
            // mapa argumentów z parametrami
        }

        getFunc(fd.getName().getContent()).accept(this);
        env.deleteBlockContext();
    }

    public void visit(TreeNodeSub.FunctionBlock fb)
    {
        //tutaj jakoś dodać nową mapę funkcji do aktualnego callstacka - to jest w functionCallu
        for(TreeNode i : fb.getStatements()){
            if(i instanceof TreeNodeSub.ReturnStatement)
            {
                //cos tu jeszcze
                i.accept(this);
            }
            i.accept(this);
        }
    }

    public void visit(TreeNodeSub.ReturnStatement rs)
    {
        rs.getReturned().accept(this);
    }

    public void visit(TreeNodeSub.IfStatement ifs) throws InterpreterException {
        ifs.getCondition().accept(this);
        if(!(env.getLastResult() instanceof Boolean)) throw new InterpreterException("G+CHUJ"); //todo zmien

        if((Boolean) (env.getLastResult()))
            ifs.getInstructionBlockIfTrue().accept(this);
        else
            ifs.getInstructionBlockIfFalse().accept(this);
    }

    public void visit(TreeNodeSub.WhileStatement ws) throws InterpreterException {
        ws.getCondition().accept(this);
        if(!(env.getLastResult() instanceof Boolean)) throw new InterpreterException("błąd"); //todo zmien

        while((Boolean) (env.getLastResult()))
        {
            ws.getWhileBody().accept(this);

            ws.getCondition().accept(this);
            if(!(env.getLastResult() instanceof Boolean)) throw new InterpreterException("błąd"); //todo zmien
        }
    }

    public void visit(TreeNodeSub.AssignStatement ass) throws InterpreterException {
        ass.getName().accept(this); // to bedzie Variable
        ass.getValue().accept(this);
        if(!((env.getLastResult() instanceof TreeNodeSub.Unit) &&  (env.getLastResult() instanceof TreeNodeSub.Num) && (env.getLastResult() instanceof TreeNodeSub.StringVar))) throw new InterpreterException("błąd"); //todo zmien

        env.funcDefs
    }

}
