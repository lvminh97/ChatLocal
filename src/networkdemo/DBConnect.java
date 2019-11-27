/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkdemo;

/**
 *
 * @author John McTavish
 */
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author John McTavish
 */
public class DBConnect {
    public String dbPath;
    public Connection conn;
    public Statement stmt;
    // Connect to database
    public void connect(String path){
        dbPath = path;
        try{
            String url = "jdbc:sqlite:" + dbPath;
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            System.out.println("Connect successful!");
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    // Execute a SQL statement
    public void execute(String sql) throws SQLException{
        stmt.execute(sql);
    }
    // Insert a record into database
    public void insert(String table, String[] param, String[] value) throws SQLException{
        String sql = "INSERT INTO " 
                   + table
                   + " (";
        for(int i = 0; i < param.length; i++){
            sql += param[i];
            if(i < param.length - 1){
                sql += ",";
            }
        }
        sql += ") VALUES (";
        for(int i = 0; i < value.length; i++){
            sql += value[i];
            if(i < value.length - 1){
                sql += ",";
            }
        }
        sql += ")";
        stmt.execute(sql);
    }
    public void insert(String table, String param, String val) throws SQLException{
        String sql = "INSERT INTO "
                   + table
                   + " (" + param + ")"
                   + " VALUES (" + val + ")";
        stmt.execute(sql);
    }
    // Update records in database
    public void update(String table, String[] param, String[] value, String cond) throws SQLException{
        String sql = "UPDATE " + table 
                   + " SET ";
        for(int i = 0; i < param.length; i++){
            sql += param[i] + "=" + value[i];
            if(i < param.length - 1){
                sql += ",";
            }
        }
        sql += " WHERE " + cond;
        stmt.execute(sql);
    }
    public void update(String table, String expr, String cond) throws SQLException{
        String sql = "UPDATE " + table
                   + " SET " + expr
                   + " WHERE " + cond;
        stmt.execute(sql);
    }
    // Select records from database
    public ResultSet select(String table, String cols, String cond) throws SQLException{
        ResultSet rs = null;
        String sql = "SELECT " + cols + " FROM " + table
                   + " WHERE " + cond;
        rs = stmt.executeQuery(sql);
        return rs;
    }
    public ResultSet select(String sql) throws SQLException{
        ResultSet rs = stmt.executeQuery(sql);
        return rs;
    }
    // Remove records from database
    public void delete(String table, String cond) throws SQLException{
        String sql = "DELETE FROM " + table + " WHERE " + cond;
        stmt.execute(sql);
    }
    // Close the connection to database
    public void close() throws SQLException{
        if(conn != null){
            conn.close();
            conn = null;
            System.out.println("Connection is closed!");
        }
    }
}
