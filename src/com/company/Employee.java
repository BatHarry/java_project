package com.company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Employee extends Entity {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private Date dateOfHire;

    public Employee(int id, String firstName, String lastName, String phone, Date dateOfHire) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.dateOfHire = dateOfHire;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public Date getDateOfHire() {
        return dateOfHire;
    }

    public String getDateOfHireString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        return dateFormat.format(dateOfHire);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDateOfHire(Date dateOfHire) {
        this.dateOfHire = dateOfHire;
    }

    public String prettyPrint(){
        return  ANSI_GREEN+"id: "+ANSI_RESET+this.id+"\n"+
                ANSI_GREEN+"First Name: "+ANSI_RESET+this.firstName+"\n"+
                ANSI_GREEN+"Last Name: "+ANSI_RESET+this.lastName+"\n"+
                ANSI_GREEN+"Phone: "+ANSI_RESET+this.phone+"\n"+
                ANSI_GREEN+"Hired at: "+ANSI_RESET+this.dateOfHire+"\n";
    }

    public static ArrayList<Employee> getAll(){
        ArrayList<Employee> allEmployees = new ArrayList<>();
        ResultSet rs = null;
        try {
            Statement stmt = DB.con().createStatement();

            if (stmt.execute("SELECT * FROM employees")) {
                rs = stmt.getResultSet();
            }

            while(rs.next()){

                Employee employee = new Employee(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("phone"), rs.getDate("date_of_hire"));
                allEmployees.add(employee);
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return allEmployees;
    }

    public static Employee get(int id){
        Employee employee = null;
        ResultSet rs = null;

        try {
            PreparedStatement stmt = DB.con().prepareStatement("SELECT * FROM employees WHERE id=?");
            stmt.setInt(1, id);
            stmt.execute();
            rs = stmt.getResultSet();

            if(rs.next()){
                employee = new Employee(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("phone"), rs.getDate("date_of_hire"));
            }else{
                return null;
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return employee;
    }

    public static void create(HashMap<String, String> data){
        try{
            PreparedStatement stmt = DB.con().prepareStatement("INSERT INTO employees(first_name, last_name, phone, date_of_hire) VALUES (?,?,?,?)");
            stmt.setString(1, data.get("first_name"));
            stmt.setString(2, data.get("last_name"));
            stmt.setString(3, data.get("phone"));
            stmt.setString(4, data.get("date_of_hire"));

            stmt.execute();
            System.out.println(ANSI_GREEN+"Employee successfully created!"+ANSI_RESET);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static boolean delete(int id){
        try {
            PreparedStatement stmt = DB.con().prepareStatement("DELETE FROM employees WHERE id=?");
            stmt.setInt(1, id);
            stmt.execute();
            return true;
        }catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    public static void edit(int id, HashMap<String, String>data){
        try{
            PreparedStatement stmt = DB.con().prepareStatement("UPDATE employees SET first_name=?, last_name=?, phone=?, date_of_hire=?  WHERE id=?");
            stmt.setString(1, data.get("first_name"));
            stmt.setString(2, data.get("last_name"));
            stmt.setString(3, data.get("phone"));
            stmt.setString(4, data.get("date_of_hire"));
            stmt.setInt(5, id);
            stmt.execute();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
