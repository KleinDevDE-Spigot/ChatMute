package de.kleindev.chatmute.listener;

import de.kleindev.chatmute.tools.Config;
import de.kleindev.chatmute.tools.MutedPlayer;
import de.kleindev.chatmute.tools.type.File;
import de.kleindev.chatmute.tools.type.MySQL;
import de.kleindev.chatmute.tools.type.SQLite;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SpigotPlugin ~ChatMute
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 2017, created by KleinDev
 */
public class ChatEvent implements Listener{
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (MutedPlayer.isMutedPlayer(e.getPlayer())) {
            for (String s : Config.getStringList("config.yml", "exception.equals")){
                if (e.getMessage().equalsIgnoreCase(s)){
                    e.setCancelled(true);
                    return;
                }
            }
            for (String s : Config.getStringList("config.yml", "exception.equals")){
                if (e.getMessage().contains(s)){
                    e.setCancelled(true);
                    return;
                }
            }

            e.setCancelled(true);
            if (MySQL.isEnabled()) {
                MutedPlayer mutedPlayer = MySQL.getMutedPlayer(e.getPlayer());
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String time = sdf.format(new Date(mutedPlayer.getTime()));
                for (String s : Config.getStringList("messages.yml", "YoureMuted")) {
                    e.getPlayer().sendMessage(s.replaceAll("&", "§")
                            .replaceAll("%reason%", mutedPlayer.getReason())
                            .replaceAll("%sender%", mutedPlayer.getSender())
                            .replaceAll("%time%", time)
                            .replaceAll("%player%", e.getPlayer().getName()));
                }
            } else if (SQLite.isEnabled()) {
                MutedPlayer mutedPlayer = SQLite.getMutedPlayer(e.getPlayer());
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String time = sdf.format(new Date(mutedPlayer.getTime()));
                for (String s : Config.getStringList("messages.yml", "YoureMuted")) {
                    e.getPlayer().sendMessage(s.replaceAll("&", "§")
                            .replaceAll("%reason%", mutedPlayer.getReason())
                            .replaceAll("%sender%", mutedPlayer.getSender())
                            .replaceAll("%time%", time));
                }
            }else if (File.isEnabled()){
                MutedPlayer mutedPlayer = File.getMutedPlayer(e.getPlayer());
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String time = sdf.format(new Date(mutedPlayer.getTime()));
                for (String s : Config.getStringList("messages.yml", "YoureMuted")) {
                    e.getPlayer().sendMessage(s.replaceAll("&", "§")
                            .replaceAll("%reason%", mutedPlayer.getReason())
                            .replaceAll("%sender%", mutedPlayer.getSender())
                            .replaceAll("%time%", time));
                }
            } else {
                e.getPlayer().sendMessage("§cEin schwerwiegender Fehler ist aufgetreten");
                e.getPlayer().sendMessage("§7Bitte melde dich im Support - Fehler CM-01CE");
            }
        }
    }
}
