package com.example.tang.chinesechess;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.funyoung.andchess.GameView;
import com.funyoung.andchess.control.GameController;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @MainThread
    private void init() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.winnerLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                showWin(null != aBoolean && aBoolean);
            }
        });

        mainViewModel.updateLiveData.observe(this, new Observer<GameController>() {
            @Override
            public void onChanged(@Nullable GameController gameController) {
                if (null != gameController) {
                    gameController.postInvalidate();
                }
            }
        });

        //GameView gameView = initGameView();
        GameView gameView = findViewById(R.id.gameView);
        mainViewModel.start(gameView, getResources());
    }

//    private GameView initGameView() {
//        GameView gameView = new GameView(this);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT
//        );
//        this.addContentView(gameView, params);
//        return gameView;
//    }

    public void showWin(boolean win) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // todo: add prompt text into resource xml.
        builder.setMessage(win ? "You Win" : "You Lose");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }
}

