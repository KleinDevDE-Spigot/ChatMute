package de.kleindev.chatmute.tools;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * SpigotPlugin ~ChatMute
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 2017, created by KleinDev
 */
public class Tools {
    public static Player getPlayer(String UUID){
        Player player1 = null;
        if (Bukkit.getPlayer(UUID) != null)
            return Bukkit.getPlayer(UUID);
        else if (Bukkit.getOfflinePlayer(UUID) != null)
            return Bukkit.getOfflinePlayer(UUID).getPlayer();
        else Debug.err("No User found with UUID: '" + UUID + "'");
        return null;
    }

    public static String getError(String errorCode){
        switch (errorCode){
            case "CM-01CE":
                return "No enabled save-type found in config!";
            default:
                return "unknown Error";
        }
    }

    public static boolean doesIDExist(Integer id){
        return Config.exist("IDs.yml", String.valueOf(id));
    }
}
