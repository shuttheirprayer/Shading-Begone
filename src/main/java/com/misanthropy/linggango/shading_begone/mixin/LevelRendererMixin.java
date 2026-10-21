package com.misanthropy.linggango.shading_begone.mixin;

import com.misanthropy.linggango.shading_begone.ShadingConfig;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @ModifyConstant(method = "buildClouds", constant = {
            @Constant(floatValue = 0.9F),
            @Constant(floatValue = 0.7F),
            @Constant(floatValue = 0.8F, ordinal = 0),
            @Constant(floatValue = 0.8F, ordinal = 1),
            @Constant(floatValue = 0.8F, ordinal = 2)
    })
    private float changeCloudBrightness(float original) {
        return ShadingConfig.cloudShadingEffective ? original : 1.0F;
    }
}
