package com.funyoung.andchess.ChessModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The Chinese Chess Board grid consist 10 horizontal and crossed with 9 vertical lines.
 * So it consists of 90 cell points, which is a crossed point between vertical and horizontal lines.
 */
public class Board {
    private final static int BOARD_COLUMN_COUNT = 9;
    private final static int BOARD_ROW_COUNT = 10;

    private final Piece[][] cells = new Piece[BOARD_ROW_COUNT][BOARD_COLUMN_COUNT];

    private final Map<String, Piece> pieces = new HashMap<>();
    private boolean isPlayer = true;

    public Board(Map<String, Piece> initPieces) {
        pieces.clear();
        if (null != initPieces) {
            pieces.putAll(initPieces);
        }
        for (Piece piece : pieces.values()) {
            update(piece);
        }
    }

    public static char hasWin(Board board) {
        /**
         * Judge has the game ended.
         * @return 'r' for RED wins, 'b' for BLACK wins, 'x' for game continues.
         * */
        if (board.isRedWin()) return 'r';
        else if (board.isBlackWin()) return 'b';
        else return 'x';
    }

    private boolean isBlackWin() {
        return pieces.get("rb0") == null;
    }

    private boolean isRedWin() {
        return pieces.get("bb0") == null;
    }

    public boolean isInside(int[] position) {
        return isInside(position[0], position[1]);
    }

    public boolean isInside(int x, int y) {
        return !(x < 0 || x >= BOARD_ROW_COUNT
                || y < 0 || y >= BOARD_COLUMN_COUNT);
    }

    public boolean isEmpty(int[] position) {
        return isEmpty(position[0], position[1]);
    }

    public boolean isEmpty(int x, int y) {
        /**越界或不存在*/
        return isInside(x, y) && cells[x][y] == null;
    }

    public Piece getPiece(int[] pos) {
        return getPiece(pos[0], pos[1]);
    }

    public Piece getPiece(int x, int y) {
        return cells[x][y];
    }

    /**
     * 位置更新
     */
    private boolean update(Piece piece) {
        int[] pos = piece.position;
        cells[pos[0]][pos[1]] = piece;
        return true;
    }

    public Piece updatePiece(String key, int[] newPos) {

        Piece orig = pieces.get(key);
        Piece inNewPos = getPiece(newPos);

        /* 如果移动的位置存在棋子则移除该棋子*/
        if (inNewPos != null)
            pieces.remove(inNewPos.key);

        /* 清除并更新*/
        int[] origPos = orig.position;
        cells[origPos[0]][origPos[1]] = null;
        cells[newPos[0]][newPos[1]] = orig;
        orig.position = newPos;
        isPlayer = !isPlayer;
        //player = player == 'r' ? 'b' : 'r';/**玩家交替*/
        return inNewPos;
    }

    public boolean backPiece(String key) {
        int[] origPos = pieces.get(key).position;
        cells[origPos[0]][origPos[1]] = pieces.get(key);
        return true;
    }

    public Collection<Piece> values() {
        return pieces.values();
    }

    public int size() {
        return pieces.size();
    }

    public void put(String key, Piece eaten) {
        pieces.put(key, eaten);
    }

    public Piece get(String name) {
        return pieces.get(name);
    }

    public Set<String> keySet() {
        return pieces.keySet();
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public boolean checkPlayer(String key) {
        return key.startsWith("r");
    }

    public int[] getReversePosition(Piece piece) {
        return new int[] { BOARD_ROW_COUNT - 1 - piece.position[0], piece.position[1] };
    }
}
