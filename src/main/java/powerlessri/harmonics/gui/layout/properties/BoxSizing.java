package powerlessri.harmonics.gui.layout.properties;

import powerlessri.harmonics.gui.widget.IWidget;

public enum BoxSizing {

    BORDER_BOX(true),
    CONTENT_BOX(true),
    PHANTOM(false),
    ;

    public final boolean included;

    BoxSizing(boolean included) {
        this.included = included;
    }

    public static boolean shouldIncludeWidget(IWidget widget) {
        if (widget instanceof ISizedBox) {
            return ((ISizedBox) widget).getBoxSizing().included;
        }
        return false;
    }
}
