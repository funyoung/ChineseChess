package com.funyoung.andchess;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.funyoung.andchess.ChessModel.Piece;
import com.funyoung.andchess.ChessModel.SelectingPiece;
import com.funyoung.andchess.control.GameController;

import java.util.HashMap;
import java.util.Map;

public class GamePresenter extends GameController {
    private final Map<String, Drawable> bitmapMap = new HashMap<>();
    private final SelectingPiece selectingPiece = new SelectingPiece();

    private Drawable selectionDrawable;
    private Drawable nextDrawable;

    private int VIEW_WIDTH;/* 界面宽度*/
    private int VIEW_HEIGHT;/* 界面高度*/
    private int PIECE_WIDTH = 67, PIECE_HEIGHT = 67;/* 棋子大小*/
    private int SY_COE = 68, SX_COE = 68;/* 棋盘内间隔*/
    private int SX_OFFSET = 50, SY_OFFSET = 15; /* 棋盘和屏幕间隔*/

    private final Resources resources;

    private final Resources getResources() {
        return resources;
    }

    public GamePresenter(GameView gameView, Resources resources) {
        super(gameView);

        this.resources = resources;
        selectionDrawable = getDrawable(R.drawable.ring);
        nextDrawable = getDrawable(R.drawable.next);

        gameView.setup(this);
    }

    /**
     * 保存图片
     */
    protected void init() {
        Map<String, Drawable> currentBitmapMap = new HashMap<>();
        for (String key : getKeySet()) {
            Drawable bitmap = getBitmapForPiece(key);
            if (null != bitmap) {
                currentBitmapMap.put(key, bitmap);
            }
        }
        bitmapMap.clear();
        bitmapMap.putAll(currentBitmapMap);
        currentBitmapMap.clear();
    }

    private Drawable getBitmapForPiece(String key) {
        if (bitmapMap.containsKey(key)) {
            return bitmapMap.get(key);
        }

        Drawable bitmap = null;
        if (key.startsWith("bj")) {
            bitmap = getDrawable(R.drawable.bj);
        } else if (key.startsWith("bm")) {
            bitmap = getDrawable(R.drawable.bm);
        } else if (key.startsWith("bx")) {
            bitmap = getDrawable(R.drawable.bx);
        } else if (key.startsWith("bs")) {
            bitmap = getDrawable(R.drawable.bs);
        } else if (key.startsWith("bb")) {
            bitmap = getDrawable(R.drawable.bb);
        } else if (key.startsWith("bp")) {
            bitmap = getDrawable(R.drawable.bp);
        } else if (key.startsWith("bz")) {
            bitmap = getDrawable(R.drawable.bz);
        } else if (key.startsWith("rj")) {
            bitmap = getDrawable(R.drawable.rj);
        } else if (key.startsWith("rm")) {
            bitmap = getDrawable(R.drawable.rm);
        } else if (key.startsWith("rx")) {
            bitmap = getDrawable(R.drawable.rx);
        } else if (key.startsWith("rs")) {
            bitmap = getDrawable(R.drawable.rs);
        } else if (key.startsWith("rb")) {
            bitmap = getDrawable(R.drawable.rb);
        } else if (key.startsWith("rp")) {
            bitmap = getDrawable(R.drawable.rp);
        } else if (key.startsWith("rz")) {
            bitmap = getDrawable(R.drawable.rz);
        }
        return bitmap;
    }

    protected void updateSize(int width, int height) {
        if (VIEW_WIDTH == width && VIEW_HEIGHT == height) {
            return;
        }

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
        drawPlayer(canvas);
    }


    /**
     * 绘制棋子
     */
    private void drawChess(Canvas canvas) {
        for (Piece piece : getPieces()) {
            draw(canvas, piece);
        }
    }

    private void draw(Canvas canvas, Piece pieceKey) {
        if (null == pieceKey) {
            return;
        }

        int[] pos = pieceKey.position;
        int[] sPos = modelToViewConverter(pos);
        viewToModelConverter(sPos);
        Drawable bitmap = bitmapMap.get(pieceKey.key);
        scaleBitmap(bitmap, sPos[0], sPos[1]);
        bitmap.draw(canvas);

        if (selectingPiece.hasSelection()) {
            int width = PIECE_WIDTH; //bitmap.getWidth();
            int height = PIECE_WIDTH; //bitmap.getHeight();

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

    private void scaleBitmap(Drawable drawable, int left, int top) {
        int width = PIECE_WIDTH;
        int height = PIECE_WIDTH;
        int right = left + width;
        int bottom = top + height;
        drawable.setBounds(left, top, right, bottom);
    }

    /**
     * 绘制角色图标
     */
    public void drawPlayer(Canvas canvas) {
        int xc = VIEW_WIDTH / 2 - PIECE_WIDTH / 2;
        int yc = VIEW_HEIGHT / 2 - PIECE_HEIGHT / 2;

        Drawable drawable = isPlayer() ? getDrawable(R.drawable.r) : getDrawable(R.drawable.b);
        scaleBitmap(drawable, xc, yc);
        //canvas.drawBitmap(d, xc, yc, mPaint);
        drawable.draw(canvas);
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
            Piece piece = getPiece(key);
            if (coordinateIsPiece(piece.position, x, y)) {
                return piece;
            }
        }

        return null;
    }

    private boolean coordinateIsPiece(int[] pos, float x, float y) {
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
        } else if (!isPlayer()) {
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
        } else if (!isPlayer()) {
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
        boolean isPlayer = isPlayer(key.key);
        if (selectingPiece.hasSelection() && !isPlayer) { //棋子吃棋子
            int[] pos = getPiece(key.key).position;

            /* If an enemy piece already has been selected.*/
            if (selectingPiece.hasMovingTarget(pos)) {
                bitmapMap.remove(key);
                moveChess(selectingPiece.getKey(), pos);
                movePieceFromModel(selectingPiece.getKey(), pos);
            }
        } else if (isPlayer) {
            select(selectingPiece, key);
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
                moveChess(selectingPiece.getKey(), pos);
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

    private Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }
}
