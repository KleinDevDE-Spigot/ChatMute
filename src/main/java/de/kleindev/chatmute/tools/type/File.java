package de.kleindev.chatmute.tools.type;

import de.kleindev.chatmute.tools.Config;
import de.kleindev.chatmute.tools.MutedPlayer;
import de.kleindev.chatmute.tools.Tools;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * SpigotPlugin ~ChatMute
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 2017, created by KleinDev
 */
public class File {
    private final static boolean isEnabled = YamlConfiguration.loadConfiguration(new java.io.File("config.yml")).getBoolean("Type.File.enabled");
    private final static FileConfiguration mutes = Config.getFile(Config.getString("config.yml", "Type.File.FileName"));


    public static boolean isEnabled(){
        return isEnabled;
    }

    public static List<MutedPlayer> getMutes(){
        List<MutedPlayer> mutedPlayers = new ArrayList<>();
        for (String uuid : mutes.getKeys(true)){
            mutedPlayers.add(getMutedPlayer(Tools.getPlayer(uuid)));
        }
        return mutedPlayers;
    }

    public static boolean addMute(Player player, String reason, long time, String sender){
        String uuid = player.getUniqueId().toString();
        mutes.set(uuid, "");
        mutes.set(uuid + ".reason", reason);
        mutes.set(uuid+".time", time + System.currentTimeMillis());
        mutes.set(uuid+".sender", sender);
        return mutes.contains(uuid);
    }

    public static boolean removeMute(Player player){
        mutes.set(player.getUniqueId().toString(), null);
        return !mutes.contains(player.getUniqueId().toString());
    }

    public static boolean isMuted(Player player){
        if (mutes.contains(player.getUniqueId().toString())){
            long time = mutes.getLong(player.getUniqueId().toString()+".time");

            if (time > System.currentTimeMillis()){
                removeMute(player);
                return true;
            } else return false;
        } else return false;
    }

    public static MutedPlayer getMutedPlayer(Player player){
        String uuid = player.getUniqueId().toString();
        if (mutes.contains(uuid)){
            String reason = mutes.getString(uuid+".reason");
            long time = mutes.getLong(uuid+".time");
            String sender = mutes.getString(uuid+".sender");
            return new MutedPlayer(player, reason, time, sender);
        } else return null;
    }

    public static boolean start(){
        java.io.File file = new java.io.File(Config.getString("config.yml", "Type.File.FileName"));
        try {
            return !file.exists() && file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
