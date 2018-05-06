package de.kleindev.chatmute.tools;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.util.List;

/**
 * SpigotPlugin ~ChatMute
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 2017, created by KleinDev
 */
public class Config {
    public static boolean set(String fileName, String key, String value){
        try {
            YamlConfiguration.loadConfiguration(new File(fileName)).set(key, value);
            return true;
        } catch(YAMLException e){
            Debug.err(e.getMessage());
            return false;
        }
    }

    public static String getString(String fileName, String key){
        try {
            return YamlConfiguration.loadConfiguration(new File(fileName)).getString(key);
        } catch (YAMLException e){
            Debug.err(e.getMessage());
            return null;
        }
    }

    public static boolean getBoolean(String fileName, String key){
        try {
            return YamlConfiguration.loadConfiguration(new File(fileName)).getBoolean(key);
        }catch(YAMLException e){
            Debug.err(e.getMessage());
            return false;
        }
    }

    public static List<String> getStringList(String fileName, String key){
        return YamlConfiguration.loadConfiguration(new File(fileName)).getStringList(key);
    }

    public static FileConfiguration getFile(String fileName){
        return YamlConfiguration.loadConfiguration(new File(fileName));
    }

    public static String getCommand(String commandField){
        return YamlConfiguration.loadConfiguration(new File("config.yml")).getString("command." + commandField);
    }

    public static String getMessage(String messageField){
        return YamlConfiguration.loadConfiguration(new File("messages.yml")).getString(messageField);
    }

    public static String getPermission(String permissionField){
        return YamlConfiguration.loadConfiguration(new File("config.yml")).getString("permission." + permissionField);
    }

    public static boolean exist(String fileName, String key){
        return YamlConfiguration.loadConfiguration(new File(fileName)).isConfigurationSection(key);
    }

    public static Long getLong(String fileName, String key) {
        return YamlConfiguration.loadConfiguration(new File(fileName)).getLong(key);
    }

    public static Integer getInteger(String fileName, String key){
        return YamlConfiguration.loadConfiguration(new File(fileName)).getInt(key);
    }
}
