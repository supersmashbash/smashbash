package com.teamSmash;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS account (account_id IDENTITY, account_name VARCHAR, account_password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS event (event_id IDENTITY, event_name VARCHAR, event_location VARCHAR, event_time VARCHAR, " +
                                          "event_date VARCHAR, event_image VARCHAR, event_description VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS account_event_map (map_id IDENTITY, account_id INT, event_id INT)");
        stmt.close();
    }

    public static int createAccount(Connection conn,String name, String password ) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO account VALUES (NULL, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, password);
        return stmt.executeUpdate();
    }

    public static int createEvent(Connection conn, String name, String location, LocalTime time,
                                      LocalDate date, String image, String description) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO event VALUES (NULL, ?, ?, ?, ?, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, location);
        stmt.setString(3, time.toString());
        stmt.setString(4, date.toString());
        stmt.setString(5, image);
        stmt.setString(6, description);
        return stmt.executeUpdate();
    }

    public static int mapUserToEvent(Connection conn, int accountId, int eventId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO account_event_map VALUES(NULL, ?, ?)");
        stmt.setInt(1, accountId);
        stmt.setInt(2, eventId);
        return stmt.executeUpdate();
    }

    public static void main(String[] args) {


    }
}
