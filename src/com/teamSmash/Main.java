package com.teamSmash;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import jodd.json.JsonSerializer;
import spark.Session;
import spark.Spark;
import java.util.*;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS account (account_id IDENTITY, account_name VARCHAR, account_password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS event (event_id IDENTITY, event_name VARCHAR, event_location VARCHAR, event_time VARCHAR, " +
                "event_date VARCHAR, event_image VARCHAR, event_description VARCHAR, event_owner INT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS account_event_map (map_id IDENTITY, account_id INT, event_id INT)");
        stmt.close();
    }

    public static int createAccount(Connection conn, String name, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO account VALUES (NULL, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, password);

        return stmt.executeUpdate();
    }

    public static int createEvent(Connection conn, String name, String location, LocalTime time,
                                  LocalDate date, String image, String description, int accountId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO event VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, name);
        stmt.setString(2, location);
        stmt.setTime(3, (Time.valueOf(time)));  //here I am needing to convert a LocalTime object into a Time object with the DB will accept more freely. I think.
        stmt.setDate(4, Date.valueOf(date));  //same here but for Date.
        stmt.setString(5, image);
        stmt.setString(6, description);
        stmt.setInt(7, accountId);
        int affected = stmt.executeUpdate(); //this is so I can check to see if something was created
        int eventId = 0;  //just have to initialize this I guess

        ResultSet result = stmt.getGeneratedKeys();  //this should contain the auto-generated ID

        if (result.next()) {
            eventId = result.getInt(1);  //assign the eventId to the auto-generated ID
        }
        stmt.close();

        PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO account_event_map VALUES (NULL, ?, ?)");
        stmt2.setInt(1, accountId);
        stmt2.setInt(2, eventId);


        return affected;
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
            int id = results.getInt(1);
            String name = results.getString(2);
            String password = results.getString(3);
            Account a = new Account(id, name, password);
            accountList.add(a);
        }

        stmt.close();
        return accountList;

    }

    //get the account by the account name
    public static Account selectAccount(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE account_name = ?");
        stmt.setString(1, name);

        ResultSet result = stmt.executeQuery();

        if (result.next()) {
            Account account = new Account(result.getInt(1), result.getString(2), result.getString(3));
            return account;
        } else {
            Account account = null;
            return account;
        }
    }

    //get the account by the account id
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


    //I hope that this method is limiting output to dates from the current date to three months from the current date.
    public static ArrayList<Event> selectEvents(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM event WHERE event_date BETWEEN CURRENT_DATE AND DATEADD(MONTH, 3, CURRENT_DATE )");
        ResultSet results = stmt.executeQuery();

        ArrayList<Event> eventList = new ArrayList<>();

        while (results.next()) {
            eventList.add(buildEventFromDb(results));
        }
        return eventList;
    }

    //select a single event by event id
    public static Event selectEvent(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM event WHERE event_id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();

        if (results.next()) {
            Event event = buildEventFromDb(results);
            return event;
        } else {
            return null;
        }
    }

    public static void editEvent(Connection conn, int eventId, String name, String location, LocalTime time, LocalDate date, String image, String description, int accountId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE event SET event_name = ?, event_location = ?, " +
                "event_time = ?, event_date = ?, event_image = ?, event_description = ?, event_owner = ?" +
                "WHERE event_id = ?");
        stmt.setString(1, name);
        stmt.setString(2, location);
        stmt.setTime(3, Time.valueOf(time));
        stmt.setDate(4, Date.valueOf(date));
        stmt.setString(5, image);
        stmt.setString(6, description);
        stmt.setInt(7, accountId);
        stmt.setInt(8, eventId);
        stmt.execute();
    }

    //once i know this works i can take some of the SQL out.
    public static ArrayList<AccountEvents> getAccountEvents(Connection conn, int accountId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT m.event_id, e.event_name " +
                                                        "FROM account_event_map m " +
                                                        "INNER JOIN event e ON m.event_id = e.event_id " +
                                                        "WHERE m.account_id = ? ");
        stmt.setInt(1, accountId);
        ResultSet results = stmt.executeQuery();
        ArrayList<AccountEvents> accountEventsList = new ArrayList<>();


