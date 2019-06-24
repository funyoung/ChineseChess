package com.funyoung.andchess;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.funyoung.andchess.ChessModel.Piece;
import com.funyoung.andchess.ChessModel.SelectingPiece;
import com.funyoung.andchess.control.GameController;
import com.funyoung.andchess.view.IGameView;

import java.util.HashMap;
import java.util.Map;

public class GamePresenter extends GameController {
    private final Map<String, Bitmap> bitmapMap = new HashMap<>();
    private final Paint mPaint = new Paint();
    private final SelectingPiece selectingPiece = new SelectingPiece();

    private Drawable selectionDrawable;
    private Drawable nextDrawable;

    private int VIEW_WIDTH;/* 界面宽度*/
    private int VIEW_HEIGHT;/* 界面高度*/
    private int PIECE_WIDTH = 67, PIECE_HEIGHT = 67;/* 棋子大小*/
    private int SY_COE = 68, SX_COE = 68;/* 棋盘内间隔*/
    private int SX_OFFSET = 50, SY_OFFSET = 15; /* 棋盘和屏幕间隔*/
    //private Board board;

    private final Resources resources;

    private final Resources getResources() {
        return resources;
    }

    public GamePresenter(IGameView gameView, Resources resources) {
        super(gameView);
        //this.board = super.board;
        //board = gameBoard;
        this.resources = resources;
        selectionDrawable = getResources().getDrawable(R.drawable.ring);
        nextDrawable = getResources().getDrawable(R.drawable.next);

        ((GameView)gameView).setup(this);
    }


    /**
     * 保存图片
     */
    protected void init() {
        Map<String, Bitmap> currentBitmapMap = new HashMap<>();
        for (String key : board.pieces.keySet()) {
            Bitmap bitmap = getBitmapForPiece(key);
            if (null != bitmap) {
                currentBitmapMap.put(key, bitmap);
            }
        }
        bitmapMap.clear();
        bitmapMap.putAll(currentBitmapMap);
        currentBitmapMap.clear();
    }

