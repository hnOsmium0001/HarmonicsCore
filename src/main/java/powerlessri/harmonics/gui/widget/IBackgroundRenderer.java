package powerlessri.harmonics.gui.widget;

@FunctionalInterface
public interface IBackgroundRenderer {

    void render(int x1, int y1, int x2, int y2, float z, boolean hovered, boolean focused);
}
