package com.parser;

import com.interpreter.INodeVisitor;
import com.interpreter.Interpreter;
import com.interpreter.InterpreterException;
import com.lexer.Token;
import java.util.ArrayList;

public class TreeNodeSub {

    public static class Program implements TreeNode{
        private ArrayList<TreeNode> functions = new ArrayList<TreeNode>();
        public ArrayList<TreeNode> getFunctions() {
            return functions;
        }
        public void addFunction(TreeNode function){
            functions.add(function);
        }

        public void accept(Interpreter visitor)
        {
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

        public void accept(Interpreter visitor)
        {
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

        public void accept(Interpreter visitor)
        {
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

        public void accept(Interpreter visitor)
        {
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

        public void accept(Interpreter visitor)
        {
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
        Token value; //todo trzeba jakoś sprawdzać czy dobra wartośc czyli tylko int albo double
        Token unitType; //todo TU MUSI BYĆ TREENODE ŻEBY TO BYŁ UNITBASIC TYPE ALBO COMPLEXTYPE basic type może być tokenem

        public Unit(Token value, Token unitType)
        {
            this.value = value;
            this.unitType = unitType;
        }

        public Token getValue() {
            return value;
        }

        public Token getUnitType() {
            return unitType;
        }

        public void accept(Interpreter visitor)
        {
            visitor.visit(this);
        }
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

        public void setValue(TreeNode value) {
            this.value = value;
        }

        public void accept(Interpreter visitor)
        {
            visitor.visit(this);
        }
    }

    public static class Num implements TreeNode{
        Token value;

        public Num(Token value)
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

        public UnitComplexType(Token name, TreeNode formula)
        {
            this.name = name;
            this.formula = formula;
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
