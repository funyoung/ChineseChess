package com.example.tang.chinesechess;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.funyoung.andchess.ChessModel.Manual;
import com.funyoung.andchess.GamePresenter;
import com.funyoung.andchess.GameView;
import com.funyoung.andchess.control.GameController;
import com.google.gson.Gson;

/**
 * @author yangfeng
 */
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
        Resources resources = getResources();
        GameView gameView = findViewById(R.id.gameView);

        String piecesText = resources.getString(R.string.full_pieces);
        Manual manual = new Gson().fromJson(piecesText, Manual.class);
        GameController controller = new GamePresenter(gameView, resources);
        MainViewModelFactory factory = new MainViewModelFactory(controller, manual);

        mainViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        //mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

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
    }

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

