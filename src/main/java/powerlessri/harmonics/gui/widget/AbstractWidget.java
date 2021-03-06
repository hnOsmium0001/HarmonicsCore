package powerlessri.harmonics.gui.widget;

import com.google.common.base.Preconditions;
import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.contextmenu.ContextMenuBuilder;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.Inspections;
import powerlessri.harmonics.gui.layout.properties.*;
import powerlessri.harmonics.gui.widget.mixin.ResizableWidgetMixin;
import powerlessri.harmonics.gui.window.IWindow;

import javax.annotation.Nullable;
import java.awt.*;

public abstract class AbstractWidget implements IWidget, Inspections.IInfoProvider, Inspections.IHighlightRenderer, ISizedBox, ResizableWidgetMixin {

    private Point location;
    private Dimension dimensions;
    private Insets border = new Insets(0, 0, 0, 0);
    private float z = 0F;

    private boolean enabled = true;
    private IWindow window;
    private IWidget parent;

    // Cached because this might reach all the up to the root node by recursion on getAbsoluteX/Y
    private int absX;
    private int absY;

    public AbstractWidget() {
        this.location = new Point();
        this.dimensions = new Dimension();
    }

    @Override
    public void attach(IWidget newParent) {
        IWidget oldParent = parent;
        this.parent = newParent;
        this.window = newParent.getWindow();
        onParentPositionChanged();
        onAttach(oldParent, newParent);
        if (oldParent == null) {
            onInitialAttach();
        }
        z = newParent.getZLevel() + 1F;
    }

    public void onAttach(@Nullable IWidget oldParent, IWidget newParent) {
    }

    public void onInitialAttach() {
    }

    @Override
    public boolean isValid() {
        return parent != null || window != null;
    }

    public void attachWindow(IWindow window) {
        IWindow oldWindow = this.window;
        this.window = window;
        this.z = window.getZLevel() + 1F;
        if (oldWindow == null) {
            onInitialAttach();
        }
        onParentPositionChanged();
    }

    @Override
    public void onParentPositionChanged() {
        Preconditions.checkState(isValid());
        updateAbsolutePosition();
    }

    @Override
    public void onRelativePositionChanged() {
        if (isValid()) {
            updateAbsolutePosition();
        }
    }

    private void updateAbsolutePosition() {
        absX = getParentAbsXSafe() + getX() + getBorderLeft();
        absY = getParentAbsYSafe() + getY() + getBorderTop();
    }

    private int getParentAbsXSafe() {
        if (parent != null) {
            return parent.getAbsoluteX();
        }
        return window.getContentX();
    }

    private int getParentAbsYSafe() {
        if (parent != null) {
            return parent.getAbsoluteY();
        }
        return window.getContentY();
    }

    public int getParentHeight() {
        if (parent != null) {
            return parent.getFullHeight();
        }
        return window.getContentHeight();
    }

    public int getParentWidth() {
        if (parent != null) {
            return parent.getFullWidth();
        }
        return window.getContentWidth();
    }

    /**
     * Helper method to set focus of a specific element. Notice this would cancel the originally focus element.
     */
    public void setFocused(boolean focused) {
        if (isValid()) {
            getWindow().setFocusedWidget(focused ? this : null);
        }
    }

    public void fillParentContainer() {
        setLocation(0, 0);
        setDimensions(parent.getDimensions());
    }

    public void expandHorizontally() {
        setWidth(Math.max(getFullWidth(), getParentWidth()));
    }

    public void expandVertically() {
        setHeight(Math.max(getFullHeight(), getParentHeight()));
    }

    @Override
    public boolean isFocused() {
        return isValid() && getWindow().getFocusedWidget() == this;
    }

    public void alignTo(IWidget other, Side side, Alignment alignment) {
        if (this.getParent() != other.getParent()) {
            return;
        }

        int otherLeft = other.getX();
        int otherTop = other.getY();
        int otherRight = otherLeft + other.getFullWidth();
        int otherBottom = otherTop + other.getFullHeight();

        switch (side) {
            case TOP:
                alignBottom(otherTop);
                alignHorizontally(alignment, otherLeft, otherRight);
                break;
            case BOTTOM:
                alignTop(otherBottom);
                alignHorizontally(alignment, otherLeft, otherRight);
                break;
            case LEFT:
                alignRight(otherLeft);
                alignVertically(alignment, otherTop, otherBottom);
                break;
            case RIGHT:
                alignLeft(otherRight);
                alignVertically(alignment, otherTop, otherBottom);
                break;
        }
    }

    private void alignHorizontally(Alignment alignment, int left, int right) {
        switch (alignment) {
            case TOP_LEFT:
                alignLeft(left);
                break;
            case CENTER:
                alignCenterX(left, right);
                break;
            case BOTTOM_RIGHT:
                alignRight(right);
                break;
        }
    }

    private void alignVertically(Alignment alignment, int top, int bottom) {
        switch (alignment) {
            case TOP_LEFT:
                alignTop(top);
                break;
            case CENTER:
                alignCenterY(top, bottom);
                break;
            case BOTTOM_RIGHT:
                alignBottom(bottom);
                break;
        }
    }

    public void alignLeft(int left) {
        setX(left);
    }

    public void alignCenterX(int left, int right) {
        setX(Render2D.computeCenterX(left, right, getFullWidth()));
    }

    public void alignRight(int right) {
        setX(Render2D.computeRightX(right, getFullWidth()));
    }

    public void alignTop(int top) {
        setY(top);
    }

    public void alignCenterY(int top, int bottom) {
        setY(Render2D.computeCenterY(top, bottom, getFullHeight()));
    }

