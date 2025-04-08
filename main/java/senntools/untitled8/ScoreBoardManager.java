package senntools.untitled8;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class ScoreBoardManager{ // класс для добавления гейского префикса донатящим великому дискофрику
    FileConfiguration config;

    ScoreboardManager manager;
    Scoreboard board;

    HashMap<String, Team> teams;

    File dongroups;

    Plugin plugin; // если что всё без модификатора - приватное

    public ScoreBoardManager(Plugin plugin) { // в конструкторе читаем конфиг и создаём по нему команды.
        this.plugin = plugin;

        this.manager = Bukkit.getScoreboardManager();
        this.board = this.manager.getNewScoreboard();

        this.dongroups = new File(plugin.getDataFolder(), "dongroups.yml");

        this.config = YamlConfiguration.loadConfiguration(dongroups);

        this.teams = new HashMap<String, Team>();

        List<String> l = this.config.getStringList("dongroups");

        for(String s: l){
            String limit = s.split(":")[0];
            String color = s.split(":")[1];

            Team team = this.board.registerNewTeam(color);
            team.setPrefix(color + " ★ " + ChatColor.RESET);

            this.teams.put(limit, team);
        }

    } // кстати в разных классах написано то через this то просто не потому что я скопипастил их, а потому что я ебучий даун не оперделившийся как лучше писать. честно.

    public void autogroupbyrichness(Player player){
        NamespacedKey star = new NamespacedKey(plugin, "star");
        NamespacedKey key = new NamespacedKey(this.plugin, "donations");

        PersistentDataContainer container = player.getPersistentDataContainer();

        if(!container.has(star, PersistentDataType.BOOLEAN) || !container.has(key, PersistentDataType.INTEGER)){return;}

        if(!container.get(star, PersistentDataType.BOOLEAN)){ // если нет звезды
            Scoreboard s = player.getScoreboard();
            if(s != null){
                Team team = s.getEntryTeam(player.getName()); // если игрок донатер - отключить звезду.
                if(team != null){team.removeEntry(player.getName());}
            }

            return;
        }

        int v = container.get(key, PersistentDataType.INTEGER);

        for(HashMap.Entry<String, Team> entry: this.teams.entrySet()){
            String limit = entry.getKey();

            int low, high;

            try{
                String[] spl = limit.split("-");
                if(spl.length == 1){
                    low = Integer.parseInt(spl[0]);
                    high = 0;
                }
                else {
                    low = Integer.parseInt(limit.split("-")[0]);
                    high = Integer.parseInt(limit.split("-")[1]);
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return;
            }

            if((v >= low && v < high && (high != 0)) || ((high == 0) && v >= low)){
                player.setScoreboard(this.board); // если пользователь подходит под конфиг то задаём ему группу
                entry.getValue().addEntry(player.getName());
                return;
            }
        }

        for(HashMap.Entry<String, Team> entry: this.teams.entrySet()){ // если не попадаешь в список то нехуй тебе звезду давать
            Team team = entry.getValue();

            if(team.getEntries().contains(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
    }
} // ты какую группу уважаешь? УАААААААААААААААААААААААААААААААА