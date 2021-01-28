package com.parser;
import com.lexer.Lexer;
import com.lexer.LexerException;
import com.lexer.Token;
import com.lexer.TokenType;
import com.parser.TreeNodeSub.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {

    Lexer lexer;
    Token presentToken;

    public Parser(File file) throws FileNotFoundException, LexerException {
        lexer = new Lexer(file);
        presentToken = lexer.buildToken();
    }

    public Parser(String program) throws FileNotFoundException, LexerException {
        lexer = new Lexer(program);
        presentToken = lexer.buildToken();
    }

    public TreeNode program() throws ParserException, LexerException {
        TreeNode program = new Program();
        while (presentToken.getType() != TokenType.EOF) {
            ((Program)program).addFunction(TryParseFunction());
        }
        return program;
    }

    private void proceed(TokenType type) throws ParserException, LexerException {
        if (presentToken.getType() == type) {
            presentToken = lexer.buildToken();
            return;
        }

        throw new ParserException("expected: " + type + "but got:" + presentToken.getType() + "at" + presentToken.getX() + ", " + presentToken.getY());
    }

    private TreeNode TryParseFunction() throws ParserException, LexerException {
        Token token = presentToken;
        if (token.getType() == TokenType.FUNCTION)
            proceed(token.getType());
        else {
            throw new ParserException("expected: " + TokenType.FUNCTION + "but got:" + presentToken.getType() + "at" + presentToken.getX() + ", " + presentToken.getY());
        }

        Token name = presentToken;
        proceed(TokenType.NAME);
        TreeNode parameters = parameters();
        TreeNode functionBlock = TryParseFunctionBlock();

        return new FunctionDef(name, parameters, functionBlock);
    }

    private TreeNode parameters() throws ParserException, LexerException {
        proceed(TokenType.LEFT_BRACKET);
        if (presentToken.getType() == TokenType.RIGHT_BRACKET) {
            proceed(TokenType.RIGHT_BRACKET);
            return null;
        }

        Token token = presentToken;
        proceed(TokenType.NAME);
        ArrayList<TreeNode> parameters = new ArrayList<TreeNode>();
        parameters.add(new Variable(token));

        while (presentToken.getType() == TokenType.COMMA) {
            proceed(TokenType.COMMA);
            token = presentToken;
            if (token.getType() == TokenType.NAME) {
                parameters.add(new Variable(presentToken));
                proceed(TokenType.NAME);
            } else {
                throw new ParserException("expected: " + TokenType.FUNCTION + "but got:" + presentToken.getType() + "at" + presentToken.getX() + ", " + presentToken.getY());
            }
        }
        proceed(TokenType.RIGHT_BRACKET);
        return new Parameters(parameters);
    }

    private TreeNode TryParseFunctionBlock() throws ParserException, LexerException {
        proceed(TokenType.LEFT_BRACE);
        ArrayList<TreeNode> statements = statements();
        proceed(TokenType.RIGHT_BRACE);
        return new FunctionBlock(statements);
    }

    private ArrayList<TreeNode> statements() throws ParserException, LexerException {
        ArrayList<TreeNode> statements = new ArrayList<>();

        while (presentToken.getType() != TokenType.RIGHT_BRACE) {
            statements.add(TryParseStatement());
            proceed(TokenType.SEMICOLON);
        }

        return statements;
    }

    private TreeNode TryParseStatement() throws ParserException, LexerException {
        TreeNode statement;
        Token name;

        if ((statement = TryParseFunctionCall()) != null) {
            return statement;
        }
        if ((statement = TryParseAssignmentStatement()) != null) {
            return statement;
        } else if ((statement = TryParseVarDeclarationStatement()) != null) {
            return statement;
        } else if ((statement = TryParseIfDeclarationStatement()) != null) {
            return statement;
        } else if ((statement = TryParseWhileStatement()) != null) {
            return statement;
        } else if ((statement = TryParseReturnStatement()) != null) {
            return statement;
        } else if ((statement = TryParsePrintStatement()) != null) {
            return statement;
        } else if ((statement = TryParseDefBasicUnit()) != null) {
            return statement;
        }
        return null;
    }

    private TreeNode TryParseDefBasicUnit() throws ParserException, LexerException {
        if (presentToken.getType() != TokenType.DEF)
            return null;
        Token unitType;
        proceed(TokenType.DEF);
        if (presentToken.getType() == TokenType.UNIT)
        {
            proceed(TokenType.UNIT);
            unitType = presentToken;
            proceed(TokenType.NAME);

            return new UnitBasicType(unitType);
        }
        return TryParseMultiplicativeFormula();
    }

    private TreeNode TryParsePrintStatement() throws ParserException, LexerException {
        if (presentToken.getType() != TokenType.PRINT)
            return null;
        proceed(TokenType.PRINT);

        TreeNode content = TryParseAdditiveExpression();

        return new PrintStatement(content);
    }

    private TreeNode TryParseFunctionCall() throws ParserException, LexerException {
        if (presentToken.getType() != TokenType.NAME || lexer.peekNextCharacter() != '(')
            return null;

        Token funcName = presentToken;
        proceed(TokenType.NAME);
        proceed(TokenType.LEFT_BRACKET);
        ArrayList<TreeNode> arguments = new ArrayList<TreeNode>();

        if (presentToken.getType() == TokenType.RIGHT_BRACKET) {
            proceed(TokenType.RIGHT_BRACKET);
            return new FunctionCall(funcName, arguments);
        }

        arguments.add(TryParseAdditiveExpression());

        while (presentToken.getType() == TokenType.COMMA) {
            proceed(TokenType.COMMA);
            arguments.add(TryParseAdditiveExpression());
        }

        proceed(TokenType.RIGHT_BRACKET);
        return new FunctionCall(funcName, arguments);
    }


    private TreeNode TryParseAssignmentStatement() throws ParserException, LexerException {
        if (presentToken.getType() != TokenType.NAME)
            return null;

        Token name = presentToken;

        TreeNode var = TryParseVariable();
        proceed(TokenType.ASSIGNMENT_OP);
        TreeNode additiveExp = TryParseAdditiveExpression();

        return new AssignStatement(var, additiveExp);
    }

    private TreeNode TryParseVarDeclarationStatement() throws ParserException, LexerException {
        if (presentToken.getType() != TokenType.VAR)
            return null;

        TreeNode additiveExp = null;

        proceed(TokenType.VAR);
        TreeNode name = TryParseVariable();

        if (presentToken.getType() == TokenType.ASSIGNMENT_OP) {
            proceed(TokenType.ASSIGNMENT_OP);
            additiveExp = TryParseAdditiveExpression();
        }

        return new VarDeclaration(name, additiveExp);
    }

    private TreeNode TryParseIfDeclarationStatement() throws ParserException, LexerException {
        if (presentToken.getType() != TokenType.IF)
            return null;

        TreeNode condition, instructionsBlockIfTrue, instructionsBlockIfFalse = null;
        proceed(TokenType.IF);
        proceed(TokenType.LEFT_BRACKET);

        condition =tryParseCondition();
        proceed(TokenType.RIGHT_BRACKET);
        instructionsBlockIfTrue = TryParseFunctionBlock();
        instructionsBlockIfFalse = null;
        if (presentToken.getType() == TokenType.ELSE) {
            proceed(TokenType.ELSE);
            instructionsBlockIfFalse = TryParseFunctionBlock();
        }
        return new IfStatement(condition, instructionsBlockIfTrue, instructionsBlockIfFalse);
    }

    private TreeNode TryParseWhileStatement() throws ParserException, LexerException {
        if (presentToken.getType() != TokenType.WHILE)
            return null;

        TreeNode condition, whileBody = null;

        proceed(TokenType.WHILE);
        proceed(TokenType.LEFT_BRACKET);
        condition =tryParseCondition();
        proceed(TokenType.RIGHT_BRACKET);
        whileBody = TryParseFunctionBlock();
        return new WhileStatement(condition, whileBody);
    }

    private TreeNode TryParseReturnStatement() throws ParserException, LexerException {
        if (presentToken.getType() != TokenType.RETURN)
            return null;

        proceed(TokenType.RETURN);

        return new ReturnStatement(TryParseAdditiveExpression());
    }

    private TreeNode tryParseCondition() throws ParserException, LexerException {
        TreeNode node = tryParseAndCondition();

        while (presentToken.getType() == TokenType.OR_OP) {
            Token token = presentToken;
            proceed(TokenType.OR_OP);
            node = new BinaryConditionOperator(node, token, tryParseAndCondition());
        }
        return node;
    }

    private TreeNode tryParseAndCondition() throws ParserException, LexerException {
        TreeNode node = tryParseEqualityCondition();

        while (presentToken.getType() == TokenType.AND_OP) {
            Token token = presentToken;
            proceed(TokenType.AND_OP);
            node = new BinaryConditionOperator(node, token, tryParseEqualityCondition());
        }
        return node;
    }

    private TreeNode tryParseEqualityCondition() throws ParserException, LexerException {
        TreeNode node = tryParseRelationalCondition();

        while (presentToken.getType() == TokenType.EQUAL_OP || presentToken.getType() == TokenType.NOT_EQUAL_OP) {
            Token token = presentToken;
            proceed(token.getType());
            node = new BinaryConditionOperator(node, token, tryParseRelationalCondition());
        }
        return node;
    }

    private TreeNode tryParseRelationalCondition() throws ParserException, LexerException {
        TreeNode node = TryParseMainCondition();

        while (presentToken.getType() == TokenType.GREATER__OP || presentToken.getType() == TokenType.SMALLER__OP || presentToken.getType() == TokenType.SMALLER_EQUAL_OP || presentToken.getType() == TokenType.GREATER_EQUAL_OP) {
            Token token = presentToken;
            proceed(token.getType());
            node = new BinaryConditionOperator(node, token, tryParseRelationalCondition());
        }
        return node;
    }

    private TreeNode TryParseMainCondition() throws ParserException, LexerException {
        Token token = presentToken;

        if (token.getType() == TokenType.LEFT_BRACKET) {
            proceed(TokenType.LEFT_BRACKET);
            TreeNode node =tryParseCondition();
            proceed(TokenType.RIGHT_BRACKET);
            return node;
        } else {
            TreeNode node = TryParseAdditiveExpression();
            return node;
        }
    }

    private TreeNode TryParseAdditiveExpression() throws ParserException, LexerException {
        TreeNode node = null;

        node = TryParseMultiplicativeExpression();

        while (presentToken.getType() == TokenType.ADDITIVE_OP || presentToken.getType() == TokenType.ADDITIVE_OP)
        {
            Token operator = presentToken;
            proceed(operator.getType());
            node = new BinOperator(node, operator, TryParseMultiplicativeExpression());
        }
        return node; // return whole additiveExpression
    }

    private TreeNode TryParseMultiplicativeExpression() throws ParserException, LexerException {
        TreeNode node = null;

        node = TryParseExpression();

        while (presentToken.getType() == TokenType.MULTIPLICATIVE_OP || presentToken.getType() == TokenType.DIVISION_OP)
        {
            Token operator = presentToken;
            proceed(operator.getType());
            node = new BinOperator(node, operator, TryParseExpression());
        }
        return node;
    }

    private TreeNode TryParseExpression() throws ParserException, LexerException { //todo check unary operation like -1 albo !a
        Token token = presentToken;
        TreeNode node = null;

        if ((node = tryParseUnit()) != null)
            return node;
        else if (presentToken.getType() == TokenType.NUMBER) {
            proceed(TokenType.NUMBER);
            return new Num(token);
        } else if(presentToken.getType() == TokenType.QUOTE){
            proceed(TokenType.QUOTE);
            return new StringVar(token);
        } else if (token.getType() == TokenType.LEFT_BRACKET) {
            proceed(TokenType.LEFT_BRACKET);
            node = TryParseAdditiveExpression();
            proceed(TokenType.RIGHT_BRACKET);
            return node;
        } else if ((node = TryParseFunctionCall()) != null) {
            return node;
        }
        return TryParseVariable();
    }

    private TreeNode tryParseUnit() throws ParserException, LexerException {
        if (presentToken.getType() != TokenType.LEFT_SQ_BRACKET)
            return null;

        proceed(TokenType.LEFT_SQ_BRACKET);
        Token value = presentToken;
        proceed(TokenType.NUMBER);
        Token unitType = presentToken;
        proceed(TokenType.NAME);
        proceed(TokenType.RIGHT_SQ_BRACKET);

        return new Unit(value, unitType);
    }

    private TreeNode TryParseVariable() throws ParserException, LexerException {
        TreeNode var = new Variable(presentToken);
        proceed(TokenType.NAME);
        return var;
    }

    private TreeNode TryParseMultiplicativeFormula() throws ParserException, LexerException { //tak naprawdę tryParseComplexType
        Token unitName = presentToken;
        proceed(TokenType.NAME);

        ArrayList<TreeNode> aboveLine = new ArrayList<>();
        ArrayList<TreeNode> belowLine = new ArrayList<>();
        TreeNode unit = null;
        if(presentToken.getType() == TokenType.DIVISION_OP)
        {
            proceed(TokenType.DIVISION_OP);
            unit = tryParseUnitBasicType();
                belowLine.add(unit);
            while (presentToken.getType() != TokenType.SEMICOLON) {
                proceed(TokenType.MULTIPLICATIVE_OP);
                unit = tryParseUnitBasicType();
                belowLine.add(unit);
            }
        }
        else
        {
            unit = tryParseUnitBasicType();
            aboveLine.add(unit);
            while(presentToken.getType() != TokenType.DIVISION_OP && presentToken.getType() != TokenType.SEMICOLON)
            {
                proceed(TokenType.MULTIPLICATIVE_OP);
                unit = tryParseUnitBasicType();
                aboveLine.add(unit);
            }
            if(presentToken.getType() != TokenType.SEMICOLON)
                proceed(TokenType.DIVISION_OP);
            if(presentToken.getType() != TokenType.SEMICOLON) {
                unit = tryParseUnitBasicType();
                belowLine.add(unit);
                while (presentToken.getType() != TokenType.SEMICOLON) {
                    proceed(TokenType.MULTIPLICATIVE_OP);
                    unit = tryParseUnitBasicType();
                    belowLine.add(unit);
                }
            }
        }
        return new UnitComplexType(unitName, aboveLine, belowLine);
    }
    private TreeNode tryParseUnitBasicType() throws ParserException, LexerException {
        TreeNode var = new UnitBasicType(presentToken);
        proceed(TokenType.NAME);
        return var;
    }
}