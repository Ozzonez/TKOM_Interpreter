package com.interpreter;
import com.lexer.TokenType;
import com.parser.TreeNode;
import com.parser.TreeNodeSub;
import com.sun.source.tree.Tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Environment {
    //HashMap<String, TreeNodeSub.Variable> globals = new HashMap<String, TreeNodeSub.Variable>(); // mapa zmiennych globalnych - potrzebna?
    ArrayDeque<CallContext> callStack = new ArrayDeque<>();
    ArrayList<TreeNode> funcDefs;
    Object lastResult;
    Object lastResultVar;
    ArrayList<TreeNode> parameters;
    ArrayList<TreeNode> parametersValues;
    HashMap<String, TreeNode> basicTypes = new HashMap<String, TreeNode>();
    ArrayList<TreeNodeSub.UnitComplexType> complexTypes = new ArrayList<>();

    //sprawdzenie czy basic type po prostu sprawdzamy czy jest w mapie z basic typami
    public void loadFormulas(TreeNode unit) throws InterpreterException {
        TreeNode tmpType;
        if((tmpType = (basicTypes.get(((TreeNodeSub.Unit)unit).getUnitType().getContent()))) != null) {
            ArrayList<TreeNode> newAboveLine = new ArrayList<>();
            newAboveLine.add(tmpType);
            ((TreeNodeSub.Unit)unit).setAboveLine(newAboveLine);
            return;
        }
        else if(((TreeNodeSub.Unit) unit).getUnitType().getContent().equals("unknown")) // jak unknown niech nie ładuje
            return; // nie trzeba ładować kiedy nie jest wpisywane z palca, za to trzeba check unit zrobić
        //todo EEEEEEEEEEJ JEŚLI TYPE UNKNOWN TO NIECH TU WYWOŁUJE METODĘ CHECKFORKNOWNFORMULAS!!!!!!!!!

        for(TreeNodeSub.UnitComplexType i : complexTypes)
        {
            if(i.getName().getContent().equals(((TreeNodeSub.Unit) unit).getUnitType().getContent()))
            {
                ((TreeNodeSub.Unit) unit).setAboveLine((ArrayList<TreeNode>) (i.getAboveLine()).clone());
                ((TreeNodeSub.Unit) unit).setBelowLine((ArrayList<TreeNode>) (i.getBelowLine()).clone());
                return;
            }
        }
        throw new InterpreterException("Unit of unknown type!");
    }

    public void checkForKnownFormulas(TreeNode unit) throws InterpreterException {
        ((TreeNodeSub.Unit)unit).shortenFractions();
        for(TreeNode i : complexTypes)
        {
            for(TreeNode j : ((TreeNodeSub.UnitComplexType)i).getAboveLine())
            {

            }
        }
    }
    //todo dwa razy to samo zadeklarowane to blad powinien?
    public void addNewBasicUnit(TreeNode basicUnit)
    {
        basicTypes.put(((TreeNodeSub.UnitBasicType)basicUnit).getName().getContent(), basicUnit);
    }
    //todo dwa razy to samo zadeklarowano to blad powinien + skracać ułamki
    public void addNewComplexType(TreeNodeSub.UnitComplexType ucx) throws InterpreterException {
        for(TreeNode i : ucx.getAboveLine())
        {
            if(basicTypes.get(((TreeNodeSub.UnitBasicType)i).getName().getContent()) == null) throw new InterpreterException("Niezadeklarowany typ podstawowy!");
        }
        for(TreeNode i : ucx.getBelowLine())
        {
            if(basicTypes.get(((TreeNodeSub.UnitBasicType)i).getName().getContent()) == null) throw new InterpreterException("Niezadeklarowany typ podstawowy!");
        }
        ucx.getBelowLine().sort(null);
        ucx.getAboveLine().sort(null);
        complexTypes.add(ucx);
    }

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

    public void setLastResult(Object lastResult) { //todo tymczasowo
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
