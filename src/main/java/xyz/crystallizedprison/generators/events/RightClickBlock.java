package xyz.crystallizedprison.generators.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.crystallizedprison.generators.Generator;
import xyz.crystallizedprison.generators.Generators;
import xyz.crystallizedprison.generators.guis.GeneratorGUI;

import java.util.List;

public class RightClickBlock implements Listener {

    public RightClickBlock(Generators main) {
        this.main = main;
    }

    Generators main;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (main.GetGen(event.getClickedBlock().getLocation()) != null){
                Generator Gen = main.GetGen(event.getClickedBlock().getLocation());
                if (Gen.getOwner().equals(event.getPlayer().getUniqueId().toString()) || event.getPlayer().hasPermission("Generators.admin.open")) {
                    GeneratorGUI.Open(event.getPlayer(), Gen, main);
                    main.getOpenGUIS().put(event.getPlayer(), Gen);
                }else{
                        event.getPlayer().sendMessage(ChatColor.DARK_AQUA +"You Dont own this Generator!");
                }
            }

            for (String Generator : main.getConfig().getConfigurationSection("Gens").getKeys(false)) {
                if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.getMaterial(main.getConfig().getString("Gens." + Generator + ".SpawnEGGMaterial"))) && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Gens." + Generator + ".SpawnEGGName")))) {
                    event.setCancelled(true);


                    Location loc = event.getClickedBlock().getLocation();
                    loc.setY(loc.getBlockY() + 1);

                    int Speed = 0, price = 0, BlocksMined = 0;

                    //need to look at if we click left side of block etc
                    if (!loc.getWorld().getBlockAt(loc).isEmpty()) {
                        event.getPlayer().sendMessage(ChatColor.AQUA + "You Can't Place a Generator Here");

                    } else {
                        loc.getBlock().setType(Material.END_PORTAL_FRAME);
                        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                            for (String lines : event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore()) {
                                if (lines.contains("Speed Level:")) {
                                    String NewLine = lines.replace(ChatColor.AQUA + "Speed Level: " + ChatColor.DARK_AQUA, "");
                                    Speed = Integer.valueOf(NewLine) - 1;
                                } else if (lines.contains("Price Level:")) {
                                    String NewLine = lines.replace(ChatColor.AQUA + "Price Level: " + ChatColor.DARK_AQUA, "");
                                    price = Integer.valueOf(NewLine) - 1;
                                } else if (lines.contains("BlocksMined:")) {
                                    String NewLine = lines.replace(ChatColor.AQUA + "BlocksMined: " + ChatColor.DARK_AQUA, "");
                                    BlocksMined = Integer.valueOf(NewLine);
                                }
                            }
                        }

                        ArmorStand am = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0.5, 0, 0.5), EntityType.ARMOR_STAND);

                        am.setVisible(false);
                        am.setBasePlate(false);
                        am.setInvulnerable(true);
                        am.setSmall(true);
                        am.setGravity(false);
                        am.getHeadPose().setZ(0);

                        am.setCustomName("Generator");



                        am.setHelmet(new ItemStack(Material.valueOf(main.getConfig().getString("Gens."+Generator + ".CenenterBlock"))));

                        Generator gen = new Generator(event.getPlayer().getUniqueId().toString(),0,Speed,price,BlocksMined,loc,Generator,am);
                        main.SaveGen(gen);


                        event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                        event.getPlayer().sendMessage(ChatColor.AQUA + "Successfully Placed a " + Generator + " Generator");
                        List<Generator> gens = main.getGenList();
                        gens.add(gen);
                        main.setGenList(gens);
                        break;
                    }


                }
            }
        }
    }
}