    public void alignBottom(int bottom) {
        setY(Render2D.computeBottomY(bottom, getFullHeight()));
    }

    @Override
    public float getZLevel() {
        return z;
    }

    @Override
    public Point getPosition() {
        return location;
    }

    @Override
    public Dimension getDimensions() {
        return dimensions;
    }

    @Override
    public Insets getBorders() {
        return border;
    }

    @Override
    public void setLocation(Point point) {
        setLocation(point.x, point.y);
    }

    @Override
    public void setLocation(int x, int y) {
        getPosition().x = x;
        getPosition().y = y;
        onRelativePositionChanged();
    }

    @Override
    public void setX(int x) {
        getPosition().x = x;
        onRelativePositionChanged();
    }

    @Override
    public void setY(int y) {
        getPosition().y = y;
        onRelativePositionChanged();
    }

    @Override
    public int getX() {
        return location.x;
    }

    @Override
    public int getY() {
        return location.y;
    }

    public int getXRight() {
        return location.x + getFullWidth();
    }

    public int getYBottom() {
        return location.y + getFullHeight();
    }

    @Override
    public int getInnerX() {
        return location.x + border.left;
    }

    @Override
    public int getInnerY() {
        return location.y + border.top;
    }

    public int getInnerXRight() {
        return location.x + border.left + dimensions.width;
    }

    public int getInnerYBottom() {
        return location.y + border.top + dimensions.height;
    }

    @Override
    public int getAbsoluteX() {
        return absX;
    }

    @Override
    public int getAbsoluteY() {
        return absY;
    }

    public int getAbsoluteXRight() {
        return absX + dimensions.width;
    }

    public int getAbsoluteYBottom() {
        return absY + dimensions.height;
    }

    @Override
    public int getOuterAbsoluteX() {
        return absX - border.left;
    }

    @Override
    public int getOuterAbsoluteY() {
        return absY - border.top;
    }

    public int getOuterAbsoluteXRight() {
        return absX + getFullWidth();
    }

    public int getOuterAbsoluteYBottom() {
        return absY + getFullHeight();
    }

    @Override
    public int getWidth() {
        return dimensions.width;
    }

    @Override
    public int getHeight() {
        return dimensions.height;
    }

    @Override
    public int getFullWidth() {
        return border.left + dimensions.width + border.right;
    }

    @Override
    public int getFullHeight() {
        return border.top + dimensions.height + border.bottom;
    }

    public void moveX(int dx) {
        setX(getX() + dx);
    }

    public void moveY(int dy) {
        setY(getY() + dy);
    }

    public void move(int dx, int dy) {
        moveX(dx);
        moveY(dy);
    }

    @Nullable
    @Override
    public IWidget getParent() {
        return parent;
    }

    @Override
    public IWindow getWindow() {
        return window;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isInside(double x, double y) {
        return getOuterAbsoluteX() <= x &&
                getOuterAbsoluteXRight() > x &&
                getOuterAbsoluteY() <= y &&
                getOuterAbsoluteYBottom() > y;
    }

    @Override
    public BoxSizing getBoxSizing() {
        return BoxSizing.BORDER_BOX;
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        receiver.line(this.toString());
        receiver.line("Position=(" + location.x + ", " + location.y + ")");
        receiver.line("Dimensions=(" + dimensions.width + ", " + dimensions.height + ")");
        receiver.line(String.format("Borders={top: %d, right: %d, bottom: %d, left: %d}", border.top, border.right, border.bottom, border.left));
        receiver.line("Enabled=" + isEnabled());
        receiver.line("Z=" + z);
        receiver.line("AbsX=" + getAbsoluteX());
        receiver.line("AbsY=" + getAbsoluteY());
    }

    @Override
    public void renderHighlight() {
        Inspections.renderBorderedHighlight(
                getOuterAbsoluteX(), getOuterAbsoluteY(),
                getAbsoluteX(), getAbsoluteY(),
                getWidth(), getHeight(),
                getFullWidth(), getFullHeight());
    }

    @Override
    public int getBorderTop() {
        return border.top;
    }

    @Override
    public int getBorderRight() {
        return border.right;
    }

    @Override
    public int getBorderBottom() {
        return border.bottom;
    }

    @Override
    public int getBorderLeft() {
        return border.left;
    }

    public int getVerticalBorder() {
        return border.top + border.bottom;
    }

    public int getHorizontalBorder() {
        return border.left + border.right;
    }

    @Override
    public void setBorderTop(int top) {
        border.top = top;
        onBorderChanged();
    }

    @Override
    public void setBorderRight(int right) {
        border.right = right;
        onBorderChanged();
    }

    @Override
    public void setBorderBottom(int bottom) {
        border.bottom = bottom;
        onBorderChanged();
    }

    @Override
    public void setBorderLeft(int left) {
        border.left = left;
        onBorderChanged();
    }

    @Override
    public void setBorders(int top, int right, int bottom, int left) {
        border.top = top;
        border.right = right;
        border.bottom = bottom;
        border.left = left;
        onBorderChanged();
    }

    @Override
    public void setBorders(int borders) {
        setBorders(borders, borders, borders, borders);
    }

    protected void onBorderChanged() {
        if (isValid()) {
            updateAbsolutePosition();
        }
    }

    public final void createContextMenu(double x, double y) {
        ContextMenuBuilder builder = new ContextMenuBuilder();
        buildContextMenu(builder);
        builder.buildAndAdd();
    }

    protected void buildContextMenu(ContextMenuBuilder builder) {
    }

    public enum Alignment {
        TOP_LEFT, CENTER, BOTTOM_RIGHT
    }
}
