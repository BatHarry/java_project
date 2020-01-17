package com.company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

// Клас със функционалностите свързани с клиентите
public class Client extends Entity {
    //Параментри
    private final int id;
    private String address;
    private int storeys;
    private int apartments;
    private int surface;
    private int common_grounds;

    public Client(int id, String address, int storeys, int apartments, int surface, int common_grounds) {
        super();
        this.id = id;
        this.address = address;
        this.storeys = storeys;
        this.apartments = apartments;
        this.surface = surface;
        this.common_grounds = common_grounds;
    }


    //Getters and Setters
    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public int getStoreys() {
        return storeys;
    }

    public int getApartments() {
        return apartments;
    }

    public int getSurface() {
        return surface;
    }

    public int getCommon_grounds() {
        return common_grounds;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", storeys=" + storeys +
                ", apartments=" + apartments +
                ", surface=" + surface +
                ", common_grounds=" + common_grounds +
                '}';
    }

    //Връща форматиран низ за изписване в командния интерфейс.
    public String prettyPrint(){
        return  ANSI_GREEN+"id: "+ANSI_RESET+this.id+"\n"+
                ANSI_GREEN+"Address: "+ANSI_RESET+this.address+"\n"+
                ANSI_GREEN+"Storeys: "+ANSI_RESET+this.storeys+"\n"+
                ANSI_GREEN+"Number of apartments: "+ANSI_RESET+this.apartments+"\n"+
                ANSI_GREEN+"Total size(sq. m.): "+ANSI_RESET+this.surface+"\n"+
                ANSI_GREEN+"Shared spaces(sq. m.): "+ANSI_RESET+this.common_grounds+"\n";
    }

    //Връща конкретен клиент. Приема id на клиент
    public static Client get(int id){
        Client client;
        ResultSet rs = null;

        try {
            PreparedStatement stmt = DB.con().prepareStatement("SELECT * FROM clients WHERE id=?");
            stmt.setInt(1, id);
            stmt.execute();
            rs = stmt.getResultSet();

            if(rs.next()){
                client = new Client(rs.getInt("id"), rs.getString("address"), rs.getInt("storeys"), rs.getInt("apartments"), rs.getInt("surface"), rs.getInt("common_grounds"));
                return client;
            }else{
                return null;
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return null;
    }

    //Процедура за създаване на нов клиент. Приема HashMap съдържащ инфорамацията необходима за създаването на новия
    //клиент
    public static void create(HashMap<String, String> data){
        try {
            PreparedStatement stmt = DB.con().prepareStatement("INSERT INTO clients(address, storeys, apartments, surface, common_grounds) VALUES (?,?,?,?,?)");
            stmt.setString(1, data.get("address"));
            stmt.setInt(2, Integer.parseInt(data.get("storeys")));
            stmt.setInt(3, Integer.parseInt(data.get("apartments")));
            stmt.setInt(4, Integer.parseInt(data.get("surface")));
            stmt.setInt(5, Integer.parseInt(data.get("common_grounds")));

            stmt.execute();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    //Метод за триене на клиент. Приема id на клиент
    public static boolean delete(int id){
        try {
            PreparedStatement stmt = DB.con().prepareStatement("DELETE FROM clients WHERE id=?");
            stmt.setInt(1, id);
            stmt.execute();
            return true;
        }catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    //Процедура за редакция на клиент. Приема HashMap съдържащ инфорамацията необходима за редакцията
    public static void edit(int id, HashMap<String, String>data){
        try{
            PreparedStatement stmt = DB.con().prepareStatement("UPDATE clients SET address=?,storeys=?,apartments=?,surface=?,common_grounds=? WHERE id=?");
            stmt.setString(1, data.get("address"));
            stmt.setInt(2, Integer.parseInt(data.get("storeys")));
            stmt.setInt(3, Integer.parseInt(data.get("apartments")));
            stmt.setInt(4, Integer.parseInt(data.get("surface")));
            stmt.setInt(5, Integer.parseInt(data.get("common_grounds")));
            stmt.setInt(6, id);
            stmt.execute();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //Връща отговорения работник за клиента
    public static String getAssigned(int id){
        String result = "";
        try{
            PreparedStatement stmt = DB.con().prepareStatement("SELECT clients.address, employees.first_name as first_name, employees.last_name as last_name FROM clients\n" +
                    "LEFT JOIN client_employee ON clients.id=client_employee.client\n" +
                    "LEFT JOIN employees ON client_employee.employee=employees.id\n" +
                    "WHERE clients.id=?");
            stmt.setInt(1, id);
            stmt.execute();

            ResultSet rs = stmt.getResultSet();
            if (rs.next()){
                result = rs.getString("address")+" "+" --> "+rs.getString("first_name")+" "+rs.getString("last_name");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    //Определя отговорен работник за клиент. Приема id на клиент и id на работник
    public static void assign(int id, int employeeId){
        try {
            PreparedStatement stmt = DB.con().prepareStatement("INSERT INTO client_employee(client, employee) VALUES (?,?)");
            stmt.setInt(1, id);
            stmt.setInt(2, employeeId);
            stmt.execute();
            System.out.println(ANSI_GREEN+"Employee successfully assigned!"+ANSI_RESET);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //Изписва всички апартамент за даден клиент. Прием id на клиент
    public static void getApartments(int id){
        ArrayList<Apartment> apartments = new ArrayList<Apartment>();

        try{
            PreparedStatement statement = DB.con().prepareStatement("SELECT id FROM apartment WHERE apartment.client=?");
            statement.setInt(1, id);
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while (rs.next()){
                apartments.add(new Apartment(rs.getInt("id")));
            }
            for (Apartment apartment : apartments){
                System.out.println(apartment.getId()+". "+apartment.getFamily_name()+" owes "+apartment.getOwes()+" BGN");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //Определя месечната такса за всеки апартамент за определен клиент. Приема id клиент и големина на таксата
    public static void setTax(int id, int amount){
        try {
            PreparedStatement statement = DB.con().prepareStatement("UPDATE taxes SET amount=? WHERE client=?");
            statement.setInt(1, amount);
            statement.setInt(2, id);
            statement.execute();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //Връща броя на всички клиенти
    public static int allCount(){
        int count = 0;
        try{
            PreparedStatement statement = DB.con().prepareStatement("SELECT COUNT(id) as `count` FROM clients");
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
}
