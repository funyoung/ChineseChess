package com.funyoung.andchess.view;

import com.funyoung.andchess.ChessModel.Board;
import com.funyoung.andchess.control.GameController;

public interface IGameView {
    void movePieceFromAI(String piece, int[] to);
    void postInvalidate();
    void setup(GameController gameController, Board board);
}
