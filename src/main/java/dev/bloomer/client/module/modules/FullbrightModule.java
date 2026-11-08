package dev.bloomer.client.module.modules;

import dev.bloomer.client.module.ClientModule;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * Adapted from the Fullbright visual module in Enough Visuals v2.6.
 * Copyright and license notices are retained in the repository LICENSE.
 */
public final class FullbrightModule extends ClientModule {
    public FullbrightModule() {
        super("Fullbright", "Night vision effect for darker areas", Category.VISUALS);
    }

    @Override
    public void onTick(net.minecraft.client.MinecraftClient client) {
        if (isEnabled() && client.player != null) {
            client.player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.NIGHT_VISION, 200, 0, false, false, false));
        }
    }

    @Override
    protected void onDisable() {
        var player = net.minecraft.client.MinecraftClient.getInstance().player;
        if (player != null) player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}
