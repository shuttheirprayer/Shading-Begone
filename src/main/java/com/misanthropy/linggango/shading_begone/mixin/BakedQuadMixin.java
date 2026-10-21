package com.misanthropy.linggango.shading_begone.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.misanthropy.linggango.shading_begone.ShadingConfig;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BakedQuad.class)
public class BakedQuadMixin {

    @ModifyReturnValue(method = "isShade", at = @At("RETURN"))
    private boolean shading_begone$removeShade(boolean original) {
        return original && ShadingConfig.blockShadingEffective;
    }
}
