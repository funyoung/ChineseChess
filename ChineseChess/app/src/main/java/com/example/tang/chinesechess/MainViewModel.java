package com.example.tang.chinesechess;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.funyoung.andchess.GamePresenter;
import com.funyoung.andchess.control.GameController;

import java.util.List;

/**
 * @author yangfeng
 */
public class MainViewModel extends ViewModel {
    protected final MutableLiveData<Boolean> winnerLiveData = new MutableLiveData<>();
    protected final MutableLiveData<GameController> updateLiveData = new MutableLiveData<>();
    protected final MutableLiveData<List<String>> loadNameLiveData = new MutableLiveData<>();

    private final GamePresenter controller;
    private ChessLoopThread currentThread;

    @MainThread
    public MainViewModel(GamePresenter controller) {
        this.controller = controller;

        new Thread("MainViewModel_loader") {
            @Override
            public void run() {
                List<String> nameList = controller.loadAllManual();
                loadNameLiveData.postValue(nameList);
            }
        }.start();
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

    @MainThread
    public void startManual(String name) {
        if (null != currentThread) {
            currentThread.interrupt();
            currentThread = null;
        }

        currentThread = new ChessLoopThread(name);
        currentThread.start();
    }

    class ChessLoopThread extends Thread {
        private final String name;

        ChessLoopThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            super.run();
            controller.loadManual(name);
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
