package xyz.crystallizedprison.generators.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.crystallizedprison.generators.Generator;
import xyz.crystallizedprison.generators.Generators;
import xyz.crystallizedprison.generators.guis.GeneratorGUI;

import javax.security.auth.login.Configuration;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager implements Listener {

    public InventoryManager(Generators main) {
        this.main = main;
    }

    Generators main;


    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event){
        if (event.getInventory().getTitle().equals(ChatColor.AQUA +"Generator")){
            event.setCancelled(true);
            
            Player player = (Player) event.getWhoClicked(); // The player that clicked the item
            ItemStack clicked = event.getCurrentItem(); // The item that was clicked
            Inventory inventory = event.getInventory();
            
            Generator Gen = main.getOpenGUIS().get(player);

            if(clicked.getType().equals(Material.HOPPER)){

                main.getEcon().depositPlayer(player,Gen.getMoneydue());

                player.sendMessage(ChatColor.AQUA + "You have Been Given "+ ChatColor.DARK_AQUA + main.getEcon().format(Gen.getMoneydue()));
                Gen.setMoneydue(0);
                main.SaveGen(Gen);
                ItemStack Upgrade = new ItemStack(Material.HOPPER, 1);
                ItemMeta UpgradeMeta = Upgrade.getItemMeta();
                UpgradeMeta.setDisplayName("§bCollect");

                ArrayList<String> lore = new ArrayList<String>();
                lore.add(ChatColor.AQUA + "Money: $" + ChatColor.DARK_AQUA + Gen.getMoneydue());

                UpgradeMeta.setLore(lore);
                Upgrade.setItemMeta(UpgradeMeta);
                inventory.setItem(22,Upgrade);



            }
            else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Speed")){
                if(Gen.getSpeedLevel() != 9){
                    if(main.getTe().getTokens(player) < (main.getConfig().getInt("Gens."+Gen.getType()+".GenPerSpeedCost") * (Gen.getSpeedLevel()+1) ) ){
                        player.sendMessage(ChatColor.AQUA + "You Don't have Enough Money To buy this upgrade. You need " + ChatColor.DARK_AQUA + (main.getConfig().getInt("Gens."+Gen.getType()+".GenPerSpeedCost") * (Gen.getSpeedLevel()+1) ) + " Tokens");
                    }else{
                        main.getTe().removeTokens(player,(main.getConfig().getInt("Gens."+Gen.getType()+".GenPerSpeedCost") * (Gen.getSpeedLevel()+1) ));
                        Gen.setSpeedLevel(Gen.getSpeedLevel()+1);
                        main.SaveGen(Gen);
                        player.closeInventory();
                        player.sendMessage(ChatColor.AQUA + "Your Generator Is now Level "+ (Gen.getSpeedLevel() + 1) );
                        GeneratorGUI.Open(player, Gen,main);
                    }
                }else{
                    player.sendMessage(ChatColor.AQUA + "You have Reached The max level");
                }
            }else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Money")){
                if(Gen.getMoneyLevel() != 9){
                    if(main.getTe().getTokens(player) < (main.getConfig().getInt("Gens."+Gen.getType()+ ".GenPerpriceCost") * (Gen.getMoneyLevel()+1) ) ){
                        player.sendMessage(ChatColor.AQUA + "You Don't have Enough Money To buy this upgrade. You need " + ChatColor.DARK_AQUA + (main.getConfig().getInt("Gens."+Gen.getType()+".GenPerpriceCost") * (Gen.getSpeedLevel()+1) ) + " Tokens");
                    }else{
                        main.getTe().removeTokens(player,(main.getConfig().getInt("Gens."+Gen.getType()+".GenPerpriceCost") * (Gen.getMoneyLevel()+1) ));
                        Gen.setMoneyLevel(Gen.getMoneyLevel()+1);
                        main.SaveGen(Gen);
                        player.closeInventory();
                        player.sendMessage(ChatColor.AQUA + "Your Generator Is now Level "+ (Gen.getMoneyLevel() + 1) );
                        GeneratorGUI.Open(player, Gen,main);
                    }
                }else{
                    player.sendMessage(ChatColor.AQUA + "You have Reached The max level");
                }
            }
            else if(clicked.getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "PickUp")) {

                if (player.getInventory().firstEmpty() != -1) {


                    Location location = Gen.getLoc();
                    main.getOpenGUIS().remove(player);
                    Gen.getArmourStand().remove();
                    player.closeInventory();
                    List<Generator> gens = main.getGenList();
                    gens.remove(Gen);
                    main.setGenList(gens);
                    main.getGenLoactions().get().set("Gens."+Gen.getLoc().getBlockX()+Gen.getLoc().getBlockY()+Gen.getLoc().getBlockZ(),null);
                    main.getGenLoactions().save();

                    location.getWorld().getBlockAt(location).setType(Material.AIR);

                    ItemStack item = new ItemStack(Material.getMaterial(main.getConfig().getString("Gens." + Gen.getType() + ".SpawnEGGMaterial")));
                    ItemMeta meta = item.getItemMeta();

                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Gens." + Gen.getType() + ".SpawnEGGName")));

                    List<String> lore = main.getConfig().getStringList("Gens." + Gen.getType() + ".SpawnEGGLore");
                    lore.add(ChatColor.AQUA + "Speed Level: " + ChatColor.DARK_AQUA + (Gen.getSpeedLevel() + 1));
                    lore.add(ChatColor.AQUA + "Price Level: " + ChatColor.DARK_AQUA + (Gen.getMoneyLevel() + 1));
                    lore.add(ChatColor.AQUA + "BlocksMined: " + ChatColor.DARK_AQUA + Gen.getBlocksMined());
                    meta.setLore(lore);


                    item.setItemMeta(meta);

                    player.getInventory().setItem(player.getInventory().firstEmpty(), item);

                } else {
                    player.sendMessage(ChatColor.AQUA + "Please Have a Empty Inventory Slot");
                }
            }
        }
    }
}
