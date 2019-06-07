package com.example.tang.chinesechess;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.funyoung.libchess.ChessModel.Board;
import com.funyoung.libchess.ChessModel.Piece;
import com.funyoung.libchess.ChessModel.Rules;
import com.funyoung.libchess.control.GameController;
import com.funyoung.libchess.view.IGameView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tang on 2017/2/23.
 */

public class GameView extends View implements IGameView, View.OnTouchListener {
    private final Map<String, Bitmap> bitmapMap = new HashMap<>();
    private final Paint mPaint = new Paint();
    private final Context context;
    private Drawable selectionDrawable;

    private int VIEW_WIDTH;/* 界面宽度*/
    private int VIEW_HEIGHT;/* 界面高度*/
    private int PIECE_WIDTH = 67, PIECE_HEIGHT = 67;/* 棋子大小*/
    private int SY_COE = 68, SX_COE = 68;/* 棋盘内间隔*/
    private int SX_OFFSET = 50, SY_OFFSET = 15; /* 棋盘和屏幕间隔*/

    private GameController controller;
    private Board board;
    private Piece selectedPieceKey;


    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        setup(null, null);
    }

    public GameView(Context context, GameController gameController, Board gameBoard) {
        super(context);

        this.context = context;
        setup(gameController, gameBoard);
        //((ImageView)((MainActivity)context).findViewById(R.id.playImage)).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.b));
        //init();
    }

    private void setup(GameController gameController, Board gameBoard) {
        this.controller = gameController;
        board = gameBoard;
        selectionDrawable = getResources().getDrawable(R.drawable.ring);
    }

    /**
     * 保存图片
     */
    private void init() {
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
        if (null != selectedPieceKey && selectedPieceKey.key.equals(pieceKey.key)) {
            selectionDrawable.setBounds(sPos[0], sPos[1],
                    sPos[0] + bitmap.getWidth(),
                    sPos[1] + bitmap.getHeight());
            selectionDrawable.draw(canvas);
        }
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
        selectedPieceKey = null;

        //invalidate();
        if (Board.hasWin(board) != 'x') {
            showWinner('r');
        } else if (board.player == 'b') {
            /** UI */
            //controller.responseMoveChess(board,this);
        }
    }

    public void movePieceFromAI(String pieceKey, int[] to) {
        selectedPieceKey = null;
        //invalidate();
        if (Board.hasWin(board) != 'x') {
            showWinner('b');
        } else if (board.player == 'b') {
            /** UI */
            //controller.responseMoveChess(board,this);
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
        if (selectedPieceKey != null && key.key.charAt(0) != board.player) { //棋子吃棋子
            int[] pos = board.pieces.get(key.key).position;
            int[] selectedPiecePos = board.pieces.get(selectedPieceKey.key).position;
            /* If an enemy piece already has been selected.*/
            for (int[] each : Rules.getNextMove(selectedPieceKey.key, selectedPiecePos, board)) {
                if (Arrays.equals(each, pos)) {
                    // Kill self and move that piece.
                    //pane.remove(bitmapMap.get(key));
                    bitmapMap.remove(key);
                    controller.moveChess(selectedPieceKey.key, pos, board);
                    movePieceFromModel(selectedPieceKey.key, pos);
                    break;
                }
            }
        } else if (key.key.charAt(0) == board.player) {
            selectedPieceKey = key;
            /* Select the piece.*/
            // todo: only invalidate the selected area
            invalidate();
        }
    }

    /**
     * 选择棋盘
     */
    public void boardClickMove(int[] point) {
        if (selectedPieceKey != null) {
            int[] sPos = {point[0], point[1]};
            int[] pos = viewToModelConverter(sPos);
            int[] selectedPiecePos = board.pieces.get(selectedPieceKey.key).position;
            for (int[] each : Rules.getNextMove(selectedPieceKey.key, selectedPiecePos, board)) {
                if (Arrays.equals(each, pos)) {/**当前位置是否可达*/
                    controller.moveChess(selectedPieceKey.key, pos, board);
                    movePieceFromModel(selectedPieceKey.key, pos);
                    break;
                }
            }

        }
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        /* 屏幕适配 */
        VIEW_WIDTH = getWidth();
        VIEW_HEIGHT = getHeight();
        PIECE_WIDTH = 78 * VIEW_WIDTH / 700;
        PIECE_HEIGHT = PIECE_WIDTH;

        SX_COE = 68 * VIEW_WIDTH / 700;
        SY_COE = 70 * VIEW_HEIGHT / 712;

        SX_OFFSET = 50 * VIEW_WIDTH / 700;
        SY_OFFSET = 15 * VIEW_HEIGHT / 712;

        /* 绘制棋子 */
        drawChess(canvas);
        drawPlayer(board.player, canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Piece piece = null;
                if ((piece = coordinateIsPiece(x, y)) != null) {
                    pieceClickMove(piece);
                } else {
                    boardClickMove(new int[]{(int) x, (int) y});
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void showWin(char r) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(r + "is Winner");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity) context).finish();
            }
        }).create().show();
    }
}
