package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.contextmenu.*;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.Spacer;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.List;

public class ContextMenuTest extends WidgetScreen {

    public ContextMenuTest() {
        super(new StringTextComponent("Test"));
    }

    @Override
    protected void init() {
        super.init();
        setPrimaryWindow(new Window());
    }

    public static class Window extends AbstractWindow {

        private final List<IWidget> children;

        public Window() {
            setContents(100, 80);
            centralize();

            Spacer spacer = new Spacer(getContentWidth(), getContentHeight()) {
                @Override
                protected void buildContextMenu(ContextMenuBuilder builder) {
                    Section section1 = builder.getSection("Section1");
                    section1.addChildren(new CallbackEntry(null, "Test1", b -> Render2D.minecraft().player.sendChatMessage("First entry got clicked!")));

                    Section section2 = builder.getSection("Section2");
                    section2.addChildren(new DefaultEntry(null, "S2 Test1"));

                    // Construct submenu
                    // TODO add builder for creating submenu
                    SubContextMenu scm = new SubContextMenu();
                    Section section = new Section();
                    scm.addSection(section);
                    section.addChildren(new DefaultEntry(null, "Sub menu entry 1"));
                    scm.reflow();
                    section2.addChildren(new ExpandableEntry<>(null, "S2T2", scm));
                    section2.addChildren(new CallbackEntry(null, "S2 Test3", b -> Render2D.minecraft().player.sendChatMessage("You clicked on an entry below S2T2")));

                    Section section3 = builder.getSection("Section3");
                    section3.addChildren(new DefaultEntry(null, "S3 Test1 and this is a very long context menu entry"));
                    section3.addChildren(new DefaultEntry(null, "S4 Test2"));
                }
            };
            spacer.attachWindow(this);

            this.children = ImmutableList.of(spacer);
        }

        @Override
        public int getBorderSize() {
            return 4;
        }

        @Override
        public List<? extends IWidget> getChildren() {
            return children;
        }

        @Override
        public void render(int mouseX, int mouseY, float particleTicks) {
            RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
            renderVanillaStyleBackground();
            renderChildren(mouseX, mouseY, particleTicks);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }
}
