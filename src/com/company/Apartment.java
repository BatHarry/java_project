package com.company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

// Клас със функционалностите свързани с апартаментите
public class Apartment extends Entity {
    private int id;
    private String family_name;
    private String contact;
    private int amountOfPeople;
    private int tax;
    private int since;
    private int paid;
    private int owes;
    private Date lastDate;
    private Date startDate;

    //при инициализация се подава id на апартамента
    public Apartment(int id) {
        ResultSet rs = null;

        try {
            PreparedStatement stmt = DB.con().prepareStatement("SELECT apartment.id, apartment.name, apartment.people, apartment.phone, t.amount, TIMESTAMPDIFF(MONTH, clients.created_at, CURRENT_TIMESTAMP) as `since`,  tp.for_month as `last_payment`, clients.created_at as start_date, COUNT(taxes_paid.id) as `paid` FROM apartment\n" +
                    "LEFT JOIN clients on apartment.client = clients.id\n" +
                    "LEFT JOIN taxes t on apartment.client = t.client\n" +
                    "LEFT JOIN taxes_paid on apartment.id=taxes_paid.apartment\n" +
                    "LEFT JOIN taxes_paid as tp ON taxes_paid.apartment=apartment.id\n" +
                    "WHERE apartment.id=?\n" +
                    "GROUP BY t.id, last_payment ORDER BY last_payment DESC LIMIT 1;");
            stmt.setInt(1, id);
            stmt.execute();
            rs = stmt.getResultSet();

            if(rs.next()){
                this.id = rs.getInt("id");
                this.family_name = rs.getString("name");
                this.contact = rs.getString("phone");
                this.amountOfPeople = rs.getInt("people");
                this.tax = rs.getInt("amount");
                this.since = rs.getInt("since");
                this.paid = rs.getInt("paid");
                this.lastDate = rs.getDate("last_payment");
                this.startDate = rs.getDate("start_date");

                this.owes = (this.since - this.paid) * this.tax;
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public int getOwes() {
        return owes;
    }

    public String getFamily_name() {
        return family_name;
    }

    public String getContact() {
        return contact;
    }

    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public int getId() {
        return id;
    }

    //Връща форматиран низ за изписване в командния интерфейс.
    public String prettyPrint(){
        return  ANSI_GREEN+"id: "+ANSI_RESET+this.id+"\n"+
                ANSI_GREEN+"Family: "+ANSI_RESET+this.family_name+"\n"+
                ANSI_GREEN+"Phone: "+ANSI_RESET+this.contact+"\n"+
                ANSI_GREEN+"People: "+ANSI_RESET+this.amountOfPeople+"\n"+
                ANSI_GREEN+"Owes: "+ANSI_RESET+this.owes+"\n"+
                ANSI_RED+"Owes for "+(this.since - this.paid)+" months\n"+ANSI_RESET;
    }

    //Процедура за създаване на нов апартамент. Приема необходимата информация в HashMap
    public static void create(HashMap<String, String> data){
        try {
            PreparedStatement stmt = DB.con().prepareStatement("INSERT INTO apartment(client, `name`, people, phone) VALUES (?,?,?,?)");
            stmt.setInt(1, Integer.parseInt(data.get("client")));
            stmt.setString(2, data.get("name"));
            stmt.setString(3, data.get("people"));
            stmt.setString(4, data.get("phone"));

            stmt.execute();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    //Процедура която отбелязва за платена най-старата дължима месечна такса.
    public void makePayment(){
        try {
            Date start;

            if(this.lastDate != null) {
                start = this.lastDate;
            }else{
                start = this.startDate;
            }

            Calendar myCal = Calendar.getInstance();
            myCal.setTime(start);
            myCal.add(Calendar.MONTH, +1);
            myCal.set(Calendar.DAY_OF_MONTH, 1);
            Date nextMonth = myCal.getTime();

            PreparedStatement stmt = DB.con().prepareStatement("INSERT INTO taxes_paid(for_month, apartment, amount) VALUES (?,?,?)");
            stmt.setDate(1, new java.sql.Date(nextMonth.getTime()));
            stmt.setInt(2, this.tax);
            stmt.setInt(3, this.id);

            stmt.execute();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    // Метод за редакция на параметрите на апартамента
    public void edit(HashMap<String, String>data){
        try{
            PreparedStatement stmt = DB.con().prepareStatement("UPDATE apartment SET `name`=?,people=?,phone=? WHERE id=?");
            stmt.setString(1, data.get("name"));
            stmt.setInt(2, Integer.parseInt(data.get("people")));
            stmt.setString(3, data.get("phone"));
            stmt.setInt(4, this.id);
            System.out.println(stmt.toString());
            stmt.execute();

            System.out.println("Successfully edited!");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //Връща общия брой на апартаментите в цялата система
    public static int allCount(){
        int count = 0;
        try{
            PreparedStatement statement = DB.con().prepareStatement("SELECT COUNT(id) as `count` FROM apartment");
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while (rs.next()){
                count = rs.getInt("count");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return count;
    }

    //Връща общия брой на хората живущи във всички апартаменти
    public static int allPeopleCount(){
        int count = 0;
        try{
            PreparedStatement statement = DB.con().prepareStatement("SELECT SUM(people) as `count` FROM apartment");
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while (rs.next()){
                count = rs.getInt("count");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return count;
    }

    // Връща обащата сума за събиране от всички клиент
    public static int totalOwed(){
        int totalDebt = 0;
        try{
            PreparedStatement statement = DB.con().prepareStatement("SELECT id FROM apartment");

            statement.execute();

            ResultSet rs = statement.getResultSet();
            while (rs.next()){
                Apartment apartment = new Apartment(rs.getInt("id"));
//                allApartments.add(apartment);

                totalDebt += apartment.owes;
            }


        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return totalDebt;
    }

    public static int totalOwed(String month) {
        int totalDebt = 0;
        month = month+"-02";

        try{
            PreparedStatement statement = DB.con().prepareStatement("SELECT apartment.id, clients.created_at, apartment.client FROM apartment LEFT JOIN clients ON apartment.client=clients.id WHERE clients.created_at <= ?");
            statement.setString(1,month);
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while (rs.next()){
                Apartment apartment = new Apartment(rs.getInt("id"));
                statement = DB.con().prepareStatement("SELECT id FROM taxes_paid WHERE apartment=? AND for_month=?");
                statement.setInt(1, apartment.getId());
                statement.setString(2,month);
                statement.execute();

                ResultSet rs2 = statement.getResultSet();
                if(!rs2.next()){
                    totalDebt += apartment.tax;
                }
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }

        return totalDebt;
    }

    //Връща общата сума събрана до мемента от клиенти
    public static int totalCollected(){
        int totalCollected = 0;
        try {
            Statement statement = DB.con().createStatement();
            statement.execute("SELECT SUM(amount) as `total` FROM taxes_paid");
            ResultSet rs = statement.getResultSet();
            while (rs.next()){
                totalCollected = rs.getInt("total");
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }

        return totalCollected;
    }

    //Връща общата сума събрана в определен месец от клиенти. Приема низ съдържащ дата под формат yyyy-mm
    public static int totalCollected(String month){
        int totalCollected = 0;
        month = month+"-01";
        try {
            PreparedStatement statement = DB.con().prepareStatement("SELECT SUM(amount) as `total` FROM taxes_paid WHERE for_month=?");
            statement.setString(1,month);
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while (rs.next()){
                totalCollected = rs.getInt("total");
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }

        return totalCollected;
    }
}