//        ResultSetMetaData rsmd = results.getMetaData();
//
//        String name = rsmd.getColumnName(1);
//        String name2 = rsmd.getColumnName(2);
//        System.out.printf("STTTOPPPP");

        while (results.next()) {
            int eventId = results.getInt(1);
            String eventName = results.getString(2);

            Account account = selectAccount(conn, accountId);
            Event event = selectEvent(conn, eventId);
            accountEventsList.add(new AccountEvents(account, event));
        }
        return accountEventsList;

    }

    public static int getAccountId(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT account_id FROM account WHERE account_name = ?");
        stmt.setString(1, name);
        ResultSet results = stmt.executeQuery();

        int accountId = 0;
        while (results.next()) {
            accountId = results.getInt(1);
        }

        return accountId;
    }

    public static Event buildEventFromDb(ResultSet results) throws SQLException {
        int id = results.getInt(1);
        String name = results.getString(2);
        String location = results.getString(3);
        Time time = results.getTime(4);
        Date date = results.getDate(5);
        String image = results.getString(6);
        String description = results.getString(7);
        int accountId = results.getInt(8);
        Event event = new Event(id, name, location, time.toLocalTime(), date.toLocalDate(), image, description, accountId);
        return event;
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        //createEvent(conn, "event1", "folly beach", LocalTime.now(), LocalDate.now(), "https://www.google.com/search?q=beach+party&espv=2&biw=1366&bih=597&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjJzpTur6fLAhXF7iYKHS3-AywQ_AUIBigB#imgrc=NeFWZwo9gu3qVM%3A", "beach party");

        Spark.externalStaticFileLocation("public");

        Spark.init();

        //remember to remove this once everything is functioning.
        Spark.get(
                "/login",
                ((request, response) -> {
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(selectAccounts(conn));
                })
        );
        Spark.get(
                "/events",
                ((request, response) -> {
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(selectEvents(conn));
                })
        );

        Spark.get(
                "/event",
                ((request, response) -> {
                    int eventId = Integer.valueOf(request.queryParams("id")) ;

                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(selectEvent(conn, eventId));
                })
        );



        Spark.post(
                "/login",
                ((request, response) -> {
                    //get user input
                    String name = request.queryParams("username");
                    String password = request.queryParams("password");
                    //grab account from DB by name if it exists (null if not)
                    Account account = selectAccount(conn, name);

                    JsonSerializer serializer = new JsonSerializer();

                    //create a session
                    Session session = request.session();

                    if ( (account != null) && (password.equals(account.getPassword())) ) {  //if exist and the pass matches
                        int id = getAccountId(conn, name);
                        session.attribute("userName", name);
                        return serializer.serialize(selectAccount(conn, id));
                    } else if (account == null) {   //if the user does not yet exist, create it
                        createAccount(conn, name, password);
                        int id = getAccountId(conn, name);
                        session.attribute("userName", name);
                        return serializer.serialize(selectAccount(conn, id));
                    } else {
                        return "Password mismatch";
                    }
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    return "";
                })
        );



//                ((request, response) -> {
//                    Session session = request.session();
//                    String name = session.attribute("accountName");
//                    return allAccounts.get(name);
//                    Account account = getAccountFromSession(request.session());
//
//                    HashMap m = new HashMap();
//                    if (account == null) {
//                        return new ModelAndView(m, "login.html");
//                    }
//                    else {
//                        m.put("name", account.getName());
//                        m.put("password", account.getPassword());
//                        m.put("id", account.getId());
//                        m.put("events", account.getEvents());
//                        return new ModelAndView(m, "home.html");
//                    }
//                }
//
//        );
//        Spark.post(
//                "/create-user",
//                ((request, response) -> {
//                    Account account = null;
//                    String name = request.queryParams("loginName");
//                    String password = request.queryParams("password");
//                    if (allAccounts.containsKey(name)) {
//                        if (password.equalsIgnoreCase(allAccounts.get(name).getPassword())) {
//                            account = allAccounts.get(name);
//                        } else {
//                        }
//                    } else {
//                        account = new Account(name, password);
//                        allAccounts.put(account.getName(), account);
//                    }
//                    Session session = request.session();
//                    session.attribute("userName", name);
//                    allAccounts.put(account.getName(), account);
//
//                    return "";
//                })
//        );
//
//    static Account getAccountFromSession(Session session) {
//        String name = session.attribute("accountName");
//        return allAccounts.get(name);
//    }
    }
}
