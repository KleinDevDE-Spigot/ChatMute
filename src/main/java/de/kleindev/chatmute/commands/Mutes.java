package de.kleindev.chatmute.commands;

import de.kleindev.chatmute.tools.Config;
import de.kleindev.chatmute.tools.MutedPlayer;
import de.kleindev.chatmute.tools.type.File;
import de.kleindev.chatmute.tools.type.MySQL;
import de.kleindev.chatmute.tools.type.SQLite;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * SpigotPlugin ~ChatMute
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 2017, created by KleinDev
 */
public class Mutes implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(Config.getPermission("mutes"))){
            List<MutedPlayer> mutedPlayers = null;
            String mutes = "";
            if (MySQL.isEnabled()) mutedPlayers = MySQL.getMutes();
            if (SQLite.isEnabled()) mutedPlayers = SQLite.getMutes();
            if (File.isEnabled()) mutedPlayers = File.getMutes();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            for (MutedPlayer mp : mutedPlayers){
                if (Config.getString("messages.yml", "ListMutes").isEmpty()){
                    if (mutes.isEmpty()) mutes = mp.getPlayer().getName();
                    else mutes = mutes + ", " + mp.getPlayer().getName();
                } else {
                    mutes = mutes + "§f, " + Config.getString("messages.yml", "ListMutes")
                            .replaceAll("%player%", mp.getPlayer().getName())
                            .replaceAll("%sender%", mp.getSender())
                            .replaceAll("%reason%", mp.getReason())
                            .replaceAll("%time%", String.valueOf(mp.getTime() / 1000 / 60 / 60));
                }
            }
        } else for (String s : Config.getStringList("messages.yml", "NoPermission"))
            sender.sendMessage(s.replaceAll("&", "§")
            .replaceAll("%permissions%", Config.getPermission("mutes"))
            .replaceAll("%command%", "mutes"));
        return false;
    }
}
