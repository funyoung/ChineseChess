package com.example.tang.chinesechess.ChessView;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.tang.chinesechess.ChessModel.Board;
import com.example.tang.chinesechess.ChessModel.Piece;
import com.example.tang.chinesechess.ChessModel.Rules;
import com.example.tang.chinesechess.MainActivity;
import com.example.tang.chinesechess.R;
import com.example.tang.chinesechess.control.GameController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tang on 2017/2/23.
 */

public class GameView extends View implements View.OnTouchListener {

    private int VIEW_WIDTH;/* 界面宽度*/
    private int VIEW_HEIGHT;/* 界面高度*/

    private int PIECE_WIDTH = 67, PIECE_HEIGHT = 67;/* 棋子大小*/
    private int SY_COE = 68, SX_COE = 68;/* 棋盘内间隔*/
    private int SX_OFFSET = 50, SY_OFFSET = 15; /* 棋盘和屏幕间隔*/

    private boolean isSelectedPiece;
    private GameController controller;
    private final Map<Piece, Bitmap> pieceObjects = new HashMap<Piece, Bitmap>();
    private Board board;

    private Piece selectedPieceKey;
    private final Paint mPaint = new Paint();

    private final Context context;

    public GameView(Context context) {
        super(context);
        this.context=context;
        controller = null;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public GameView(Context context, GameController gameController, Board gameBoard) {
        super(context);
        this.controller = gameController;
        board = gameBoard;
        this.context=context;
        //((ImageView)((MainActivity)context).findViewById(R.id.playImage)).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.b));
        //init();
    }

    /**保存图片*/
    private void init() {
        pieceObjects.clear();
        Map<String, Piece> pieces = board.pieces;
        for (Map.Entry<String, Piece> stringPieceEntry : pieces.entrySet()) {
            String key = stringPieceEntry.getKey();
            Bitmap bitmap = null;
            key = key.substring(0, 2);
            switch (key) {
                case "bj":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bj);
                    break;
                case "bm":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bm);
                    break;
                case "bx":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bx);
                    break;
                case "bs":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bs);
                    break;
                case "bb":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bb);
                    break;
                case "bp":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bp);
                    break;
                case "bz":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bz);
                    break;

                case "rj":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rj);
                    break;
                case "rm":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rm);
                    break;
                case "rx":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rx);
                    break;
                case "rs":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rs);
                    break;
                case "rb":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rb);
                    break;
                case "rp":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rp);
                    break;
                case "rz":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rz);
                    break;
            }
            pieceObjects.put(stringPieceEntry.getValue(), bitmap);
        }
    }
    /** 绘制棋子 */
    private void drawChess(Canvas canvas) {
        Map<Piece, Bitmap> bitmaps = pieceObjects;
        for (Map.Entry<Piece, Bitmap> stringPieceEntry : bitmaps.entrySet()) {
            int[] pos = stringPieceEntry.getKey().position;
            int[] sPos = modelToViewConverter(pos);
            pos= viewToModelConverter(sPos);
            Bitmap bitmap = stringPieceEntry.getValue();
            bitmap= scaleBitmap(bitmap);
            canvas.drawBitmap(bitmap, sPos[0], sPos[1], mPaint);
        }
    }
    /** 缩放图片 */
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
    /** model转view */
    private int[] modelToViewConverter(int[] pos) {
        int sx = pos[1] * SX_COE + SX_OFFSET, sy = pos[0] * SY_COE + SY_OFFSET;
        return new int[]{sx, sy};
    }
    /** view转model */
    private int[] viewToModelConverter(int[] sPos) {
        /* To make things right, I have to put an 'additional sy offset'. God knows why. */
        int ADDITIONAL_SY_OFFSET = 0;
        int y = (sPos[0] - SX_OFFSET) / SX_COE, x = (sPos[1] - SY_OFFSET - ADDITIONAL_SY_OFFSET) / SY_COE;
        return new int[]{x, y};
    }
    /** 判断是否单击棋子 */
    private Piece coordinateIsPiece(float x,float y){
        Map<Piece, Bitmap> bitmaps = pieceObjects;

        for (Map.Entry<Piece, Bitmap> stringPieceEntry : bitmaps.entrySet()) {
            int[] pos = stringPieceEntry.getKey().position;
            int[] sPos = modelToViewConverter(pos);
            if(sPos[0]+ PIECE_WIDTH >=x&&sPos[1]+ PIECE_HEIGHT >=y&&sPos[0]<=x&&sPos[1]<=y)
            {
                return stringPieceEntry.getKey();
            }
        }
        return null;
    }

    public void movePieceFromModel(String pieceKey, int[] to) {
        selectedPieceKey = null;
        isSelectedPiece =false;
        //invalidate();
        if (controller.hasWin(board) != 'x'){
            showWinner('r');
        }
        else if(board.player=='b'){
            /** UI */
            //controller.responseMoveChess(board,this);
        }
    }
    public void movePieceFromAI(String pieceKey, int[] to) {
        selectedPieceKey = null;
        isSelectedPiece =false;
        //invalidate();
        if (controller.hasWin(board) != 'x'){
            showWinner('b');
        }
        else if(board.player=='b'){
            /** UI */
            //controller.responseMoveChess(board,this);
        }

    }

    /**显示游戏结果*/
    public void showWinner(char player) {

        //((MainActivity)context).finish();
        //System.exit(0);
    }

    /** 选择棋子 */
    public void pieceClickMove(Piece key) {
        if (selectedPieceKey != null&& key.key.charAt(0) != board.player) {//棋子吃棋子
            int[] pos = board.pieces.get(key.key).position;
            int[] selectedPiecePos = board.pieces.get(selectedPieceKey.key).position;
                /* If an enemy piece already has been selected.*/
            for (int[] each : Rules.getNextMove(selectedPieceKey.key, selectedPiecePos, board)) {
                if (Arrays.equals(each, pos)) {
                    // Kill self and move that piece.
                    //pane.remove(pieceObjects.get(key));
                    pieceObjects.remove(key);
                    controller.moveChess(selectedPieceKey.key, pos, board);
                    movePieceFromModel(selectedPieceKey.key, pos);
                    break;
                }
            }
        }
        else if (key.key.charAt(0) == board.player) {
            selectedPieceKey = key;
            isSelectedPiece =true;
            /* Select the piece.*/
        }
    }

    /** 选择棋盘*/
    public void boardClickMove(int[] point){
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

    /** 绘制角色图标*/
    public void drawPlayer(char player, Canvas canvas) {
        if(player=='r')
        {
            canvas.drawBitmap(scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.r)), VIEW_WIDTH /2- PIECE_WIDTH /2, VIEW_HEIGHT /2- PIECE_HEIGHT /2, mPaint);
        }
        else{
            canvas.drawBitmap(scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.b)), VIEW_WIDTH /2- PIECE_WIDTH /2, VIEW_HEIGHT /2- PIECE_HEIGHT /2, mPaint);
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

        SX_COE = 68* VIEW_WIDTH /700;
        SY_COE = 70 * VIEW_HEIGHT / 712;

        SX_OFFSET = 50 * VIEW_WIDTH / 700;
        SY_OFFSET = 15 * VIEW_HEIGHT / 712;

        /* 绘制棋子 */
        drawChess(canvas);
        drawPlayer(board.player,canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Piece piece=null;
                if((piece= coordinateIsPiece(x,y))!=null)
                {
                    pieceClickMove(piece);
                }
                else{
                    boardClickMove(new int[]{(int)x,(int)y});
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
        builder.setMessage(r+"is Winner");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity) context).finish();
            }
        }).create().show();
    }
}
