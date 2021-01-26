package com.tests.Lexer;

import com.lexer.Lexer;
import com.lexer.LexerException;
import com.lexer.Token;
import com.lexer.TokenType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {
    @Test
    void exceptionsDot() throws Exception, LexerException {
        Lexer lexer = new Lexer(".2");
        Exception exception = assertThrows(LexerException.class, lexer::buildToken);
        assertEquals("Unknown token", exception.getMessage());
    }

    @Test
    void exceptionsNumDotSign() throws Exception, LexerException {
        Lexer lexer = new Lexer("0.a");
        Exception exception = assertThrows(LexerException.class, lexer::buildToken);
        assertEquals("Unknown token", exception.getMessage());
    }

    @Test
    void exceptionsSignDotNum() throws Exception, LexerException {
        Lexer lexer = new Lexer("a.0");
        Token token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("a", token.getContent());

        Exception exception = assertThrows(LexerException.class, lexer::buildToken);
        assertEquals("Unknown token", exception.getMessage());
    }

    @Test
    void exceptionsUnknownSign() throws Exception {
        Lexer lexer = new Lexer("fu?nc");
        Token token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("fu", token.getContent());

        Exception exception = assertThrows(LexerException.class, lexer::buildToken);
        assertEquals("Unknown token", exception.getMessage());
    }

    //todo testy na nielegalne pojedyncze znaki
//    //        assertEquals(TokenType.UNKNOWN, token.getType());
////        assertEquals("|", token.getContent());
//    token = lexer.buildToken();
//    assertEquals(TokenType.ASSIGNMENT_OP, token.getType());
//    assertEquals("=", token.getContent());
//    token = lexer.buildToken();
//    assertEquals(TokenType.NEGATION_OP, token.getType());
//    assertEquals("!", token.getContent());
//    token = lexer.buildToken();
////        assertEquals(TokenType.UNKNOWN, token.getType());
////        assertEquals("&", token.getContent());

    @Test
    void decimalFractionsTests() throws Exception {
        Lexer lexer = new Lexer("2 0.2 0.234 0.23a 00.3 23434.23123");
        Token token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(2, token.getNumcontent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(0.2, token.getNumcontent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(0.234, token.getNumcontent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(0.23, token.getNumcontent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("a", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(0, token.getNumcontent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(0.3, token.getNumcontent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(23434.23123, token.getNumcontent());
    }
    @Test
    void numbersAndNames() throws Exception {
        Lexer lexer = new Lexer("0023 func2 2func 4532 func fu!nc ");
        Token token = lexer.buildToken();
        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(0, token.getNumcontent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(0, token.getNumcontent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(23, token.getNumcontent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("func2", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(2, token.getNumcontent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("func", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(4532, token.getNumcontent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("func", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("fu", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NEGATION_OP);
        assertEquals("!", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("nc", token.getContent());
    }
    @Test
    void functionKeyWord() throws Exception {
        Lexer lexer = new Lexer("function");
        Token token = lexer.buildToken();
        assertEquals(TokenType.FUNCTION, token.getType());
        assertEquals("function", token.getContent());
    }

    @Test
    void returnKeyWord() throws Exception {
        Lexer lexer = new Lexer("return");
        Token token = lexer.buildToken();
        assertEquals(TokenType.RETURN, token.getType());
        assertEquals("return", token.getContent());
    }

    @Test
    void verKeyWord() throws Exception {
        Lexer lexer = new Lexer("var");
        Token token = lexer.buildToken();
        assertEquals(TokenType.VAR, token.getType());
        assertEquals("var", token.getContent());
    }

    @Test
    void whileKeyWord() throws Exception {
        Lexer lexer = new Lexer("while");
        Token token = lexer.buildToken();
        assertEquals(TokenType.WHILE, token.getType());
        assertEquals("while", token.getContent());
    }

    @Test
    void defKeyWord() throws Exception {
        Lexer lexer = new Lexer("def");
        Token token = lexer.buildToken();
        assertEquals(TokenType.DEF, token.getType());
        assertEquals("def", token.getContent());
    }

    @Test
    void ifKeyWord() throws Exception {
        Lexer lexer = new Lexer("if");
        Token token = lexer.buildToken();
        assertEquals(TokenType.IF, token.getType());
        assertEquals("if", token.getContent());
    }

    @Test
    void elseKeyWord() throws Exception {
        Lexer lexer = new Lexer("else");
        Token token = lexer.buildToken();
        assertEquals(TokenType.ELSE, token.getType());
        assertEquals("else", token.getContent());
    }

    @Test
    void printKeyWord() throws Exception {
        Lexer lexer = new Lexer("print");
        Token token = lexer.buildToken();
        assertEquals(TokenType.PRINT, token.getType());
        assertEquals("print", token.getContent());
    }

    @Test
    void unitKeyWord() throws Exception {
        Lexer lexer = new Lexer("unit");
        Token token = lexer.buildToken();
        assertEquals(TokenType.UNIT, token.getType());
        assertEquals("unit", token.getContent());
    }

    @Test
    void singleSymbols() throws Exception {
        Lexer lexer = new Lexer("*/[}+-(){];,><a=!");
        Token token = lexer.buildToken();
        assertEquals(TokenType.MULTIPLICATIVE_OP, token.getType());
        assertEquals("*", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.DIVISION_OP, token.getType());
        assertEquals("/", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.LEFT_SQ_BRACKET, token.getType());
        assertEquals("[", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.RIGHT_BRACE, token.getType());
        assertEquals("}", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.ADDITIVE_OP, token.getType());
        assertEquals("+", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.ADDITIVE_OP, token.getType());
        assertEquals("-", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getType());
        assertEquals("(", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getType());
        assertEquals(")", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.LEFT_BRACE, token.getType());
        assertEquals("{", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.RIGHT_SQ_BRACKET, token.getType());
        assertEquals("]", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.SEMICOLON, token.getType());
        assertEquals(";", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.COMMA, token.getType());
        assertEquals(",", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.GREATER__OP, token.getType());
        assertEquals(">", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.SMALLER__OP, token.getType());
        assertEquals("<", token.getContent());
        token = lexer.buildToken();
        token = lexer.buildToken();
        assertEquals(TokenType.ASSIGNMENT_OP, token.getType());
        assertEquals("=", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.NEGATION_OP, token.getType());
        assertEquals("!", token.getContent());
    }
    @Test
    void doubleSymbols() throws Exception {
        Lexer lexer = new Lexer("!===>= <= ||&&");
        Token token = lexer.buildToken();
        assertEquals(TokenType.NOT_EQUAL_OP, token.getType());
        assertEquals("!=", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.EQUAL_OP, token.getType());
        assertEquals("==", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.GREATER_EQUAL_OP, token.getType());
        assertEquals(">=", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.SMALLER_EQUAL_OP, token.getType());
        assertEquals("<=", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.OR_OP, token.getType());
        assertEquals("||", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.AND_OP, token.getType());
        assertEquals("&&", token.getContent());
    }
    @Test
    void position() throws Exception {
        Lexer lexer = new Lexer("&  0 \r \n& &");
        Token token = lexer.buildToken();
        assertEquals(1, token.getX());
        assertEquals(1, token.getY());
        token = lexer.buildToken();
        assertEquals(4, token.getX());
        assertEquals(1, token.getY());
        token = lexer.buildToken();
        assertEquals(1, token.getX());
        assertEquals(2, token.getY());
        token = lexer.buildToken();
        assertEquals(3, token.getX());
        assertEquals(2, token.getY());
    }
    @Test
    void stringLoading() throws Exception {
        Lexer lexer = new Lexer("\"test test \"");
        Token token = lexer.buildToken();
        assertEquals(TokenType.QUOTE, token.getType());
        assertEquals("\"test test \"", token.getContent());

        Lexer lexer2 = new Lexer("\"test test \r \n test");
        Exception exception = assertThrows(LexerException.class, lexer2::buildToken);
        assertEquals("Illegal line end is string literal", exception.getMessage());
    }
    @Test
    void comments() throws Exception {
        Lexer lexer = new Lexer("code // comment \r \n codeCont");
        Token token = lexer.buildToken();
        assertEquals(TokenType.NAME, token.getType());
        assertEquals("code", token.getContent());

        token = lexer.buildToken();
        assertEquals(TokenType.NAME, token.getType());
        assertEquals("codeCont", token.getContent());
    }
    @Test
    void EOF() throws Exception {
        Lexer lexer = new Lexer("test ");
        Token token = lexer.buildToken();
        token = lexer.buildToken();
        assertEquals(TokenType.EOF, token.getType());
        assertEquals("EOF", token.getContent());
    }
//    @Test
//    void xdF() throws Exception {
//        Lexer lexer = new Lexer("x > 30){");
//        Token token = lexer.buildToken();
//        token = lexer.buildToken();
//        assertEquals(TokenType.NAME, token.getType());
//        assertEquals("x", token.getContent());
//
//        token = lexer.buildToken();
//        assertEquals(TokenType.GREATER__OP, token.getType());
//        assertEquals(">", token.getContent());
//
//        token = lexer.buildToken();
//        assertEquals(TokenType.NUMBER, token.getType());
//        assertEquals("30", token.getContent());
//
//        token = lexer.buildToken();
//        assertEquals(TokenType.RIGHT_BRACKET, token.getType());
//        assertEquals(")", token.getContent());
//
//        token = lexer.buildToken();
//        assertEquals(TokenType.LEFT_SQ_BRACKET, token.getType());
//        assertEquals("{", token.getContent());
//    }
}