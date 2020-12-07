package com.lexer;
import java.io.FileNotFoundException;


public class Lexer {

    char character;
    char nextCharacter;
    boolean isEOF = false;
    int x = 0;
    int y = 1;
    CharLoader cl;
    TokenDictionairy tokenDictionairy = new TokenDictionairy();
    int counter = 0;

    public Lexer(String path, int i) throws FileNotFoundException {
        if(i == 0)
            cl = new CharLoader(path, i);
        else
            cl = new CharLoader(path);
    }

    public Token buildToken()
    {
        if(nextCharacter != '\0') // updating character with nextCharacter var - moving one character forwards
        {
            character = nextCharacter;
            nextCharacter = '\0';
        }

        else
        {
            //x++;
            character = cl.getNextSymbol();
        }

        if(isEOF(character))
        {
            isEOF = true;
            return new Token(TokenType.EOF, "EOF", x, y);
        }

        if(character == ' ') {
            x++;
            return buildToken();
        }

        else if(character == '\r' || character == '\n')
        {
            counter++;
            x = 0;
            if(counter == 2)
            {
                y++;
                counter = 0;
            }
            return buildToken();
        }

        if(character == '/')
            return commentSymbol();

        if(tokenDictionairy.getSingleSymbol(character) != null)
            return new Token(tokenDictionairy.getSingleSymbol(character), Character.toString(character), ++x, y);

        if(tokenDictionairy.getPrefixDoubleSymbol(character) != null)
            return doubleSymbolsIterator();

        if(character == '\"')
            return quotationTokenIterator();

        if(character == '0')
            return zeroTokenIterator();

        else if(Character.isDigit(character))
            return  digitTokenIterator();

        else if(Character.isLetter(character))
            return readKeyWordOrName();

        return new Token(TokenType.UNKNOWN, Character.toString(character), x, y);
    }

    public Token commentSymbol()
    {
        String word = "";
        word = word + character;
        nextCharacter = cl.getNextSymbol();
        word = word + nextCharacter;
        if(word.equals("//"))
        {
            while(character != '\r' && character != '\n' && !isEOF(character))
            {
                word = word + character;
                character = cl.getNextSymbol();
            }
            nextCharacter = character;
            return  buildToken();
        }
        else {
            return new Token(TokenType.DIVISION_OP, Character.toString(character), ++x, y);
        }
    }

    public Token doubleSymbolsIterator()
    {
        String word = "";
        word = word + character;
        nextCharacter = cl.getNextSymbol();
        word = word + nextCharacter;
        if(tokenDictionairy.getDoubleSymbol(word) != null)
        {
            nextCharacter = cl.getNextSymbol();
            x = x + 2;
            return new Token(tokenDictionairy.getDoubleSymbol(word), word, x, y);
        }

        return new Token(tokenDictionairy.getPrefixDoubleSymbol(character), Character.toString(character), ++x, y);
    }

    public Token quotationTokenIterator()
    {
        String word = "";
        TokenType tmp;

        tmp = TokenType.QUOTE;
        word = word + character;
        character = cl.getNextSymbol();

        while(character != '\"' && character != '\r' && character != '\n' && !isEOF(character))
        {
            word = word + character;
            character = cl.getNextSymbol();
        }

        if(character == '\"')
        {
            word = word + character;
            x = x + word.length();
            nextCharacter = cl.getNextSymbol();
        }
        else
        {
            x = x + word.length();
            nextCharacter = character;
            tmp = TokenType.UNKNOWN;
        }

        return new Token(tmp, word, x, y);
    }

    public Token zeroTokenIterator()
    {
        String word = "";
        TokenType tmp;

        tmp = TokenType.NUMBER;
        word = word + character; //adding 0 to token
        character = cl.getNextSymbol(); // reading next symbol
        if(character == '.')
        {
            word = word + character;
            character = cl.getNextSymbol();
            while(!isEOF(character) && Character.isDigit(character))
            {
                word = word + character;
                character = cl.getNextSymbol();
            }
            if(word.equals("0."))
                tmp = TokenType.UNKNOWN;
        }
        else // token == 0
        {
            x = x + word.length();
            nextCharacter = character;
            return new Token(tmp, word, x, y);
        }
        // double type token detected
        x = x + word.length();
        nextCharacter = character;
        return new Token(tmp, word, x, y);
    }

    public Token digitTokenIterator()
    {
        String word = "";
        TokenType tmp;
        int counter = 0;

        tmp = TokenType.NUMBER;
        while(!isEOF(character) && Character.isDigit(character))
        {
            word = word + character;
            character = cl.getNextSymbol();
        }
        if(character == '.')
        {
            word = word + character;
            character = cl.getNextSymbol();
            while(!isEOF(character) && Character.isDigit(character))
            {
                counter = 1;
                word = word + character;
                character = cl.getNextSymbol();
            }

            if(counter == 0)
                tmp = TokenType.UNKNOWN;
        }
        x = x + word.length();
        nextCharacter = character;
        return new Token(tmp, word, x, y);
    }

    public Token readKeyWordOrName()
    {
        String token = "";

        while (Character.isDigit(character) || Character.isLetter(character))
        {
            token = token + character;
            character = cl.getNextSymbol();
        }
        x = x + token.length();
        nextCharacter = character;
        if(tokenDictionairy.getKeyWord(token) != null)
            return new Token(tokenDictionairy.getKeyWord(token), token, x, y);
        else
            return new Token(TokenType.NAME, token, x, y);
    }

    public static boolean isEOF(char atom)
    {
        return atom == '\u001a';
    }
}
