package com.funyoung.libchess.view;

public interface IGameView {
    void movePieceFromAI(String piece, int[] to);

    void postInvalidate();

    void showWin(char win);
}
