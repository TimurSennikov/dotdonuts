package senntools.untitled8;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EventListener implements Listener {
    Plugin plugin;

    ScoreBoardManager manager;

    public EventListener(Plugin p, ScoreBoardManager m) {
        this.plugin = p;
        this.manager = m;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        File deferred_list = new File(this.plugin.getDataFolder(), "deferred_list_donalts.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(deferred_list);

        List<String> l = config.getStringList("deferred_list"); // hi! i`m freakbob!

        for(String s: l){
            String nick = s.split(":")[0];
            String donation = s.split(":")[1];
            int formdon;

            if(player.getName().equals(nick)){ // бля ну типа это как там его короче ну ты понял типа если игрок есть в списке
                try {
                    formdon = Integer.parseInt(donation);
                }
                catch (NumberFormatException e) {
                    return;
                }

                NamespacedKey key = new NamespacedKey(this.plugin, "donations");
                PersistentDataContainer container = player.getPersistentDataContainer();

                container.set(key, PersistentDataType.INTEGER, formdon);

                l.remove(s);
                break;
            }
        }

        config.set("deferred_list", l);

        try {
            config.save(deferred_list);
        }
        catch(IOException e){
            player.sendMessage(ChatColor.RED + "Could not save deferred_list! !PLEASE! REPORT THIS TO SERVER ADMIN" + ChatColor.RESET);
        }

        this.manager.autogroupbyrichness(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();

        Team t = player.getScoreboard().getEntryTeam(player.getName());

        if(t == null){
            return;
        }

        event.setMessage(t.getPrefix() + ChatColor.RESET + event.getMessage());
    }
}