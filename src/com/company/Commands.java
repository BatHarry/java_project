package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

// Класът Commands съдържа всичките команди, които могат да се извикат от Main

public class Commands {
    private Connection con;


    public Commands(){
    }

    // Връща всички клиенти
    public ArrayList<Client> getAllClients(){
        ArrayList<Client> allClients = new ArrayList<Client>();
        ResultSet rs = null;
        try {
            Statement stmt = DB.con().createStatement();

            if (stmt.execute("SELECT * FROM clients")) {
                rs = stmt.getResultSet();
            }

            while(rs.next()){
                Client client = new Client(rs.getInt("id"), rs.getString("address"), rs.getInt("storeys"), rs.getInt("apartments"), rs.getInt("surface"), rs.getInt("common_grounds"));
                allClients.add(client);
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }

        return allClients;
    }

    // Връща посочен клиен с id. Приема id на клиент
    public Client getClient(int id){
        Client client = Client.get(id);
        if(client != null){
            return client;
        }
        return null;
    }

    //Процедури за създаване на клиент или работник. Приема низ за типа (client/employee)
    public void createClient(String type)
    {
        Scanner myObj = new Scanner(System.in);
        HashMap<String, String> data = new HashMap<String, String>();

        System.out.println("Please enter the following...");

        if(type.equals("client")) {
            // Get input for address;
            System.out.print("\u001B[32mAddress:\u001B[0m ");
            data.put("address", myObj.nextLine());

            // Get input for Storeys;
            System.out.print("\u001B[32mStoreys:\u001B[0m ");
            data.put("storeys", myObj.nextLine());

            // Get input for Apartments;
            System.out.print("\u001B[32mApartments:\u001B[0m ");
            data.put("apartments", myObj.nextLine());

            // Get input for Surface;
            System.out.print("\u001B[32mSurface:\u001B[0m ");
            data.put("surface", myObj.nextLine());

            // Get input for Shared spaces;
            System.out.print("\u001B[32mShared spaces:\u001B[0m ");
            data.put("common_grounds", myObj.nextLine());

            Client.create(data);
        }else if(type.equals("employee")){
            // Get input for First Name;
            System.out.print("\u001B[32mFirst Name:\u001B[0m ");
            data.put("first_name", myObj.nextLine());

            // Get input for Last Name;
            System.out.print("\u001B[32mLast Name:\u001B[0m ");
            data.put("last_name", myObj.nextLine());

            // Get input for Phone;
            System.out.print("\u001B[32mPhone:\u001B[0m ");
            data.put("phone", myObj.nextLine());

            // Get input for Surface;
            System.out.print("\u001B[32mDate of hire (yyyy-mm-dd):\u001B[0m ");
            data.put("date_of_hire", myObj.nextLine());

            Employee.create(data);
        }else {
            System.out.println("\u001B[31mNo such entity!\u001B[0m");
        }
    }

    //Процедура за редактиран на клиент. Приема id на клиент
    public void editClient(int id){
        Scanner myObj = new Scanner(System.in);
        HashMap<String, String> data = new HashMap<String, String>();
        Client client = Client.get(id);

        if(client != null){
            System.out.println("Please enter the following...");

            // Edit Address
            System.out.print("\u001B[32mAddress:\u001B[0m \u001B[34m"+client.getAddress()+"\u001B[0m\n\u001B[32mNew Address: \u001B[0m");
            String address = myObj.nextLine();
            data.put("address", address.equals("") ? client.getAddress() : address);

            // Edit Storeys
            System.out.print("\u001B[32mStoreys:\u001B[0m \u001B[34m"+client.getStoreys()+"\u001B[0m\n\u001B[32mNew Storeys: \u001B[0m");
            String storeys = myObj.nextLine();
            data.put("storeys", storeys.equals("") ? Integer.toString(client.getStoreys()) : storeys);

            // Edit Apartments
            System.out.print("\u001B[32mApartments:\u001B[0m \u001B[34m"+client.getApartments()+"\u001B[0m\n\u001B[32mNew Apartments: \u001B[0m");
            String apartments = myObj.nextLine();
            data.put("apartments", apartments.equals("") ? Integer.toString(client.getApartments()) : apartments );

            // Edit Surface
            System.out.print("\u001B[32mSurface:\u001B[0m \u001B[34m"+client.getSurface()+"\u001B[0m\n\u001B[32mNew Surface: \u001B[0m");
            String surface = myObj.nextLine();
            data.put("surface", surface.equals("") ? Integer.toString(client.getSurface()) : surface );

            // Edit Shared spaces
            System.out.print("\u001B[32mShared spaces:\u001B[0m \u001B[34m"+client.getCommon_grounds()+"\u001B[0m\n\u001B[32mShared spaces: \u001B[0m");
            String common_grounds = myObj.nextLine();
            data.put("common_grounds", common_grounds.equals("") ? Integer.toString(client.getCommon_grounds()) : common_grounds );

            Client.edit(id, data);
        }

    }

    // Процедура за създаване на апратамент. Приема id на клиент
    public void createApartment(int client){
        Scanner myObj = new Scanner(System.in);
        HashMap<String, String> data = new HashMap<String, String>();

        System.out.println("Please enter the following...");

        // Get input for First Name;
        System.out.print("\u001B[32mFamily Name:\u001B[0m ");
        data.put("name", myObj.nextLine());

        // Get input for People living in apartment;
        System.out.print("\u001B[32mPeople in apartment:\u001B[0m ");
        data.put("people", myObj.nextLine());

        // Get input for Phone;
        System.out.print("\u001B[32mPhone:\u001B[0m ");
        data.put("phone", myObj.nextLine());

        data.put("client", Integer.toString(client));
        Apartment.create(data);

        System.out.println("Apartment successfully added!");
    }

    //Процедура за редакция на апартамент. Приема id на апартамент
    public void editApartment(int id){
        Scanner myObj = new Scanner(System.in);
        HashMap<String, String> data = new HashMap<String, String>();
        Apartment apartment = new Apartment(id);

        if(apartment != null){
            System.out.println("Please enter the following...");

            // Edit Family Name
            System.out.print("\u001B[32mFamily Name:\u001B[0m \u001B[34m"+apartment.getFamily_name()+"\u001B[0m\n\u001B[32mNew Family Name: \u001B[0m");
            String name = myObj.nextLine();
            data.put("name", name.equals("") ? apartment.getFamily_name() : name);

            System.out.print("\u001B[32mPeople living in apartment:\u001B[0m \u001B[34m"+apartment.getAmountOfPeople()+"\u001B[0m\n\u001B[32mNew Amount: \u001B[0m");
            String amount = myObj.nextLine();
            data.put("people", amount.equals("") ? Integer.toString(apartment.getAmountOfPeople()) : amount);

            System.out.print("\u001B[32mPhone:\u001B[0m \u001B[34m"+apartment.getContact()+"\u001B[0m\n\u001B[32mNew Phone: \u001B[0m");
            String phone = myObj.nextLine();
            data.put("phone", phone.equals("") ? apartment.getContact() : phone );

            apartment.edit(data);
        }

    }

    //Метод за триене на клиент. Приема id на клиент
    public void deleteClient(int id){
        if(Client.delete(id)){
            System.out.println("\u001B[32mClient successfully deleted!\u001B[0m");
        }
    }

    // Метод, който изброява всички работници.
    public void getAllEmployees(){
        ArrayList<Employee> employees = Employee.getAll();
        for(Employee employee : employees){
            System.out.print(employee.prettyPrint());
        }
    }

    //Метод, който връща подробна информация за даден работник. Приема id на работник
    public void getEmployee(int id){
        Employee employee = Employee.get(id);
        if(employee != null){
            System.out.print(employee.prettyPrint());
        }
    }

    // Метод, който трие работник. Прием id на работник
    public void deleteEmployee(int id){
        if(Employee.delete(id)){
            System.out.println("\u001B[32mEmployee successfully deleted!\u001B[0m");
        }
    }

    // Процедура за редактиране на работник. Приема id на работник
    public void editEmployee(int id){
        Scanner myObj = new Scanner(System.in);
        HashMap<String, String> data = new HashMap<String, String>();
        Employee employee = Employee.get(id);

        if(employee != null){
            System.out.println("Please enter the following...");

            // Edit First Name
            System.out.print("\u001B[32mFirst Name:\u001B[0m \u001B[34m"+employee.getFirstName()+"\u001B[0m\n\u001B[32mNew First Name: \u001B[0m");
            String first_name = myObj.nextLine();
            data.put("first_name", first_name.equals("") ? employee.getFirstName() : first_name);

            // Edit Last Name
            System.out.print("\u001B[32mLast Name:\u001B[0m \u001B[34m"+employee.getLastName()+"\u001B[0m\n\u001B[32mNew Last Name: \u001B[0m");
            String last_name = myObj.nextLine();
            data.put("last_name", last_name.equals("") ? employee.getLastName() : last_name);

            // Edit Phone
            System.out.print("\u001B[32mPhone:\u001B[0m \u001B[34m"+employee.getPhone()+"\u001B[0m\n\u001B[32mNew Phone: \u001B[0m");
            String phone = myObj.nextLine();
            data.put("phone", phone.equals("") ? employee.getPhone() : phone );

            // Edit Date of Hire
            System.out.print("\u001B[32mDate of Hire:\u001B[0m \u001B[34m"+employee.getDateOfHire()+"\u001B[0m\n\u001B[32mNew Date of Hire: \u001B[0m");
            String dateOfHire = myObj.nextLine();
            data.put("date_of_hire", dateOfHire.equals("") ? employee.getDateOfHireString() : dateOfHire );

            Employee.edit(id, data);
        }
    }

    //Изписва отговорен работник за клиент. Приема id на клиент
    public void getAssigned(int id){
        System.out.println(Client.getAssigned(id));
    }

    //Определя отговорен за клиент. Приема клиентски id и id на работник като втори параметър
    public void assign(int client, int employee){
        Client.assign(client, employee);
    }

    //Изписва всички апартамен на клиент(сграда). Приема id на клиент
    public void getApartments(int id){
        Client.getApartments(id);
    }

    //Изписва подробна информация за конкретен апартамент. Приема id на апартамент
    public void getApartment(int id){
        Apartment apartment = new Apartment(id);
        System.out.print(apartment.prettyPrint());
    }

    //Създава плащане. Приема id на Апартамент
    public void makePayment(int id){
        Apartment apartment = new Apartment(id);
        apartment.makePayment();
    }

    //Определя месечна такса за клиент, който трябва да се плаща от всеки апартамент. Приема id на клиент и сума
    public void setTax(int id, int amount){
        Client.setTax(id, amount);
    }

    //Изписва обшия брой клиенти
    public void allClientsCounts(){
        System.out.println("Total number of clients is "+Integer.toString(Client.allCount()));
    }

    //Изписва обшия брой апартамент
    public void getApartmentsCount(){
        System.out.println("Total number of apartments is "+Integer.toString(Apartment.allCount()));
    }

    //Изписва обшия брой хора живеещи във всички апартаменти
    public void getPeopleCount(){
        System.out.println("Total number of people is "+Integer.toString(Apartment.allPeopleCount()));
    }

    //Изписва обшата сума, която е за събиране от клиентите
    public void totalOwed(){
        System.out.println("Total debt: \u001B[31m"+Apartment.totalOwed()+"\u001B[0m BGN");
    }

//    public void totalOwed(String month){
//        try {
//            System.out.println("Total owed for "+month+": \u001B[32m"+Apartment.totalOwed(month)+"\u001B[0m BGN");
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }

    //Изписва общата сума събрани от такси
    public void totalCollected(){
        System.out.println("Total collected: \u001B[32m"+Apartment.totalCollected()+"\u001B[0m BGN");
    }

    //Изписва общата сума събрана от такси в даден месец. Приема низ за месец във формат yyyy-mm
    public void totalCollected(String month){
        System.out.println("Total collected for "+month+": \u001B[32m"+Apartment.totalCollected(month)+"\u001B[0m BGN");
    }
}
