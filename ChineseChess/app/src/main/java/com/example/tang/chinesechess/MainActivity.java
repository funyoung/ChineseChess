package com.example.tang.chinesechess;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

