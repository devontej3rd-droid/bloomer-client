package dev.bloomer.client.ui;

import dev.bloomer.client.module.ClientModule;
import dev.bloomer.client.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class BloomerScreen extends Screen {
    private static final int PANEL_WIDTH = 390;
    private static final int PANEL_HEIGHT = 246;
    private static int opacity = 218;
    private int left, top;
    private boolean draggingOpacity;

    public BloomerScreen() { super(Text.literal("Bloomer Client")); }

    @Override
    protected void init() {
        left = (width - PANEL_WIDTH) / 2;
        top = (height - PANEL_HEIGHT) / 2;
    }

    private int panelColor() { return (opacity << 24) | 0x10151E; }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // Deliberately no full-screen blur: it can bleed across panel edges and soften text.
        ctx.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xB8000000);
        ctx.fill(left + 1, top + 1, left + PANEL_WIDTH - 1, top + PANEL_HEIGHT - 1, panelColor());
        ctx.fill(left, top, left + PANEL_WIDTH, top + 2, 0xFF8CE6C0);
        ctx.fill(left, top, left + 112, top + PANEL_HEIGHT, 0x99101620);
        ctx.fill(left + 112, top + 48, left + 113, top + PANEL_HEIGHT - 14, 0x334D5968);

        ctx.drawText(textRenderer, "BLOOMER", left + 18, top + 17, 0xFFF1F5F8, false);
        ctx.drawText(textRenderer, "CLIENT  /  0.1.1", left + 18, top + 31, 0xFF8CE6C0, false);
        ctx.drawText(textRenderer, "VISUALS", left + 18, top + 66, 0xFF8CE6C0, false);
        ctx.fill(left + 12, top + 84, left + 100, top + 108, 0x553C9B78);
        ctx.drawText(textRenderer, "Modules", left + 22, top + 92, 0xFFFFFFFF, false);
        ctx.drawText(textRenderer, "SETTINGS", left + 18, top + 137, 0xFF778493, false);
        ctx.drawText(textRenderer, "Interface", left + 22, top + 158, 0xFFD5DCE4, false);

        ctx.drawText(textRenderer, "Visual modules", left + 132, top + 19, 0xFFF1F5F8, false);
        ctx.drawText(textRenderer, "Toggle a module to enable or disable it.", left + 132, top + 34, 0xFF8996A5, false);

        int y = top + 66;
        for (ClientModule module : ModuleManager.getModules()) {
            boolean active = module.isEnabled();
            int rowColor = active ? 0x443C9B78 : 0x332A3440;
            ctx.fill(left + 130, y, left + PANEL_WIDTH - 18, y + 34, rowColor);
            ctx.fill(left + 130, y, left + 132, y + 34, active ? 0xFF8CE6C0 : 0xFF536171);
            ctx.drawText(textRenderer, module.getName(), left + 144, y + 7, 0xFFE8EDF2, false);
            ctx.drawText(textRenderer, active ? "ON" : "OFF", left + PANEL_WIDTH - 58, y + 7,
                    active ? 0xFF8CE6C0 : 0xFF9AA5B1, false);
            y += 42;
        }

        int sliderLeft = left + 204, sliderRight = left + PANEL_WIDTH - 26;
        int sliderY = top + PANEL_HEIGHT - 30;
        ctx.drawText(textRenderer, "Panel opacity", left + 132, sliderY - 2, 0xFFB7C1CC, false);
        ctx.fill(sliderLeft, sliderY + 3, sliderRight, sliderY + 7, 0xFF46515E);
        int knob = sliderLeft + (sliderRight - sliderLeft) * opacity / 255;
        ctx.fill(sliderLeft, sliderY + 3, knob, sliderY + 7, 0xFF8CE6C0);
        ctx.fill(knob - 2, sliderY, knob + 3, sliderY + 11, 0xFFE8FFF5);
        ctx.drawText(textRenderer, (opacity * 100 / 255) + "%", sliderRight - 26, sliderY - 12, 0xFF8CE6C0, false);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int y = top + 66;
            for (ClientModule module : ModuleManager.getModules()) {
                if (mouseX >= left + 130 && mouseX <= left + PANEL_WIDTH - 18
                        && mouseY >= y && mouseY <= y + 34) {
                    module.toggle();
                    return true;
                }
                y += 42;
            }
            int sliderY = top + PANEL_HEIGHT - 30;
            if (mouseY >= sliderY - 5 && mouseY <= sliderY + 14
                    && mouseX >= left + 204 && mouseX <= left + PANEL_WIDTH - 26) {
                draggingOpacity = true;
                updateOpacity(mouseX);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingOpacity && button == 0) {
            updateOpacity(mouseX);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) draggingOpacity = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void updateOpacity(double mouseX) {
        double ratio = (mouseX - (left + 204.0)) / (PANEL_WIDTH - 230.0);
        opacity = Math.max(70, Math.min(255, (int) Math.round(ratio * 255)));
    }

    @Override public boolean shouldPause() { return false; }
}
