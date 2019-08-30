package powerlessri.harmonics.gui.window;

import powerlessri.harmonics.gui.IWindow;

public interface IPopupWindow extends IWindow {

    boolean shouldDiscard();

    default void move(int xOffset, int yOffset) {
        setPosition(getX() + xOffset, getY() + yOffset);
    }
}