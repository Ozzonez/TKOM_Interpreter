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
    public void returnTest() throws InterpreterException, ParserException, LexerException, FileNotFoundException {
        Parser parser = new Parser("function main(){" +
                "return 5;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(5, ((TreeNodeSub.Num) result).getValue());
    }

    @Test
    public void VarDeclarationTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
        Parser parser = new Parser("function main(){" +
                "var x = 5;" +
                "return x;" +
                "}");
        Interpreter visitor = new Interpreter(parser.program());
        Object result = visitor.run();
        assertTrue(result instanceof TreeNodeSub.Num);
        assertEquals(5, ((TreeNodeSub.Num) result).getValue());
    }

//    @Test
//    public void VarDeclarationBisTest() throws InterpreterException, ParserException, FileNotFoundException, LexerException {
//        Parser parser = new Parser("function main(){" +
//                "def kg: [WEIGHT];" +
//                "var x = [2 kg];" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.UnitResult);
//        assertEquals("kg", ((TreeNodeSub.UnitResult) result).getName().getValue());
//        assertEquals(1, ((TreeNodeSub.UnitResult) result).getMultiplicity());
//        assertEquals(2, ((TreeNodeSub.UnitResult) result).getNumber());
//        assertEquals("kg", ((TreeNodeSub.UnitResult) result).getParentName().getValue());
//    }
//
//    @Test
//    public void VarDeclarationTerTest() throws InterpreterException, ParserException, LexerException, FileNotFoundException {
//        Parser parser = new Parser("function main(){" +
//                "var y = \"abc\";" +
//                "var x = y;" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.StringVar);
//        assertEquals("abc", ((TreeNodeSub.StringVar) result).getValue().getContent());
//    }
//
//    @Test
//    public void VarDeclarationQuaterTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = test();" +
//                "return x;" +
//                "}" +
//                "function test(){" +
//                "return \"abc\";" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.StringVar);
//        assertEquals("abc", ((TreeNodeSub.StringVar) result).getValue().getValue());
//    }
//
//    @Test
//    public void AddingTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var y = 7.0;" +
//                "var x = 5.0;" +
//                "return x + y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.DoubleNum);
//        assertEquals(12.0, ((TreeNodeSub.DoubleNum) result).getValue());
//    }
//
//    @Test
//    public void AddingTestBis() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var y = 7;" +
//                "var x = 5.0;" +
//                "return x + y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.DoubleNum);
//        assertEquals(12.0, ((TreeNodeSub.DoubleNum) result).getValue());
//    }
//
//    @Test
//    public void AddingTestTer() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var y = 7;" +
//                "var x = 5;" +
//                "return x + y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(12, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void AddingTestQuater() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = \"abc\";" +
//                "var y = \"def\";" +
//                "return x + y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.StringVar);
//        assertEquals("abcdef", ((TreeNodeSub.StringVar) result).getValue().getValue());
//    }
//
//    @Test
//    public void UnitAddingTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "def m: [DISTANCE];" +
//                "var x = [12 m];" +
//                "def km: 1000 m;" +
//                "var y = [2 km];" +
//                "return x + y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.UnitResult);
//        assertEquals(2012, ((TreeNodeSub.UnitResult) result).getNumber());
//        assertEquals("m", ((TreeNodeSub.UnitResult) result).getName().getValue());
//        assertEquals("m", ((TreeNodeSub.UnitResult) result).getParentName().getValue());
//        assertEquals(1, ((TreeNodeSub.UnitResult) result).getMultiplicity());
//    }
//
//    @Test
//    public void UnitAddingTestBis() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "def m: [DISTANCE];" +
//                "var x = [12 m];" +
//                "def km: 1000 m;" +
//                "var y = [2 km];" +
//                "return y + x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.UnitResult);
//        assertEquals(2.012, ((TreeNodeSub.UnitResult) result).getNumber());
//        assertEquals("km", ((TreeNodeSub.UnitResult) result).getName().getValue());
//        assertEquals("m", ((TreeNodeSub.UnitResult) result).getParentName().getValue());
//        assertEquals(1000, ((TreeNodeSub.UnitResult) result).getMultiplicity());
//    }
//
//    @Test
//    public void UnitSubstractTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "def m: [DISTANCE];" +
//                "var x = [1305 m];" +
//                "def km: 1000 m;" +
//                "var y = [2 km];" +
//                "return y - x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.UnitResult);
//        assertEquals(0.695, ((TreeNodeSub.UnitResult) result).getNumber());
//        assertEquals("km", ((TreeNodeSub.UnitResult) result).getName().getValue());
//        assertEquals("m", ((TreeNodeSub.UnitResult) result).getParentName().getValue());
//        assertEquals(1000, ((TreeNodeSub.UnitResult) result).getMultiplicity());
//    }
//
//    @Test
//    public void MultiplicatingTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var y = 7.0;" +
//                "var x = 5.0;" +
//                "return x * y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.DoubleNum);
//        assertEquals(35.0, ((TreeNodeSub.DoubleNum) result).getValue());
//    }
//
//    @Test
//    public void MultiplicatingTestBis() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var y = 7.0;" +
//                "var x = 5.0;" +
//                "return x + x * y - y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.DoubleNum);
//        assertEquals(33.0, ((TreeNodeSub.DoubleNum) result).getValue());
//    }
//
//    @Test
//    public void MultiplicatingTestTer() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var y = 7.0;" +
//                "var x = 5.0;" +
//                "return (x + x) * y - y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.DoubleNum);
//        assertEquals(63.0, ((TreeNodeSub.DoubleNum) result).getValue());
//    }
//
//    @Test
//    public void DividingTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var y = 7.0;" +
//                "var x = 5.0;" +
//                "return y / x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.DoubleNum);
//        assertEquals(1.4, ((TreeNodeSub.DoubleNum) result).getValue());
//    }
//
//    @Test
//    public void IfStatementTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 5;" +
//                "if(x == 5){" +
//                "x = 3;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(3, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void IfStatementTestBis() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 5;" +
//                "if(x > 10){" +
//                "x = 3;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(5, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void IfStatementTestTer() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 5;" +
//                "if(x < 10){" +
//                "x = 3;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(3, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void IfStatementTestQuater() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 5;" +
//                "if(x <= 5){" +
//                "x = 3;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(3, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void ElseTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 7;" +
//                "if(x <= 5){" +
//                "x = 3;" +
//                "}" +
//                "else {" +
//                "x = 10;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(10, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void AndOpTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 7;" +
//                "if(x <= 5 & x > 10){" +
//                "x = 3;" +
//                "}" +
//                "else {" +
//                "x = 10;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(10, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void AndOpTestBis() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 7;" +
//                "if(x >= 5 & x < 10){" +
//                "x = 3;" +
//                "}" +
//                "else {" +
//                "x = 10;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(3, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void OrOpTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 12;" +
//                "if(x <= 5 | x > 10){" +
//                "x = 3;" +
//                "}" +
//                "else {" +
//                "x = 10;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(3, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void OrOpTestBis() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 7;" +
//                "if(x <= 5 | x > 10){" +
//                "x = 3;" +
//                "}" +
//                "else {" +
//                "x = 10;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(10, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void UnitEqualityTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "def m: [DISTANCE];" +
//                "var x = [2000 m];" +
//                "def km: 1000 m;" +
//                "var y = [2 km];" +
//                "if(x == y){" +
//                "x = 3;" +
//                "}" +
//                "else {" +
//                "x = 10;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(3, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void UnitUnequalityTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "def m: [DISTANCE];" +
//                "var x = [2000 m];" +
//                "def km: 1000 m;" +
//                "var y = [20 km];" +
//                "if(x == y){" +
//                "x = 3;" +
//                "}" +
//                "else {" +
//                "x = 10;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(10, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void UnitComparisonTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "def m: [DISTANCE];" +
//                "var x = [2000 m];" +
//                "def km: 1000 m;" +
//                "var y = [20 km];" +
//                "if(x > y){" +
//                "x = 3;" +
//                "}" +
//                "else {" +
//                "x = 10;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(10, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void UnitComparisonTestBis() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "def m: [DISTANCE];" +
//                "var x = [2000 m];" +
//                "def km: 1000 m;" +
//                "var y = [20 km];" +
//                "if(x <= y){" +
//                "x = 3;" +
//                "}" +
//                "else {" +
//                "x = 10;" +
//                "};" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(3, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void WhileTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 7;" +
//                "var y = 0;" +
//                "while(x <= 12){" +
//                "x = x + 1;" +
//                "y = y + x;" +
//                "};" +
//                "return y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(63, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void WhileTestBis() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 7;" +
//                "var y = 0;" +
//                "while(x - 5 <= 12){" +
//                "x = x + 1;" +
//                "y = y + x;" +
//                "};" +
//                "return y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(143, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void WhileTestTer() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 7;" +
//                "var y = \"a\";" +
//                "while(x < 10){" +
//                "x = x + 1;" +
//                "y = y + y;" +
//                "};" +
//                "return y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.StringVar);
//        assertEquals("aaaaaaaa", ((TreeNodeSub.StringVar) result).getValue().getValue());
//    }
//
//    @Test
//    public void ScopeTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 7;" +
//                "var y = 13;" +
//                "while(x < 10){" +
//                "x = x + 1;" +
//                "var y = 3;" +
//                "};" +
//                "return y;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(13, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void FunctionsTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var y = 3;" +
//                "var x = foo(y);" +
//                "return x;" +
//                "}" +
//                "function foo(temp){" +
//                "temp = temp * 2;" +
//                "return temp;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(6, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void FunctionsTestBis() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 3;" +
//                "var y = 4;" +
//                "var x = foo(x, y);" +
//                "return x;" +
//                "}" +
//                "function foo(a, b){" +
//                "a = a * b;" +
//                "return a;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(12, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    @Test
//    public void FunctionsTestTer() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "def m: [DISTANCE];" +
//                "var x = [1971 m];" +
//                "def km: 1000 m;" +
//                "var y = [1 km];" +
//                "var x = foo(x, y);" +
//                "return x;" +
//                "}" +
//                "function foo(a, b){" +
//                "var x = a + b;" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.UnitResult);
//        assertEquals(2971, ((TreeNodeSub.UnitResult) result).getNumber());
//        assertEquals("m", ((TreeNodeSub.UnitResult) result).getName().getValue());
//    }
//
//    @Test
//    public void FunctionsScopeTest() throws  InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 5;" +
//                "var y = 4;" +
//                "var x = foo(x, y);" +
//                "return x;" +
//                "}" +
//                "function foo(a, b){" +
//                "var x = a * b;" +
//                "return x;" +
//                "}");
//        Interpreter visitor = new Interpreter(parser.program());
//        Object result = visitor.run();
//        assertTrue(result instanceof TreeNodeSub.Num);
//        assertEquals(20, ((TreeNodeSub.Num) result).getValue());
//    }
//
//    // **************************************************************************************************************  negative tests
//
//    @Test
//    public void NoMainTest() throws InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function notmain(){" +
//                "return 5;" +
//                "}");
//        String message = " ";
//        try {
//            Interpreter visitor = new Interpreter(parser.program());
//            Object result = visitor.run();
//        }
//        catch (InterpreterException e) {
//            message = e.toString();
//        }
//        assertEquals("Function main not declared.", message);
//    }
//
//    @Test
//    public void NoFunctionTest() throws InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 5;" +
//                "var y = 4;" +
//                "var x = foo(x, y);" +
//                "return x;" +
//                "}");
//        String message = " ";
//        try {
//            Interpreter visitor = new Interpreter(parser.program());
//            Object result = visitor.run();
//        }
//        catch (InterpreterException e) {
//            message = e.toString();
//        }
//        assertEquals("Function foo not declared.", message);
//    }
//
//    @Test
//    public void UndeclaredVariableTest() throws InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 5;" +
//                "var y = 4;" +
//                "z = x + y;" +
//                "return z;" +
//                "}");
//        String message = " ";
//        try {
//            Interpreter visitor = new Interpreter(parser.program());
//            Object result = visitor.run();
//        }
//        catch (InterpreterException e) {
//            message = e.toString();
//        }
//        assertEquals("Variable z is not declared in this scope", message);
//    }
//
//    @Test
//    public void WrongAdditionTest() throws InterpreterException, ParserException
//    {
//        Parser parser = new Parser("function main(){" +
//                "var x = 5;\n" +
//                "var y = \"a\";\n" +
//                "z = x + y;\n" +
//                "return z;\n" +
//                "}");
//        String message = " ";
//        try {
//            Interpreter visitor = new Interpreter(parser.program());
//            Object result = visitor.run();
//        }
//        catch (InterpreterException e) {
//            message = e.toString();
//        }
//        assertEquals("Variable z is not declared in this scope", message);
//    }
}
