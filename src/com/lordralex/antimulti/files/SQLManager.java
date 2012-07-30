package com.lordralex.antimulti.files;

import com.lordralex.antimulti.config.Configuration;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.PatPeter.SQLibrary.MySQL;

/**
 * @version 1.0
 * @author Joshua
 */
public class SQLManager implements Manager {

    MySQL mysql;
    Connection conn;

    @Override
    public String getPassword(String name) {
        ResultSet result = mysql.query("SELECT pass FROM ampasswords WHERE name=' " + name + "'");
        if (result == null) {
            return null;
        }
        try {
            String pw = result.getString("pass");
            return pw;
        } catch (SQLException ex) {
            Logger.getLogger(SQLManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void setPassword(String name, String newPass) throws IOException {
        mysql.query("UPDATE ampassword SET pass='" + newPass + "' WHERE name='" + name + "'");
    }

    @Override
    public Manager setup() {
        String[] info = Configuration.getSQLInfo();
        mysql = new MySQL(Configuration.getPlugin().getLogger(), "[AM]", info[0], info[1], info[4], info[2], info[3]);
        if (mysql.checkConnection()) {
            conn = mysql.open();
            mysql.createTable("CREATE TABLE ampasswords (name VARCHAR(16), pass(512)");
            return this;
        } else {
            Logger logger = Configuration.getPlugin().getLogger();
            logger.severe("Unable to connect to mySQL database, using flatfile system");
            Manager manager = new FlatFileManager();
            manager.setup();
            return manager;
        }

    }

    @Override
    public void close() {
        try {
            conn.close();
            mysql.close();
        } catch (SQLException ex) {
            Logger.getLogger(Configuration.getPlugin().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
