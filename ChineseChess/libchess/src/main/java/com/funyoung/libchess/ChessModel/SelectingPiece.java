package com.funyoung.libchess.ChessModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectingPiece {
    private Piece selectedPieceKey;
    private List<int[]> nextMoveList;

    public boolean isSameKey(String key) {
        return null != selectedPieceKey && selectedPieceKey.key.equals(key);
    }

    public void clear() {
        selectedPieceKey = null;
    }

    public boolean hasSelection() {
        return selectedPieceKey != null;
    }

    public String getKey() {
        return hasSelection() ? selectedPieceKey.key : null;
    }

    public void select(Piece key, Board board) {
        selectedPieceKey = key;
        if (null == key) {
            nextMoveList = Collections.emptyList();
        } else {
            int[] selectedPiecePos = board.pieces.get(key.key).position;
            nextMoveList = Rules.getNextMove(key.key, selectedPiecePos, board);
        }
    }

    public boolean hasMovingTarget(int[] pos) {
        for (int[] each : nextMoveList) {
            if (Arrays.equals(each, pos)) {
                return true;
            }
        }

        return false;
    }
}
