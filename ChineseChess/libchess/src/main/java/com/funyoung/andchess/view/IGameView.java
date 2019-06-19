package com.funyoung.andchess.view;

public interface IGameView {
    void movePieceFromAI(String piece, int[] to);
    void postInvalidate();
}
