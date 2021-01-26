package com.tests.Parser;
import com.lexer.*;
import com.parser.ParserException;
import com.parser.TreeNode;
import com.parser.TreeNodeSub;
import org.junit.jupiter.api.Test;
import com.parser.Parser;
import com.lexer.TokenType;
import com.parser.TreeNodeSub.*;

import java.io.FileNotFoundException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ParserTests {

    Parser parser = new Parser("function foo (param1, param2)\n" +
            "{\n" +
            "    foo2(arg1);\n" +
            "    x = 20;\n" +
            "    var variable;\n" +
            "    if(x > 10)\n" +
            "    {\n" +
            "\n" +
            "    };\n" +
            "    while(i <= 100)\n" +
            "    {\n" +
            "\n" +
            "    };\n" +
            "    return variable;\n" +
            "    print(x + 2);\n" +
            "    def unit kg;\n" +
            "    def newton kg*m/s*s;\n" +
            "}");

    TreeNode program = parser.program();

    public ParserTests() throws FileNotFoundException, ParserException, LexerException {
    }

    ArrayList<TreeNode> statements = ((FunctionBlock)((FunctionDef)(((Program) program).getFunctions().get(0))).getFunctionBlock()).getStatements();

    @Test
    void checkFunctionDefArgs() throws Exception {
        assertEquals("param1", ((Variable)((Parameters)(((FunctionDef)(((Program) program).getFunctions().get(0))).getParameters())).getParameters().get(0)).getName().getContent());
        assertEquals(TokenType.NAME, ((Variable)((Parameters)(((FunctionDef)(((Program) program).getFunctions().get(0))).getParameters())).getParameters().get(0)).getName().getType());

        assertEquals("param2", ((Variable)((Parameters)(((FunctionDef)(((Program) program).getFunctions().get(0))).getParameters())).getParameters().get(1)).getName().getContent());
        assertEquals(TokenType.NAME, ((Variable)((Parameters)(((FunctionDef)(((Program) program).getFunctions().get(0))).getParameters())).getParameters().get(1)).getName().getType());
    }
    @Test
    void checkFunctionCalls() throws Exception {
        assertEquals(FunctionCall.class, statements.get(0).getClass());
    }
    @Test
    void checkAssignment() throws Exception {
        assertEquals(AssignStatement.class, statements.get(1).getClass());
    }
    @Test
    void checkVarDeclaration() throws Exception {
        assertEquals(VarDeclaration.class, statements.get(2).getClass());
    }
    @Test
    void checkIfStatement() throws Exception {
        assertEquals(IfStatement.class, statements.get(3).getClass());
    }
    @Test
    void checkWhileStatement() throws Exception {
        assertEquals(WhileStatement.class, statements.get(4).getClass());
    }
    @Test
    void checkReturnStatement() throws Exception {
        assertEquals(ReturnStatement.class, statements.get(5).getClass());
    }
    @Test
    void checkPrint() throws Exception {
        assertEquals(PrintStatement.class, statements.get(6).getClass());
    }
    @Test
    void checkDefBasicUnitStatement() throws Exception {
        assertEquals(UnitBasicType.class, statements.get(7).getClass());
    }
    @Test
    void checkDefComplexUnitStatement() throws Exception {
        assertEquals(UnitComplexType.class, statements.get(8).getClass());
    }


    @Test
    void checkFuncCallArgs() throws Exception {
        TreeNode functionCall = statements.get(0);

        assertEquals(TokenType.NAME, ((Variable)((FunctionCall)functionCall).getArguments().get(0)).getName().getType());
        assertEquals("arg1", ((Variable)((FunctionCall)functionCall).getArguments().get(0)).getName().getContent());
    }
    @Test
    void checkFuncCallName() throws Exception {
        TreeNode functionCall = statements.get(0);

        assertEquals(TokenType.NAME, ((FunctionCall)functionCall).getName().getType());
        assertEquals("foo2", ((FunctionCall)functionCall).getName().getContent());
    }


    @Test
    void checkAssignmentName() throws Exception {
        Parser parser2 = new Parser("function foo (param1, param2)\n" +
                "{\n" +
                "    x = 20;\n" +
                "}\n");
        TreeNode program2 = parser2.program();
        TreeNode statement2 = (((FunctionBlock)((FunctionDef)(((Program) program2).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);

        assertEquals(TokenType.NAME, ((Variable)((AssignStatement)statement2).getName()).getName().getType());
        assertEquals("x", ((Variable)((AssignStatement)statement2).getName()).getName().getContent());
    }
    @Test
    void checkAssignmentValue() throws Exception {
        Parser parser2 = new Parser("function foo (param1, param2)\n" +
                "{\n" +
                "    x = 20;\n" +
                "}\n");
        TreeNode program2 = parser2.program();
        TreeNode statement2 = (((FunctionBlock)((FunctionDef)(((Program) program2).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);

        assertEquals(TokenType.NUMBER, ((Num)((AssignStatement)statement2).getValue()).getValue().getType());
        assertEquals(20, ((Num)((AssignStatement)statement2).getValue()).getValue().getNumcontent());
    }


    @Test
    void checkVarDecName() throws Exception {
        Parser parser3 = new Parser("function foo (param1, param2)\n" +
                "{\n" +
                "    var variable;;\n" +
                "}\n");
        TreeNode program3 = parser3.program();
        TreeNode statement3 = (((FunctionBlock)((FunctionDef)(((Program) program3).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);

        assertEquals(TokenType.NAME, ((Variable)((VarDeclaration)statement3).getName()).getName().getType());
        assertEquals("variable", ((Variable)((VarDeclaration)statement3).getName()).getName().getContent());
    }

    Parser parser4 = new Parser("function foo (param1, param2)\n" +
            "{\n" +
            "    if(x > 10)\n" +
            "    {\n" +
            "        x = x + 1;\n" +
            "    };\n" +
            "}\n");
    TreeNode program4 = parser4.program();
    TreeNode statement4 = (((FunctionBlock)((FunctionDef)(((Program) program4).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);
    @Test
    void checkIfStatementCondition() throws Exception {
        assertEquals(BinaryConditionOperator.class, ((BinaryConditionOperator)((IfStatement)statement4).getCondition()).getClass());
    }
    @Test
    void checkIfStatementConditionOperator() throws Exception {
        assertEquals(TokenType.GREATER__OP, (((BinaryConditionOperator)((IfStatement)statement4).getCondition()).getOperator()).getType());
    }
    @Test
    void checkIfStatementConditionLeftExp() throws Exception {
        assertEquals(TokenType.NAME, ((Variable)(((BinaryConditionOperator)((IfStatement)statement4).getCondition()).getLeftExp())).getName().getType());
    }
    @Test
    void checkIfStatementConditionRightExp() throws Exception {
        assertEquals(TokenType.NUMBER, ((Num)(((BinaryConditionOperator)((IfStatement)statement4).getCondition()).getRightExp())).getValue().getType());
    }
    @Test
    void checkIfStatementBlock() throws Exception {
        assertEquals(FunctionBlock.class, ((IfStatement)statement4).getInstructionBlockIfTrue().getClass());
        assertNull(((IfStatement)statement4).getInstructionBlockIfFalse());
    }

    Parser parser5 = new Parser("function foo (param1, param2)\n" +
            "{\n" +
            "    while(i <= 100 && ( x == 3))\n" +
            "    {\n" +
            "        x = (x - 10) + 5 * 2;\n" +
            "    };\n" +
            "}\n");
    TreeNode program5 = parser5.program();
    TreeNode statement5 = (((FunctionBlock)((FunctionDef)(((Program) program5).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);
    @Test
    void checkWhileStatementCondition() throws Exception {
        assertEquals(BinaryConditionOperator.class, ((BinaryConditionOperator)((WhileStatement)statement5).getCondition()).getClass());
    }
    @Test
    void checkWhileStatementCondition2() throws Exception {
        assertEquals(BinaryConditionOperator.class, ((BinaryConditionOperator)((WhileStatement)statement5).getCondition()).getLeftExp().getClass());
    }
    @Test
    void checkWhileStatementCondition3() throws Exception {
        assertEquals(BinaryConditionOperator.class, ((BinaryConditionOperator)((WhileStatement)statement5).getCondition()).getRightExp().getClass());
    }
    @Test
    void checkWhileStatementCondition4() throws Exception {
        assertEquals(3, ((Num)((BinaryConditionOperator)(((BinaryConditionOperator)((WhileStatement)statement5).getCondition()).getRightExp())).getRightExp()).getValue().getNumcontent());
    }
    @Test
    void checkWhileStatementCondition5() throws Exception {
        assertEquals("x", ((Variable)((BinaryConditionOperator)(((BinaryConditionOperator)((WhileStatement)statement5).getCondition()).getRightExp())).getLeftExp()).getName().getContent());
    }
    @Test
    void checkWhileStatementCondition6() throws Exception {
        assertEquals(100, ((Num)((BinaryConditionOperator)(((BinaryConditionOperator)((WhileStatement)statement5).getCondition()).getLeftExp())).getRightExp()).getValue().getNumcontent());
    }
    @Test
    void checkWhileStatementCondition7() throws Exception {
        assertEquals("i", ((Variable)((BinaryConditionOperator)(((BinaryConditionOperator)((WhileStatement)statement5).getCondition()).getLeftExp())).getLeftExp()).getName().getContent());
    }
    @Test
    void checkWhileStatementCondition8() throws Exception {
        assertEquals("<=", ((BinaryConditionOperator)(((BinaryConditionOperator)((WhileStatement)statement5).getCondition()).getLeftExp())).getOperator().getContent());
    }
    @Test
    void checkWhileStatementCondition9() throws Exception {
        assertEquals("==", ((BinaryConditionOperator)(((BinaryConditionOperator)((WhileStatement)statement5).getCondition()).getRightExp())).getOperator().getContent());
    }




    @Test
    void checkReturn() throws Exception {
        Parser parser6 = new Parser("function foo (param1, param2)\n" +
                "    {\n" +
                "        return variable;\n" +
                "    }");
        TreeNode program6 = parser6.program();
        TreeNode statement6 = (((FunctionBlock)((FunctionDef)(((Program) program6).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);

        assertEquals(TokenType.NAME, ((Variable)((ReturnStatement)statement6).getReturned()).getName().getType());
        assertEquals("variable", ((Variable)((ReturnStatement)statement6).getReturned()).getName().getContent());
    }



    @Test
    void checkPrintContent() throws Exception {
        Parser parser = new Parser("function foo (param1, param2)\n" +
                "{\n" +
                "    print(x + 2);\n" +
                "}");
        TreeNode program7 = parser.program();
        TreeNode statement7 = (((FunctionBlock)((FunctionDef)(((Program) program7).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);

        assertEquals(BinOperator.class, (((PrintStatement)statement7).getContent()).getClass());
    }


    @Test
    void checkBasicUnit() throws Exception {
        Parser parser8 = new Parser("function foo (param1, param2)\n" +
                "{\n" +
                "    def unit kg;\n" +
                "}");
        TreeNode program8 = parser8.program();
        TreeNode statement8 = (((FunctionBlock)((FunctionDef)(((Program) program8).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);

        assertEquals(TokenType.NAME, ((UnitBasicType)statement8).getName().getType());
        assertEquals("kg",  ((UnitBasicType)statement8).getName().getContent());
    }

    @Test
    void checkComplexUnit() throws Exception {
        Parser parser8 = new Parser("function foo (param1, param2)\n" +
                "{\n" +
                "    def newton kg*m/s*s;\n" +
                "}");
        TreeNode program8 = parser8.program();
        TreeNode statement8 = (((FunctionBlock)((FunctionDef)(((Program) program8).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);

        assertEquals(TokenType.NAME, ((UnitComplexType)statement8).getName().getType());
        assertEquals("newton",  ((UnitComplexType)statement8).getName().getContent());

        //assertEquals(BinOperator.class, ((UnitComplexType)statement8).getFormula().getClass());
    }

    @Test
    void checkExpression() throws Exception {
        Parser parser9 = new Parser("function foo (param1, param2)\n" +
                "{\n" +
                "    x = (x - 10) + 5 * 2;\n" +
                "}\n");
        TreeNode program9 = parser9.program();
        TreeNode statement9 = (((FunctionBlock)((FunctionDef)(((Program) program9).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);

        assertEquals(TokenType.ADDITIVE_OP, ((BinOperator)((AssignStatement)statement9).getValue()).getOperator().getType());
    }
    @Test
    void checkExpression2() throws Exception {
        Parser parser9 = new Parser("function foo (param1, param2)\n" +
                "{\n" +
                "    x = (x - 10) + 5 * 2;\n" +
                "}\n");
        TreeNode program9 = parser9.program();
        TreeNode statement9 = (((FunctionBlock)((FunctionDef)(((Program) program9).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);

        assertEquals("-", ((BinOperator)((BinOperator)((AssignStatement)statement9).getValue()).getLeftExp()).getOperator().getContent());
    }
    @Test
    void checkExpression3() throws Exception {
        Parser parser9 = new Parser("function foo (param1, param2)\n" +
                "{\n" +
                "    x = (x - 10) + 5 * 2;\n" +
                "}\n");
        TreeNode program9 = parser9.program();
        TreeNode statement9 = (((FunctionBlock)((FunctionDef)(((Program) program9).getFunctions().get(0))).getFunctionBlock()).getStatements()).get(0);

        assertEquals("*", ((BinOperator)((BinOperator)((AssignStatement)statement9).getValue()).getRightExp()).getOperator().getContent());
    }

    @Test
    void unitTest() throws Exception {
        Parser parser9 = new Parser("function foo (param1, param2)\n" +
                "{\n" +
                "    def unit dag;\n" +
                "    def newton w / m*kg;\n" + //w*m / kg
                "    def newton4 w * c / ;\n" +
                "    def newton2 w /;\n" +
                "    def newton3  / kg * m;\n" + //niedziala above null, below m
                "    def newton4  / kg;\n" + //niedziala above null 2
                "    var x = [1 kg];\n" +
                "}\n");
        TreeNode program9 = parser9.program();
        System.out.println("test");
    }
}
