/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evm;

import MFS100.FingerData;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class DatabaseHandler {
    private final String HOST = "jdbc:mysql://localhost:3306/evm_tmp_db";
    private final String USER = "root";
    private final String PASS = "";

    private Connection conn;
    private Statement statement;
    
    public DatabaseHandler() {
        init();
    }

    private void init() {
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(HOST, USER, PASS);
            statement = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        } /*catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    public boolean addToDB(String name, String ansi, String iso){
        //System.out.println(ansi+"\n"+iso);
        try {
            statement.execute("INSERT INTO fpdata(Name, ansi, iso) VALUES('"+name+"','"+ansi+"','"+iso+"')");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public boolean addToDB(String name, String arr[]){
        try {
            StringBuilder str = new StringBuilder();
            str.append("INSERT INTO finger_data(Name, fingerL1, fingerL2, fingerL3, fingerL4, fingerL5, fingerR1, fingerR2, fingerR3, fingerR4, fingerR5) VALUES('");
            str.append(name+"','");
            
            for(int i = 0;i < 10; i++){
                if(i <= 8)
                    str.append(arr[i]+"','");
                else
                    str.append(arr[i]+"')");
            }
            statement.execute(str.toString());
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
    
    
    public String getANSI(int rowno) throws Exception{
        try {
            ResultSet rset = statement.executeQuery("SELECT ansi FROM fpdata ORDER BY slno LIMIT "+rowno+",1");
            rset.first();
            return rset.getString(1);
        } catch (SQLException ex) {
            //Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("EODB");  
            //return null;
        }
    }
    
    public String[] getFingerANSI(int rowno) throws Exception{
        try {
            ResultSet rset = statement.executeQuery("SELECT * FROM finger_data ORDER BY slno LIMIT "+rowno+",1");
            rset.first();
            String arr[] = new String[10];
            
            for(int i = 0; i < 10; i++){
                arr[i] = rset.getString(i+4);
            }
            
            return arr;
        } catch (SQLException ex) {
            //Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("EODB");  
            //return null;
        }
    }
    
    public String getUsername(String ansi){
        try {
            ResultSet rset = statement.executeQuery("SELECT name FROM finger_data WHERE ansi = '"+ansi+"'");
            rset.first();
            return rset.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getUsername(int rowno){
        try {
            ResultSet rset = statement.executeQuery("SELECT name FROM finger_data ORDER BY slno LIMIT "+rowno+",1");
            rset.first();
            return rset.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int count(){
        try {
            ResultSet rset = statement.executeQuery("SELECT COUNT(slno) FROM finger_data");
            rset.first();
            System.out.println(rset.getInt(1));
            return rset.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public void incrementVote(String user, int nominee){
        try {
            ResultSet rset = statement.executeQuery("SELECT * FROM polldata ORDER BY nominee LIMIT "+(nominee-1)+",1");
            rset.first();
            String nmdb = rset.getString(1);
            int count = rset.getInt(2);
            count++;
            int ret = statement.executeUpdate("UPDATE polldata SET count = "+count+" WHERE nominee = '"+nmdb+"'");
            ret = statement.executeUpdate("UPDATE finger_data SET flag = 1 WHERE name = '"+user+"'");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getFlag(String username){
        try {
            ResultSet rset = statement.executeQuery("SELECT flag FROM finger_data where name = '"+username+"'");
            rset.first();
            return rset.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
}
