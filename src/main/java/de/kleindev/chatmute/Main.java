package de.kleindev.chatmute;

import de.kleindev.chatmute.tools.MutedPlayer;
import de.kleindev.chatmute.tools.type.File;
import de.kleindev.chatmute.tools.type.MySQL;
import de.kleindev.chatmute.tools.type.SQLite;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {
    public static List<MutedPlayer> mutedPlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        check();
        if (MySQL.isEnabled()) {
            MySQL.connect();
            MySQL.configurate();
        } else if (SQLite.isEnabled()) SQLite.start();
        else if (File.isEnabled()) ;
        loadMutes();
    }

    @Override
    public void onDisable() {
        if (MySQL.isEnabled()) MySQL.close();
    }

    void loadMutes(){
        if (MySQL.isEnabled())
            mutedPlayers = MySQL.getMutes();
        if (SQLite.isEnabled())
            mutedPlayers = SQLite.getMutes();
        if (File.isEnabled())
            mutedPlayers = File.getMutes();
    }

    void check(){
        int active = 0;
        if (MySQL.isEnabled()) active++;
        if (SQLite.isEnabled()) active++;
        if (File.isEnabled()) active++;
        if (active > 1){
            //Multiple save allowed or not?
            System.err.println("Yout must define ONE type in the config! -- MySQL | SQLite | File");
            System.exit(1);
        } else if (active < 1){
            System.err.println("You must define one type in the config! -- MySQL | SQLite | File");
            System.exit(1);
        }

        if (MySQL.isEnabled()) {
            if (getConfig().getString("Type.MySQL.host").isEmpty()) {
                System.err.println("No MySQL Host defined, please check your Config!");
                System.exit(1);
            } else if (getConfig().getString("Type.MySQL.port").isEmpty()) {
                System.err.println("No MySQL Port defined, please check your Config!");
                System.exit(1);
            } else if (getConfig().getString("Type.MySQL.database").isEmpty()) {
                System.err.println("No MySQL Database defined, please check your Config!");
                System.exit(1);
            } else if (getConfig().getString("Type.MySQL.username").isEmpty()) {
                System.err.println("No MySQL Username defined, please check your Config!");
                System.exit(1);
            } else if (getConfig().getString("Type.MySQL.password").isEmpty()) {
                System.err.println("No MySQL Password defined, please check your Config!");
                System.exit(1);
            }
        } else if (SQLite.isEnabled()) {
            if (getConfig().getString("Type.SQLite.Filename").isEmpty()){
                System.err.println("No SQLite Database Filename specified, please check your Config!");
                System.exit(1);
            }
        } else if (File.isEnabled()){
            if (getConfig().getString("Type.File.Filename").isEmpty()){
                System.err.println("No File Filename specified, please check your Config!");
                System.exit(1);
            }
        }
    }
}
