/*
 * Copyright (C) 2013 Lord_Ralex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * MySQL Inherited subclass for making a connection to a MySQL server.
 *
 * Date Created: 2011-08-26 19:08
 *
 * @author PatPeter
 */
package com.lordralex.antimulti.patpeter.SQLibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public final class MySQL extends Database {

    private String hostname = "localhost";
    private String portnmbr = "3306";
    private String username = "minecraft";
    private String password = "";
    private String database = "minecraft";

    public MySQL(Logger log, String prefix, String hostname, String portnmbr, String database, String username, String password) {
        super(log, "[MySQL] ");
        this.hostname = hostname;
        this.portnmbr = portnmbr;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Override
    protected boolean initialize() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return true;
        } catch (ClassNotFoundException e) {
            this.writeError("Class Not Found Exception: " + e.getMessage() + ".", true);
            return false;
        }
    }

    @Override
    public Connection open() {
        if (initialize()) {
            String url = "";
            try {
                url = "jdbc:mysql://" + hostname + ":" + portnmbr + "/" + database;
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                writeError(url, true);
                writeError("Could not be resolved because of an SQL Exception: " + e.getMessage() + ".", true);
            }
        }
        return null;
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            this.writeError("Failed to close database connection: " + e.getMessage(), true);
        }
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public boolean checkConnection() {
        if (connection != null) {
            return true;
        }
        return false;
    }

    @Override
    public ResultSet query(String query) {
        Statement statement;
        ResultSet result = null;
        try {
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT CURTIME()");

            switch (this.getStatement(query)) {
                case SELECT:
                    result = statement.executeQuery(query);
                    break;

                default:
                    statement.executeUpdate(query);
            }
            return result;
        } catch (SQLException e) {
            writeError("Error in SQL query: " + e.getMessage(), false);
        }
        return result;
    }

    @Override
    public PreparedStatement prepare(String query) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
            return ps;
        } catch (SQLException e) {
            if (!e.toString().contains("not return ResultSet")) {
                writeError("Error in SQL prepare() query: " + e.getMessage(), false);
            }
        }
        return ps;
    }

    @Override
    public boolean createTable(String query) {
        Statement statement;
        try {
            if (query == null || query.isEmpty()) {
                writeError("SQL query empty: createTable(" + query + ")", true);
                return false;
            }

            statement = connection.createStatement();
            statement.execute(query);
            return true;
        } catch (Exception e) {
            writeError(e.getMessage(), true);
            return false;
        }
    }

    @Override
    public boolean checkTable(String table) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + table);
            if (result == null) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("exist")) {
                return false;
            } else {
                writeError("Error in SQL query: " + e.getMessage(), false);
            }
        }
        if (query("SELECT * FROM " + table) == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean wipeTable(String table) {
        Statement statement;
        String query;
        try {
            if (!checkTable(table)) {
                writeError("Error wiping table: \"" + table + "\" does not exist.", true);
                return false;
            }
            statement = connection.createStatement();
            query = "DELETE FROM " + table + ";";
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            if (!e.toString().contains("not return ResultSet")) {
                return false;
            }
        }
        return false;
    }
}