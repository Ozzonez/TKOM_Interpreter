package com.interpreter;
import com.lexer.Token;
import com.lexer.TokenType;
import com.parser.TreeNode;
import com.parser.TreeNodeSub;
import com.sun.source.tree.Tree;

import java.util.*;

public class Environment {
    ArrayDeque<CallContext> callStack = new ArrayDeque<>();
    ArrayList<TreeNode> funcDefs;
    Object lastResult;
    Object lastResultVar;
    ArrayList<TreeNode> parameters;
    ArrayList<TreeNode> parametersValues;
    HashMap<String, TreeNode> basicTypes = new HashMap<String, TreeNode>();
    ArrayList<TreeNodeSub.UnitComplexType> complexTypes = new ArrayList<>();
    Boolean returnMet = false;


    public void loadFormulas(TreeNode unit) throws InterpreterException {
        TreeNode tmpType;
        if((tmpType = (basicTypes.get(((TreeNodeSub.Unit)unit).getUnitType().getContent()))) != null) {
            ArrayList<TreeNode> newAboveLine = new ArrayList<>();
            newAboveLine.add(tmpType);
            ((TreeNodeSub.Unit)unit).setAboveLine(newAboveLine);
            return;
        }
        else if(((TreeNodeSub.Unit) unit).getUnitType().getContent().equals("unknown"))
            return;

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

        ((TreeNodeSub.Unit)unit).shortenFractions(); //todo JESZCZE SHORTEN FRACTIONS DLA UNITCOMPLEXTYPÓW ENVA

        ArrayList<TreeNode> currentAboveList = ((TreeNodeSub.Unit)(unit)).getAboveLine();
        ArrayList<TreeNode> currentBelowList = ((TreeNodeSub.Unit)(unit)).getBelowLine();
        int belowSize = currentBelowList.size();
        int aboveSize = currentAboveList.size();
        TreeNodeSub.UnitBasicType tmp;
        boolean inCorrect = false;

        if(belowSize == 0 && aboveSize == 1) {
            if ((tmp = (TreeNodeSub.UnitBasicType) basicTypes.get(((TreeNodeSub.UnitBasicType)(currentAboveList.get(0))).getName().getContent())) == null) throw new InterpreterException("unknwn mistake");
            ((TreeNodeSub.Unit)(unit)).setUnitType(new Token(TokenType.NAME, tmp.getName().getContent(), 0, 0));
            return;
        }


        Collections.sort(currentAboveList, Comparator.comparing(p -> ((TreeNodeSub.UnitBasicType) p).getName().getContent()));
        Collections.sort(currentBelowList, Comparator.comparing(p -> ((TreeNodeSub.UnitBasicType) p).getName().getContent()));

        for(TreeNode i : complexTypes)
        {
            if(((TreeNodeSub.UnitComplexType)i).getAboveLine().size() == aboveSize &&
                    ((TreeNodeSub.UnitComplexType)i).getBelowLine().size() == belowSize) {
                for (int j = 0; j < aboveSize; j++) {
                    if (!(((TreeNodeSub.UnitBasicType) currentAboveList.get(j)).getName().getContent().equals(((TreeNodeSub.UnitBasicType) ((TreeNodeSub.UnitComplexType) i).getAboveLine().get(j)).getName().getContent())))
                        inCorrect = true;
                }
                for (int j = 0; j < aboveSize; j++) {
                    if (!(((TreeNodeSub.UnitBasicType) currentBelowList.get(j)).getName().getContent().equals(((TreeNodeSub.UnitBasicType) ((TreeNodeSub.UnitComplexType) i).getBelowLine().get(j)).getName().getContent())))
                        inCorrect = true;
                }

                if (!inCorrect)
                    ((TreeNodeSub.Unit) (unit)).setUnitType(new Token(TokenType.NAME, ((TreeNodeSub.UnitComplexType) (i)).getName().getContent(), 0, 0));
                inCorrect = false;
            }
        }
    }

    public void addNewBasicUnit(TreeNode basicUnit)
    {
        basicTypes.put(((TreeNodeSub.UnitBasicType)basicUnit).getName().getContent(), basicUnit);
    }
    //todo dwa razy to samo zadeklarowano to blad powinien
    public void addNewComplexType(TreeNodeSub.UnitComplexType ucx) throws InterpreterException {

        for(TreeNode i : complexTypes)
        {
            if(ucx.getName().getContent().equals(((TreeNodeSub.UnitComplexType) i).getName().getContent())) throw new InterpreterException("Unit with this name already defined" + ucx.getName().getLine());
        }

        if(ucx.getAboveLine().size() > 0 && ucx.getBelowLine().size() > 0) {
            for (Iterator<TreeNode> it = ucx.getAboveLine().iterator(); it.hasNext(); ) {
                TreeNodeSub.UnitBasicType tmp = ((TreeNodeSub.UnitBasicType) (it.next()));
                for (Iterator<TreeNode> it2 = ucx.getBelowLine().iterator(); it2.hasNext(); ) {
                    if (tmp.getName().getContent().equals(((TreeNodeSub.UnitBasicType) (it2.next())).getName().getContent())) {
                        it2.remove();
                        it.remove();
                        break;
                    }
                }
            }
        }
        Collections.sort(ucx.getBelowLine(), Comparator.comparing(p -> ((TreeNodeSub.UnitBasicType) p).getName().getContent()));
        Collections.sort(ucx.getBelowLine(), Comparator.comparing(p -> ((TreeNodeSub.UnitBasicType) p).getName().getContent()));

        for(TreeNode i : ucx.getAboveLine())
        {
            if(basicTypes.get(((TreeNodeSub.UnitBasicType)i).getName().getContent()) == null) throw new InterpreterException("Niezadeklarowany typ podstawowy!");
        }
        for(TreeNode i : ucx.getBelowLine())
        {
            if(basicTypes.get(((TreeNodeSub.UnitBasicType)i).getName().getContent()) == null) throw new InterpreterException("Niezadeklarowany typ podstawowy!");
        }
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

    public void setLastResult(TreeNode lastResult) {
        this.lastResult = lastResult;
    }

    public void setLastResult(Object lastResult) {
        this.lastResult = lastResult;
    }

    public Object getLastResult() {
        return lastResult;
    }

    public void setLastResultVar(TreeNode lastResultVar) {
        this.lastResultVar = lastResultVar;
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

    public void updateVarInCurrentBlockContext(TreeNode name, TreeNode value) throws InterpreterException {
        assert callStack.peek() != null;
        callStack.peek().updateVarInBlockContext(name, value);
    }

    public void declareVarInCurrentScope(TreeNode name, TreeNode value) throws InterpreterException {
        assert callStack.peek() != null;
        callStack.peek().declareVarInCurrentScope(name, value);
    }

    public TreeNode getVarValue(TreeNode name) throws InterpreterException {
        return callStack.peek().getVarValue(name);
    }

    public Boolean getReturnMet() {
        return returnMet;
    }

    public void setReturnMet(Boolean returnMet) {
        this.returnMet = returnMet;
    }
}
