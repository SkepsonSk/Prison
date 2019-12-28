package pl.trollcraft.util;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    // --------

    private Connection connection;

    // -------- --------- -------- ------- ---------

    public DatabaseHandler(Main plugin) {
        host = plugin.getConfig().getString("host");
        port = plugin.getConfig().getString("port");
        database = plugin.getConfig().getString("database");
        username = plugin.getConfig().getString("username");
        password = plugin.getConfig().getString("password");
    }

    public Connection openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        synchronized (Main.getDatabaseHandler()) {
            if (connection != null && !connection.isClosed())
                return connection;

            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        }

        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void prepare() {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS prison_warps (");
        query.append("name VARCHAR(32) NOT NULL PRIMARY KEY,");
        query.append("location VARCHAR(64) NOT NULL)");
        try{
            Statement statement = openConnection().createStatement();
            statement.executeUpdate(query.toString());
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

}
