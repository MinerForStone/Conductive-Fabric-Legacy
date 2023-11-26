package com.minerforstone.conductive;

import com.minerforstone.conductive.block.RedstoneComponent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Conductive implements ModInitializer {
    /**
     * Runs the mod initializer.
     */
    public static final Block REDSTONE_COMPONENT = new RedstoneComponent(FabricBlockSettings.create());

    @Override
    public void onInitialize() {
        // Blocks
        Registry.register(Registries.BLOCK, new Identifier("conductive", "redstone_component"), REDSTONE_COMPONENT);

        // Items
        Registry.register(Registries.ITEM, new Identifier("conductive", "redstone_component"), new BlockItem(REDSTONE_COMPONENT, new Item.Settings()));


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
            content.add(REDSTONE_COMPONENT);
        });
    }
}
