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

public class togglestar implements CommandExecutor {
    private Plugin plugin;
    private ScoreBoardManager manager;

    public togglestar(Plugin plugin, ScoreBoardManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args){
        NamespacedKey key = new NamespacedKey(plugin, "star");

        Player player = (Player)sender;
        PersistentDataContainer container = player.getPersistentDataContainer();

        if(container.has(key, PersistentDataType.BOOLEAN)){
            boolean now = !container.get(key, PersistentDataType.BOOLEAN);

            container.set(key, PersistentDataType.BOOLEAN, now);

            sender.sendMessage(ChatColor.GREEN + "Your donate start in now " + (now ? (ChatColor.DARK_GREEN + "on" + ChatColor.GREEN) : (ChatColor.RED + "off" + ChatColor.GREEN)) + "." + ChatColor.RESET);
        }
        else{
            container.set(key, PersistentDataType.BOOLEAN, true);
            sender.sendMessage(ChatColor.GREEN + "Your donate star in now on." + ChatColor.RESET);
        }

        this.manager.autogroupbyrichness(player);
        return true;
    }
}