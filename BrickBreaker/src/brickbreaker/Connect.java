/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.sql.*;

/**
 * The Connect class makes connections to the database and allows queries and
 * updates to run
 *
 * @author
 */
public class Connect
{

    private static Connection conn = null;

    Connect()
    {
        try
        {
            String userName = "root";
            String password = "root";
            String url = "jdbc:mysql://localhost/accountmanager";
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connection established");
        } catch (Exception e)
        {
            System.err.println("Cannot connect to database server" + e);
        }
    }

    public int update(String sql) throws SQLException
    {
        int numRowsUpdated = 0;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            // run query
            numRowsUpdated = stmt.executeUpdate();
            
            System.out.println("SQL Code run : " + sql);
        } catch (SQLException e) {
            System.err.println("[DB] - update/delete/insert error - " + e);
            System.out.println("SQL CODE THAT FAILED: " + sql);
        }
        return numRowsUpdated;
    }//update method

    public ResultSet query(String sql) throws SQLException
    {
        ResultSet rs = null;
        try {
            // allow free movement of cursor
            PreparedStatement stmt = conn.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery();
            
           // System.out.println("SQL Code run : " + sql);
        } catch (SQLException e) {
            System.err.println("[DB] - query error - " + e);
            System.out.println("SQL CODE THAT FAILED: " + sql);
        }
        
        return rs;
    }//query method

    void close() throws SQLException
    {
        this.conn.close();
    }
}
