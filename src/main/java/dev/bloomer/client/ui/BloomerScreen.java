package dev.bloomer.client.ui;

import dev.bloomer.client.module.ClientModule;
import dev.bloomer.client.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class BloomerScreen extends Screen {
    private static final int PANEL_WIDTH = 250;
    private static final int PANEL_HEIGHT = 150;
    private int left;
    private int top;

    public BloomerScreen() {
        super(Text.literal("Bloomer Client"));
    }

    @Override
    protected void init() {
        left = (width - PANEL_WIDTH) / 2;
        top = (height - PANEL_HEIGHT) / 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xEE10141B);
        context.fill(left, top, left + PANEL_WIDTH, top + 25, 0xFF202A35);
        context.drawText(textRenderer, "BLOOMER CLIENT", left + 10, top + 9, 0xFFFFFFFF, true);
        context.drawText(textRenderer, "VISUALS", left + 10, top + 37, 0xFF8CE6C0, true);

        int y = top + 56;
        for (ClientModule module : ModuleManager.getModules()) {
            int color = module.isEnabled() ? 0xFF8CE6C0 : 0xFFB8C0CC;
            int background = module.isEnabled() ? 0x553C9B78 : 0x33202A35;
            context.fill(left + 8, y - 3, left + PANEL_WIDTH - 8, y + 18, background);
            context.drawText(textRenderer, module.getName(), left + 14, y + 3, color, false);
            context.drawText(textRenderer, module.isEnabled() ? "ON" : "OFF",
                    left + PANEL_WIDTH - 38, y + 3, color, false);
            y += 26;
        }

        context.drawText(textRenderer, "Right Shift closes this menu",
                left + 10, top + PANEL_HEIGHT - 17, 0xFF8993A1, false);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && mouseX >= left + 8 && mouseX <= left + PANEL_WIDTH - 8) {
            int row = (int) ((mouseY - (top + 53)) / 26);
            int rowTop = top + 53;
            if (row >= 0 && row < ModuleManager.getModules().size()
                    && mouseY >= rowTop
                    && mouseY < rowTop + ModuleManager.getModules().size() * 26) {
                ModuleManager.getModules().get(row).toggle();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
