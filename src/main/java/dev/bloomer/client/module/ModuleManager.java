package dev.bloomer.client.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public final class ModuleManager {
    private static final List<ClientModule> MODULES = new ArrayList<>();
    private static boolean registered;

    private ModuleManager() { }

    public static void registerDefaults() {
        if (registered) return;
        registered = true;

        MODULES.add(new ClientModule("HUD Test") {
            @Override
            public void onHudRender(net.minecraft.client.gui.DrawContext context) {
                if (isEnabled()) {
                    var client = net.minecraft.client.MinecraftClient.getInstance();
                    context.drawText(client.textRenderer,
                            "BLOOMER | TEST MODULE ACTIVE", 8, 8, 0xFF8CE6C0, true);
                }
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) ->
                MODULES.forEach(module -> module.onHudRender(drawContext)));
    }

    public static List<ClientModule> getModules() {
        return Collections.unmodifiableList(MODULES);
    }
}
