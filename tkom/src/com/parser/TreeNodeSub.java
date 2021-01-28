package com.parser;

import com.interpreter.INodeVisitor;
import com.interpreter.Interpreter;
import com.interpreter.InterpreterException;
import com.lexer.Token;
import com.lexer.TokenType;
import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.Iterator;

public class TreeNodeSub {

    public static class Program implements TreeNode{
        private ArrayList<TreeNode> functions = new ArrayList<TreeNode>();
        public ArrayList<TreeNode> getFunctions() {
            return functions;
        }
        public void addFunction(TreeNode function){
            functions.add(function);
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class FunctionDef implements TreeNode{
        Token name;
        TreeNode type; //@todo nie ma tego???
        TreeNode parameters;
        TreeNode functionBlock;

        public FunctionDef(Token name, TreeNode parameters, TreeNode functionBlock)
        {
            this.name = name;
            this.parameters = parameters;
            this.functionBlock = functionBlock;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }

        public Token getName() {
            return name;
        }

        public TreeNode getType() {
            return type;
        }

        public TreeNode getParameters() {
            return parameters;
        }

        public TreeNode getFunctionBlock() {
            return functionBlock;
        }
    }

    public static class Parameters implements TreeNode{
        ArrayList<TreeNode> parameters;

        public Parameters(ArrayList<TreeNode> parameters) {
            this.parameters = parameters;
        }

        public ArrayList<TreeNode> getParameters() {
            return parameters;
        }

        public void accept(Interpreter visitor)
        {
            visitor.visit(this);
        }
    }

    public static class FunctionBlock implements TreeNode {
        ArrayList<TreeNode> statements;

        public FunctionBlock(ArrayList<TreeNode> statements) {
            this.statements = statements;
        }

        public ArrayList<TreeNode> getStatements() {
            return statements;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class FunctionCall implements  TreeNode{
        ArrayList<TreeNode> arguments;
        Token name;

        public FunctionCall(Token name, ArrayList<TreeNode> arguments) {
            this.arguments = arguments;
            this.name = name;
        }

        public ArrayList<TreeNode> getArguments() {
            return arguments;
        }

        public Token getName() {
            return name;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class IfStatement implements  TreeNode{
        TreeNode condition;
        TreeNode instructionBlockIfTrue;
        TreeNode instructionBlockIfFalse;

        public IfStatement(TreeNode condition, TreeNode instructionBlockIfTrue, TreeNode instructionBlockIfFalse) {
            this.condition = condition;
            this.instructionBlockIfTrue = instructionBlockIfTrue;
            this.instructionBlockIfFalse = instructionBlockIfFalse;
        }

        public TreeNode getCondition() {
            return condition;
        }

        public TreeNode getInstructionBlockIfTrue() {
            return instructionBlockIfTrue;
        }

        public TreeNode getInstructionBlockIfFalse() {
            return instructionBlockIfFalse;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class WhileStatement implements  TreeNode{
        TreeNode condition;
        TreeNode whileBody;

        public WhileStatement(TreeNode condition, TreeNode whileBody) {
            this.condition = condition;
            this.whileBody = whileBody;
        }

        public TreeNode getCondition() {
            return condition;
        }

        public TreeNode getWhileBody() {
            return whileBody;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class BinaryConditionOperator implements TreeNode{
        TreeNode leftExp;
        Token operator;
        TreeNode rightExp;

        public BinaryConditionOperator(TreeNode leftExp, Token operator, TreeNode rightExp) {
            this.leftExp = leftExp;
            this.operator = operator;
            this.rightExp = rightExp;
        }

        public TreeNode getLeftExp() {
            return leftExp;
        }

        public Token getOperator() {
            return operator;
        }

        public TreeNode getRightExp() {
            return rightExp;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class BinOperator implements TreeNode{
        TreeNode leftExp;
        Token operator;
        TreeNode rightExp;

        public BinOperator(TreeNode leftExp, Token operator, TreeNode rightExp) {
            this.leftExp = leftExp;
            this.operator = operator;
            this.rightExp = rightExp;
        }

        public TreeNode getLeftExp() {
            return leftExp;
        }

        public Token getOperator() {
            return operator;
        }

        public TreeNode getRightExp() {
            return rightExp;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }

        @Override
        public String toString() {
            return "" + leftExp + operator.getContent() + rightExp;
        }
    }

    public static class AssignStatement implements TreeNode{
        TreeNode name;
        TreeNode value;

        public AssignStatement(TreeNode name, TreeNode value)
        {
            this.name = name;
            this.value = value;
        }

        public TreeNode getName() {
            return name;
        }

        public TreeNode getValue() {
            return value;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class VarDeclaration implements TreeNode{
        TreeNode name; //czy to powinno być variable()??
        TreeNode value;

        public VarDeclaration(TreeNode name, TreeNode value)
        {
            this.name = name;
            this.value = value;
        }

        public TreeNode getName() {
            return name;
        }

        public TreeNode getValue() {
            return value;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class ReturnStatement implements TreeNode{
        TreeNode returned; //@todo narazie jest additiveExp a nie assignable

        public ReturnStatement(TreeNode returned)
        {
            this.returned = returned;
        }

        public TreeNode getReturned() {
            return returned;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class PrintStatement implements TreeNode{
        TreeNode content;

        public PrintStatement(TreeNode content)
        {
            this.content = content;
        }

        public TreeNode getContent() {
            return content;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class Unit implements TreeNode{
        Token value;
        Token unitType; //to jest name
        ArrayList<TreeNode> aboveLine = new ArrayList<>();
        ArrayList<TreeNode> belowLine = new ArrayList<>();

        public Unit(Token value, Token unitType)
        {
            this.value = value;
            this.unitType = unitType;
        }

        public Unit(Token value, Token unitType, ArrayList<TreeNode> aboveLine, ArrayList<TreeNode> belowLine)
        {
            this.value = value;
            this.unitType = unitType;
            this.aboveLine = aboveLine;
            this.belowLine = belowLine;
        }

        public ArrayList<TreeNode> getAboveLine() {
            return aboveLine;
        }

        public ArrayList<TreeNode> getBelowLine() {
            return belowLine;
        }

        public void setUnitType(Token unitType) {
            this.unitType = unitType;
        }

        public Token getValue() {
            return value;
        }

        public Token getUnitType() {
            return unitType;
        }

        public void setAboveLine(ArrayList<TreeNode> aboveLine) {
            this.aboveLine = aboveLine;
        }

        public void setBelowLine(ArrayList<TreeNode> belowLine) {
            this.belowLine = belowLine;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }

        public void shortenFractions()
        {
            int i = 0;
            if(aboveLine.size() == 0 || belowLine.size() == 0) return;
            for(Iterator<TreeNode> it = aboveLine.iterator(); it.hasNext();)
            {
                UnitBasicType tmp = ((UnitBasicType) (it.next()));
                for(Iterator<TreeNode> it2 = belowLine.iterator(); it2.hasNext();) {
                    if(tmp.getName().getContent().equals(((UnitBasicType) (it2.next())).getName().getContent()))
                    {
                        it2.remove();
                        it.remove();
                        break;
                    }
                }
            }
        }

        public Boolean equals(TreeNode unit)
        {
            return this.value.getNumcontent() == ((Unit)unit).getValue().getNumcontent();
        }
        public Boolean notEquals(TreeNode unit)
        {
            return this.value.getNumcontent() != ((Unit)unit).getValue().getNumcontent();
        }
        public Boolean greater(TreeNode unit)
        {
            return this.value.getNumcontent() > ((Unit)unit).getValue().getNumcontent();
        }
        public Boolean greaterEqual(TreeNode unit)
        {
            return this.value.getNumcontent() >= ((Unit)unit).getValue().getNumcontent();
        }
        public Boolean smaller(TreeNode unit)
        {
            return this.value.getNumcontent() < ((Unit)unit).getValue().getNumcontent();
        }
        public Boolean smallerEqual(TreeNode unit)
        {
            return this.value.getNumcontent() <= ((Unit)unit).getValue().getNumcontent();
        }
        public Unit multiply(TreeNode unit) throws InterpreterException {
            ArrayList<TreeNode> tmpAboveLine = (ArrayList<TreeNode>) (aboveLine.clone());
            ArrayList<TreeNode> tmpBelowLine = (ArrayList<TreeNode>) (belowLine.clone());

            double tmpValue = value.getNumcontent() * ((Unit)unit).getValue().getNumcontent();
            tmpAboveLine.addAll(((Unit) unit).getAboveLine());
            tmpBelowLine.addAll(((Unit) unit).getBelowLine());
            Token name = new Token(TokenType.NAME, "unknown", 0, 0);
            Token val = new Token(TokenType.NUMBER, tmpValue, 0, 0);

            return new Unit(val, name, tmpAboveLine, tmpBelowLine);
        }
        public Unit divide(TreeNode unit) throws InterpreterException {
            ArrayList<TreeNode> tmpAboveLine = (ArrayList<TreeNode>) (aboveLine.clone());
            ArrayList<TreeNode> tmpBelowLine = (ArrayList<TreeNode>) (belowLine.clone());

            double tmpValue = value.getNumcontent() / ((Unit)unit).getValue().getNumcontent();
            tmpAboveLine.addAll(((Unit) unit).getBelowLine());
            tmpBelowLine.addAll(((Unit) unit).getAboveLine());
            Token name = new Token(TokenType.NAME, "unknown", 0, 0);
            Token val = new Token(TokenType.NUMBER, tmpValue, 0, 0);

            return new Unit(val, name, tmpAboveLine, tmpBelowLine);
        }
        public Unit add(TreeNode unit) throws InterpreterException {
            ArrayList<TreeNode> tmpAboveLine = (ArrayList<TreeNode>) (aboveLine.clone());
            ArrayList<TreeNode> tmpBelowLine = (ArrayList<TreeNode>) (belowLine.clone());

            double tmpValue = value.getNumcontent() + ((Unit)unit).getValue().getNumcontent();

            Token name = new Token(TokenType.NAME, "unknown", 0, 0);
            Token val = new Token(TokenType.NUMBER, tmpValue, 0, 0);

            return new Unit(val, name, tmpAboveLine, tmpBelowLine);
        }
        public Unit subtract(TreeNode unit) throws InterpreterException {
            ArrayList<TreeNode> tmpAboveLine = (ArrayList<TreeNode>) (aboveLine.clone());
            ArrayList<TreeNode> tmpBelowLine = (ArrayList<TreeNode>) (belowLine.clone());

            double tmpValue = value.getNumcontent() - ((Unit)unit).getValue().getNumcontent();

            Token name = new Token(TokenType.NAME, "unknown", 0, 0);
            Token val = new Token(TokenType.NUMBER, tmpValue, 0, 0);

            return new Unit(val, name, tmpAboveLine, tmpBelowLine);
        }
        public Unit multiplyByNum(TreeNode num) throws InterpreterException {
            ArrayList<TreeNode> tmpAboveLine = (ArrayList<TreeNode>) (aboveLine.clone());
            ArrayList<TreeNode> tmpBelowLine = (ArrayList<TreeNode>) (belowLine.clone());

            double tmpValue = value.getNumcontent() * ((Num)num).getValue().getNumcontent();

            Token name = new Token(TokenType.NAME, unitType.getContent(), 0, 0);
            Token val = new Token(TokenType.NUMBER, tmpValue, 0, 0);

            return new Unit(val, name, tmpAboveLine, tmpBelowLine);
        }
        public Unit divideByNum(TreeNode num) throws InterpreterException {
            ArrayList<TreeNode> tmpAboveLine = (ArrayList<TreeNode>) (aboveLine.clone());
            ArrayList<TreeNode> tmpBelowLine = (ArrayList<TreeNode>) (belowLine.clone());

            double tmpValue = value.getNumcontent() / ((Num)num).getValue().getNumcontent();

            Token name = new Token(TokenType.NAME, unitType.getContent(), 0, 0);
            Token val = new Token(TokenType.NUMBER, tmpValue, 0, 0);

            return new Unit(val, name, tmpAboveLine, tmpBelowLine);
        }

        @Override
        public String toString() {
            return value.getNumcontent() + " " + unitType.getContent();
        }

        //todo dodaj metody do dodawania i mmnożenia ze sobą unitów, sortowanie itp
    }

    public static class Variable implements TreeNode{
        Token name;
        //String value;
        TreeNode value; //zmieniono na TreeNode z Token

        public Variable(Token name)
        {
            this.name = name;
        }

        public Token getName() {
            return name;
        }

        public TreeNode getValue() {
            return value;
        }

        public void setValue(TreeNode value) {
            this.value = value;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class Num implements TreeNode{
        Token value;

        public Num(Token value)
        {
            this.value = value;
        }

        public Num() {};

        public Token getValue() {
            return value;
        }

        public void accept(Interpreter visitor)
        {
            visitor.visit(this);
        }

        public boolean equals(double comapared)
        {
            return (value.getNumcontent() == comapared);
        }
        public boolean notEquals(double comapared)
        {
            return (value.getNumcontent() != comapared);
        }
        public boolean greater(double comapared)
        {
            return (value.getNumcontent() > comapared);
        }
        public boolean greaterEqual(double comapared)
        {
            return (value.getNumcontent() >= comapared);
        }
        public boolean smaller(double comapared)
        {
            return (value.getNumcontent() < comapared);
        }
        public boolean smallerEqual(double comapared)
        {
            return (value.getNumcontent() <= comapared);
        }

        @Override
        public String toString() {
            return "" + value.getNumcontent();
        }
    }

    public static class StringVar implements TreeNode{
        Token value;

        public StringVar(Token value)
        {
            this.value = value;
        }

        public Token getValue() {
            return value;
        }

        public void accept(Interpreter visitor)
        {
            visitor.visit(this);
        }

        public boolean equals(String comapared)
        {
            return (value.getContent().equals(comapared));
        }
        public boolean notEquals(String comapared)
        {
            return (!value.getContent().equals(comapared));
        }

        @Override
        public String toString() {
            return "" + value.getNumcontent();
        }
    }

    public static class UnitBasicType implements TreeNode{
        Token name;

        public UnitBasicType(Token name)
        {
            this.name = name;
        }

        public Token getName() {
            return name;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class UnitComplexType implements TreeNode{
        Token name;
        TreeNode formula;
        ArrayList<TreeNode> aboveLine = new ArrayList<>();
        ArrayList<TreeNode> belowLine = new ArrayList<>();

        public UnitComplexType(Token name, TreeNode formula)
        {
            this.name = name;
            this.formula = formula;
        }

        public UnitComplexType(Token name, ArrayList<TreeNode> aboveLine, ArrayList<TreeNode> belowLine)
        {
            this.name = name;
            this.aboveLine = aboveLine;
            this.belowLine = belowLine;
        }

        public ArrayList<TreeNode> getAboveLine() {
            return aboveLine;
        }

        public ArrayList<TreeNode> getBelowLine() {
            return belowLine;
        }

        public Token getName() {
            return name;
        }

        public TreeNode getFormula() {
            return formula;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }
}
