package com.tests;

import com.lexer.Lexer;
import com.lexer.Token;
import com.lexer.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {
    @Test
    void decimalFractionsTests() throws Exception {
        Lexer lexer = new Lexer(".2 0.2 0.234 0.a a.0 0.23a 00.3 23434.23123", 0);
        Token token = lexer.buildToken();
        assertEquals(token.getType(), TokenType.UNKNOWN);
        assertEquals(".", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("2", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("0.2", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("0.234", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.UNKNOWN);
        assertEquals("0.", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("a", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("a", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.UNKNOWN);
        assertEquals(".", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("0", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("0.23", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("a", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("0", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("0.3", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("23434.23123", token.getContent());
    }
    @Test
    void numbersAndNames() throws Exception {
        Lexer lexer = new Lexer("0023 func2 2func 4532 fu?nc fu!nc ", 0);
        Token token = lexer.buildToken();
        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("0", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("0", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("23", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("func2", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("2", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("func", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals("4532", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("fu", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.UNKNOWN);
        assertEquals("?", token.getContent());
        token = lexer.buildToken();

        assertEquals(token.getType(), TokenType.NAME);
        assertEquals("nc", token.getContent());
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
        Lexer lexer = new Lexer("function", 0);
        Token token = lexer.buildToken();
        assertEquals(TokenType.FUNCTION, token.getType());
        assertEquals("function", token.getContent());
    }

    @Test
    void returnKeyWord() throws Exception {
        Lexer lexer = new Lexer("return", 0);
        Token token = lexer.buildToken();
        assertEquals(TokenType.RETURN, token.getType());
        assertEquals("return", token.getContent());
    }

    @Test
    void verKeyWord() throws Exception {
        Lexer lexer = new Lexer("var", 0);
        Token token = lexer.buildToken();
        assertEquals(TokenType.VAR, token.getType());
        assertEquals("var", token.getContent());
    }

    @Test
    void whileKeyWord() throws Exception {
        Lexer lexer = new Lexer("while", 0);
        Token token = lexer.buildToken();
        assertEquals(TokenType.WHILE, token.getType());
        assertEquals("while", token.getContent());
    }

    @Test
    void defKeyWord() throws Exception {
        Lexer lexer = new Lexer("def", 0);
        Token token = lexer.buildToken();
        assertEquals(TokenType.DEF, token.getType());
        assertEquals("def", token.getContent());
    }

    @Test
    void ifKeyWord() throws Exception {
        Lexer lexer = new Lexer("if", 0);
        Token token = lexer.buildToken();
        assertEquals(TokenType.IF, token.getType());
        assertEquals("if", token.getContent());
    }

    @Test
    void elseKeyWord() throws Exception {
        Lexer lexer = new Lexer("else", 0);
        Token token = lexer.buildToken();
        assertEquals(TokenType.ELSE, token.getType());
        assertEquals("else", token.getContent());
    }

    @Test
    void printKeyWord() throws Exception {
        Lexer lexer = new Lexer("print", 0);
        Token token = lexer.buildToken();
        assertEquals(TokenType.PRINT, token.getType());
        assertEquals("print", token.getContent());
    }

    @Test
    void unitKeyWord() throws Exception {
        Lexer lexer = new Lexer("unit", 0);
        Token token = lexer.buildToken();
        assertEquals(TokenType.UNIT, token.getType());
        assertEquals("unit", token.getContent());
    }

    @Test
    void singleSymbols() throws Exception {
        Lexer lexer = new Lexer("*/[}+-(){];,><|=!&", 0);
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
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("|", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.ASSIGNMENT_OP, token.getType());
        assertEquals("=", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.NEGATION_OP, token.getType());
        assertEquals("!", token.getContent());
        token = lexer.buildToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("&", token.getContent());
    }
    @Test
    void doubleSymbols() throws Exception {
        Lexer lexer = new Lexer("!===>= <= ||&&", 0);
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
        Lexer lexer = new Lexer("&  0 \r \n& &", 0);
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
        Lexer lexer = new Lexer("\"test test \"", 0);
        Token token = lexer.buildToken();
        assertEquals(TokenType.QUOTE, token.getType());
        assertEquals("\"test test \"", token.getContent());

        Lexer lexer2 = new Lexer("\"test test \r \n test", 0);
        Token token2 = lexer2.buildToken();
        assertEquals(TokenType.UNKNOWN, token2.getType());
        assertEquals("\"test test ", token2.getContent());
    }
    @Test
    void comments() throws Exception {
        Lexer lexer = new Lexer("code // comment \r \n codeCont", 0);
        Token token = lexer.buildToken();
        assertEquals(TokenType.NAME, token.getType());
        assertEquals("code", token.getContent());

        token = lexer.buildToken();
        assertEquals(TokenType.NAME, token.getType());
        assertEquals("codeCont", token.getContent());
    }
    @Test
    void EOF() throws Exception {
        Lexer lexer = new Lexer("test ", 0);
        Token token = lexer.buildToken();
        token = lexer.buildToken();
        assertEquals(TokenType.EOF, token.getType());
        assertEquals("EOF", token.getContent());
    }
}