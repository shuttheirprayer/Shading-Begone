package com.misanthropy.linggango.shading_begone.client;

import com.misanthropy.linggango.shading_begone.ShadingConfig;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class IrisShaderState {

    private static final Logger LOGGER = LogManager.getLogger(IrisShaderState.class);

    private IrisShaderState() {
    }

    public static void register() {
        try {
            IrisApi.getInstance();
        } catch (Throwable t) {
            LOGGER.warn("shading_begone: Iris API unavailable, shader pack detection disabled", t);
            return;
        }
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) {
                poll();
            }
        });
    }

    private static void poll() {
        boolean inUse = IrisApi.getInstance().isShaderPackInUse();
        if (inUse == ShadingConfig.INSTANCE.shadersActive) {
            return;
        }
        ShadingConfig.INSTANCE.shadersActive = inUse;
        ShadingConfig.updateEffectiveStates();

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.levelRenderer != null) {
            minecraft.levelRenderer.allChanged();
        }
    }
}
