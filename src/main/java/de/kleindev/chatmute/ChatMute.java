package de.kleindev.chatmute;

import de.kleindev.chatmute.tools.Debug;
import de.kleindev.chatmute.tools.MutedPlayer;
import de.kleindev.chatmute.tools.type.File;
import de.kleindev.chatmute.tools.type.MySQL;
import de.kleindev.chatmute.tools.type.SQLite;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * SpigotPlugin ~ ChatMute
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 2017, created by KleinDev
 */
public class ChatMute {

    /**
     * Check if player is muted
     * @param player - The specific Player you want to know its muted
     * @return - boolean if Player is muted
     */
    public static boolean isMuted(Player player){
        if (MySQL.isEnabled()){
            return MySQL.isMuted(player);
        }

        return false;
    }

    /**
     * Mute Player
     * @param player - The specific Player you want to mute
     * @param id - The specific ID (you can see an Overview in the File IDs.yml)
     * @return - boolean if Player is successfully muted
     */
    public static boolean mutePlayer(Player player, Integer id, String sender){
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(new java.io.File("IDs.yml"));
        if (cfg.getString(String.valueOf(id)) != null) {
            String reason = cfg.getString(id + ".reason");
            long time = cfg.getLong(id + ".time") * 1000;
            if (MySQL.isEnabled()) {
                return MySQL.addMute(player, reason, time, sender);
            } else if (SQLite.isEnabled()) {
                return SQLite.addMute(player, reason, time, sender);
            } else if (File.isEnabled()) {
                return File.addMute(player, reason, time, sender);
            } else {
                Debug.err("No savetype defined in config!");
                return false;
            }
        } else {
            Debug.err("No Mute-Template found with id '" + id + "'");
            return false;
        }
    }

    /**
     * Mute Player
     * @param player - The specific Player you want to mute
     * @param reason - Your reason
     * @param time - Time in Seconds
     * @return - boolean if Player is successfully muted
     */
    public static boolean mutePlayer(Player player, String reason, int time, String sender){
        if (MySQL.isEnabled()) {
            return MySQL.addMute(player, reason, time*1000, sender);
        } else if (SQLite.isEnabled()){
            return SQLite.addMute(player, reason, time*1000, sender);
        } else if (File.isEnabled()){
            return File.addMute(player, reason, time*1000, sender);
        } else {
            Debug.err("No savetype defined in config!");
            return false;
        }
    }

    /**
     * Mute Player permanently
     * @param player - The specific Player you want to mute
     * @param reason - Your reason
     * @param sender - Who mutes the Player? You can set 'null' if you want
     * @return - boolean if Player is successfully muted
     */
    public static boolean mutePlayer(Player player, String reason, String sender){
        if (MySQL.isEnabled()) {
            return MySQL.addMute(player, reason, -1, sender);
        } else if (SQLite.isEnabled()){
            return SQLite.addMute(player, reason, -1, sender);
        } else if (File.isEnabled()){
            return File.addMute(player, reason, -1, sender);
        } else {
            Debug.err("No savetype defined in config!");
            return false;
        }
    }

    /**
     * Unmute Player
     * @param player - The specific Player you want to unmute
     * @return - boolean if Player is successfully unmuted
     */
    public static boolean unmutePlayer(Player player){
        if (MySQL.isEnabled()){
            return MySQL.removeMute(player);
        } else if (SQLite.isEnabled()){
            return SQLite.removeMute(player);
        } else if(File.isEnabled()){
            return File.removeMute(player);
        } else {
            Debug.err("No savetype defined in config!");
            return false;
        }
    }

    /**
     * List all muted Players
     * @return - List of all muted Players
     */
    public static List<MutedPlayer> listMutes(){
        if (MySQL.isEnabled()){
            return MySQL.getMutes();
        } else if (SQLite.isEnabled()){
            return SQLite.getMutes();
        } else if (File.isEnabled()){
            return File.getMutes();
        } else {
            Debug.err("No savetype defined in config!");
            return null;
        }
    }

}
