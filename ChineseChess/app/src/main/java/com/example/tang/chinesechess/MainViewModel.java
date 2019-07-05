package com.example.tang.chinesechess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.res.Resources;
import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import com.funyoung.andchess.ChessModel.Manual;
import com.funyoung.andchess.GamePresenter;
import com.funyoung.andchess.GameView;
import com.funyoung.andchess.control.GameController;
import com.google.gson.Gson;

public class MainViewModel extends ViewModel {
    protected final MutableLiveData<Boolean> winnerLiveData = new MutableLiveData<>();
    protected final MutableLiveData<GameController> updateLiveData = new MutableLiveData<>();

    private GamePresenter controller;
    @MainThread
    public void start(GameView gameView, Resources resources) {
        controller = new GamePresenter(gameView, resources);
        GameThread th = new GameThread(resources);
        th.start();
    }


    private class GameThread extends Thread {
        private final Resources resources;

        private GameThread(Resources resources) {
            this.resources = resources;
        }

        @Override
        public void run() {
            super.run();
            String piecesText = resources.getString(R.string.full_pieces);
            Manual manual = new Gson().fromJson(piecesText, Manual.class);
            controller.update(manual);
            while (!controller.isDead()) {
                checkUserWin();

                if (controller.isDead()) {
                    interrupt();//中断线程
                }

                runOppositeMove();
            }
            showOppositeWin();
        }
    }

    @WorkerThread
    private void updateAndWait(int mills) {
        updateView();
        /* 界面更新缓冲*/
        waitFor(mills);

    }

    @WorkerThread
    private void checkUserWin() {
        updateView();

        /* User in. */
        while (controller.isPlayer()) {
            waitFor(1000);
        }

        updateAndWait(500);

        if (controller.isDead()) {
            // 红棋赢
            winnerLiveData.postValue(true);
        }
    }

    @WorkerThread
    private void runOppositeMove() {
        /* AI in. */
        controller.responseMoveChess();
        updateAndWait(500);
    }


    @WorkerThread
    private void waitFor(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showOppositeWin() {
        /**黑棋赢*/
        winnerLiveData.postValue(false);
    }

    @WorkerThread
    private void updateView() {
        updateLiveData.postValue(controller);
    }

}
