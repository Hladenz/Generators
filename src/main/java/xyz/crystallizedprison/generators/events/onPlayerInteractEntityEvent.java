package xyz.crystallizedprison.generators.events;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import xyz.crystallizedprison.generators.Generators;

public class onPlayerInteractEntityEvent implements Listener {

    public onPlayerInteractEntityEvent(Generators main) {
        this.main = main;
    }

    Generators main;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerArmorStandManipulateEvent event) {
        ArmorStand ArmStand = event.getRightClicked();

        if (ArmStand.getHelmet().equals(new ItemStack(Material.BEDROCK))){
            event.setCancelled(true);
        }else if (ArmStand.getHelmet().equals(new ItemStack(Material.IRON_BLOCK))){
            event.setCancelled(true);
        }else if (ArmStand.getHelmet().equals(new ItemStack(Material.GOLD_BLOCK))){
            event.setCancelled(true);
        }else if (ArmStand.getHelmet().equals(new ItemStack(Material.DIAMOND_BLOCK))){
            event.setCancelled(true);
        }else if (ArmStand.getHelmet().equals(new ItemStack(Material.OBSIDIAN))){
            event.setCancelled(true);
        }else if (ArmStand.getHelmet().equals(new ItemStack(Material.BEDROCK))){
            event.setCancelled(true);
        }

    }


}
