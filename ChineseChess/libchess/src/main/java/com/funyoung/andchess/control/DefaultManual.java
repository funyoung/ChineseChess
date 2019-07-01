package com.funyoung.andchess.control;

import com.funyoung.andchess.ChessModel.Piece;

import java.util.HashMap;
import java.util.Map;

class DefaultManual {
    /** 初始化全局棋子图片和位置 */
    public static Map<String, Piece> initPieces() {
        Map<String, Piece> pieces = new HashMap<>();
        addBlackPieces(pieces);
        addRedPieces(pieces);
        return pieces;
    }

    private static void addRedPieces(Map<String, Piece> pieces) {
        addPiece(pieces, true, 'j', 0, 9, 0);
        addPiece(pieces, true, 'j', 1, 9, 8);

        addPiece(pieces, true, 'm', 0, 9, 1);
        addPiece(pieces, true, 'm', 1, 9, 7);

        addPiece(pieces, true, 'x', 0, 9, 2);
        addPiece(pieces, true, 'x', 1, 9, 6);

        addPiece(pieces, true, 's', 0, 9, 3);
        addPiece(pieces, true, 's', 1, 9, 5);

        addPiece(pieces, true, 'b', 0, 9, 4);

        addPiece(pieces, true, 'p', 0, 7, 1);
        addPiece(pieces, true, 'p', 1, 7, 7);

        addPiece(pieces, true, 'z', 0, 6, 0);
        addPiece(pieces, true, 'z', 1, 6, 2);
        addPiece(pieces, true, 'z', 2, 6, 4);
        addPiece(pieces, true, 'z', 3, 6, 6);
        addPiece(pieces, true, 'z', 4, 6, 8);
    }

    private static void addBlackPieces(Map<String, Piece> pieces) {
        addPiece(pieces, false, 'j', 0, 0, 0);
        addPiece(pieces, false, 'j', 1, 0, 8);

        addPiece(pieces, false, 'm', 0, 0, 1);
        addPiece(pieces, false, 'm', 1, 0, 7);

        addPiece(pieces, false, 'x', 0, 0, 2);
        addPiece(pieces, false, 'x', 1, 0, 6);

        addPiece(pieces, false, 's', 0, 0, 3);
        addPiece(pieces, false, 's', 1, 0, 5);

        addPiece(pieces, false, 'b', 0, 0, 4);
        addPiece(pieces, false, 'p', 0, 2, 1);
        addPiece(pieces, false, 'p', 1, 2, 7);

        addPiece(pieces, false, 'z', 0, 3, 0);
        addPiece(pieces, false, 'z', 1, 3, 2);
        addPiece(pieces, false, 'z', 2, 3, 4);
        addPiece(pieces, false, 'z', 3, 3, 6);
        addPiece(pieces, false, 'z', 4, 3, 8);
    }

    protected static void addPiece(Map<String, Piece> pieces, boolean red,
                                   char character, int index, int row, int col) {
        ManualParser.addPiece(pieces, red, character, index, row, col);
    }
}
