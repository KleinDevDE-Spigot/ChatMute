package de.kleindev.chatmute.commands;

import de.kleindev.chatmute.tools.Config;
import de.kleindev.chatmute.tools.Tools;
import de.kleindev.chatmute.tools.type.File;
import de.kleindev.chatmute.tools.type.MySQL;
import de.kleindev.chatmute.tools.type.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SpigotPlugin ~ChatMute
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 2017, created by KleinDev
 */
public class Mute implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(Config.getPermission("mute"))){
            if (args.length == 0){
                sender.sendMessage("");
            } else if (args.length == 1){
                if (args[0].equalsIgnoreCase("list")) {
                    int ids = 0;
                    if (!Config.exist("IDs.yml", "0")) ids = 1;
                    int maxsites = Config.getFile("IDs.yml").getKeys(true).size() / 5;
                    sender.sendMessage("§7=========[ §3ChatMute §7]==========");
                    for (int count = 0; count >= 5; count++) {
                        int time = Config.getInteger("IDs.yml", ids+".time");
                        int jahre = time / 60 / 24 / 365;
                        int monate = time / 60 / 24 / 365 / 12;
                        int tage = time / 60 / 24 % 365;
                        int stunden = time / 60 % 24;
                        int minuten = time % 60;
                        String name = Config.getString("Ids.yml", ids+".name");
                        String reason = Config.getString("IDs.yml", ids+".reason");
                        String description = Config.getString("IDs.yml", ids+".description");
                        sender.sendMessage(Config.getString("messages.yml", "listMutesFormat")
                        .replaceAll("&", "§")
                        .replaceAll("%name%", name)
                        .replaceAll("%reason%", reason)
                        .replaceAll("%description%", description)
                        .replaceAll("%years%", String.valueOf(jahre))
                        .replaceAll("%months%", String.valueOf(monate))
                        .replaceAll("%days%", String.valueOf(tage))
                        .replaceAll("%hours%", String.valueOf(stunden))
                        .replaceAll("%minutes%", String.valueOf(minuten)));
                    }
                    sender.sendMessage("§7 --- Site §3[1§7] of [§3" + maxsites + "§7] sites");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("list")) {
                    if (Integer.valueOf(args[1]) != null) {
                        int site = Integer.valueOf(args[1]);
                        int ids = 0;
                        if (site == 1){
                            if (!Config.exist("IDs.yml", "0")) ids = 1;
                        } else {
                            ids = (site*5)+1;
                        }

                        int maxsites = Config.getFile("IDs.yml").getKeys(true).size() / 5;
                        if (maxsites == 0) maxsites = 1;
                        sender.sendMessage("§7=========[ §3ChatMute §7]==========");
                        for (int count = 0; count % 5 == 0; count++) {
                            String format = Config.getString("messages.yml", "listMutesFormat");
                            int time = Config.getInteger("IDs.yml", ids+".time");



                            int jahre = time / 60 / 24 / 365;
                            int monate = time / 60 / 24 / 365 / 12;
                            int tage = time / 60 / 24 % 365;
                            int stunden = time / 60 % 24;
                            int minuten = time % 60;
                            String name = Config.getString("Ids.yml", ids+".name");
                            String reason = Config.getString("IDs.yml", ids+".reason");
                            String description = Config.getString("IDs.yml", ids+".description");
                            sender.sendMessage(format
                                    .replaceAll("&", "§")
                                    .replaceAll("%name%", name)
                                    .replaceAll("%reason%", reason)
                                    .replaceAll("%description%", description)
                                    .replaceAll("%years%", String.valueOf(jahre))
                                    .replaceAll("%months%", String.valueOf(monate))
                                    .replaceAll("%days%", String.valueOf(tage))
                                    .replaceAll("%hours%", String.valueOf(stunden))
                                    .replaceAll("%minutes%", String.valueOf(minuten)));
                        }
                        sender.sendMessage("§7 --- Site [§3" + site + "§7] of [§3" + maxsites + "§7] sites");
                    } else sender.sendMessage("The site '" + args[1] + "' is not a number!");
                } else {
                    if (Bukkit.getOfflinePlayer(args[0]) != null) {
                        if (Integer.valueOf(args[1]) != null) {
                            int id = Integer.parseInt(args[1]);
                            if (Tools.doesIDExist(id)) {
                                OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                                if (player.getPlayer().hasPermission(
                                        Config.getString("config.yml", "Permission.bypass"))){
                                    sender.sendMessage(Config.getString("messages.yml", "PlayerHasBypass")
                                    .replaceAll("&", "§")
                                    .replaceAll("%player%", player.getName()));
                                }
                                String reason = Config.getString("IDs.yml", id + ".reason");
                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                                long time = Config.getLong("IDs.yml", id + ".time");
                                String date = sdf.format(new Date(time + System.currentTimeMillis()));
                                if (MySQL.isEnabled())
                                    MySQL.addMute(player.getPlayer(), reason, time, sender.getName());
                                if (SQLite.isEnabled())
                                    SQLite.addMute(player.getPlayer(), reason, time, sender.getName());
                                if (File.isEnabled()) File.addMute(player.getPlayer(), reason, time, sender.getName());
                                for (String s : Config.getStringList("messages.yml", "onMute1")) {
                                    sender.sendMessage(s.replaceAll("&", "§")
                                            .replaceAll("%player%", player.getName()
                                                    .replaceAll("%time%", date)
                                                    .replaceAll("%reason%", reason)
                                                    .replaceAll("%sender%", sender.getName())));
                                }
                                if (player.isOnline()) {
                                    for (String s : Config.getStringList("messages.yml", "onMute2")) {
                                        player.getPlayer().sendMessage(s.replaceAll("&", "§")
                                                .replaceAll("%player%", player.getName()
                                                        .replaceAll("%time%", date)
                                                        .replaceAll("%reason%", reason)
                                                        .replaceAll("%sender%", sender.getName())));
                                    }
                                }
                            } else for (String s : Config.getStringList("messages.yml", "IDoesNotExist"))
                                sender.sendMessage(s
                                        .replaceAll("&", "§")
                                        .replaceAll("%id%", args[1]));
                        } else sender.sendMessage("§cThe id '" + args[1] + "' isnt a Number");
                    } else for (String s : Config.getStringList("messages.yml", "PlayerDoesNotExist"))
                        sender.sendMessage(s.replaceAll("&", "§")
                                .replaceAll("%player%", args[0]));
                }
            }
        } else for (String s : Config.getStringList("messages.yml", "NoPermission"))
            sender.sendMessage(s.replaceAll("&", "§")
                    .replaceAll("%permissions%", Config.getPermission("mutes"))
                    .replaceAll("%command%", "mutes"));
        return false;
    }
}
