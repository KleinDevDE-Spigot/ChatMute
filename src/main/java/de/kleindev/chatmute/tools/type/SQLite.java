package de.kleindev.chatmute.tools.type;

import de.kleindev.chatmute.tools.Config;
import de.kleindev.chatmute.tools.MutedPlayer;
import de.kleindev.chatmute.tools.Tools;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SpigotPlugin ~ChatMute
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 2017, created by KleinDev
 */
public class SQLite {
    private static Connection c;
    private final static boolean isEnabled = YamlConfiguration.loadConfiguration(new File("config")).getBoolean("Type.SQLite.enabled");

    public static boolean isEnabled(){
        return isEnabled;
    }


    public static List<MutedPlayer> getMutes(){
        List<MutedPlayer> output = new ArrayList<>();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM Mutes");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                output.add(new MutedPlayer(
                        Tools.getPlayer(rs.getString("UUID")),
                        rs.getString("Reason"),
                        rs.getLong("Time"),
                        rs.getString("Sender")
                ));
            }
            ps.close();
            rs.close();
            return output;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MutedPlayer getMutedPlayer(Player player){
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM Mutes WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new MutedPlayer(player,
                        rs.getString("Reason"),
                        rs.getLong("Time"),
                        rs.getString("Sender"));
            }
            rs.close();
            ps.close();
            return null;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addMute(Player player, String reason, long time, String sender){
        try {
            c.setAutoCommit(false);

            PreparedStatement ps = c.prepareStatement("INSERT INTO Mutes ('UUID', 'Reason', 'Time', 'Sender') " +
                    "VALUES (?, ?, ?, ?);");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, reason);
            ps.setLong(3, time + System.currentTimeMillis());
            ps.setString(4, sender);
            boolean status =  ps.execute();

            ps.close();
            c.commit();
            return status;
        } catch ( Exception e ) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeMute(Player player){
        try {
            PreparedStatement ps = c.prepareStatement("DELETE FROM Mutes WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            boolean status = ps.execute();
            ps.close();
            c.commit();
            return status;
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
    }

    public static boolean isMuted(Player player){
        try {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM Mutes WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                long time = rs.getLong("Time");
                if (time > System.currentTimeMillis()){
                    removeMute(player);
                    return true;
                } else return false;
            }
            rs.close();
            ps.close();
            return false;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean start(){
        boolean status = true;
        boolean failure = false;
        c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite: plugins/ChatMute/" + Config.getString("config.yml", "Type.SQLite.FileName"));
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            status = false;
        }
        return status;
    }

    private static boolean createTables(){
        try {

            PreparedStatement ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS Players " +
                    "(Nam               TEXT    NOT NULL," +
                    " UUID              TEXT    PRIMARY KEY NOT NULL, " +
                    " Aussteller        TEXT    NOT NULL, " +
                    " ID                TEXT    REAL    , " +
                    " Zeitraum          TEXT    NOT NULL)");
            ps.executeUpdate();
            ps.close();
            return true;
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
    }
}
