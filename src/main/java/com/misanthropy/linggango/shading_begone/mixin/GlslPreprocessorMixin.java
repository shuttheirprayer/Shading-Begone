package com.misanthropy.linggango.shading_begone.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.misanthropy.linggango.shading_begone.ShadingConfig;
import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GlslPreprocessor.class)
public class GlslPreprocessorMixin {

    @Unique
    private static final Logger shading_begone$LOGGER = LogManager.getLogger("shading_begone");

    @Unique
    private static final String shading_begone$SEARCH =
            "vec4 minecraft_mix_light(vec3 lightDir0, vec3 lightDir1, vec3 normal, vec4 color) {";

    @Unique
    private static final String shading_begone$REPLACEMENT = """
            #define minecraft_mix_light(lightDir0, lightDir1, normal, color) (ProjMat[3].x != -1.0 ? color : minecraft_mix_light_helper(lightDir0, lightDir1, normal, color))
            vec4 minecraft_mix_light_helper(vec3 lightDir0, vec3 lightDir1, vec3 normal, vec4 color) {
            """;

    @WrapOperation(
            method = "processImports",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/preprocessor/GlslPreprocessor;applyImport(ZLjava/lang/String;)Ljava/lang/String;"
            )
    )
    private String shading_begone$modifyLightingCalculations(
            GlslPreprocessor instance,
            boolean quotesUsed,
            String file,
            Operation<String> original
    ) {
        String source = original.call(instance, quotesUsed, file);
        if (source == null || quotesUsed || ShadingConfig.entityShadingEffective || !"light.glsl".equals(file)) {
            return source;
        }
        int index = source.indexOf(shading_begone$SEARCH);
        if (index < 0) {
            shading_begone$LOGGER.warn("shading_begone: light.glsl pattern not found, entity shading patch skipped");
            return source;
        }
        return source.substring(0, index)
                + shading_begone$REPLACEMENT
                + source.substring(index + shading_begone$SEARCH.length());
    }
}
