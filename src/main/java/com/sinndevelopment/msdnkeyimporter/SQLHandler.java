package com.sinndevelopment.msdnkeyimporter;

import java.sql.*;

public class SQLHandler
{
    private Connection connection;

    public SQLHandler(String host, int port, String database, String user, String pass) throws SQLException
    {
        connectToDatabase(host, port, database, user, pass);
        if(connection != null)
        {
            createTables(connection);
        }
    }

    public Connection getConnection()
    {
        return connection;
    }

    private boolean connectToDatabase(String host, int port, String database, String user, String pass)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        try
        {
            System.out.println("MySQL JDBC Driver Registered");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, pass);
        }
        catch (SQLException e)
        {
            System.out.println("[SQL] Connection Failed. Check output console");
            e.printStackTrace();
            return false;
        }
        System.out.println("Connected.");
        return connection != null;
    }

    private void createTables(Connection connection) throws SQLException
    {
        PreparedStatement stmt;
        String query;
        for (ProductKey.Type type : ProductKey.Type.values())
        {
            query = "CREATE TABLE IF NOT EXISTS " + type.toString().toLowerCase()
                    + "(productname VARCHAR(255) NOT NULL,"
                    + "productkey VARCHAR(767) NOT NULL,"
                    + "claimdate VARCHAR(255));";
            stmt = connection.prepareStatement(query);
            stmt.executeUpdate();
            stmt.close();
        }
    }

    public void insertProductKey(ProductKey key) throws SQLException
    {
        if (exists(connection, key)) return;
        if(key.getKey().length() > 254)
            key.setKey("Too Long - Please see MSDN.");

        PreparedStatement statement;
        String insertRowSQL =
                "INSERT INTO " +
                        key.getKeyType().toString().toLowerCase() +
                        "(productname, productkey, claimdate) VALUES" +
                        "(?,?,?)";
        statement = connection.prepareStatement(insertRowSQL);
        statement.setString(1, key.getProduct());
        statement.setString(2, (key.getKey().length() > 254 ? key.getKey().substring(0, 254): key.getKey()));
        statement.setString(3, "");
        statement.executeUpdate();
        statement.close();
        System.out.println("[SQL] Added " + key.getKey() + " to the database");
    }

    private boolean exists(Connection connection, ProductKey key) throws SQLException
    {
        PreparedStatement statement;
        String existsSQL = "SELECT 1 FROM " + key.getKeyType().toString().toLowerCase() +
                " WHERE productkey = ?";

        statement = connection.prepareStatement(existsSQL);
        statement.setString(1, key.getKey());
        ResultSet rs = statement.executeQuery();
        System.out.println("[SQL] Product Key: " + key.getKey() + " existed in database, not writing to database");
        return rs.next();
    }

}
