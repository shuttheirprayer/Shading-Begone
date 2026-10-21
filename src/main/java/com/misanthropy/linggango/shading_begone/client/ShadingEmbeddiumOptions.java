package com.misanthropy.linggango.shading_begone.client;

import com.misanthropy.linggango.shading_begone.Shading_begone;
import com.misanthropy.linggango.shading_begone.ShadingConfig;
import me.jellysquid.mods.sodium.client.gui.options.OptionFlag;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.api.OptionPageConstructionEvent;
import org.embeddedt.embeddium.client.gui.options.StandardOptions;

public final class ShadingEmbeddiumOptions {

    private static final OptionStorage<ShadingConfig> STORAGE = new OptionStorage<>() {
        @Override
        public ShadingConfig getData() {
            return ShadingConfig.INSTANCE;
        }

        @Override
        public void save() {
            ShadingConfig.save();
        }
    };

    private ShadingEmbeddiumOptions() {
    }

    public static void register() {
        OptionPageConstructionEvent.BUS.addListener(ShadingEmbeddiumOptions::onPageBuilt);
    }

    private static void onPageBuilt(OptionPageConstructionEvent event) {
        if (!event.getId().matches(StandardOptions.Pages.QUALITY)) {
            return;
        }

        var blockShading = OptionImpl.createBuilder(Boolean.class, STORAGE)
                .setId(new ResourceLocation(Shading_begone.MODID, "block_shading"))
                .setName(Component.literal("Block Shading"))
                .setTooltip(Component.literal(
                        "Vanilla directional shading on blocks and fluids: north/south faces darkened to 80%, "
                                + "east/west to 60%, bottom faces to 50%. Off flattens every face to full "
                                + "brightness. Ignored while a shader pack is loaded."))
                .setControl(TickBoxControl::new)
                .setBinding((config, value) -> {
                            config.blockShadingEnabled = value;
                            ShadingConfig.updateEffectiveStates();
                        }, config -> config.blockShadingEnabled)
                .setImpact(OptionImpact.LOW)
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build();

        var cloudShading = OptionImpl.createBuilder(Boolean.class, STORAGE)
                .setId(new ResourceLocation(Shading_begone.MODID, "cloud_shading"))
                .setName(Component.literal("Cloud Shading"))
                .setTooltip(Component.literal(
                        "Per-face shading on cloud geometry. Off makes clouds a flat white slab. "
                                + "Ignored while a shader pack is loaded."))
                .setControl(TickBoxControl::new)
                .setBinding((config, value) -> {
                            config.cloudShadingEnabled = value;
                            ShadingConfig.updateEffectiveStates();
                        }, config -> config.cloudShadingEnabled)
                .setImpact(OptionImpact.LOW)
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build();

        var entityShading = OptionImpl.createBuilder(Boolean.class, STORAGE)
                .setId(new ResourceLocation(Shading_begone.MODID, "entity_shading"))
                .setName(Component.literal("Entity Shading"))
                .setTooltip(Component.literal(
                        "Two-directional diffuse lighting applied to entities and held items in the world. "
                                + "Off leaves them at flat texture brightness; inventory item models keep their "
                                + "shading either way. Reloads resources when changed. "
                                + "Ignored while a shader pack is loaded."))
                .setControl(TickBoxControl::new)
                .setBinding((config, value) -> {
                            config.entityShadingEnabled = value;
                            ShadingConfig.updateEffectiveStates();
                        }, config -> config.entityShadingEnabled)
                .setImpact(OptionImpact.LOW)
                .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                .build();

        event.addGroup(OptionGroup.createBuilder()
                .setId(new ResourceLocation(Shading_begone.MODID, "shading"))
                .add(blockShading)
                .add(cloudShading)
                .add(entityShading)
                .build());
    }
}
