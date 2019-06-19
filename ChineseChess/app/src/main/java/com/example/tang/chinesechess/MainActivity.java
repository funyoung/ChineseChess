package com.example.tang.chinesechess;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.funyoung.andchess.GameView;
import com.funyoung.andchess.view.IGameView;
import com.funyoung.andchess.control.GameController;
import com.funyoung.andchess.ChessModel.Board;

public class MainActivity extends AppCompatActivity {

    private final static int UPDATE_VIEW = 123;
    private final static int SHOW_WIN = 321;

    private Board board;
    private GameController controller;
    private IGameView gameView;

    /**
     * 消息队列
     */
    Handler myHandler = new Handler() {
        // 接收到消息后处理
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VIEW:
                    gameView.postInvalidate(); // 刷新界面
                    break;
                case SHOW_WIN:
                    showWin(msg.getData().getChar("win"));
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        controller = new GameController();
        board = controller.playChess();
        gameView = initGameView();

        myThread th = new myThread();
        th.start();
    }

    // todo: move into factory methods
    private IGameView initGameView() {
        GameView contentView = new GameView(this, controller, board);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        this.addContentView(contentView, params);
        return contentView;
    }


    class myThread extends Thread {
        @Override
        public void run() {
            super.run();

            while (Board.hasWin(board) == 'x') {
                updateView();

                /* User in. */
                while (board.player == 'r') {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                updateView();
                /* 界面更新缓冲*/
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (Board.hasWin(board) != 'x') {
                    showWin('r');
                }

                if (Board.hasWin(board) != 'x') {
                    interrupt();//中断线程
                }

                /* AI in. */
                controller.responseMoveChess(board, gameView);

                /* 更新界面*/
                updateView();

                /* 界面更新缓冲*/
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /**黑棋赢*/
            showWin('b');
        }

        private void showWin(char player) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putChar("win", player);
            msg.what = SHOW_WIN;
            msg.setData(bundle);
            myHandler.sendMessage(msg);
        }

        private void updateView() {
            Message message;
            message = new Message();
            message.what = UPDATE_VIEW;
            myHandler.sendMessage(message);
        }
    }

    public void showWin(char r) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(r + "is Winner");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }
}

