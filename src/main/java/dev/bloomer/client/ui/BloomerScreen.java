package dev.bloomer.client.ui;

import dev.bloomer.client.module.ClientModule;
import dev.bloomer.client.module.ModuleManager;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public final class BloomerScreen extends Screen {
    private static final int PANEL_WIDTH = 470;
    private static final int PANEL_HEIGHT = 300;
    private static int opacity = 224;
    private int left, top, scroll;
    private boolean draggingOpacity;
    private ClientModule.Category selected = ClientModule.Category.VISUALS;
    private TextFieldWidget search;

    public BloomerScreen() { super(Text.literal("Bloomer Client")); }

    @Override
    protected void init() {
        left = (width - PANEL_WIDTH) / 2;
        top = (height - PANEL_HEIGHT) / 2;
        search = new TextFieldWidget(textRenderer, left + 166, top + 48, 282, 20, Text.literal("Search modules"));
        search.setPlaceholder(Text.literal("Search modules..."));
        search.setMaxLength(48);
        addDrawableChild(search);
    }

    private int panelColor() { return (opacity << 24) | 0x10151E; }

    private List<ClientModule> visibleModules() {
        String query = search == null ? "" : search.getText().toLowerCase(Locale.ROOT);
        return ModuleManager.getModules().stream()
                .filter(m -> m.getCategory() == selected)
                .filter(m -> query.isBlank() || m.getName().toLowerCase(Locale.ROOT).contains(query)
                        || m.getDescription().toLowerCase(Locale.ROOT).contains(query))
                .toList();
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xB8000000);
        ctx.fill(left + 1, top + 1, left + PANEL_WIDTH - 1, top + PANEL_HEIGHT - 1, panelColor());
        ctx.fill(left, top, left + PANEL_WIDTH, top + 2, 0xFF8CE6C0);
        ctx.fill(left, top, left + 142, top + PANEL_HEIGHT, 0x99101620);
        ctx.fill(left + 142, top + 43, left + 143, top + PANEL_HEIGHT - 16, 0x334D5968);

        ctx.drawText(textRenderer, "BLOOMER", left + 18, top + 16, 0xFFF1F5F8, false);
        ctx.drawText(textRenderer, "CLIENT  /  0.1.2", left + 18, top + 31, 0xFF8CE6C0, false);
        ctx.drawText(textRenderer, "CATEGORIES", left + 16, top + 67, 0xFF778493, false);

        ClientModule.Category[] categories = ClientModule.Category.values();
        String[] labels = {"Visuals", "World", "HUD", "Misc"};
        for (int i = 0; i < categories.length; i++) {
            int y = top + 82 + i * 34;
            boolean active = selected == categories[i];
            if (active) ctx.fill(left + 10, y - 5, left + 130, y + 22, 0x553C9B78);
            ctx.fill(left + 10, y - 5, left + 12, y + 22, active ? 0xFF8CE6C0 : 0xFF536171);
            ctx.drawText(textRenderer, labels[i], left + 22, y + 3, active ? 0xFFFFFFFF : 0xFFB7C1CC, false);
        }

        ctx.drawText(textRenderer, "Visual modules", left + 164, top + 17, 0xFFF1F5F8, false);
        ctx.drawText(textRenderer, "Search, filter and toggle client-side modules.", left + 164, top + 31, 0xFF8996A5, false);

        int y = top + 82 - scroll;
        for (ClientModule module : visibleModules()) {
            if (y + 34 > top + 76 && y < top + PANEL_HEIGHT - 48) {
                boolean active = module.isEnabled();
                ctx.fill(left + 160, y, left + PANEL_WIDTH - 18, y + 34, active ? 0x443C9B78 : 0x332A3440);
                ctx.fill(left + 160, y, left + 162, y + 34, active ? 0xFF8CE6C0 : 0xFF536171);
                ctx.drawText(textRenderer, module.getName(), left + 174, y + 5, 0xFFE8EDF2, false);
                ctx.drawText(textRenderer, module.getDescription(), left + 174, y + 19, 0xFF8996A5, false);
                ctx.drawText(textRenderer, active ? "ON" : "OFF", left + PANEL_WIDTH - 52, y + 11,
                        active ? 0xFF8CE6C0 : 0xFF9AA5B1, false);
            }
            y += 42;
        }

        int sliderLeft = left + 274, sliderRight = left + PANEL_WIDTH - 28;
        int sliderY = top + PANEL_HEIGHT - 29;
        ctx.drawText(textRenderer, "Opacity", left + 160, sliderY - 2, 0xFFB7C1CC, false);
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
            for (int i = 0; i < ClientModule.Category.values().length; i++) {
                int y = top + 77 + i * 34;
                if (mouseX >= left + 10 && mouseX <= left + 130 && mouseY >= y && mouseY <= y + 27) {
                    selected = ClientModule.Category.values()[i];
                    scroll = 0;
                    return true;
                }
            }
            int y = top + 82 - scroll;
            for (ClientModule module : visibleModules()) {
                if (mouseX >= left + 160 && mouseX <= left + PANEL_WIDTH - 18 && mouseY >= y && mouseY <= y + 34) {
                    module.toggle();
                    return true;
                }
                y += 42;
            }
            int sliderY = top + PANEL_HEIGHT - 29;
            if (mouseY >= sliderY - 5 && mouseY <= sliderY + 14
                    && mouseX >= left + 274 && mouseX <= left + PANEL_WIDTH - 28) {
                draggingOpacity = true;
                updateOpacity(mouseX);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingOpacity && button == 0) { updateOpacity(mouseX); return true; }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) draggingOpacity = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scroll = Math.max(0, Math.min(Math.max(0, visibleModules().size() * 42 - 170),
                scroll - (int) (verticalAmount * 24)));
        return true;
    }

    private void updateOpacity(double mouseX) {
        double ratio = (mouseX - (left + 274.0)) / (PANEL_WIDTH - 302.0);
        opacity = Math.max(70, Math.min(255, (int) Math.round(ratio * 255)));
    }

    @Override public boolean shouldPause() { return false; }
}
