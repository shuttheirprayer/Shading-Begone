package com.misanthropy.linggango.shading_begone.mixin;

import com.misanthropy.linggango.shading_begone.ShadingConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer", remap = false)
public class FluidRendererMixin {

    @ModifyVariable(method = "updateQuad", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float changeShade(float brightness) {
        return ShadingConfig.blockShadingEffective ? brightness : 1.0F;
    }
}
