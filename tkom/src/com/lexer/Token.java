package com.lexer;

public class Token {

    TokenType type;
    String content;
    double Numcontent;
    int x, y;

    public Token(TokenType type, String content, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.type = type;
        this.content = content;
    }
    public Token(TokenType type, double Numcontent, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.type = type;
        this.Numcontent = Numcontent;
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

    public double getNumcontent() { return Numcontent; }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getLine() { return " line: " + y + " " + "row: " + x;}

    ;
    // cos jeszcze zapamietujace miejsce w pliku na przyszlosc????


}
