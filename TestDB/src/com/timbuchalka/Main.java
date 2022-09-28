package com.timbuchalka;

import java.sql.*;

public class Main {
    public static final String DB_NAME = "testjava.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\vtriw\\IdeaProjects\\TestDB\\testjava.db" + DB_NAME;

    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";


    public static void main(String[] args) {

//        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:/Volumes/Production/Courses/Programs/JavaPrograms/TestDB/testjava.db");
//            Statement statement = conn.createStatement()) {
//            statement.execute("CREATE TABLE contacts (name TEXT, phone INTEGER, email TEXT)");
        try {
            Connection conn = DriverManager.getConnection(CONNECTION_STRING);
//            conn.setAutoCommit(false);
            Statement statement = conn.createStatement();
            statement.execute("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS +
                    " (" + COLUMN_NAME + " text, " +
                    COLUMN_PHONE + " integer, " +
                    COLUMN_EMAIL + " text" + " )");

            statement.execute("INSERT INTO " + TABLE_CONTACTS + " (" +
                    COLUMN_NAME + ", " +
                    COLUMN_PHONE + ", " +
                    COLUMN_EMAIL + " )" + "VALUES('Tim',654678,'tim@email.com')");

            statement.execute("INSERT INTO " + TABLE_CONTACTS + " (" +
                    COLUMN_NAME + ", " +
                    COLUMN_PHONE + ", " +
                    COLUMN_EMAIL + " )" + "VALUES('Joe',45632,'joe@anywhere.com')");

            statement.execute("INSERT INTO " + TABLE_CONTACTS + " (" +
                    COLUMN_NAME + ", " +
                    COLUMN_PHONE + ", " +
                    COLUMN_EMAIL + " )" + "VALUES('Jane',4829484,'jane@somewhere.com')");

            statement.execute("INSERT INTO " + TABLE_CONTACTS + " (" +
                    COLUMN_NAME + ", " +
                    COLUMN_PHONE + ", " +
                    COLUMN_EMAIL + " )" + "VALUES('Fido',9038,'dog@email.com')");

            statement.execute("UPDATE " + TABLE_CONTACTS + " SET " + COLUMN_PHONE + "=5566789" + " WHERE " + COLUMN_NAME + "= 'Jane'");
            statement.execute("DELETE FROM " + TABLE_CONTACTS + " WHERE " + COLUMN_NAME + "='Joe'");

            ResultSet results = statement.executeQuery("SELECT * FROM contacts");
            while (results.next()){
                System.out.println(results.getString("name") + " " + results.getInt("phone") + " " + results.getString("email"));
            }
            results.close();

            statement.close();
            conn.close();

//            Connection conn = DriverManager.getConnection("jdbc:sqlite:D:\\databases\\testjava.db");
//            Class.forName("org.sql.JDBC");

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void insertContacts(Statement statement, String name, int phone, String email) throws SQLException {

    }
}
