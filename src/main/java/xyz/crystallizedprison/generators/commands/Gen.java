package xyz.crystallizedprison.generators.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.crystallizedprison.generators.Generator;
import xyz.crystallizedprison.generators.Generators;

public class Gen implements CommandExecutor {

    public Gen(Generators main) {
        this.main = main;
    }

    Generators main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length != 0) {
                if (args[0].toLowerCase().equals("give")) {
                    if (args.length < 3) {
                        player.sendMessage(ChatColor.DARK_AQUA +"Invalid format /Generator give [name] [type]");
                        return true;
                    }

                    if (!player.hasPermission("Generator.give")){
                        player.sendMessage(ChatColor.DARK_AQUA +"You Don't Have Perms");
                        return true;
                    }

                    if (Bukkit.getPlayer(args[1]) == null){
                        player.sendMessage(ChatColor.DARK_AQUA +"Invalid Player");
                        return true;
                    }

                    if (!main.getConfig().contains("Gens."+args[2])){
                        player.sendMessage(ChatColor.DARK_AQUA +"Invalid Generator Type");
                        return true;
                    }

                    main.GivePlayerGen(Bukkit.getPlayer(args[1]),args[2]);

                }
            }
        }else{
            if (args.length != 0) {
                if (args[0].toLowerCase().equals("give")) {
                    if (args.length < 3) {
                        System.out.println(ChatColor.DARK_AQUA +"Invalid format /Generator give [name] [type]");
                        return true;
                    }

                    if (Bukkit.getPlayer(args[1]) == null){
                        System.out.println(ChatColor.DARK_AQUA +"Invalid Player");
                        return true;
                    }

                    if (!main.getConfig().contains("Gens."+args[2])){
                        System.out.println(ChatColor.DARK_AQUA +"Invalid Generator Type");
                        return true;
                    }

                    main.GivePlayerGen(Bukkit.getPlayer(args[1]),args[2]);

                }
            }
        }

        return false;
    }
}
