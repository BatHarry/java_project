package com.company;

import java.sql.Connection;
import java.sql.DriverManager;

// Клас на базата от данни, който съдържа статични метод за връщане на връзката с базата. Така има само една връзка.
public class DB {
    private static Connection con;

    //Инициализация на връзката. Въведени са параметрите необходми за връзка с базата. Промени със своите параметри,
    //за да се осъществи връзка на твоята машина
    public static void createConnection(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/domoupravitel","user","password");
        }catch(Exception e){ System.out.println(e.toString());}
    }

    //Статичен метод, който връща връзката към базата данни
    public static Connection con() {
        return con;
    }
}
