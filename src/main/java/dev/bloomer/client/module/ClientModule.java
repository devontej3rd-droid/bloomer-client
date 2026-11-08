package dev.bloomer.client.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class ClientModule {
    public enum Category { VISUALS, WORLD, HUD, MISC }

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;

    protected ClientModule(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    protected ClientModule(String name) {
        this(name, "Client visual module", Category.VISUALS);
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void toggle() { setEnabled(!enabled); }

    public void setEnabled(boolean value) {
        if (enabled == value) return;
        if (value && !canEnable()) return;
        enabled = value;
        if (value) onEnable();
        else onDisable();
    }

    protected boolean canEnable() { return true; }
    protected void onEnable() { }
    protected void onDisable() { }
    public void onTick(MinecraftClient client) { }
    public void onHudRender(DrawContext context) { }
}
