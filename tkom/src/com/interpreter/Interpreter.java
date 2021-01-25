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
       throw new InterpreterException("Function not declared!");
    }

    public void visit(TreeNodeSub.Program program) throws InterpreterException {
        env = new Environment(program.getFunctions());
        TreeNode main = getFunc("main"); //todo błąd nie znaleziono maina
        main.accept(this); // git tak? main wskazuje na rekord z nazwą funkcji main w array, która jest polem Programu
    }

    public void visit(TreeNodeSub.FunctionDef fd) throws InterpreterException {
        env.makeBlockContext();

        if(((TreeNodeSub.FunctionDef)(getFunc(fd.getName().getContent()))).getParameters() != null) {
            env.setParameters(((TreeNodeSub.Parameters) ((TreeNodeSub.FunctionDef) (getFunc(fd.getName().getContent()))).getParameters()).getParameters());

            if (env.getParameters().size() != env.getParametersValues().size())
                throw new InterpreterException("Expected: " + env.getParameters().size() + "arguments but got: " + env.getParametersValues().size());
            int j = 0;
            for (TreeNode i : env.getParameters()) {
                ((TreeNodeSub.Variable) i).setValue((env.getParametersValues().get(j))); //todo to jedyne dozwolone użycie value w Variable
                j++;
            }
        }
        fd.getFunctionBlock().accept(this);
        env.deleteBlockContext();
    }

    //TODO OBIEKTU VARIABLE NIE TRAKTUJEMY JAKO PRAWDZIWE VAR - TRZEBA WZIĄĆ VARIABLE Z VALUE Z CURRENT VAR CONTEXT
    public void visit(TreeNodeSub.FunctionCall fd) throws InterpreterException// przekazac argumenty z funcCalla do zmiennych lokalnych w nowym callstacku
    {
        //tutaj zapisanie wartości tych parametrów po kolei do Array parameters
        ArrayList<TreeNode> tmp = new ArrayList<>();
        for(TreeNode i : fd.getArguments())
        {
            if(i instanceof TreeNodeSub.Variable || i instanceof TreeNodeSub.FunctionCall)
            {
                i.accept(this); // po wyjściu stąd w lastResult będzie wartość vara albo functioncalla

                tmp.add((TreeNode)env.getLastResult());
            }
            else // będzie num, unit, albo string bez ifa
            tmp.add(i);
        }
        env.setParametersValues(tmp);
        getFunc(fd.getName().getContent()).accept(this); // wyszukuje funkcję w arrayu funkcji
    }

    public void visit(TreeNodeSub.FunctionBlock fb) throws InterpreterException {
        env.addVarContext();
        if(env.getParameters() != null)
        for(TreeNode i : env.getParameters()) //todo ZLEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        {
            env.declareVarInCurrentScope(i, ((TreeNodeSub.Variable) i).getValue()); //todo DOZWOLONE TYLKO W TYM PRZYPADKU, W INNYCH GETVALUE NIE REPREZENTUJE WARTOŚCI ZMIENNEJ
        }

        for(TreeNode i : fb.getStatements()){
            if(i instanceof TreeNodeSub.ReturnStatement)
            {
                i.accept(this);
                return;
            }
            i.accept(this);
        }

        env.deleteVarContext();
    }

    public void visit(TreeNodeSub.ReturnStatement rs) throws InterpreterException {
        rs.getReturned().accept(this);
    }

    public void visit(TreeNodeSub.IfStatement ifs) throws InterpreterException {
        ifs.getCondition().accept(this);
        if(!(env.getLastResult() instanceof Boolean)) throw new InterpreterException("105"); //todo zmien nwm co tu miałoby wyrzucać, już wcześniej rzuci błąd

        if((Boolean) (env.getLastResult()))
            ifs.getInstructionBlockIfTrue().accept(this);
        else if(ifs.getInstructionBlockIfFalse() != null)
            ifs.getInstructionBlockIfFalse().accept(this);
    }

    public void visit(TreeNodeSub.WhileStatement ws) throws InterpreterException {
        ws.getCondition().accept(this);
        if(!(env.getLastResult() instanceof Boolean)) throw new InterpreterException("115"); //todo zmien

        while((Boolean) (env.getLastResult()))
        {
            ws.getWhileBody().accept(this);
            ws.getCondition().accept(this);
            if(!(env.getLastResult() instanceof Boolean)) throw new InterpreterException("121"); //todo zmien
        }
    }

    public void visit(TreeNodeSub.AssignStatement ass) throws InterpreterException {
        TreeNode var = ass.getName();
        ass.getValue().accept(this); // w lastresult wynik tego
        if(!((env.getLastResult() instanceof TreeNodeSub.Unit) || (env.getLastResult() instanceof TreeNodeSub.Num) || (env.getLastResult() instanceof TreeNodeSub.StringVar))) throw new InterpreterException("131"); //todo zmien

        env.updateVarInCurrentBlockContext(var, (TreeNode)env.getLastResult());
    }

    public void visit(TreeNodeSub.VarDeclaration vd) throws InterpreterException {
        TreeNode var = vd.getName();
        if(vd.getValue() != null)
        vd.getValue().accept(this); // ustawi lastResult na num, string lub unit

        if(!((env.getLastResult() instanceof TreeNodeSub.Unit) || (env.getLastResult() instanceof TreeNodeSub.Num) || (env.getLastResult() instanceof TreeNodeSub.StringVar) || env.getLastResult() == null)) throw new InterpreterException("142");

        if(vd.getValue() != null)
            env.declareVarInCurrentScope(var, (TreeNode)env.getLastResult());
        else env.declareVarInCurrentScope(var, null);
    }

    public void visit(TreeNodeSub.PrintStatement pt) throws InterpreterException {
        pt.getContent().accept(this);
        //todo sprawdzać co jest w lastResult i rzucać błąd jeśli źle ----- toString zaimplementować
        System.out.println(env.getLastResult());
    }

    public void visit(TreeNodeSub.UnitBasicType ubt) throws InterpreterException {
        //todo zrobic
    }

    public void visit(TreeNodeSub.UnitComplexType uct) throws InterpreterException {
        //todo zrobić
    }

    //wystarczy obliczać lewy z prawym, już parser zajął się pierwszeństwem wykonania
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
                double result = ((TreeNodeSub.Num)tmpLeft).getValue().getNumcontent() * ((TreeNodeSub.Num)tmpRight).getValue().getNumcontent();
                env.setLastResult(new TreeNodeSub.Num(new Token(TokenType.NUMBER, result, 0, 0)));
            }
            else  if(operator.getType() == TokenType.DIVISION_OP) {
                double result = ((TreeNodeSub.Num)tmpLeft).getValue().getNumcontent() / ((TreeNodeSub.Num)tmpRight).getValue().getNumcontent();
                env.setLastResult(new TreeNodeSub.Num(new Token(TokenType.NUMBER, result, 0, 0)));
            }
            else  if(operator.getContent().equals("-")) { //todo dodać w lexerze rozróżnianie - i +
                double result = ((TreeNodeSub.Num)tmpLeft).getValue().getNumcontent() - ((TreeNodeSub.Num)tmpRight).getValue().getNumcontent();
                env.setLastResult(new TreeNodeSub.Num(new Token(TokenType.NUMBER, result, 0, 0)));
            }
            else  if(operator.getContent().equals("+")) { //todo dodać w lexerze rozróżnianie - i +
                double result = ((TreeNodeSub.Num)tmpLeft).getValue().getNumcontent() + ((TreeNodeSub.Num)tmpRight).getValue().getNumcontent();
                env.setLastResult(new TreeNodeSub.StringVar(new Token(TokenType.NUMBER, result, 0, 0)));
            }
        }
        else if(tmpLeft instanceof TreeNodeSub.StringVar && tmpRight instanceof TreeNodeSub.StringVar)
        {
            if(operator.getContent().equals("-")) { //todo dodać w lexerze rozróżnianie - i +
            String result = ((TreeNodeSub.StringVar)tmpLeft).getValue().getContent() + ((TreeNodeSub.StringVar)tmpRight).getValue().getContent();
            env.setLastResult(new TreeNodeSub.Num(new Token(TokenType.QUOTE, result, 0, 0)));
            }
        }
        //todo dla unitów inne rodzaje dodawania, klasa ComplexUnit trzymająca strukturę użytych jednostek
        else throw new InterpreterException("Forbidden operation!");
    }

    //wystarczy porównywać lewy z prawym, już parser zajął się pierwszeństwem wykonania
    public void visit(TreeNodeSub.BinaryConditionOperator bco) throws  InterpreterException {
        bco.getLeftExp().accept(this);
        Object leftExp = env.getLastResult();
        bco.getRightExp().accept(this);
        Object rightExp = env.getLastResult();

        if(rightExp instanceof Boolean && leftExp instanceof Boolean)
        {
            if(bco.getOperator().getType() == TokenType.AND_OP)
                env.setLastResult((Boolean)leftExp && (Boolean) rightExp); //TODO JAK BŁĄD WYWALI SPRAWDŹ TO
            else if(bco.getOperator().getType() == TokenType.OR_OP)
                env.setLastResult((Boolean)leftExp || (Boolean) rightExp);
            else if(bco.getOperator().getType() == TokenType.EQUAL_OP)
                env.setLastResult((Boolean)leftExp == (Boolean) rightExp);
            else if(bco.getOperator().getType() == TokenType.NOT_EQUAL_OP)
                env.setLastResult((Boolean)leftExp != (Boolean) rightExp);
            else throw new InterpreterException("Forbidden logical operation!"); //TODO coś tu więcej info (ściągnij z errorów javovych)
        }
        else if(leftExp instanceof TreeNodeSub.Num && rightExp instanceof TreeNodeSub.Num)
        {
            if(bco.getOperator().getType() == TokenType.EQUAL_OP)
                env.setLastResult(((TreeNodeSub.Num) leftExp).getValue().getNumcontent() == ((TreeNodeSub.Num) rightExp).getValue().getNumcontent());
            else if(bco.getOperator().getType() == TokenType.NOT_EQUAL_OP)
                env.setLastResult(((TreeNodeSub.Num) leftExp).getValue().getNumcontent() != ((TreeNodeSub.Num) rightExp).getValue().getNumcontent());
            else if(bco.getOperator().getType() == TokenType.GREATER__OP)
                env.setLastResult(((TreeNodeSub.Num) leftExp).getValue().getNumcontent() > ((TreeNodeSub.Num) rightExp).getValue().getNumcontent());
            else if(bco.getOperator().getType() == TokenType.GREATER_EQUAL_OP)
                env.setLastResult(((TreeNodeSub.Num) leftExp).getValue().getNumcontent() >= ((TreeNodeSub.Num) rightExp).getValue().getNumcontent());
            else if(bco.getOperator().getType() == TokenType.SMALLER__OP)
                env.setLastResult(((TreeNodeSub.Num) leftExp).getValue().getNumcontent() < ((TreeNodeSub.Num) rightExp).getValue().getNumcontent());
            else if(bco.getOperator().getType() == TokenType.SMALLER_EQUAL_OP)
                env.setLastResult(((TreeNodeSub.Num) leftExp).getValue().getNumcontent() <= ((TreeNodeSub.Num) rightExp).getValue().getNumcontent());
        }
        else if(leftExp instanceof TreeNodeSub.StringVar && rightExp instanceof TreeNodeSub.StringVar)
        {
            throw new InterpreterException("String operations not yet implemented");
        }
        else throw new InterpreterException("Forbidden logical operation!");
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

    public void visit(TreeNodeSub.Unit unit) {
    }
}
