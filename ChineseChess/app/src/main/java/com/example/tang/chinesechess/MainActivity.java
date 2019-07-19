package com.example.tang.chinesechess;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.funyoung.andchess.GamePresenter;
import com.funyoung.andchess.GameView;
import com.funyoung.andchess.control.GameController;
import com.funyoung.andchess.view.IGameView;

import java.util.List;

import com.example.manual.SimpleTextAdapter;

/**
 * @author yangfeng
 */
public class MainActivity extends AppCompatActivity {
    private GameView gameView;
    private IGameView view;
    private MainViewModel mainViewModel;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private SimpleTextAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);

        mRecyclerView = findViewById(R.id.recyclerView);

        view = new IGameView() {
            @Override
            public void postInvalidate() {
                if (null != gameView) {
                    gameView.postInvalidate();
                }
            }

            @Override
            public void setup(GameController controller) {
                if (null != gameView) {
                    gameView.setup(controller);
                }
            }
        };

        init();
    }

    @MainThread
    private void init() {
        GamePresenter controller = new GamePresenter(view, getResources());
        MainViewModelFactory factory = new MainViewModelFactory(controller);

        mainViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);

        mainViewModel.winnerLiveData.observe(this, aBoolean -> showWin(null != aBoolean && aBoolean));

        mainViewModel.updateLiveData.observe(this, gameController -> {
            if (null != gameController) {
                gameController.postInvalidate();
            }
        });

        mainViewModel.loadNameLiveData.observe(this, nameList -> {
            onNameListLoaded(nameList);
        });

        adapter = new SimpleTextAdapter(this, v -> {
            Object tag = v.getTag();
            if (tag instanceof String) {
                mainViewModel.startManual((String)tag);
            }
        });
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    private void onNameListLoaded(List<String> nameList) {
        adapter.reset(nameList);
        String name = null;
        if (null != nameList && !nameList.isEmpty()) {
            name = nameList.get(0);
        }
        mainViewModel.startManual(name);
    }

    public void showWin(boolean win) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // todo: add prompt text into resource xml.
        builder.setMessage(win ? "You Win" : "You Lose");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", (dialog, which) -> finish()).create().show();
    }
}

