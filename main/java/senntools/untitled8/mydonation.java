package senntools.untitled8;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class mydonation implements CommandExecutor {
    private Plugin plugin;

    public mydonation(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args){
        if(args.length != 0){
            sender.sendMessage(ChatColor.RED + "Usage: mydonation" + ChatColor.RESET);
            return true;
        }

        Player player = (Player) sender;

        NamespacedKey key = new NamespacedKey(plugin, "donations");

        PersistentDataContainer container = player.getPersistentDataContainer();

        if(container.has(key, PersistentDataType.INTEGER)){
            sender.sendMessage(ChatColor.GRAY + "Your donation to server is " + container.get(key, PersistentDataType.INTEGER) + "." + ChatColor.RESET);
        }
        else{
            sender.sendMessage(ChatColor.YELLOW + "You did not make a donation to server!" + ChatColor.RESET);
        }

        return true;
    }
}
