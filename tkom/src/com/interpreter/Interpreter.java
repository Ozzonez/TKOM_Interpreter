package com.interpreter;

import com.lexer.Token;
import com.lexer.TokenType;
import com.parser.TreeNode;
import com.parser.TreeNodeSub;

public class Interpreter{
    Environment env;

    public void getFunc(String name) throws InterpreterException {
        TreeNode temporaryFunctionDef = new TreeNodeSub.Variable(TokenNode );
        //todo implement searching
        if(not found) throw new InterpreterException("Function not declared!");
        //return temporaryFunctionDef; //todo TUTAJ JAKIŚ TEGES SZMEGES
    }

    public void visit(TreeNode x) // przeciazony visit - usunac na koniec
    {

    }

    public void visit(TreeNodeSub.Program program)
    {
        env = new Environment(program.getFunctions());
        //jakis visit co co do maina przesunie nas itepe i tede
    }

    public void visit(TreeNodeSub.FunctionDef fd) throws InterpreterException {
        fd.getFunctionBlock().accept(this);
    }

    public void visit(TreeNodeSub.FunctionCall fd) throws InterpreterException// przekazac argumenty z funcCalla do zmiennych lokalnych w nowym callstacku
    {
        env.makeBlockContext();
        env.addVarContext(); // to nie moze byc tu
        ((TreeNodeSub.FunctionDef)(getFunc(fd.getName().getContent()))).getParameters();
        for(TreeNode i : ((TreeNodeSub.Parameters)((TreeNodeSub.FunctionDef)(getFunc(fd.getName().getContent()))).getParameters()).getParameters()){
            // mapa argumentów z parametrami
        }

        getFunc(fd.getName().getContent()).accept(this);
        env.deleteBlockContext();
    }

    public void visit(TreeNodeSub.FunctionBlock fb) throws InterpreterException {
        env.addVarContext();

        for(TreeNode i : fb.getStatements()){
            if(i instanceof TreeNodeSub.ReturnStatement)
            {
                //cos tu jeszcze
                i.accept(this);
            }
            i.accept(this);
        }

        env.deleteVarContext();
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
        if(!(env.getLastResult() instanceof TreeNodeSub.Variable)) throw new InterpreterException("Not a variable");
        TreeNodeSub.Variable var = (TreeNodeSub.Variable) env.getLastResult(); // zapisać zmienno

        ass.getValue().accept(this);
        if(!((env.getLastResult() instanceof TreeNodeSub.Unit) &&  (env.getLastResult() instanceof TreeNodeSub.Num) && (env.getLastResult() instanceof TreeNodeSub.StringVar))) throw new InterpreterException("błąd"); //todo zmien

        env.updateVarInCurrentBlockContext(var, (TreeNode)env.getLastResult());
    }

    public void visit(TreeNodeSub.VarDeclaration vd) throws InterpreterException {
        vd.getName().accept(this); // tu variable
        if(!(env.getLastResult() instanceof TreeNodeSub.Variable)) throw new InterpreterException("Not a variable");
        TreeNodeSub.Variable var = (TreeNodeSub.Variable) env.getLastResult();

        vd.getValue().accept(this); //todo TRZEBA ZROBIĆ TAK, ŻE JAK NIE MA ASSIGNMENTU TO NULL ZAPISUJE DO LASTRESULT - NULL oznacza deklarację bez zainicjowania
        if(!((env.getLastResult() instanceof TreeNodeSub.Unit) &&  (env.getLastResult() instanceof TreeNodeSub.Num) && (env.getLastResult() instanceof TreeNodeSub.StringVar) && env.getLastResult() == null)) throw new InterpreterException("błąd");

        env.declareVarInCurrentScope(var, (TreeNode)env.getLastResult());
    }

    public void visit(TreeNodeSub.PrintStatement pt) throws InterpreterException {
        pt.getContent().accept(this);
        //todo sprawdzać co jest w lastResult i rzucać błąd jeśli źle
        System.out.println(env.getLastResult());
    }

    public void visit(TreeNodeSub.UnitBasicType ubt) throws InterpreterException {
        //todo zrobic
    }

    public void visit(TreeNodeSub.UnitComplexType uct) throws InterpreterException {
        //todo zrobić
    }

    public void visit(TreeNodeSub.BinOperator bo) throws InterpreterException {
        bo.getLeftExp().accept(this);
        TreeNode tmpLeft = (TreeNode)env.getLastResult();
        //todo spradzić czy tmpLeft jest Num coś tam String -- tu można sprawdzić mnożenie i dodawanie unitów
        bo.getRightExp().accept(this);
        TreeNode tmpRight = (TreeNode)env.getLastResult();
        //todo też sprawdzić
        Token operator = bo.getOperator();

        if(tmpLeft instanceof TreeNodeSub.Num && tmpRight instanceof TreeNodeSub.Num)
        {
            if(operator.getType() == TokenType.MULTIPLICATIVE_OP) {
                double result = ((TreeNodeSub.Num)tmpLeft).getValue().getContent() * ((TreeNodeSub.Num)tmpRight).getValue().getContent();
                env.setLastResult(new TreeNodeSub.Num(new Token(TokenType.NUMBER, result, 0, 0)));
            }
        }
    }

    public void visit(TreeNodeSub.BinaryConditionOperator bco) throws  InterpreterException {
        bco.getLeftExp().accept(this);
        if(!((env.getLastResult() instanceof TreeNodeSub.Unit) && (env.getLastResult() instanceof TreeNodeSub.Num) && (env.getLastResult() instanceof TreeNodeSub.StringVar))) throw new InterpreterException("błąd");

    }

}
