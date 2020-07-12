package xyz.crystallizedprison.generators;

import com.vk2gpz.tokenenchant.TokenEnchant;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.crystallizedprison.generators.ConfigHandler.GenLoactions;
import xyz.crystallizedprison.generators.commands.Gen;
import xyz.crystallizedprison.generators.events.InventoryManager;
import xyz.crystallizedprison.generators.events.RightClickBlock;
import xyz.crystallizedprison.generators.events.onPlayerInteractEntityEvent;
import xyz.crystallizedprison.petssystem.Pet;
import xyz.crystallizedprison.petssystem.PetsSystem;

import java.util.*;

public final class Generators extends JavaPlugin {

    List<Generator> GenList = new ArrayList<>();
    HashMap<Player,Generator> OpenGUIS = new HashMap<>();
    GenLoactions genLoactions = new GenLoactions();
    PetsSystem petsSystem;

    private static Economy econ = null;

    public static Economy getEcon() {
        return econ;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public List<Generator> getGenList() {
        return GenList;
    }

    public void setGenList(List<Generator> genList) {
        GenList = genList;
    }

    public HashMap<Player, Generator> getOpenGUIS() {
        return OpenGUIS;
    }

    public TokenEnchant getTe() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("TokenEnchant");
        if ((plugin == null)) {
            return null;
        }
        return (TokenEnchant)plugin;
    }

