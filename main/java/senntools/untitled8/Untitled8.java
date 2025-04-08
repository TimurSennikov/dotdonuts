package senntools.untitled8;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;

public final class Untitled8 extends JavaPlugin {
    private ScoreBoardManager manager;

    @Override
    public void onEnable() {
        this.manager = new ScoreBoardManager(this);

        Bukkit.getPluginManager().registerEvents(new EventListener(this, this.manager), this);

        this.getCommand("setdonation").setExecutor(new setdonation(this));
        this.getCommand("mydonation").setExecutor(new mydonation(this));
        this.getCommand("star").setExecutor(new togglestar(this, this.manager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}