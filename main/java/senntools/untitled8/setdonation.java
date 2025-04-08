package senntools.untitled8;

import com.sun.org.apache.xerces.internal.xs.StringList;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import sun.security.krb5.internal.KdcErrException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class setdonation implements CommandExecutor {
    private Plugin plugin;

    public setdonation(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args){
        if(!(sender instanceof ConsoleCommandSender)){sender.sendMessage(ChatColor.DARK_RED + "You are not permitted to use this command!" + ChatColor.RESET); return false;}
        if(args.length != 2){
            sender.sendMessage(ChatColor.RED + "Usage: setdonation <nick> <donation>" + ChatColor.RESET);
            return true;
        }

        String nick = args[0];
        String donationstr = args[1];
        int donation;

        try {
            donation = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e){
            sender.sendMessage(ChatColor.RED + "Invalid donation (expected integer), suka pidor" + ChatColor.RESET);
            return true;
        }

        for(Player p: Bukkit.getOnlinePlayers()){
            if(p.getName().equals(nick)){
                NamespacedKey donations = new NamespacedKey(this.plugin, "donations");
                p.getPersistentDataContainer().set(donations, PersistentDataType.INTEGER, donation);
                sender.sendMessage("Ok, new donation of " + nick + " has been set to " + donation);
                return true;
            }
        }

        // не нашли игрока в онлайн списке, значит что он сын шлюхи. Проверяем, есть ли вообще.
        for(OfflinePlayer p: Bukkit.getOfflinePlayers()){
            if(p.getName().equals(nick)){ // спит гандурас
                File deferred_list = new File(this.plugin.getDataFolder(), "deferred_list_donalts.yml");

                FileConfiguration config = YamlConfiguration.loadConfiguration(deferred_list);

                List<String> l = config.getStringList("deferred_list");

                List<String> copy = l;

                for(String s: l){if(s.startsWith(nick)){copy.remove(s); break;}} // вообще не рекомендуют так дрочить списки но если сразу после изменения перестать то можно нахуй.
                l = copy; // процесс фильтрации говна

                l.add(nick + ":" + donationstr); // смотри EventListener!

                config.set("deferred_list", l);

                try{
                    config.save(deferred_list);
                }
                catch(IOException e){
                    sender.sendMessage(ChatColor.RED + "Could not save deferred list because of IOException! Check FS permissions!" + ChatColor.RESET);
                    return true;
                }

                sender.sendMessage("Player is found on server but not online. Added donation info to deferred list. Awaiting " + nick + " login.");
                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + "Username is not online and not found on server." + ChatColor.RESET);
        return true;
    }
}
