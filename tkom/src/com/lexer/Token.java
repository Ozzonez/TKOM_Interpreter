package com.lexer;

public class Token {




    TokenType type;
    String content;
    int x, y;

    public Token(TokenType type, String content, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.type = type;
        this.content = content;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TokenType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    ;
    // cos jeszcze zapamietujace miejsce w pliku na przyszlosc????


}
