package net.dev.art.core;

import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftChest;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Test {

    public void event(BlockBreakEvent event) {

        if (!event.getBlock().getDrops().isEmpty()) {
            ItemStack itemStack = new ArrayList<ItemStack>(event.getBlock().getDrops()).get(0);
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()){
                //Tem nome
            }
        }
    }

}
