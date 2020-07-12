package xyz.crystallizedprison.generators.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.crystallizedprison.generators.Generator;
import xyz.crystallizedprison.generators.Generators;

import java.util.ArrayList;

public class GeneratorGUI {
    public static String Title = ChatColor.AQUA +"Generator";



    public static void Open(Player player, Generator gen,Generators main){
        if(gen == null){
            System.out.println("No Generator Provide");

        }else {
            Inventory inv = Bukkit.createInventory(null, 36, Title);
            ItemStack[] items = new ItemStack[36];


            ItemStack Stats = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta StatsMeta = (SkullMeta) Stats.getItemMeta();
            StatsMeta.setOwner(player.getName());
            StatsMeta.setDisplayName("§bStats");

            ArrayList<String> lore = new ArrayList<String>();
            lore.add(ChatColor.AQUA + "BlocksMined: " + ChatColor.DARK_AQUA + gen.getBlocksMined());

            StatsMeta.setLore(lore);
            Stats.setItemMeta(StatsMeta);
            items[4] = Stats;


            ItemStack Speed = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);

            if(gen.getSpeedLevel() >= 1 && gen.getSpeedLevel() < 3){
                Speed.setType(Material.ORANGE_STAINED_GLASS_PANE);
            }else if(gen.getSpeedLevel() >=3 && gen.getSpeedLevel() < 7){
                Speed.setType(Material.YELLOW_STAINED_GLASS_PANE);
            }else if(gen.getSpeedLevel() >= 7 && gen.getSpeedLevel() < 9){
                Speed.setType(Material.LIME_STAINED_GLASS_PANE);
            }else if(gen.getSpeedLevel() ==9){
                Speed.setType(Material.GREEN_STAINED_GLASS_PANE);
            }

            ItemMeta SpeedMeta = Speed.getItemMeta();
            SpeedMeta.setDisplayName("§bSpeed");

            lore = new ArrayList<String>();
            lore.add(ChatColor.AQUA + "Level: " + ChatColor.DARK_AQUA + (gen.getSpeedLevel()+1));
            lore.add(ChatColor.AQUA + "Delay: " + ChatColor.DARK_AQUA + (( main.getConfig().getInt("Gens."+gen.getType()+".StartingSpeed") ) - ( main.getConfig().getInt("Gens."+gen.getType()+".IncreasePerSpeed")* gen.getSpeedLevel() ) ));
            if(gen.getSpeedLevel() < 9) {
                lore.add(ChatColor.AQUA + "Price: " + ChatColor.DARK_AQUA + (main.getConfig().getInt("Gens."+gen.getType()+ ".GenPerSpeedCost") * (gen.getSpeedLevel() + 1)));
            }

            SpeedMeta.setLore(lore);
            Speed.setItemMeta(SpeedMeta);
            items[20] = Speed;


            ItemStack Upgrade = new ItemStack(Material.HOPPER, 1);
            ItemMeta UpgradeMeta = Upgrade.getItemMeta();
            UpgradeMeta.setDisplayName("§bCollect");

            lore = new ArrayList<String>();
            lore.add(ChatColor.AQUA + "Money: " + ChatColor.DARK_AQUA + main.getEcon().format(gen.getMoneydue()));

            UpgradeMeta.setLore(lore);
            Upgrade.setItemMeta(UpgradeMeta);
            inv.addItem(Upgrade);
            items[22] = Upgrade;

            ItemStack price = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);

            if(gen.getMoneyLevel() >= 1 && gen.getMoneyLevel() < 3){
                price.setType(Material.ORANGE_STAINED_GLASS_PANE);
            }else if(gen.getMoneyLevel() >=3 && gen.getMoneyLevel() < 7){
                price.setType(Material.YELLOW_STAINED_GLASS_PANE);
            }else if(gen.getMoneyLevel() >= 7 && gen.getMoneyLevel() < 9){
                price.setType(Material.LIME_STAINED_GLASS_PANE);
            }else if(gen.getMoneyLevel() ==9){
                price.setType(Material.GREEN_STAINED_GLASS_PANE);
            }
            
            ItemMeta priceMeta = price.getItemMeta();
            priceMeta.setDisplayName("§bMoney");

            lore = new ArrayList<String>();
            lore.add(ChatColor.AQUA + "Level: " + ChatColor.DARK_AQUA + (gen.getMoneyLevel()+1));
            lore.add(ChatColor.AQUA + "Payout: " + ChatColor.DARK_AQUA + main.getEcon().format(main.getConfig().getInt("Gens."+gen.getType()+".BaseIncome")+(main.getConfig().getInt("Gens."+gen.getType()+".BaseIncomeIncrease")*gen.getMoneyLevel())) );
            if(gen.getMoneyLevel() < 9) {
                lore.add(ChatColor.AQUA + "Price: " + ChatColor.DARK_AQUA + (main.getConfig().getInt("Gens."+gen.getType()+ ".GenPerpriceCost") * (gen.getMoneyLevel() + 1)));
            }
            priceMeta.setLore(lore);

            price.setItemMeta(priceMeta);
            items[24] = price;


            ItemStack Filler = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
            ItemMeta FillerMeta = Filler.getItemMeta();
            FillerMeta.setDisplayName("§r");
            Filler.setItemMeta(FillerMeta);

            items[0] = Filler;
            items[1] = Filler;
            items[2] = Filler;
            items[3] = Filler;
            items[5] = Filler;
            items[6] = Filler;
            items[7] = Filler;
            items[8] = Filler;

            items[9] = Filler;
            items[10] = Filler;
            items[11] = Filler;
            items[12] = Filler;
            items[13] = Filler;
            items[14] = Filler;
            items[15] = Filler;
            items[16] = Filler;
            items[17] = Filler;

            items[18] = Filler;
            items[19] = Filler;
            items[21] = Filler;
            items[23] = Filler;
            items[25] = Filler;
            items[26] = Filler;

            items[27] = Filler;
            items[28] = Filler;
            items[29] = Filler;
            items[30] = Filler;
            items[31] = Filler;
            items[32] = Filler;
            items[33] = Filler;
            items[34] = Filler;

            ItemStack Close = new ItemStack(Material.BARRIER,1);
            ItemMeta Meta = Close.getItemMeta();
            Meta.setDisplayName(ChatColor.DARK_RED + "PickUp");

            lore = lore = new ArrayList<String>();
            lore.add(ChatColor.RED + "WARNING: loose all money Due");

            Meta.setLore(lore);
            Close.setItemMeta(Meta);



            items[35] = Close;


            inv.setContents(items);



            player.openInventory(inv);
        }



    }
}
