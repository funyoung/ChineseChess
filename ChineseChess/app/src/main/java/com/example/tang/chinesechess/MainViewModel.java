package com.example.tang.chinesechess;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.funyoung.andchess.ChessModel.Manual;
import com.funyoung.andchess.control.GameController;

/**
 * @author yangfeng
 */
public class MainViewModel extends ViewModel {
    protected final MutableLiveData<Boolean> winnerLiveData = new MutableLiveData<>();
    protected final MutableLiveData<GameController> updateLiveData = new MutableLiveData<>();

    private final GameController controller;
    private final Manual manual;

    @MainThread
    public MainViewModel(GameController controller, Manual manual) {
        this.controller = controller;
        this.manual = manual;

        new ChessLoopThread().start();
    }

    @WorkerThread
    /** 界面更新缓冲*/
    private void updateAndWait(int mills) {
        updateView();
        waitFor(mills);
    }

    @WorkerThread
    private void checkUserWin() {
        updateView();

        /** User in.*/
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

    @WorkerThread
    /**黑棋赢*/
    private void showOppositeWin() {
        winnerLiveData.postValue(false);
    }

    @WorkerThread
    private void updateView() {
        updateLiveData.postValue(controller);
    }

    class ChessLoopThread extends Thread {
        @Override
        public void run() {
            super.run();
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
}
