package com.funyoung.andchess.ChessModel;

/**
 */
public class Piece {
    public final String key;
    private final boolean red;
    private final char character;
    public int[] position;

    public Piece(boolean red, char character, int index, int row, int col) {
        this.key = (red ? "r" : "b") + character + index;
        this.red = red;
        this.character = character;
        this.position = new int[] { row, col };
    }

    public boolean isRed() {
        return red;
    }

    public boolean isColor(boolean player) {
        return red == player;
    }

    public char getCharacter() {
        return character;
    }
}
