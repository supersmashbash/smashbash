package com.teamSmash;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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
        Main.createAccount(conn, "Sal", "Amander");
        endConnection(conn);
        assertTrue(affected == 1);
    }

    @Test
    public void testCreateEvent() throws SQLException {
        Connection conn = startConnection();
        int affected = Main.createEvent(conn, "event", "place", LocalTime.now(), LocalDate.now(), "image", "descrip");
        endConnection(conn);
        assertTrue(affected == 1);
    }

    @Test
    public void testTableMapping() throws SQLException {
        Connection connection = startConnection();
        int affected = Main.mapUserToEvent(connection, 1, 1);
        endConnection(connection);
        assertTrue(affected == 1);
    }

    @Test
    public void testSelectAccounts() throws SQLException {
        Connection conn = startConnection();

        Main.createAccount(conn, "bob", "bobby");
        Main.createAccount(conn, "Sal", "Amander");

        ArrayList<Account> accountList = Main.selectAccounts(conn);
        endConnection(conn);

        assertTrue(!accountList.isEmpty());
    }

    @Test
    public void testSelectAccount() throws SQLException {
        Connection conn = startConnection();

        Main.createAccount(conn, "bob", "bobby");
        Main.createAccount(conn, "Sal", "Amander");

        Account a = Main.selectAccount(conn, 1);
        endConnection(conn);

        assertTrue(a.getName().equals("bob"));
    }


}