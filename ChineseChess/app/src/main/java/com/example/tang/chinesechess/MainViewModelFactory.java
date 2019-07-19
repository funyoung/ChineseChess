package com.example.tang.chinesechess;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.funyoung.andchess.ChessModel.Manual;
import com.funyoung.andchess.GamePresenter;
import com.funyoung.andchess.control.GameController;

/**
 * @author yangfeng
 */
public class MainViewModelFactory implements ViewModelProvider.Factory {
    private final GamePresenter presenter;

    public MainViewModelFactory(GamePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(presenter);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
