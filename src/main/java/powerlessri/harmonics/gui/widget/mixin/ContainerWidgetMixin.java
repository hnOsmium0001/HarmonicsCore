package powerlessri.harmonics.gui.widget.mixin;

import powerlessri.harmonics.gui.widget.IContainer;
import powerlessri.harmonics.gui.widget.IWidget;

public interface ContainerWidgetMixin<T extends IWidget> extends IContainer<T> {

    default void renderChildren(int mouseX, int mouseY, float particleTicks) {
        for (T child : getPanels()) {
            child.render(mouseX, mouseY, particleTicks);
        }
    }

    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (T child : getPanels()) {
            if (child.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (T child : getPanels()) {
            if (child.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (T child : getPanels()) {
            if (child.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        for (T child : getPanels()) {
            if (child.mouseScrolled(mouseX, mouseY, scroll)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (T child : getPanels()) {
            if (child.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (T child : getPanels()) {
            if (child.keyReleased(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean charTyped(char charTyped, int keyCode) {
        for (T child : getPanels()) {
            if (child.charTyped(charTyped, keyCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default void mouseMoved(double mouseX, double mouseY) {
        for (T child : getPanels()) {
            child.mouseMoved(mouseX, mouseY);
        }
    }

    @Override
    default void update(float particleTicks) {
        for (T child : getPanels()) {
            child.update(particleTicks);
        }
    }
}
