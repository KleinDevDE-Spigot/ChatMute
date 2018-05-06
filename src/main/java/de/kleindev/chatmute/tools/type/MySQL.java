package de.kleindev.chatmute.tools.type;


import de.kleindev.chatmute.tools.Config;
import de.kleindev.chatmute.tools.Debug;
import de.kleindev.chatmute.tools.MutedPlayer;
import de.kleindev.chatmute.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQL {
	 private final static String host = Config.getString("config.yml", "MySQL.host");
	 private final static String port = Config.getString("config.yml", "MySQL.port");
	 private final static String database = Config.getString("config.yml", "MySQL.database");
	 private final static String username = Config.getString("config.yml", "MySQL.username");
	 private final static String password = Config.getString("config.yml", "MySQL.password");
	 private static Connection con;
	 private final static boolean isEnabled = YamlConfiguration.loadConfiguration(new File("config")).getBoolean("Type.MySQL.enabled");

	 public static boolean isEnabled(){
	     return isEnabled;
     }


	public static void connect() {
		if(!isConnected()) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username+"", password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
    public static void close() {
    	if (isConnected()) {
    		try {
    			con.close();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	
	}
    
    public static boolean isConnected() {
		return (con == null ? false : true);
	}
    
    public static void configurate() {
    	try {
			PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS ChatMute(" +
					"UUID VARCHAR(100)," +
					"Reason VARCHAR(100)," +
					"Time VARCHAR(100)," +
					"Sender VARCHAR(100))");
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public static void update(String qry) {
		try {
			PreparedStatement ps = con.prepareStatement(qry);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    public static ResultSet getResult(String qry) {
    	try {
			PreparedStatement ps = con.prepareStatement(qry);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return null;
	}

    public static List<MutedPlayer> getMutes() {
		List<MutedPlayer> list = new ArrayList<>();
    	try {
    		PreparedStatement ps = con.prepareStatement("SELECT * FROM ChatMute");
    		ResultSet rs = ps.executeQuery();
	    	while(rs.next()) {
				list.add(new MutedPlayer(Tools.getPlayer(rs.getString("UUID")), rs.getString("Reason"), rs.getLong("Time"), rs.getString("Sender")));
	    	}
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return list;
    }
    public static boolean addMute(Player player, String reason, long time, String sender){
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO ChatMute ('UUID', 'Reason', 'Time', 'Sender')" +
                    "VALUES (?, ?, ?, ?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, reason);
            ps.setLong(3, time + System.currentTimeMillis());
            ps.setString(4, sender.toString());
            return ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeMute(Player player){
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM ChatMute WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            return ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isMuted(Player player){
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ChatMute WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            rs.next();
            long time = rs.getLong("Time");
			if (time > System.currentTimeMillis()){
				removeMute(player);
				return true;
			} else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static MutedPlayer getMutedPlayer(Player player){
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ChatMute WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            long time = 0;
            String reason = "";
            String sender = "";
            if (rs.next()){
                player = Tools.getPlayer(rs.getString("UUID"));

                reason = rs.getString("Reason");
                time = rs.getLong("Time");
                sender = rs.getString("Sender");
            }
            return new MutedPlayer(player, reason, time, sender);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
