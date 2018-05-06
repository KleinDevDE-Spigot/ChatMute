package de.kleindev.chatmute.tools;

/**
 * SpigotPlugin ~ChatMute
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 2017, created by KleinDev
 */
public class Debug {
    private final static boolean isEnabled = Config.getBoolean("config.yml", "Debug");

    public static void err(String message){
        if (isEnabled()) System.err.println(message);
    }

    public static boolean isEnabled(){
        return isEnabled;
    }
}
