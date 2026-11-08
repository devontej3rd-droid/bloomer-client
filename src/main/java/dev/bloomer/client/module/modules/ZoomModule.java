package dev.bloomer.client.module.modules;

import dev.bloomer.client.module.ClientModule;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

/**
 * Zoom module based on the hold-to-zoom behavior in Enough Visuals v2.6.
 */
public final class ZoomModule extends ClientModule {
    private float currentMultiplier = 1.0F;
    private float previousMultiplier = 1.0F;
    private float zoomFactor = 4.0F;

    public ZoomModule() {
        super("Zoom", "Hold C to smoothly zoom the camera", Category.VISUALS);
    }

    @Override
    public void onTick(net.minecraft.client.MinecraftClient client) {
        if (client.player == null || client.getWindow() == null) {
            currentMultiplier = previousMultiplier = 1.0F;
            return;
        }
        previousMultiplier = currentMultiplier;
        boolean held = isEnabled() && client.currentScreen == null
                && GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_C) == GLFW.GLFW_PRESS;
        float target = held ? 1.0F / zoomFactor : 1.0F;
        currentMultiplier += (target - currentMultiplier) * 0.35F;
    }

    public float getMultiplier(float tickDelta) {
        return MathHelper.lerp(tickDelta, previousMultiplier, currentMultiplier);
    }

    @Override
    protected void onDisable() {
        currentMultiplier = previousMultiplier = 1.0F;
    }
}
