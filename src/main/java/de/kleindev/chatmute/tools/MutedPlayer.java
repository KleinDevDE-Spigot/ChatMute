package de.kleindev.chatmute.tools;

import de.kleindev.chatmute.Main;
import de.kleindev.chatmute.tools.type.File;
import de.kleindev.chatmute.tools.type.MySQL;
import de.kleindev.chatmute.tools.type.SQLite;
import org.bukkit.entity.Player;

/**
 * SpigotPlugin ~ChatMute
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 2017, created by KleinDev
 */
public class MutedPlayer {
    private Player player;
    private String reason;
    private long time;
    private String sender;

    public MutedPlayer(Player player, String reason, long time, String sender){
        this.player = player;
        this.reason = reason;
        this.time = time;
        this.sender = sender;
    }

    public Player getPlayer() {
        return player;
    }

    public String getReason() {
        return reason;
    }

    public long getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }

    public static boolean isMutedPlayer(Player player){
        if (MySQL.isEnabled()) return MySQL.isMuted(player);
        else if (SQLite.isEnabled()) return SQLite.isMuted(player);
        else if (File.isEnabled()) return File.isMuted(player);
        else return false;
    }
}
