package com.lexer;
import java.io.FileNotFoundException;
import java.util.EnumMap;
import java.util.HashMap;
import com.lexer.TokenType;

public class TokenDictionairy {

    HashMap<String, TokenType> singleSymbols = new HashMap<String, TokenType>();
    HashMap<String, TokenType> doubleSymbols = new HashMap<String, TokenType>();
    HashMap<String, TokenType> prefixDoubleSymbols = new HashMap<String, TokenType>();
    HashMap<String, TokenType> keyWords = new HashMap<String, TokenType>();

    public TokenDictionairy()
    {
        singleSymbols.put("+", TokenType.ADDITIVE_OP);
        singleSymbols.put("-", TokenType.ADDITIVE_OP);
        singleSymbols.put("*", TokenType.MULTIPLICATIVE_OP);
        singleSymbols.put("(", TokenType.LEFT_BRACKET);
        singleSymbols.put(")", TokenType.RIGHT_BRACKET);
        singleSymbols.put("{", TokenType.LEFT_BRACE);
        singleSymbols.put("}", TokenType.RIGHT_BRACE);
        singleSymbols.put("[", TokenType.LEFT_SQ_BRACKET);
        singleSymbols.put("]", TokenType.RIGHT_SQ_BRACKET);
        singleSymbols.put(";", TokenType.SEMICOLON);
        singleSymbols.put(",", TokenType.COMMA);

        doubleSymbols.put("!=", TokenType.NOT_EQUAL_OP);
        doubleSymbols.put("==", TokenType.EQUAL_OP);
        doubleSymbols.put(">=", TokenType.GREATER_EQUAL_OP);
        doubleSymbols.put("<=", TokenType.SMALLER_EQUAL_OP);
        doubleSymbols.put("||", TokenType.OR_OP);
        doubleSymbols.put("&&", TokenType.AND_OP);

        prefixDoubleSymbols.put("!", TokenType.NEGATION_OP);
        prefixDoubleSymbols.put("=", TokenType.ASSIGNMENT_OP);
        prefixDoubleSymbols.put(">", TokenType.GREATER__OP);
        prefixDoubleSymbols.put("<", TokenType.SMALLER__OP);
        prefixDoubleSymbols.put("|", TokenType.UNKNOWN);
        prefixDoubleSymbols.put("&", TokenType.UNKNOWN);
        //prefixDoubleSymbols.put("/", TokenType.DIVISION_OP);

        keyWords.put("function", TokenType.FUNCTION);
        keyWords.put("return", TokenType.RETURN);
        keyWords.put("while", TokenType.WHILE);
        keyWords.put("var", TokenType.VAR);
        keyWords.put("def", TokenType.DEF);
        keyWords.put("if", TokenType.IF);
        keyWords.put("else", TokenType.ELSE);
        keyWords.put("print", TokenType.PRINT);
        keyWords.put("unit", TokenType.UNIT);
    }



    public TokenType getSingleSymbol(char symbol)
    {
        String tmp = Character.toString(symbol);
        return singleSymbols.get(tmp);
    }

    public TokenType getPrefixDoubleSymbol(char symbol)
    {
        String tmp = Character.toString(symbol);
        return prefixDoubleSymbols.get(tmp);
    }

    public TokenType getKeyWord(String string)
    {
        return keyWords.get(string);
    }

    public TokenType getDoubleSymbol(String string)
    {
        return doubleSymbols.get(string);
    }
}