    private Bitmap getBitmapForPiece(String key) {
        if (bitmapMap.containsKey(key)) {
            return bitmapMap.get(key);
        }

        Bitmap bitmap = null;
        if (key.startsWith("bj")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bj);
        } else if (key.startsWith("bm")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bm);
        } else if (key.startsWith("bx")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bx);
        } else if (key.startsWith("bs")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bs);
        } else if (key.startsWith("bb")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bb);
        } else if (key.startsWith("bp")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bp);
        } else if (key.startsWith("bz")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bz);
        } else if (key.startsWith("rj")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rj);
        } else if (key.startsWith("rm")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rm);
        } else if (key.startsWith("rx")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rx);
        } else if (key.startsWith("rs")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rs);
        } else if (key.startsWith("rb")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rb);
        } else if (key.startsWith("rp")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rp);
        } else if (key.startsWith("rz")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rz);
        }
        return bitmap;
    }

    protected void updateSize(int width, int height) {
        /* 屏幕适配 */
        VIEW_WIDTH = width;
        VIEW_HEIGHT = height;
        PIECE_WIDTH = 78 * VIEW_WIDTH / 700;
        PIECE_HEIGHT = PIECE_WIDTH;

        SX_COE = 68 * VIEW_WIDTH / 700;
        SY_COE = 70 * VIEW_HEIGHT / 712;

        SX_OFFSET = 50 * VIEW_WIDTH / 700;
        SY_OFFSET = 15 * VIEW_HEIGHT / 712;
    }

    protected void onDraw(Canvas canvas) {
        /* 绘制棋子 */
        drawChess(canvas);
        drawPlayer(board.player, canvas);
    }


    /**
     * 绘制棋子
     */
    private void drawChess(Canvas canvas) {
        for (Piece piece : board.pieces.values()) {
            draw(canvas, piece);
        }
    }

    private void draw(Canvas canvas, Piece pieceKey) {
        if (null == pieceKey) {
            return;
        }

        int[] pos = pieceKey.position;
        int[] sPos = modelToViewConverter(pos);
        pos = viewToModelConverter(sPos);
        Bitmap bitmap = bitmapMap.get(pieceKey.key);
        bitmap = scaleBitmap(bitmap);
        canvas.drawBitmap(bitmap, sPos[0], sPos[1], mPaint);

        if (selectingPiece.hasSelection()) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            if (selectingPiece.isSameKey(pieceKey.key)) {
                draw(canvas, selectionDrawable, sPos, width, height);
            }

            for (int[] each : selectingPiece.getNextMoveList()) {
                int[] nextPos = modelToViewConverter(each);
                draw(canvas, nextDrawable, nextPos, width, height);
            }
        }
    }

    private void draw(Canvas canvas, Drawable selectionDrawable, int[] sPos, int width, int height) {
        selectionDrawable.setBounds(sPos[0], sPos[1],
                sPos[0] + width,
                sPos[1] + height);
        selectionDrawable.draw(canvas);
    }

    /**
     * 缩放图片
     */
    private Bitmap scaleBitmap(Bitmap bitMap) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = PIECE_WIDTH;
        int newHeight = PIECE_WIDTH;
        // 计算缩放比例
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
    }

    /**
     * 绘制角色图标
     */
    public void drawPlayer(char player, Canvas canvas) {
        if (player == 'r') {
            canvas.drawBitmap(scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.r)), VIEW_WIDTH / 2 - PIECE_WIDTH / 2, VIEW_HEIGHT / 2 - PIECE_HEIGHT / 2, mPaint);
        } else {
            canvas.drawBitmap(scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.b)), VIEW_WIDTH / 2 - PIECE_WIDTH / 2, VIEW_HEIGHT / 2 - PIECE_HEIGHT / 2, mPaint);
        }
    }

    /**
     * model转view
     */
    private int[] modelToViewConverter(int[] pos) {
        int sx = pos[1] * SX_COE + SX_OFFSET, sy = pos[0] * SY_COE + SY_OFFSET;
        return new int[]{sx, sy};
    }

    /**
     * view转model
     */
    private int[] viewToModelConverter(int[] sPos) {
        /* To make things right, I have to put an 'additional sy offset'. God knows why. */
        int ADDITIONAL_SY_OFFSET = 0;
        int y = (sPos[0] - SX_OFFSET) / SX_COE, x = (sPos[1] - SY_OFFSET - ADDITIONAL_SY_OFFSET) / SY_COE;
        return new int[]{x, y};
    }

    /**
     * 判断是否单击棋子
     */
    private Piece coordinateIsPiece(float x, float y) {
        for (String key : bitmapMap.keySet()) {
            Piece piece = board.pieces.get(key);
            if (coordinateIsPiece(piece, x, y)) {
                return piece;
            }
        }

        return null;
    }

    private boolean coordinateIsPiece(Piece pieceKey, float x, float y) {
        int[] pos = pieceKey.position;
        int[] sPos = modelToViewConverter(pos);
        if (sPos[0] + PIECE_WIDTH >= x && sPos[1] + PIECE_HEIGHT >= y
                && sPos[0] <= x && sPos[1] <= y) {
            return true;
        }
        return false;
    }

    public void movePieceFromModel(String pieceKey, int[] to) {
        selectingPiece.clear();

        //invalidate();
        if (!hasWin()) {
            showWinner('r');
        } else if (board.player == 'b') {
            /** UI */
            //responseMoveChess(board,this);
        }
    }

    @Override
    public void movePieceFromAI(String pieceKey, int[] to) {
        selectingPiece.clear();

        //invalidate();
        if (!hasWin()) {
            showWinner('b');
        } else if (board.player == 'b') {
            /** UI */
            //responseMoveChess(board,this);
        }

    }

    /**
     * 显示游戏结果
     */
    public void showWinner(char player) {

        //((MainActivity)context).finish();
        //System.exit(0);
    }

    /**
     * 选择棋子, 1. 点击前状态分已经选中一颗棋子，或者没有选中棋子
     * 2. 当前点击的棋子分是本方棋子和对方棋子，最后本次选择棋子有3种可能结果
     * A. 无效选择 B. 本次点击新选中本方棋子 C. 上次选择的棋子吃掉本次点中的对方棋子
     */
    public void pieceClickMove(Piece key) {
        if (selectingPiece.hasSelection() && key.key.charAt(0) != board.player) { //棋子吃棋子
            int[] pos = board.pieces.get(key.key).position;

            /* If an enemy piece already has been selected.*/
            if (selectingPiece.hasMovingTarget(pos)) {
                bitmapMap.remove(key);
                moveChess(selectingPiece.getKey(), pos, board);
                movePieceFromModel(selectingPiece.getKey(), pos);
            }
        } else if (key.key.charAt(0) == board.player) {
            selectingPiece.select(key, board);
            /* Select the piece.*/
            // todo: only invalidate the selected area
            invalidate();
        }
    }

    /**
     * 选择棋盘
     */
    public void boardClickMove(int[] point) {
        if (selectingPiece.hasSelection()) {
            int[] sPos = {point[0], point[1]};
            int[] pos = viewToModelConverter(sPos);
            if (selectingPiece.hasMovingTarget(pos)) {
                moveChess(selectingPiece.getKey(), pos, board);
                movePieceFromModel(selectingPiece.getKey(), pos);
            }
//            int[] selectedPiecePos = board.pieces.get(selectingPiece.getKey()).position;
//            for (int[] each : Rules.getNextMove(selectingPiece.getKey(), selectedPiecePos, board)) {
//                if (Arrays.equals(each, pos)) {/**当前位置是否可达*/
//                    moveChess(selectingPiece.getKey(), pos, board);
//                    movePieceFromModel(selectingPiece.getKey(), pos);
//                    break;
//                }
//            }
        }
    }

    public void touchDown(float x, float y) {
        Piece piece = null;
        if ((piece = coordinateIsPiece(x, y)) != null) {
            pieceClickMove(piece);
        } else {
            boardClickMove(new int[]{(int) x, (int) y});
        }
    }
}
