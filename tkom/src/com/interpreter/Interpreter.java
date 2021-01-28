package com.interpreter;

import com.lexer.Token;
import com.lexer.TokenType;
import com.parser.TreeNode;
import com.parser.TreeNodeSub;

import java.util.ArrayList;

public class Interpreter{
    Environment env;
    TreeNode program;

    public Interpreter(TreeNode program)
    {
        this.program = program;
    }

    public Object run() throws InterpreterException {
        program.accept(this);
        return env.getLastResult();
    }

    public TreeNode getFunc(String name) throws InterpreterException {
        for(TreeNode i : env.getFuncDefs())
        {
            if(((TreeNodeSub.FunctionDef) i).getName().getContent().equals(name))
                return i;
        }
        throw new InterpreterException("Function " + name + " not declared!");
    }

    public void visit(TreeNodeSub.Program program) throws InterpreterException {
        env = new Environment(program.getFunctions());
        TreeNode main = getFunc("main");
        main.accept(this);
    }

    public void visit(TreeNodeSub.FunctionDef fd) throws InterpreterException {
        env.makeBlockContext();

        if(((TreeNodeSub.FunctionDef)(getFunc(fd.getName().getContent()))).getParameters() != null) {
            env.setParameters(((TreeNodeSub.Parameters) ((TreeNodeSub.FunctionDef) (getFunc(fd.getName().getContent()))).getParameters()).getParameters());

            if (env.getParameters().size() != env.getParametersValues().size())
                throw new InterpreterException("Expected: " + env.getParameters().size() + "arguments but got: " + env.getParametersValues().size());
        }
        env.addVarContext();

        int j = 0;
        ArrayList<TreeNode> parametersValues = env.getParametersValues();
        if(env.getParameters() != null)
            for(TreeNode i : env.getParameters())
            {
                env.declareVarInCurrentScope(i, parametersValues.get(j));
                j++;
            }

        fd.getFunctionBlock().accept(this);
        env.setReturnMet(false);
        env.deleteBlockContext();
    }

    public void visit(TreeNodeSub.FunctionCall fd) throws InterpreterException
    {
        ArrayList<TreeNode> tmp = new ArrayList<>();
        for(TreeNode i : fd.getArguments())
        {
            if(i instanceof TreeNodeSub.Variable || i instanceof TreeNodeSub.FunctionCall)
            {
                i.accept(this);
                tmp.add((TreeNode)env.getLastResult());
            }
            else
                tmp.add(i);
        }
        env.setParametersValues(tmp);
        getFunc(fd.getName().getContent()).accept(this);
    }

    public void visit(TreeNodeSub.FunctionBlock fb) throws InterpreterException {
        env.addVarContext();

        for(TreeNode i : fb.getStatements()){
            if(i instanceof TreeNodeSub.ReturnStatement)
            {
                i.accept(this);
                env.setReturnMet(true);
                break;
            }
            i.accept(this);
            if(env.getReturnMet())
                break;
        }
        env.deleteVarContext();
    }

    public void visit(TreeNodeSub.ReturnStatement rs) throws InterpreterException {
        rs.getReturned().accept(this);
    }

    public void visit(TreeNodeSub.IfStatement ifs) throws InterpreterException {
        ifs.getCondition().accept(this);
        if(!(env.getLastResult() instanceof Boolean)) throw new InterpreterException("Not a boolean if condition");

        if((Boolean) (env.getLastResult()))
            ifs.getInstructionBlockIfTrue().accept(this);
        else if(ifs.getInstructionBlockIfFalse() != null)
            ifs.getInstructionBlockIfFalse().accept(this);
    }

    public void visit(TreeNodeSub.WhileStatement ws) throws InterpreterException {
        ws.getCondition().accept(this);
        if(!(env.getLastResult() instanceof Boolean)) throw new InterpreterException("Not a boolean while condition");

        while((Boolean) (env.getLastResult()))
        {
            ws.getWhileBody().accept(this);
            if(env.getReturnMet())
                break;
            ws.getCondition().accept(this);
            if(!(env.getLastResult() instanceof Boolean)) throw new InterpreterException("Not a boolean while condition");
        }
    }

