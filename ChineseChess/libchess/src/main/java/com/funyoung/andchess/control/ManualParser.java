package com.funyoung.andchess.control;

import com.funyoung.andchess.ChessModel.Manual;
import com.funyoung.andchess.ChessModel.Piece;

import java.util.HashMap;
import java.util.Map;

class ManualParser {
    public static Map<String, Piece> parse(Manual manual) throws Exception {
        Map<String, Piece> pieces = new HashMap<>(32);
        addBlackPieces(pieces, manual.getB());
        addRedPieces(pieces, manual.getR());
        return pieces;
    }

    private static void addRedPieces(Map<String, Piece> pieces, Manual.Side r) {
        convert(pieces, true, r);
    }

    private static void addBlackPieces(Map<String, Piece> pieces, Manual.Side b) {
        convert(pieces,false, b);
    }

    public static void convert(Map<String, Piece> pieces, boolean red, Manual.Side side) {
        addPiece(pieces, red, 'j', side.getJ());
        addPiece(pieces, red, 'm', side.getM());
        addPiece(pieces, red, 'x', side.getX());
        addPiece(pieces, red, 's', side.getS());
        addPiece(pieces, red, 'b', side.getB());
        addPiece(pieces, red, 'p', side.getP());
        addPiece(pieces, red, 'z', side.getZ());
    }

    protected static void addPiece(Map<String, Piece> pieces, boolean red, char character, String loc) {
        if (null != loc && loc.length() % 2 == 0) {
            char[] a = loc.toCharArray();
            for (int i = 0; i < a.length / 2; i++) {
                int row = a[2 * i] - '0';
                int col = a[2 * i + 1] - '0';
                addPiece(pieces, red, character, i, row, col);
            }
        }
    }

    protected static void addPiece(Map<String, Piece> pieces, boolean red,
                                   char character, int index, int row, int col) {
        Piece piece = new Piece(red, character, index, row, col);
        pieces.put(piece.key, piece);
    }
}
