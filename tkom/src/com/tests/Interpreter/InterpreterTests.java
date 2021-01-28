package com.tests.Interpreter;
import com.interpreter.Interpreter;
import com.interpreter.InterpreterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.lexer.*;
import com.parser.ParserException;
import com.parser.TreeNode;
import com.parser.TreeNodeSub;
import com.parser.Parser;
import com.lexer.TokenType;
import com.parser.TreeNodeSub.*;
import com.interpreter.Interpreter;
import com.interpreter.InterpreterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTests
{

    @Test
    public void UnitComparisonTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main()\n" +
                "{\n" +
                "    def unit kg;\n" +
                "    def unit m;\n" +
                "    var y = [1 m];\n" +
                "    var x = [1 m];\n" +
                "\n" +
                "    if(x == y)\n" +
                "    {\n" +
                "        return 1;\n" +
                "    };\n" +
                "    return 0;\n" +
                "}\n");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(1, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void UnitComparisonUnequalityTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main()\n" +
                "{\n" +
                "    def unit kg;\n" +
                "    def unit m;\n" +
                "    var y = [2 m];\n" +
                "    var x = [1 m];\n" +
                "\n" +
                "    if(x != y)\n" +
                "    {\n" +
                "        return 1;\n" +
                "    };\n" +
                "    return 0;\n" +
                "}\n");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(1, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void UnitComparisonFailTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main()\n" +
                "{\n" +
                "    def unit kg;\n" +
                "    def unit m;\n" +
                "    var y = [1 m];\n" +
                "    var x = [1 kg];\n" +
                "\n" +
                "    if(x == y)\n" +
                "    {\n" +
                "        return 1;\n" +
                "    };\n" +
                "    return 0;\n" +
                "}\n");
        String message = " ";
        try {
            Interpreter visitor = new Interpreter(parser.program());
            Object result = visitor.run();
        }
        catch (InterpreterException e) {
            message = e.toString();
        }
        assertEquals("Cannot compare units of two different types at line: 4 row: 11", message);
    }

    @Test
    public void UnitAdditionTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main()\n" +
                "{\n" +
                "    def unit kg;\n" +
                "    def unit m;\n" +
                "    def unit s;\n" +
                "    def newton kg * m / s * s;\n" +
                "    var z = [1 newton];\n" +
                "    var w = [1 newton];\n" +
                "\n" +
                "    return w + z;\n" +
                "}\n" +
                "\n");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals(2, ((TreeNodeSub.Unit) result).getValue().getNumcontent());
        assertEquals("newton", ((TreeNodeSub.Unit) result).getUnitType().getContent());
    }

    @Test
    public void UnitAdditionFailTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main()\n" +
                "{\n" +
                "    def unit kg;\n" +
                "    def unit m;\n" +
                "    def unit s;\n" +
                "    def newton kg * m / s * s;\n" +
                "    var z = [1 kg];\n" +
                "    var w = [1 newton];\n" +
                "\n" +
                "    return w + z;\n" +
                "}\n" +
                "\n");
        String message = " ";
        try {
            Interpreter visitor = new Interpreter(parser.program());
            Object result = visitor.run();
        }
        catch (InterpreterException e) {
            message = e.toString();
        }
        assertEquals("Cannot add units of two different types at line: 5 row: 14", message);
    }

    @Test
    public void UnitSubtractTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main()\n" +
                "{\n" +
                "    def unit kg;\n" +
                "    def unit m;\n" +
                "    var z = [1 kg];\n" +
                "    var w = [1 kg];\n" +
                "\n" +
                "    return w - z;\n" +
                "}\n" +
                "\n");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals(0, ((TreeNodeSub.Unit) result).getValue().getNumcontent());
        assertEquals("kg", ((TreeNodeSub.Unit) result).getUnitType().getContent());
    }

    @Test
    public void UnitMultiplicationTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "def unit kg;" +
                "def unit m;" +
                "def unit s;" +
                "def newton kg * m / s * s;" +
                "var x = [1 kg];" +
                "var y = [1 s];" +
                "var z = [1 m];" +
                "x = x * z;" +
                "x = x / y;" +
                "x = x / y;" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals(1, ((TreeNodeSub.Unit) result).getValue().getNumcontent());
        assertEquals("newton", ((TreeNodeSub.Unit) result).getUnitType().getContent());
    }

    @Test
    public void UnitMultiplicationByNumTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "def unit kg;" +
                "var x = [1 kg];" +
                "var y = 2;" +
                "x = x * y;" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals(2, ((TreeNodeSub.Unit) result).getValue().getNumcontent());
        assertEquals("kg", ((TreeNodeSub.Unit) result).getUnitType().getContent());
    }

    @Test
    public void UnitDivisionByNumTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "def unit kg;" +
                "var x = [1 kg];" +
                "var y = 2;" +
                "x = x / y;" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals(0.5, ((TreeNodeSub.Unit) result).getValue().getNumcontent());
        assertEquals("kg", ((TreeNodeSub.Unit) result).getUnitType().getContent());
    }

    @Test
    public void ReturnToBasicTypeTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "def unit m;" +
                "def unit s;" +
                "def velocity m / s;" +
                "var x = [2 s];" +
                "var y = [2 velocity];" +
                "y = y * x;" +
                "return y;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals(4.0, ((TreeNodeSub.Unit) result).getValue().getNumcontent());
        assertEquals("m", ((TreeNodeSub.Unit) result).getUnitType().getContent());
    }

    @Test
    public void UnitDivisionTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "def unit kg;" +
                "def unit m;" +
                "def unit s;" +
                "def denominator / s;" +
                "def velocity m / s;" +
                "var x = [2 m];" +
                "var y = [2 denominator];" +
                "x = x * y;" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals(4, ((TreeNodeSub.Unit) result).getValue().getNumcontent());
        assertEquals("velocity", ((TreeNodeSub.Unit) result).getUnitType().getContent());
    }

    @Test
    public void UnitDivisionByMultiplicationTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "def unit kg;" +
                "def unit m;" +
                "def unit s;" +
                "def denominator / s;" +
                "def velocity m / s;" +
                "var x = [2 m];" +
                "var y = [2 denominator];" +
                "x = x * y;" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals(4, ((TreeNodeSub.Unit) result).getValue().getNumcontent());
        assertEquals("velocity", ((TreeNodeSub.Unit) result).getUnitType().getContent());
    }

    @Test
    public void UnitDefinitionShorteningFractions() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "def unit kg;" +
                "def unit m;" +
                "def unit s;" +
                "def newton kg * m * s * s / s * s * s * s;" +
                "var x = [1 kg];" +
                "var z = [1 m];" +
                "var y = [1 s];" +
                "y = y * y;" +
                "x = x * z;" +
                "x = x / y;" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals(1, ((TreeNodeSub.Unit) result).getValue().getNumcontent());
        assertEquals("newton", ((TreeNodeSub.Unit) result).getUnitType().getContent());
    }

    @Test
    public void ShortenFractionsTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "def unit kg;" +
                "def unit m;" +
                "def unit s;" +
                "def denominator / s;" +
                "def velocity m / s;" +
                "var x = [2 m];" +
                "var y = [2 denominator];" +
                "x = x * x;" +
                "x = x / x;" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals(1.0, ((TreeNodeSub.Unit) result).getValue().getNumcontent());
        assertEquals("unknown", ((TreeNodeSub.Unit) result).getUnitType().getContent());
    }

    @Test
    public void returnTest() throws InterpreterException, ParserException, LexerException, FileNotFoundException {
        Parser parser = new Parser("function main(){" +
                "return 5;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(5.0, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void VarDeclarationNumTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 5;" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(5.0, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void VarDeclarationUnitTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "def unit kg;" +
                "var x = [2 kg];" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals("kg", ((TreeNodeSub.Unit) result).getUnitType().getContent());
        assertEquals(2, ((TreeNodeSub.Unit) result).getValue().getNumcontent());
    }

    @Test
    public void VarDeclarationStringTest() throws InterpreterException, ParserException, LexerException, FileNotFoundException {
        Parser parser = new Parser("function main(){" +
                "var y = \"abc\";" +
                "var x = y;" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.StringVar);
        assertEquals("abc", ((TreeNodeSub.StringVar) result).getValue().getContent());
    }

    @Test
    public void VarDeclarationByFuncCallTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = test();" +
                "return x;" +
                "}" +
                "function test(){" +
                "return \"abc\";" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.StringVar);
        assertEquals("abc", ((TreeNodeSub.StringVar) result).getValue().getContent());
    }

    @Test
    public void SameVarDeclaration() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main()\n" +
                "{\n" +
                "    def unit kg;\n" +
                "    def unit m;\n" +
                "    var y = [1 m];\n" +
                "    var y = [1 kg];\n" +
                "    return 0;\n" +
                "}\n");
        String message = " ";
        try {
            Interpreter visitor = new Interpreter(parser.program());
            Object result = visitor.run();
        }
        catch (InterpreterException e) {
            message = e.toString();
        }
        assertEquals("Variable already declared in this scope line: 3 row: 9", message);
    }

    @Test
    public void AddingTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var y = 7.0;" +
                "var x = 5.0;" +
                "return x + y;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(12.0, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void AddingTestTer() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var y = 7;" +
                "var x = 5;" +
                "return x + y;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(12, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void AddingStringsTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = \"abc\";" +
                "var y = \"def\";" +
                "return x + y;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.StringVar);
        assertEquals("abcdef", ((TreeNodeSub.StringVar) result).getValue().getContent());
    }

    @Test
    public void MultiplicationTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var y = 7.0;" +
                "var x = 5.0;" +
                "return x * y;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(35.0, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void MultiplicationPriorityTestBis() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var y = 7;" +
                "var x = 5;" +
                "return x + x * y - y;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(33.0, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void BracketsPriorityTestTer() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var y = 7.0;" +
                "var x = 5.0;" +
                "return (x + x) * y - y;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(63.0, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void DividingTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var y = 7.0;" +
                "var x = 5.0;" +
                "return y / x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(1.4, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void IfStatementTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 5;" +
                "if(x == 5){" +
                "x = 3;" +
                "};" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(3, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void IfStatementTestBis() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 5;" +
                "if(x > 10){" +
                "x = 3;" +
                "};" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(5, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void IfStatementTestTer() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 5;" +
                "if(x < 10){" +
                "x = 3;" +
                "};" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(3, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void IfStatementTestQuater() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 5;" +
                "if(x <= 5){" +
                "x = 3;" +
                "};" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(3, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void ElseTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 7;" +
                "if(x <= 5){" +
                "x = 3;" +
                "}" +
                "else {" +
                "x = 10;" +
                "};" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(10, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void AndOpTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 7;" +
                "if(x <= 5 && x > 10) {" +
                "x = 3;" +
                "}" +
                "else {" +
                "x = 10;" +
                "};" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(10.0, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void AndOpTestBis() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 7;" +
                "if(x >= 5 && x < 10){" +
                "x = 3;" +
                "}" +
                "else {" +
                "x = 10;" +
                "};" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(3, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void OrOpTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 12;" +
                "if(x <= 5 || x > 10){" +
                "x = 3;" +
                "}" +
                "else {" +
                "x = 10;" +
                "};" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(3, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void OrOpTestBis() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 7;" +
                "if(x <= 5 || x > 10){" +
                "x = 3;" +
                "}" +
                "else {" +
                "x = 10;" +
                "};" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(10, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void WhileTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 7;" +
                "var y = 0;" +
                "while(x <= 12){" +
                "x = x + 1;" +
                "y = y + x;" +
                "};" +
                "return y;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(63, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void WhileTestBis() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 7;" +
                "var y = 0;" +
                "while(x - 5 <= 12){" +
                "x = x + 1;" +
                "y = y + x;" +
                "};" +
                "return y;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(143, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void WhileTestTer() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 7;" +
                "var y = \"a\";" +
                "while(x < 10){" +
                "x = x + 1;" +
                "y = y + y;" +
                "};" +
                "return y;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.StringVar);
        assertEquals("aaaaaaaa", ((TreeNodeSub.StringVar) result).getValue().getContent());
    }

    @Test
    public void ScopeTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 7;" +
                "var y = 13;" +
                "while(x < 10){" +
                "x = x + 1;" +
                "var y = 3;" +
                "};" +
                "return y;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(13, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void FunctionsTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var y = 3;" +
                "var x = foo(y);" +
                "return x;" +
                "}" +
                "function foo(temp){" +
                "temp = temp * 2;" +
                "return temp;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(6, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void FunctionsTestBis() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 3;" +
                "var y = 4;" +
                "x = foo(x, y);" +
                "return x;" +
                "}" +
                "function foo(a, b){" +
                "a = a * b;" +
                "return a;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(12, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void FunctionsScopeTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 5;" +
                "var y = 4;" +
                "x = foo(x, y);" +
                "return x;" +
                "}" +
                "function foo(a, b){" +
                "var x = a * b;" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(20.0, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void FunctionsWhileScopeTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main()\n" +
                "{\n" +
                "    var x = 1;\n" +
                "    var y = 3;\n" +
                "    while(x < 3)\n" +
                "    {\n" +
                "        var y = 5;\n" +
                "        return y;\n" +
                "        x = x + 1;\n" +
                "    };\n" +
                "\n" +
                "    return y;\n" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(5, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void NoMainTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function notmain(){" +
                "return 5;" +
                "}");
        String message = " ";
        try {
            Interpreter visitor = new Interpreter(parser.program());
            Object result = visitor.run();
        }
        catch (InterpreterException e) {
            message = e.toString();
        }
        assertEquals("Function main not declared!", message);
    }

    @Test
    public void NoFunctionTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 5;" +
                "var y = 4;" +
                "var x = foo(x, y);" +
                "return x;" +
                "}");
        String message = " ";
        try {
            Interpreter visitor = new Interpreter(parser.program());
            Object result = visitor.run();
        }
        catch (InterpreterException e) {
            message = e.toString();
        }
        assertEquals("Function foo not declared!", message);
    }

    @Test
    public void UndeclaredVariableTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 5;" +
                "var y = 4;" +
                "z = x + y;" +
                "return z;" +
                "}");
        String message = " ";
        try {
            Interpreter visitor = new Interpreter(parser.program());
            Object result = visitor.run();
        }
        catch (InterpreterException e) {
            message = e.toString();
        }
        assertEquals("Variable z is not declared in this scope", message);
    }

    @Test
    public void WrongAdditionTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 5;\n" +
                "var y = \"a\";\n" +
                "var z = x + y;\n" +
                "return z;\n" +
                "}");
        String message = " ";
        try {
            Interpreter visitor = new Interpreter(parser.program());
            Object result = visitor.run();
        }
        catch (InterpreterException e) {
            message = e.toString();
        }
        assertEquals("Forbidden operation at line: 2 row: 11", message);
    }

    @Test
    public void RecursionTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function power(x, n)\n" +
                "{\n" +
                "    if(n == 1)\n" +
                "    {\n" +
                "        return x;\n" +
                "    };\n" +
                "    var differenece = n - 1;\n" +
                "    var s = power(x, differenece);\n" +
                "    return x * s;\n" +
                "}\n" +
                "function main()\n" +
                "{\n" +
                "    return power(2, 10);\n" +
                "}\n");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(1024, ((TreeNodeSub.Num) result).getValue().getNumcontent());
    }

    @Test
    public void PrintTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main()\n" +
                "{\n" +
                "    def unit kg;\n" +
                "    var x = [3 kg];\n" +
                "    print(x);\n" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Unit);
        assertEquals(result.toString(), "3.0 kg");
    }

    @Test
    public void PrintFunctionTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function power(x, n)\n" +
                "{\n" +
                "    if(n == 1)\n" +
                "    {\n" +
                "        return x;\n" +
                "    };\n" +
                "    var differenece = n - 1;\n" +
                "    var s = power(x, differenece);\n" +
                "    return x * s;\n" +
                "}\n" +
                "function main()\n" +
                "{\n" +
                "    print(power(2, 10));\n" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(result.toString(), "1024.0");
    }
}