    public void visit(TreeNodeSub.AssignStatement ass) throws InterpreterException {
        TreeNode var = ass.getName();
        ass.getValue().accept(this);
        if(!((env.getLastResult() instanceof TreeNodeSub.Unit) || (env.getLastResult() instanceof TreeNodeSub.Num) || (env.getLastResult() instanceof TreeNodeSub.StringVar))) throw new InterpreterException("131"); //todo zmien

        env.updateVarInCurrentBlockContext(var, (TreeNode)env.getLastResult());
    }

    public void visit(TreeNodeSub.VarDeclaration vd) throws InterpreterException {
        TreeNode var = vd.getName();
        if(vd.getValue() != null)
        vd.getValue().accept(this);

        if(!((env.getLastResult() instanceof TreeNodeSub.Unit) || (env.getLastResult() instanceof TreeNodeSub.Num) ||
                (env.getLastResult() instanceof TreeNodeSub.StringVar) || env.getLastResult() == null))
            throw new InterpreterException("Wrong variable declaration");

        if(vd.getValue() != null)
            env.declareVarInCurrentScope(var, (TreeNode)env.getLastResult());
        else env.declareVarInCurrentScope(var, null);
    }

    public void visit(TreeNodeSub.PrintStatement pt) throws InterpreterException {
        pt.getContent().accept(this);
        if(!((env.getLastResult() instanceof TreeNodeSub.Unit) || (env.getLastResult() instanceof TreeNodeSub.Num) ||
                (env.getLastResult() instanceof TreeNodeSub.StringVar) || env.getLastResult() == null))
            throw new InterpreterException("Wrong print statement use");
        System.out.println(env.getLastResult());
    }

    public void visit(TreeNodeSub.UnitBasicType ubt) throws InterpreterException {
        env.addNewBasicUnit(ubt);
    }

    public void visit(TreeNodeSub.UnitComplexType uct) throws InterpreterException {
        env.addNewComplexType(uct);
    }

