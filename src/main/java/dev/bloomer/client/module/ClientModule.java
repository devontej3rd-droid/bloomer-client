package dev.bloomer.client.module;

import net.minecraft.client.gui.DrawContext;

public abstract class ClientModule {
    private final String name;
    private boolean enabled;

    protected ClientModule(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public boolean isEnabled() { return enabled; }
    public void toggle() { setEnabled(!enabled); }

    public void setEnabled(boolean value) {
        if (enabled == value) return;
        enabled = value;
        if (value) onEnable();
        else onDisable();
    }

    protected void onEnable() { }
    protected void onDisable() { }
    public void onHudRender(DrawContext context, float tickDelta) { }
}
