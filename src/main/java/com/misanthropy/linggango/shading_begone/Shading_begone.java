package com.misanthropy.linggango.shading_begone;

import com.misanthropy.linggango.shading_begone.client.IrisShaderState;
import com.misanthropy.linggango.shading_begone.client.ShadingEmbeddiumOptions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Shading_begone.MODID)
public class Shading_begone {

    public static final String MODID = "shading_begone";

    public Shading_begone() {
        ShadingConfig.load();

        if (FMLEnvironment.dist != Dist.CLIENT) {
            return;
        }
        if (ModList.get().isLoaded("embeddium")) {
            ShadingEmbeddiumOptions.register();
        }
        if (ModList.get().isLoaded("oculus") || ModList.get().isLoaded("iris")) {
            IrisShaderState.register();
        }
    }
}