    public PetsSystem getPets() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PetsSystem");
        if ((plugin == null)) {
            return null;
        }
        return (xyz.crystallizedprison.petssystem.PetsSystem)plugin;
    }

    public GenLoactions getGenLoactions() {
        return genLoactions;
    }



    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        saveDefaultConfig();



        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("Generator").setExecutor(new Gen(this));


        getServer().getPluginManager().registerEvents(new InventoryManager(this),this);
        getServer().getPluginManager().registerEvents(new RightClickBlock(this),this);
        getServer().getPluginManager().registerEvents(new onPlayerInteractEntityEvent(this),this);

        new BukkitRunnable() {

            @Override
            public void run() {
                GenLoactions.setup();
                Load();
                Loop();
            }
        }.runTaskLater(this, 5);

        petsSystem = getPets();
    }

    @Override
    public void onDisable() {
        for (Generator gen:GenList){
            ArmorStand am = gen.getArmourStand();
            gen.setArmourStand(null);
            am.remove();
        }
    }

    public void GivePlayerGen(Player player, String Type){

        ItemStack item = new ItemStack(Material.getMaterial(getConfig().getString("Gens."+Type+".SpawnEGGMaterial")));
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',getConfig().getString("Gens."+Type+".SpawnEGGName")));
        meta.setLore(getConfig().getStringList("Gens."+Type+".SpawnEGGLore"));

        item.setItemMeta(meta);

        player.getInventory().setItem(player.getInventory().firstEmpty(),item);

    }

    public Generator GetGen(Location Loc){

        for (Generator gen:GenList){
            if (gen.getLoc().equals(Loc)){
                return gen;
            }
        }

        return null;
    }

    private void Load(){
        if (genLoactions.get().contains("Gens")){
            for (String id:genLoactions.get().getConfigurationSection("Gens").getKeys(false)){
                ConfigurationSection configSection = genLoactions.get().getConfigurationSection("Gens."+id);
                Location loc = (Location) configSection.get("loc");

                String Owner = configSection.getString("Owner");
                String Type = configSection.getString("Type");

                int MoneyLevel = configSection.getInt("MoneyLevel");
                int SpeedLevel = configSection.getInt("SpeedLevel");
                int Blocksmined = configSection.getInt("BlocksMined");
                Double MoneyDue = configSection.getDouble("MoneyDue");


                Collection<Entity> nearbyEntites = loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0, 0.5), 0.2, 0.2, 0.2);
                for (Entity ent:nearbyEntites){
                    ent.remove();
                }

                ArmorStand am = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0.5, 0, 0.5), EntityType.ARMOR_STAND);
                am.setVisible(false);
                am.setBasePlate(false);
                am.setInvulnerable(true);
                am.setSmall(true);
                am.setGravity(false);

                am.getHeadPose().setZ(0);

                am.setHelmet(new ItemStack(Material.valueOf(getConfig().getString("Gens."+Type+ ".CenenterBlock"))));

                Generator gen = new Generator(Owner,MoneyDue,SpeedLevel,MoneyLevel,Blocksmined,loc,Type,am);
                GenList.add(gen);

            }
        }
    }

    public void SaveGen(Generator Gen){
        ConfigurationSection configSection;
        if (genLoactions.get().contains("Gens."+Gen.getLoc().getBlockX()+Gen.getLoc().getBlockY()+Gen.getLoc().getBlockZ())){
            configSection = genLoactions.get().getConfigurationSection("Gens."+Gen.getLoc().getBlockX()+Gen.getLoc().getBlockY()+Gen.getLoc().getBlockZ());
        }else {
            configSection = genLoactions.get().createSection("Gens."+Gen.getLoc().getBlockX()+Gen.getLoc().getBlockY()+Gen.getLoc().getBlockZ());
        }
        configSection.set("loc",Gen.getLoc());
        configSection.set("Owner",Gen.getOwner());
        configSection.set("SpeedLevel",Gen.getSpeedLevel());
        configSection.set("MoneyLevel",Gen.getMoneyLevel());
        configSection.set("MoneyDue",Gen.getMoneydue());
        configSection.set("BlocksMined",Gen.getBlocksMined());
        configSection.set("Type",Gen.getType());
        genLoactions.save();

    }

    private void Loop(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                for(Generator Gen:GenList){
                    if (Gen.getTimeLeft() == null) {
                        long Wait = (Integer.valueOf(getConfig().getString("Gens." + Gen.getType() + ".StartingSpeed")) - (Integer.valueOf(getConfig().getString("Gens." + Gen.getType() + ".IncreasePerSpeed")) * Gen.getSpeedLevel())) * 20L;
                        Gen.setTimeLeft(Wait);
                    }else if (Gen.getTimeLeft() <=0){
                        long Wait = ( Integer.valueOf(getConfig().getString("Gens."+Gen.getType()+ ".StartingSpeed")) - (Integer.valueOf(getConfig().getString("Gens."+Gen.getType()+".IncreasePerSpeed"))*Gen.getSpeedLevel()) )*20L;
                        Double Increase= Double.valueOf(getConfig().getInt("Gens."+Gen.getType() +".BaseIncome") + (getConfig().getInt("Gens."+Gen.getType() +".BaseIncomeIncrease")*Gen.getMoneyLevel()) );


                        if (Bukkit.getOfflinePlayer(UUID.fromString(Gen.getOwner())).isOnline()) {
                            if (petsSystem.GetPlayerInfo(Bukkit.getOfflinePlayer(UUID.fromString(Gen.getOwner())).getPlayer()).getPet() != null) {
                                if (petsSystem.GetPlayerInfo(Bukkit.getOfflinePlayer(UUID.fromString(Gen.getOwner())).getPlayer()).getPet().equals(Pet.INVESTOR)) {
                                    Increase*=2;
                                }
                            }
                        }

                        Gen.setMoneydue(Double.sum(Gen.getMoneydue(),Increase));
                        Gen.setBlocksMined(Gen.getBlocksMined()+1);
                        Gen.setTimeLeft(Wait);
                    } else {

                        Gen.setTimeLeft(Gen.getTimeLeft()-5);
                    }

                    //Section - Particles
                    Location loc = Gen.getLoc().clone();
                    loc.add(0.2,1,0.4);
                    int points = 10;
                    double radius = 0.2;
                    Location origin = loc;

                    for (int i = 0; i < points; i++) {
                        double angle = 2 * Math.PI * i / points;
                        Location point = origin.add(radius * Math.sin(angle), 0, radius * Math.cos(angle));
                        point.getWorld().spawnParticle(Particle.FLAME,loc,0,0,0,0);

                    }

                }
            }

        }, 20, 5);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                for (Generator Gen:GenList){
                    ConfigurationSection configSection;
                    if (genLoactions.get().contains("Gens."+Gen.getLoc().getBlockX()+Gen.getLoc().getBlockY()+Gen.getLoc().getBlockZ())){
                        configSection = genLoactions.get().getConfigurationSection("Gens."+Gen.getLoc().getBlockX()+Gen.getLoc().getBlockY()+Gen.getLoc().getBlockZ());
                    }else {
                        configSection = genLoactions.get().createSection("Gens."+Gen.getLoc().getBlockX()+Gen.getLoc().getBlockY()+Gen.getLoc().getBlockZ());
                    }
                    configSection.set("loc",Gen.getLoc());
                    configSection.set("Owner",Gen.getOwner());
                    configSection.set("SpeedLevel",Gen.getSpeedLevel());
                    configSection.set("MoneyLevel",Gen.getMoneyLevel());
                    configSection.set("MoneyDue",Gen.getMoneydue());
                    configSection.set("BlocksMined",Gen.getBlocksMined());
                    configSection.set("Type",Gen.getType());

                }
                genLoactions.save();
            }

        }, 60, 120);
    }




}

