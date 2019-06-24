package com.funyoung.andchess.control;

import com.funyoung.andchess.ChessModel.Board;
import com.funyoung.andchess.ChessModel.Piece;
import com.funyoung.andchess.alogrithm.AlphaBetaNode;
import com.funyoung.andchess.alogrithm.SearchModel;
import com.funyoung.andchess.view.IGameView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tang on 2017/2/22.
 */

public class GameController {
    private final Board board;
    private final IGameView gameView;

    public GameController(IGameView gameView) {
        this.gameView = gameView;
        board = playChess();

        gameView.setup(this, board);
    }

    /**初始化棋子图片和位置*/
    private Map<String, Piece> initPieces() {
        Map<String, Piece> pieces = new HashMap<String, Piece>();
        pieces.put("bj0", new Piece("bj0", new int[]{0, 0}));
        pieces.put("bm0", new Piece("bm0", new int[]{0, 1}));
        pieces.put("bx0", new Piece("bx0", new int[]{0, 2}));
        pieces.put("bs0", new Piece("bs0", new int[]{0, 3}));
        pieces.put("bb0", new Piece("bb0", new int[]{0, 4}));
        pieces.put("bs1", new Piece("bs1", new int[]{0, 5}));
        pieces.put("bx1", new Piece("bx1", new int[]{0, 6}));
        pieces.put("bm1", new Piece("bm1", new int[]{0, 7}));
        pieces.put("bj1", new Piece("bj1", new int[]{0, 8}));
        pieces.put("bp0", new Piece("bp0", new int[]{2, 1}));
        pieces.put("bp1", new Piece("bp1", new int[]{2, 7}));
        pieces.put("bz0", new Piece("bz0", new int[]{3, 0}));
        pieces.put("bz1", new Piece("bz1", new int[]{3, 2}));
        pieces.put("bz2", new Piece("bz2", new int[]{3, 4}));
        pieces.put("bz3", new Piece("bz3", new int[]{3, 6}));
        pieces.put("bz4", new Piece("bz4", new int[]{3, 8}));

        pieces.put("rj0", new Piece("rj0", new int[]{9, 0}));
        pieces.put("rm0", new Piece("rm0", new int[]{9, 1}));
        pieces.put("rx0", new Piece("rx0", new int[]{9, 2}));
        pieces.put("rs0", new Piece("rs0", new int[]{9, 3}));
        pieces.put("rb0", new Piece("rb0", new int[]{9, 4}));
        pieces.put("rs1", new Piece("rs1", new int[]{9, 5}));
        pieces.put("rx1", new Piece("rx1", new int[]{9, 6}));
        pieces.put("rm1", new Piece("rm1", new int[]{9, 7}));
        pieces.put("rj1", new Piece("rj1", new int[]{9, 8}));
        pieces.put("rp0", new Piece("rp0", new int[]{7, 1}));
        pieces.put("rp1", new Piece("rp1", new int[]{7, 7}));
        pieces.put("rz0", new Piece("rz0", new int[]{6, 0}));
        pieces.put("rz1", new Piece("rz1", new int[]{6, 2}));
        pieces.put("rz2", new Piece("rz2", new int[]{6, 4}));
        pieces.put("rz3", new Piece("rz3", new int[]{6, 6}));
        pieces.put("rz4", new Piece("rz4", new int[]{6, 8}));
        return pieces;
    }

    /**初始化棋盘*/
    private Board initBoard() {
        Board board = new Board();
        board.pieces = initPieces();
        for (Piece piece : board.pieces.values())
            board.update(piece);
        return board;
    }
    /**开始游戏*/
    public Board playChess() {
        /**
         * Start game.
         * */
        initPieces();
        return initBoard();
    }

    public void moveChess(String key, int[] position, Board board) {
        /**
         * Implements user's action.
         * */
        board.updatePiece(key, position);
    }

    public void responseMoveChess() {
        /**
         * Implements artificial intelligence.
         * */
        SearchModel searchModel = new SearchModel();
        AlphaBetaNode result = searchModel.search(board);
        board.updatePiece(result.piece, result.to);
        gameView.movePieceFromAI(result.piece, result.to);
    }

    public void postInvalidate() {
        gameView.postInvalidate(); // 刷新界面
    }

    public boolean hasWin() {
        return Board.hasWin(board) == 'x';
    }

    public boolean isUserIn() {
        return board.player == 'r';
    }
}
