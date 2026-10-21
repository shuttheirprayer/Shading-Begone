package com.misanthropy.linggango.shading_begone.mixin;

import com.misanthropy.linggango.shading_begone.ShadingConfig;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {

    @ModifyArg(
            method = "tesselate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/BlockAndTintGetter;getShade(Lnet/minecraft/core/Direction;Z)F"),
            index = 1
    )
    private boolean changeShade(boolean shade) {
        return shade && ShadingConfig.blockShadingEffective;
    }
}
