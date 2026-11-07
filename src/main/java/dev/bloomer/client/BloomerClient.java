package dev.bloomer.client;

import dev.bloomer.client.module.ModuleManager;
import dev.bloomer.client.ui.BloomerScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class BloomerClient implements ClientModInitializer {
    private KeyBinding openGui;

    @Override
    public void onInitializeClient() {
        ModuleManager.registerDefaults();
        openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bloomer.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.bloomer"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGui.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new BloomerScreen());
                } else if (client.currentScreen instanceof BloomerScreen) {
                    client.setScreen(null);
                }
            }
        });
    }
}
