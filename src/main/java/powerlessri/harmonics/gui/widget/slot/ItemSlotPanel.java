package powerlessri.harmonics.gui.widget.slot;

import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.AbstractContainer;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemSlotPanel extends AbstractContainer<AbstractItemSlot> {

    private int width;
    private int height;

    private List<AbstractItemSlot> children;

    public ItemSlotPanel(int width, int height) {
        this(width, height, DefaultSlot::new);
    }

    public ItemSlotPanel(int width, int height, Supplier<AbstractItemSlot> factory) {
        this.width = width;
        this.height = height;

        this.children = new ArrayList<>();
        int size = width * height;
        for (int i = 0; i < size; i++) {
            addChildren(factory.get());
        }
        updateDimensions();
        reflow();
    }

    public ItemSlotPanel(int width, int height, List<ItemStack> stacks) {
        this(width, height, stacks, DefaultSlot::new);
    }

    public ItemSlotPanel(int width, int height, List<ItemStack> stacks, Function<ItemStack, AbstractItemSlot> factory) {
        int size = width * height;
        Preconditions.checkArgument(size == stacks.size());

        this.width = width;
        this.height = height;

        this.children = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            addChildren(factory.apply(stacks.get(i)));
        }
        updateDimensions();
        reflow();
    }

    private void updateDimensions() {
        int pw = children.stream()
                .mapToInt(IWidget::getWidth)
                .sum();
        int ph = children.stream()
                .mapToInt(IWidget::getWidth)
                .sum();
        setDimensions(pw, ph);
    }

    @Override
    public List<AbstractItemSlot> getChildren() {
        return children;
    }

    @Override
    public ItemSlotPanel addChildren(AbstractItemSlot widget) {
        children.add(widget);
        return this;
    }

    @Override
    public ItemSlotPanel addChildren(Collection<AbstractItemSlot> widgets) {
        children.addAll(widgets);
        return this;
    }

    @Override
    public void reflow() {
        int x = 0;
        int y = 0;
        int i = 0;
        for (int yi = 0; yi < height; yi++) {
            int maxHeight = 0;
            for (int xi = 0; xi < width; xi++) {
                AbstractItemSlot slot = children.get(i);
                slot.setLocation(x, y);
                y += slot.getWidth();
                maxHeight = Math.max(maxHeight, slot.getHeight());
                i++;
            }
            y += maxHeight;
        }
    }

    static class DefaultSlot extends AbstractItemSlot {

        private ItemStack stack;

        public DefaultSlot() {
            this(ItemStack.EMPTY);
        }

        public DefaultSlot(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public ItemStack getRenderedStack() {
            return stack;
        }

        @Nonnull
        @Override
        public ItemSlotPanel getParentWidget() {
            return (ItemSlotPanel) Objects.requireNonNull(super.getParentWidget());
        }
    }
}