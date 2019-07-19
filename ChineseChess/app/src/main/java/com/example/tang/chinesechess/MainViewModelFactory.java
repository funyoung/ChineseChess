package com.example.tang.chinesechess;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.funyoung.andchess.ChessModel.Manual;
import com.funyoung.andchess.control.GameController;

/**
 * @author yangfeng
 */
public class MainViewModelFactory implements ViewModelProvider.Factory {
    private final GameController presenter;
    private final Manual manual;

    public MainViewModelFactory(GameController presenter, Manual manual) {
        this.presenter = presenter;
        this.manual = manual;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(presenter, manual);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
