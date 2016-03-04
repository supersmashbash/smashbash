package com.teamSmash;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

public class Main {

    static HashMap<String, Account> allAccounts = new HashMap();

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

    //this is the table we will use to populate lists of all events being attending by a user
    //as well as all users going to an event
    public static int mapUserToEvent(Connection conn, int accountId, int eventId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO account_event_map VALUES(NULL, ?, ?)");
        stmt.setInt(1, accountId);
        stmt.setInt(2, eventId);

        return stmt.executeUpdate();
    }

    public static ArrayList<Account> selectAccounts(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account");
        ResultSet results = stmt.executeQuery();

        ArrayList<Account> accountList = new ArrayList<>();

        while (results.next()) {
            int id = results.getInt("account_id");
            String name = results.getString("account_name");
            String password = results.getString("account_password");
            Account a = new Account(id, name, password);
            accountList.add(a);
        }

        stmt.close();
        return accountList;

    }

    public static Account selectAccount(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE account_id = ?");
        stmt.setInt(1, id);

        ResultSet result = stmt.executeQuery();

        if (result.next()) {
            Account account = new Account(result.getInt(1), result.getString(2), result.getString(3));
            return account;
        } else {
            Account account = null;
            return account;
        }
    }



    public static void main(String[] args) {
        Spark.init();


        Spark.get(
                "/login",
                ((request, response) -> {
                    Session session = new Session(request.session());
                    String name = session.attribute("accountName");
                    return allAccounts.get(name);
                    Account account = getAccountFromSession(request.session());

                    HashMap m = new HashMap();
                    if (account == null) {
                        return new ModelAndView(m, "login.html");
                    }
                    else {
                        m.put("name", account.getName());
                        m.put("password", account.getPassword());
                        m.put("id", account.getId());
                        m.put("events", account.getEvents());
                        return new ModelAndView(m, "home.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    Account account = null;
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    if (allAccounts.containsKey(name)) {
                        if (password.equalsIgnoreCase(allAccounts.get(name).getPassword())) {
                            account = allAccounts.get(name);
                            response.redirect("/login");
                        } else {
                            response.redirect("/login");
                        }
                    } else {
                        account = new Account(name, password);
                        allAccounts.put(account.getName(), account);
                        response.redirect("/login");
                    }
                    Session session = request.session();
                    session.attribute("userName", name);
                    allAccounts.put(account.getName(), account);

                    return "";
                })
        );

    static Account getAccountFromSession(Session session) {
        String name = session.attribute("accountName");
        return allAccounts.get(name);
    }
}
