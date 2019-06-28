package com.example.tang.chinesechess;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.funyoung.andchess.GamePresenter;
import com.funyoung.andchess.GameView;

public class MainActivity extends AppCompatActivity {

    private final static int UPDATE_VIEW = 123;
    private final static int SHOW_WIN = 321;

    private GamePresenter controller;

    /**
     * 消息队列
     */
    Handler myHandler = new Handler() {
        // 接收到消息后处理
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VIEW:
                    controller.postInvalidate();
                    break;
                case SHOW_WIN:
                    showWin(msg.getData().getBoolean("win"));
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
        GameView gameView = new GameView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        this.addContentView(gameView, params);

        controller = new GamePresenter(gameView, getResources());

        myThread th = new myThread();
        th.start();
    }

    class myThread extends Thread {
        @Override
        public void run() {
            super.run();

            while (!controller.isDead()) {
                updateView();

                /* User in. */
                while (controller.isPlayer()) {
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

                // todo: 合并同一判断条件内的两个语句
                if (controller.isDead()) {
                    showWin(true);
                }

                if (controller.isDead()) {
                    interrupt();//中断线程
                }

                /* AI in. */
                controller.responseMoveChess();

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
            showWin(false);
        }

        private void showWin(boolean player) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("win", player);
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

    public void showWin(boolean player) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(player ? "You Win" : "You Lose");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }
}

