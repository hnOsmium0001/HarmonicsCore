package powerlessri.harmonics.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import powerlessri.harmonics.HarmonicsCore;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public final class Render2D {

    public static final float REGULAR_WINDOW_Z = 0F;
    public static final float POPUP_WINDOW_Z = 128F;
    public static final float CONTEXT_MENU_Z = 200F;

    public static final Identifier INVALID_TEXTURE = new Identifier(HarmonicsCore.MODID, "textures/gui/invalid.png");
    public static final Identifier COMPONENTS = new Identifier(HarmonicsCore.MODID, "textures/gui/default_components.png");
    public static final Identifier DELETE = new Identifier(HarmonicsCore.MODID, "textures/gui/icons/delete.png");
    public static final Identifier CUT = new Identifier(HarmonicsCore.MODID, "textures/gui/icons/cut.png");
    public static final Identifier COPY = new Identifier(HarmonicsCore.MODID, "textures/gui/icons/copy.png");
    public static final Identifier PASTE = new Identifier(HarmonicsCore.MODID, "textures/gui/icons/paste.png");
    public static final Identifier BACK = new Identifier(HarmonicsCore.MODID, "textures/gui/icons/back.png");
    public static final Identifier CLOSE = new Identifier(HarmonicsCore.MODID, "textures/gui/icons/close.png");
    public static final Identifier ITEM_SLOT = new Identifier(HarmonicsCore.MODID, "textures/gui/icons/item_slot.png");

    private Render2D() {
    }

    public static boolean isInside(int x, int y, int mx, int my) {
        return isInside(x, y, 0, 0, mx, my);
    }

    public static boolean isInside(int x, int y, int bx1, int by1, int bx2, int by2) {
        return x >= bx1 && x < bx2 && y >= by1 && y < by2;
    }

    public static MinecraftClient minecraft() {
        return MinecraftClient.getInstance();
    }

    public static int scaledWidth() {
        return MinecraftClient.getInstance().window.getScaledWidth();
    }

    public static int scaledHeight() {
        return MinecraftClient.getInstance().window.getScaledHeight();
    }

    public static TextRenderer fontRenderer() {
        return MinecraftClient.getInstance().textRenderer;
    }

    public static int fontHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    public static void color(int color) {
        int alpha = (color >> 24) & 255;
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;
        GlStateManager.color4f(red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }

    public static void bindTexture(Identifier texture) {
        MinecraftClient.getInstance().getTextureManager().bindTexture(texture);
    }

    public static void beginColoredQuad() {
        Tessellator.getInstance().getBufferBuilder().begin(GL_QUADS, VertexFormats.POSITION_COLOR);
    }

    public static void beginTexturedQuad() {
        Tessellator.getInstance().getBufferBuilder().begin(GL_QUADS, VertexFormats.POSITION_UV);
    }

    public static void draw() {
        Tessellator.getInstance().draw();
    }

    public static void quad(BufferBuilder buffer, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, float z, int color) {
        int alpha = (color >> 24) & 255;
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;
        buffer.vertex(x1, y1, z).color(red, green, blue, alpha).end();
        buffer.vertex(x2, y2, z).color(red, green, blue, alpha).end();
        buffer.vertex(x3, y3, z).color(red, green, blue, alpha).end();
        buffer.vertex(x4, y4, z).color(red, green, blue, alpha).end();
    }

    public static void coloredRect(Point position, Dimension dimensions, float z, int color) {
        coloredRect(position.x, position.y, dimensions.width, dimensions.height, z, color);
    }

    public static void coloredRect(int x, int y, Dimension dimensions, float z, int color) {
        coloredRect(x, y, dimensions.width, dimensions.height, z, color);
    }

    public static void coloredRect(Point position, int width, int height, float z, int color) {
        coloredRect(position.x, position.y, width, height, z, color);
    }

    public static void coloredRect(int x1, int y1, int x2, int y2, float z, int color) {
        int alpha = (color >> 24) & 255;
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;
        BufferBuilder renderer = Tessellator.getInstance().getBufferBuilder();
        renderer.vertex(x1, y1, z).color(red, green, blue, alpha).end();
        renderer.vertex(x1, y2, z).color(red, green, blue, alpha).end();
        renderer.vertex(x2, y2, z).color(red, green, blue, alpha).end();
        renderer.vertex(x2, y1, z).color(red, green, blue, alpha).end();
    }

    public static void coloredRect(int x1, int y1, int x2, int y2, int color) {
        coloredRect(x1, y1, x2, y2, 0F, color);
    }

    public static void verticalGradientRect(int x1, int y1, int x2, int y2, float z, int color1, int color2) {
        int a1 = (color1 >> 24) & 255;
        int r1 = (color1 >> 16) & 255;
        int g1 = (color1 >> 8) & 255;
        int b1 = color1 & 255;
        int a2 = (color2 >> 24) & 255;
        int r2 = (color2 >> 16) & 255;
        int g2 = (color2 >> 8) & 255;
        int b2 = color2 & 255;

        BufferBuilder buffer = Tessellator.getInstance().getBufferBuilder();
        buffer.vertex(x2, y1, z).color(r1, g1, b1, a1).end();
        buffer.vertex(x1, y1, z).color(r1, g1, b1, a1).end();
        buffer.vertex(x1, y2, z).color(r2, g2, b2, a2).end();
        buffer.vertex(x2, y2, z).color(r2, g2, b2, a2).end();
    }

    public static void horizontalGradientRect(int x1, int y1, int x2, int y2, float z, int color1, int color2) {
        int a1 = (color1 >> 24) & 255;
        int r1 = (color1 >> 16) & 255;
        int g1 = (color1 >> 8) & 255;
        int b1 = color1 & 255;
        int a2 = (color2 >> 24) & 255;
        int r2 = (color2 >> 16) & 255;
        int g2 = (color2 >> 8) & 255;
        int b2 = color2 & 255;

        BufferBuilder buffer = Tessellator.getInstance().getBufferBuilder();
        buffer.vertex(x1, y1, z).color(r1, g1, b1, a1).end();
        buffer.vertex(x1, y2, z).color(r1, g1, b1, a1).end();
        buffer.vertex(x2, y2, z).color(r2, g2, b2, a2).end();
        buffer.vertex(x2, y1, z).color(r2, g2, b2, a2).end();
    }

    public static void thickBeveledBox(int x1, int y1, int x2, int y2, float z, int thickness, int topLeftColor, int bottomRightColor, int fillColor) {
        coloredRect(x1, y1, x2, y2, z, bottomRightColor);
        coloredRect(x1, y1, x2 - thickness, y2 - thickness, z, topLeftColor);
        coloredRect(x1 + thickness, y1 + thickness, x2 - thickness, y2 - thickness, z, fillColor);
    }

    public static void textureVertices(int x1, int y1, int x2, int y2, float z, float u1, float v1, float u2, float v2) {
        BufferBuilder buffer = Tessellator.getInstance().getBufferBuilder();
        buffer.vertex(x1, y1, z).texture(u1, v1).end();
        buffer.vertex(x1, y2, z).texture(u1, v2).end();
        buffer.vertex(x2, y2, z).texture(u2, v2).end();
        buffer.vertex(x2, y1, z).texture(u2, v1).end();
    }

    public static void verticalLine(int x, int y1, int y2, float z) {
        glVertex3f(x, y1, z);
        glVertex3f(x, y2, z);
    }

    public static void horizontalLine(int x1, int x2, int y, float z) {
        glVertex3f(x1, y, z);
        glVertex3f(x2, y, z);
    }

    public static void completeTexture(int x1, int y1, int x2, int y2, float z, Identifier texture) {
        bindTexture(texture);
        textureVertices(x1, y1, x2, y2, z, 0.0F, 0.0F, 1.0F, 1.0F);
    }

    public static void completeTexture(int x1, int y1, int x2, int y2, Identifier texture) {
        completeTexture(x1, y1, x2, y2, 0F, texture);
    }

    public static int computeCenterY(int top, int bottom, int height) {
        return top + (bottom - top) / 2 - height / 2;
    }

    public static int computeBottomY(int bottom, int height) {
        return bottom - height;
    }

    public static int computeCenterX(int left, int right, int width) {
        return left + (right - left) / 2 - width / 2;
    }

    public static int computeRightX(int right, int width) {
        return right - width;
    }

    public static int getXForHorizontallyCenteredText(String text, int left, int right) {
        return getXForHorizontallyCenteredText(TextRenderers.vanilla(), text, left, right);
    }

    public static int getXForHorizontallyCenteredText(ITextRenderer textRenderer, String text, int left, int right) {
        int textWidth = textRenderer.calculateWidth(text);
        return computeCenterX(left, right, textWidth);
    }

    public static int getYForVerticallyCenteredText(int top, int bottom) {
        return getYForVerticallyCenteredText(TextRenderers.vanilla(), top, bottom);
    }

    public static int getYForVerticallyCenteredText(ITextRenderer textRenderer, int top, int bottom) {
        return computeCenterY(top, bottom, (int) textRenderer.getFontHeight());
    }

    public static void renderVerticallyCenteredText(String text, int leftX, int top, int bottom, float z, int color) {
        int y = getYForVerticallyCenteredText(top, bottom);
        GlStateManager.pushMatrix();
        GlStateManager.translatef(0F, 0F, z + 0.1F);
        fontRenderer().draw(text, leftX, y, color);
        GlStateManager.popMatrix();
    }

    public static void renderVerticallyCenteredText(ITextRenderer textRenderer, String text, int leftX, int top, int bottom, float z) {
        int y = getYForVerticallyCenteredText(textRenderer, top, bottom);
        textRenderer.renderText(text, leftX, y, z);
    }

    public static void renderHorizontallyCenteredText(String text, int left, int right, int topY, float z, int color) {
        int x = getXForHorizontallyCenteredText(text, left, right);
        GlStateManager.pushMatrix();
        GlStateManager.translatef(0F, 0F, z + 0.1F);
        fontRenderer().draw(text, x, topY, color);
        GlStateManager.popMatrix();
    }

    public static void renderHorizontallyCenteredText(ITextRenderer textRenderer, String text, int left, int right, int topY, float z) {
        int x = getXForHorizontallyCenteredText(textRenderer, text, left, right);
        textRenderer.renderText(text, x, topY, z);
    }

    public static void renderCenteredText(String text, int top, int bottom, int left, int right, float z, int color) {
        int x = getXForHorizontallyCenteredText(text, left, right);
        int y = getYForVerticallyCenteredText(top, bottom);
        GlStateManager.pushMatrix();
        GlStateManager.translatef(0F, 0F, z + 0.1F);
        fontRenderer().draw(text, x, y, color);
        GlStateManager.popMatrix();
    }

    public static void renderCenteredText(ITextRenderer textRenderer, String text, int top, int bottom, int left, int right, float z) {
        int x = getXForHorizontallyCenteredText(textRenderer, text, left, right);
        int y = getYForVerticallyCenteredText(textRenderer, top, bottom);
        textRenderer.renderText(text, x, y, z);
    }

    public static void useGradientGLStates() {
        GlStateManager.disableTexture();
        GlStateManager.disableAlphaTest();
        GlStateManager.enableBlend();
        GlStateManager.shadeModel(GL_SMOOTH);
        GlStateManager.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
    }

    public static void useBlendingGLStates() {
        GlStateManager.disableTexture();
        GlStateManager.disableAlphaTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
    }

    public static void usePlainColorGLStates() {
        GlStateManager.disableTexture();
        GlStateManager.disableBlend();
    }

    public static void useTextureGLStates() {
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
        GlStateManager.color3f(1F, 1F, 1F);
    }
}
