package com.company;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    private static Connection con;

    public static void createConnection(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/domoupravitel","mharalanov","ABcd_1234!");
        }catch(Exception e){ System.out.println(e.toString());}
    }

    public static Connection con() {
        return con;
    }
}
