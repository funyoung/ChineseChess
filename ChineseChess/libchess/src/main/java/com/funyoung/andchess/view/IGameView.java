package com.funyoung.andchess.view;

import com.funyoung.andchess.control.GameController;

public interface IGameView {
    void postInvalidate();
    void setup(GameController controller);
}
