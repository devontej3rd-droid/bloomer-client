package dev.bloomer.client.module;

import dev.bloomer.client.module.modules.FullbrightModule;
import dev.bloomer.client.module.modules.ZoomModule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public final class ModuleManager {
    private static final List<ClientModule> MODULES = new ArrayList<>();
    private static boolean registered;

    private ModuleManager() { }

    public static void registerDefaults() {
        if (registered) return;
        registered = true;

        MODULES.add(new FullbrightModule());
        MODULES.add(new ZoomModule());

        ClientTickEvents.END_CLIENT_TICK.register(client ->
                MODULES.forEach(module -> module.onTick(client)));
        HudRenderCallback.EVENT.register((drawContext, tickDelta) ->
                MODULES.forEach(module -> module.onHudRender(drawContext)));
    }

    public static List<ClientModule> getModules() {
        return Collections.unmodifiableList(MODULES);
    }
}