    public void visit(TreeNodeSub.BinOperator bo) throws InterpreterException {
        bo.getLeftExp().accept(this);
        TreeNode tmpLeft = (TreeNode)env.getLastResult();
        bo.getRightExp().accept(this);
        TreeNode tmpRight = (TreeNode)env.getLastResult();
        Token operator = bo.getOperator();

        if(tmpLeft instanceof TreeNodeSub.Num && tmpRight instanceof TreeNodeSub.Num)
        {
            if(operator.getType() == TokenType.MULTIPLICATIVE_OP) {
                double result = ((TreeNodeSub.Num)tmpLeft).getValue().getNumcontent() * ((TreeNodeSub.Num)tmpRight).getValue().getNumcontent();
                env.setLastResult(new TreeNodeSub.Num(new Token(TokenType.NUMBER, result, 0, 0)));
            }
            else  if(operator.getType() == TokenType.DIVISION_OP) {
                double result = ((TreeNodeSub.Num)tmpLeft).getValue().getNumcontent() / ((TreeNodeSub.Num)tmpRight).getValue().getNumcontent();
                env.setLastResult(new TreeNodeSub.Num(new Token(TokenType.NUMBER, result, 0, 0)));
            }
            else  if(operator.getContent().equals("-")) {
                double result = ((TreeNodeSub.Num)tmpLeft).getValue().getNumcontent() - ((TreeNodeSub.Num)tmpRight).getValue().getNumcontent();
                env.setLastResult(new TreeNodeSub.Num(new Token(TokenType.NUMBER, result, 0, 0)));
            }
            else  if(operator.getContent().equals("+")) {
                double result = ((TreeNodeSub.Num)tmpLeft).getValue().getNumcontent() + ((TreeNodeSub.Num)tmpRight).getValue().getNumcontent();
                env.setLastResult(new TreeNodeSub.Num(new Token(TokenType.NUMBER, result, 0, 0)));
            }
        }
        else if(tmpLeft instanceof TreeNodeSub.StringVar && tmpRight instanceof TreeNodeSub.StringVar)
        {
            if(operator.getContent().equals("+")) {
            String result = ((TreeNodeSub.StringVar)tmpLeft).getValue().getContent() + ((TreeNodeSub.StringVar)tmpRight).getValue().getContent();
            env.setLastResult(new TreeNodeSub.StringVar(new Token(TokenType.QUOTE, result, 0, 0)));
            }
            else throw new InterpreterException("Forbidden comparison between strings at" + operator.getLine());
        }
        else if(tmpLeft instanceof TreeNodeSub.Unit && tmpRight instanceof TreeNodeSub.Unit)
        {
            if(operator.getContent().equals("*")) {
                env.setLastResult(((TreeNodeSub.Unit) tmpLeft).multiply(tmpRight));
                env.checkForKnownFormulas((TreeNode) env.getLastResult());
            }
            else if(operator.getContent().equals("/")) {
                env.setLastResult(((TreeNodeSub.Unit) tmpLeft).divide(tmpRight));
                env.checkForKnownFormulas((TreeNode) env.getLastResult());
            }
            else if(operator.getContent().equals("+")) {
                if(!((TreeNodeSub.Unit) tmpLeft).getUnitType().getContent().equals(((TreeNodeSub.Unit) tmpRight).getUnitType().getContent()))
                    throw new InterpreterException("Cannot add units of two different types at" + operator.getLine());
                env.setLastResult(((TreeNodeSub.Unit) tmpLeft).add(tmpRight));
                env.checkForKnownFormulas((TreeNode) env.getLastResult());
            }
            else if(operator.getContent().equals("-")) {
                if(!((TreeNodeSub.Unit) tmpLeft).getUnitType().getContent().equals(((TreeNodeSub.Unit) tmpRight).getUnitType().getContent()))
                    throw new InterpreterException("Cannot subtract units of two different types at" + operator.getLine());
                env.setLastResult(((TreeNodeSub.Unit) tmpLeft).subtract(tmpRight));
                env.checkForKnownFormulas((TreeNode) env.getLastResult());
            }
        }
        else if(tmpLeft instanceof TreeNodeSub.Unit && tmpRight instanceof TreeNodeSub.Num)
        {
            if(operator.getContent().equals("*")) {
                env.setLastResult(((TreeNodeSub.Unit) tmpLeft).multiplyByNum(tmpRight));
                env.checkForKnownFormulas((TreeNode) env.getLastResult());
            }
            else if(operator.getContent().equals("/")) {
                env.setLastResult(((TreeNodeSub.Unit) tmpLeft).divideByNum(tmpRight));
                env.checkForKnownFormulas((TreeNode) env.getLastResult());
            }
            else throw new InterpreterException("Addition and subtraction of unit type and number type forbidden" + operator.getLine());
        }
        else if(tmpLeft instanceof TreeNodeSub.Num && tmpRight instanceof TreeNodeSub.Unit)
        {
            if(operator.getContent().equals("*")) {
                env.setLastResult(((TreeNodeSub.Unit) tmpRight).multiplyByNum(tmpLeft));
                env.checkForKnownFormulas((TreeNode) env.getLastResult());
            }
            else if(operator.getContent().equals("/")) {
                env.setLastResult(((TreeNodeSub.Unit) tmpRight).divideByNum(tmpLeft));
                env.checkForKnownFormulas((TreeNode) env.getLastResult());
            }
            else throw new InterpreterException("Addition and subtraction of unit type and number type forbidden" + operator.getLine());
        }
        else throw new InterpreterException("Forbidden operation at" + operator.getLine());
    }

