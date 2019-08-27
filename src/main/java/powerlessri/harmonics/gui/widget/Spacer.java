package powerlessri.harmonics.gui.widget;

import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

public class Spacer extends AbstractWidget implements LeafWidgetMixin {

    public Spacer(int width, int height) {
        this(0, 0, width, height);
    }

    public Spacer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }
}
