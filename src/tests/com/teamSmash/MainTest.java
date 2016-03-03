package com.teamSmash;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * Created by branden on 3/3/16 at 18:18.
 */
public class MainTest {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./test");
        Main.createTables(conn);
        return conn;
    }

    //kill the tables so we have fresh data for new tests
    public void endConnection(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE account");
        stmt.execute("DROP TABLE event");
        stmt.execute("DROP TABLE account_event_map");
        conn.close();
    }

    @Test
    public void testCreateAccount() throws SQLException {
        Connection conn = startConnection();
        int affected = Main.createAccount(conn, "bob", "bobby");
        endConnection(conn);
        assert (affected == 1);
    }

    @Test
    public void testCreateEvent() throws SQLException {
        Connection conn = startConnection();
        int affected = Main.createEvent(conn, "event", "place", LocalTime.now(), LocalDate.now(), "image", "descrip");
        endConnection(conn);
        assert (affected == 1);
    }

    @Test
    public void testTableMapping() throws SQLException {
        Connection connection = startConnection();
        int affected = Main.mapUserToEvent(connection, 1, 1);
        endConnection(connection);
        assert (affected == 1);
    }

}