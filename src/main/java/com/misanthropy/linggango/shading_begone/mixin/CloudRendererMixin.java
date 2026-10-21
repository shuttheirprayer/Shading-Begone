package com.misanthropy.linggango.shading_begone.mixin;

import com.misanthropy.linggango.shading_begone.ShadingConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.immediate.CloudRenderer", remap = false)
public class CloudRendererMixin {

    @Shadow
    private int cachedRenderDistance;

    @ModifyArg(
            method = "rebuildGeometry",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/api/util/ColorMixer;mul(II)I",
                    remap = false
            ),
            index = 1,
            remap = false
    )
    private int redirectCloudColorMix(int originalShadingColor) {
        return ShadingConfig.cloudShadingEffective ? originalShadingColor : -1;
    }

    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void shading_begone$invalidateCloudGeometry(ClientLevel level, LocalPlayer player, PoseStack poseStack,
                                                        Matrix4f projectionMatrix, float ticks, float tickDelta,
                                                        double cameraX, double cameraY, double cameraZ,
                                                        CallbackInfo ci) {
        if (ShadingConfig.cloudGeometryDirty) {
            ShadingConfig.cloudGeometryDirty = false;
            this.cachedRenderDistance = -1;
        }
    }
}