    public void visit(TreeNodeSub.BinaryConditionOperator bco) throws  InterpreterException {
        bco.getLeftExp().accept(this);
        Object leftExp = env.getLastResult();
        bco.getRightExp().accept(this);
        Object rightExp = env.getLastResult();

        if(rightExp instanceof Boolean && leftExp instanceof Boolean)
        {
            if(bco.getOperator().getType() == TokenType.AND_OP)
                env.setLastResult((Boolean)leftExp && (Boolean) rightExp);
            else if(bco.getOperator().getType() == TokenType.OR_OP)
                env.setLastResult((Boolean)leftExp || (Boolean) rightExp);
            else if(bco.getOperator().getType() == TokenType.EQUAL_OP)
                env.setLastResult((Boolean)leftExp == (Boolean) rightExp);
            else if(bco.getOperator().getType() == TokenType.NOT_EQUAL_OP)
                env.setLastResult((Boolean)leftExp != (Boolean) rightExp);
            else throw new InterpreterException("Forbidden logical operation at" + bco.getOperator().getLine());
        }
        else if(leftExp instanceof TreeNodeSub.Num && rightExp instanceof TreeNodeSub.Num)
        {
            TreeNodeSub.Num left = ((TreeNodeSub.Num) leftExp);
            double right = ((TreeNodeSub.Num) rightExp).getValue().getNumcontent();
            if(bco.getOperator().getType() == TokenType.EQUAL_OP)
                env.setLastResult(left.equals(right));
            else if(bco.getOperator().getType() == TokenType.NOT_EQUAL_OP)
                env.setLastResult(left.notEquals(right));
            else if(bco.getOperator().getType() == TokenType.GREATER__OP)
                env.setLastResult(left.greater(right));
            else if(bco.getOperator().getType() == TokenType.GREATER_EQUAL_OP)
                env.setLastResult(left.greaterEqual(right));
            else if(bco.getOperator().getType() == TokenType.SMALLER__OP)
                env.setLastResult(left.smaller(right));
            else if(bco.getOperator().getType() == TokenType.SMALLER_EQUAL_OP)
                env.setLastResult(left.smallerEqual(right));
            else throw new InterpreterException("Forbidden logical operation at" + bco.getOperator().getLine());
        }
        else if(leftExp instanceof TreeNodeSub.StringVar && rightExp instanceof TreeNodeSub.StringVar)
        {
            TreeNodeSub.StringVar left = ((TreeNodeSub.StringVar) leftExp);
            String right = ((TreeNodeSub.StringVar) rightExp).getValue().getContent();
            if(bco.getOperator().getType() == TokenType.EQUAL_OP)
                env.setLastResult(left.equals(right));
            else if(bco.getOperator().getType() == TokenType.NOT_EQUAL_OP)
                env.setLastResult(left.notEquals(right));
            else throw new InterpreterException(bco.getOperator().getContent() + " Cannot be applied to strings at" +  bco.getOperator().getLine());
        }
        else if(leftExp instanceof TreeNodeSub.Unit && rightExp instanceof TreeNodeSub.Unit)
        {
            if(!((TreeNodeSub.Unit) leftExp).getUnitType().getContent().equals(((TreeNodeSub.Unit) rightExp).getUnitType().getContent()))
                throw new InterpreterException("Cannot compare units of two different types at" + bco.getOperator().getLine());

            TreeNodeSub.Unit left = (TreeNodeSub.Unit) leftExp;
            TreeNodeSub.Unit right = (TreeNodeSub.Unit) rightExp;

            if(bco.getOperator().getType() == TokenType.EQUAL_OP)
                env.setLastResult(left.equals(right));
            else if(bco.getOperator().getType() == TokenType.NOT_EQUAL_OP)
                env.setLastResult(left.notEquals(right));
            else if(bco.getOperator().getType() == TokenType.GREATER__OP)
                env.setLastResult(left.greater(right));
            else if(bco.getOperator().getType() == TokenType.GREATER_EQUAL_OP)
                env.setLastResult(left.greaterEqual(right));
            else if(bco.getOperator().getType() == TokenType.SMALLER__OP)
                env.setLastResult(left.smaller(right));
            else if(bco.getOperator().getType() == TokenType.SMALLER_EQUAL_OP)
                env.setLastResult(left.smallerEqual(right));
            else throw new InterpreterException("Forbidden logical operation at" + bco.getOperator().getLine());
        }
        else throw new InterpreterException("Forbidden logical operation! Comparison of two different types at" + bco.getOperator().getLine());
    }

    //odwiedzona variable będzie ustawiała dwa pola env
    public void visit(TreeNodeSub.Variable v) throws  InterpreterException {
        env.setLastResultVar(v);
        env.setLastResult(env.getVarValue(v));
    }

    public void visit(TreeNodeSub.Num num) {
        env.setLastResult(num);
    }

    public void visit(TreeNodeSub.StringVar stringVar) {
        env.setLastResult(stringVar);
    }

    public void visit(TreeNodeSub.Parameters parameters) {
        //niepotrzebne chyba, rzutuję już wcześniej
    }

    public void visit(TreeNodeSub.Unit unit) throws InterpreterException { // wywolywany tylko na variablach tak to przechowuje
        // unitType może być typu basic albo complex
        env.loadFormulas(unit); //to potrzebne tylko do variable chyba? //do odwiedzanych unitów napisanych z palca typu [1 newton]
        env.setLastResult(unit);
    }
}
