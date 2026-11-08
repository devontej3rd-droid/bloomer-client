package dev.bloomer.client.mixin;

import dev.bloomer.client.module.ModuleManager;
import dev.bloomer.client.module.modules.ZoomModule;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void bloomer$applyZoom(Camera camera, float tickDelta, boolean changingFov,
                                   CallbackInfoReturnable<Double> cir) {
        ZoomModule zoom = ModuleManager.getZoomModule();
        if (zoom != null && zoom.isEnabled()) {
            cir.setReturnValue(cir.getReturnValue() * zoom.getMultiplier(tickDelta));
        }
    }
}
