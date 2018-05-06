package de.kleindev.chatmute.commands;

import de.kleindev.chatmute.tools.Config;
import de.kleindev.chatmute.tools.MutedPlayer;
import de.kleindev.chatmute.tools.type.File;
import de.kleindev.chatmute.tools.type.MySQL;
import de.kleindev.chatmute.tools.type.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * SpigotPlugin ~ChatMute
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 2017, created by KleinDev
 */
public class UnMute implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(Config.getPermission("unmute"))){
            if (Bukkit.getOfflinePlayer(args[0]) != null){
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (MutedPlayer.isMutedPlayer(offlinePlayer.getPlayer())){
                    if (MySQL.isEnabled()) MySQL.removeMute(offlinePlayer.getPlayer());
                    if (SQLite.isEnabled()) SQLite.removeMute(offlinePlayer.getPlayer());
                    if (File.isEnabled()) File.removeMute(offlinePlayer.getPlayer());

                    for (String s : Config.getStringList("messages.yml", "onUnMute1"))
                        sender.sendMessage(s.replaceAll("&", "§")
                                .replaceAll("%player%", args[0])
                                .replaceAll("%sender%", sender.getName()));
                    for (String s : Config.getStringList("messages.yml", "onUnMute2"))
                        sender.sendMessage(s.replaceAll("&", "§")
                                .replaceAll("%player%", args[0])
                                .replaceAll("%sender%", sender.getName()));

                } else for (String s : Config.getStringList("messages.yml", "PlayerIsNotMuted"))
                    sender.sendMessage(s.replaceAll("%plaer%", args[0]).replaceAll("&", "§"));
            } else for (String s : Config.getStringList("messages.yml", "PlayerDoesNotExist"))
                sender.sendMessage(s.replaceAll("%player%", args[0]).replaceAll("&", "§"));
        } else for (String s : Config.getStringList("messages.yml", "NoPermission"))
            sender.sendMessage(s.replaceAll("&", "§")
                    .replaceAll("%permissions%", Config.getPermission("mutes"))
                    .replaceAll("%command%", "mutes"));
        return false;
    }
}
