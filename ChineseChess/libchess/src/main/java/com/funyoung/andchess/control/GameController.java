package com.funyoung.andchess.control;

import com.funyoung.andchess.ChessModel.Board;
import com.funyoung.andchess.ChessModel.Manual;
import com.funyoung.andchess.ChessModel.Piece;
import com.funyoung.andchess.ChessModel.SelectingPiece;
import com.funyoung.andchess.alogrithm.AlphaBetaNode;
import com.funyoung.andchess.alogrithm.SearchModel;
import com.funyoung.andchess.view.IGameView;

import java.util.Collection;
import java.util.Set;

/**
 * @author yangfeng
 */
abstract public class GameController {
    private final Board board;
    private final IGameView gameView;

    public GameController(IGameView gameView) {
        this.gameView = gameView;
        board = new Board();
    }

    public void moveChess(String key, int[] position) {
        /**
         * Implements user's action.
         * */
        board.updatePiece(key, position);
    }

    /**
     * Implements artificial intelligence.
     * */
    public void responseMoveChess() {
        SearchModel searchModel = new SearchModel();
        AlphaBetaNode result = searchModel.search(board);
        board.updatePiece(result.piece, result.to);
        movePieceFromAI(result.piece, result.to);
    }

    public void postInvalidate() {
        gameView.postInvalidate(); // 刷新界面
    }

    public boolean isDead() {
        return board.isDead();
    }

    protected void invalidate() {
        gameView.postInvalidate();
    }

    protected Set<String> getKeySet() {
        return board.keySet();
    }

    protected Collection<Piece> getPieces() {
        return board.values();
    }

    public boolean isPlayer() {
        return board.isPlayer();
    }

    protected Piece getPiece(String key) {
        return board.get(key);
    }

    protected void select(SelectingPiece selectingPiece, Piece key) {
        selectingPiece.select(key, board);
    }

    abstract public void movePieceFromAI(String pieceKey, int[] to);

    public void update(Manual manual) {
        try {
            board.update(manual.getName(), ManualParser.parse(manual));
        } catch (Exception ex) {
            board.update("DEFAULT", DefaultManual.initPieces());
        }
    }

    public abstract void touchDown(float x, float y);
    public abstract void onDraw(Object canvas, int width, int height);
}
