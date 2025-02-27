package com.funyoung.andchess.alogrithm;

import com.funyoung.andchess.ChessModel.Board;
import com.funyoung.andchess.ChessModel.Piece;
import com.funyoung.andchess.ChessModel.Rules;
import java.util.ArrayList;

/**
 * Alpha beta search.
 * @author yangfeng
 */
public class SearchModel {
    private static int DEPTH = 2;
    private Board board;

    public AlphaBetaNode search(Board board) {
        this.board = board;
        setDepth(board.size());

        long startTime = System.currentTimeMillis();
        AlphaBetaNode best = null;
        ArrayList<AlphaBetaNode> moves = generateMovesForAll(true);
        for (AlphaBetaNode n : moves) {
            /* Move*/
            Piece eaten = board.updatePiece(n.piece, n.to);
            n.value = alphaBeta(DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            /* Select a best move during searching to save time*/
            if (best == null || n.value >= best.value) {
                best = n;
            }
            /* Back move*/
            board.updatePiece(n.piece, n.from);
            if (eaten != null) {
                board.put(eaten.key, eaten);
                board.backPiece(eaten.key);
            }
        }
        long finishTime = System.currentTimeMillis();
        System.out.println(finishTime - startTime);
        return best;
    }

    private void setDepth(int size) {
        if (size < 28) {
            DEPTH = 3;
        }
        if (size < 16) {
            DEPTH = 4;
        }
        if (size < 6) {
            DEPTH = 5;
        }
        if (size < 4) {
            DEPTH = 6;
        }
    }

    private int alphaBeta(int depth, int alpha, int beta, boolean isMax) {
        /* Return evaluation if reaching leaf node or any side won.*/
        if (depth == 0 || board.isDead()) {
            return new EvalModel().eval(board, 'b');
        }

        ArrayList<AlphaBetaNode> moves = generateMovesForAll(isMax);

        synchronized (this) {
            for (AlphaBetaNode n : moves) {
                Piece eaten = board.updatePiece(n.piece, n.to);
            /* Is maximizing player? */
                final int finalBeta = beta;
                final int finalAlpha = alpha;
                final int finalDepth = depth;
                final int[] temp = new int[1];
                /* Only adopt multi threading strategy in depth 2. To avoid conjunction.*/
                if (depth == 2) {
                    if (isMax) {
                        new Thread(() -> temp[0] = Math.max(finalAlpha, alphaBeta(finalDepth - 1, finalAlpha, finalBeta, false))).run();
                        alpha = temp[0];
                    } else {
                        new Thread(() -> temp[0] = Math.min(finalBeta, alphaBeta(finalDepth - 1, finalAlpha, finalBeta, true))).run();
                        beta = temp[0];
                    }
                }
                else {
                    if (isMax) {
                        alpha = Math.max(alpha, alphaBeta(depth - 1, alpha, beta, false));
                    } else {
                        beta = Math.min(beta, alphaBeta(depth - 1, alpha, beta, true));
                    }
                }
                board.updatePiece(n.piece, n.from);
                if (eaten != null) {
                    board.put(eaten.key, eaten);
                    board.backPiece(eaten.key);
                }
            /* Cut-off */
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return isMax ? alpha : beta;
    }
    private ArrayList<AlphaBetaNode> generateMovesForAll(boolean isMax) {
        ArrayList<AlphaBetaNode> moves = new ArrayList<>();
        for (Piece piece : board.values()) {
            if (isMax && piece.isRed()) {
                continue;
            }
            if (!isMax && !piece.isRed()) {
                continue;
            }
            for (int[] nxt : Rules.getNextMove(piece, board)) {
                moves.add(new AlphaBetaNode(piece.key, piece.position, nxt));
            }
        }
        return moves;
    }

}