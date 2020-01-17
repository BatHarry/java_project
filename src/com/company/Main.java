package com.company;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DB.createConnection();
        String menu = "Main menu:\n" +
                "1. clients                             --> List all clients (buildings)\n" +
                "   clients -number                     --> Total number of clients\n" +
                "   client -ID                          --> List all attributes for client\n" +
                "   client -ID -edit                    --> Edit client\n" +
                "   client -ID -delete                  --> Delete client\n" +
                "   client -ID -tax -AMOUNT             --> Delete client\n" +
                "2. employees                           --> List all employees\n" +
                "   employee -ID                        --> List all attributes for employee\n" +
                "   employee -ID delete                 --> Delete employee\n" +
                "3. assigned -CLIENT_ID                 --> List client with their assigned employees\n" +
                "4. assign -CLIENT_ID -EMPLOYEE_ID      --> Assign employee to client\n" +
                "5. create -TYPE                        --> Create new entity. TYPE could be employee/client\n" +
                "6. apartments -CLIENT_ID               --> Get all apartments for client\n" +
                "   apartments -all-people              --> Get total number of people"+
                "   apartments -all                     --> Get total number of apartments"+
                "7. apartment -ID                       --> See details for apartment\n" +
                "8. apartment -CLIENT_ID -create        --> Create new apartment\n" +
                "9. apartment -ID -pay                  --> Mark as paid oldest debt for apartment\n" +
                "10. apartment -ID -edit                --> Edit apartment\n" +
                "10. total -owed                        --> See the total amount owed\n" +
                "10. total -collected (optional) yyy-mm --> See the total amount collected. Optional parameter month\n " +
                "11. menu                               --> List current menu\n" +
                "12. exit                               --> Exit Program\n";
        System.out.print(menu);
        Commands commands = new Commands();

        while(true){
            Scanner myObj = new Scanner(System.in);
            System.out.print("Enter command: ");

            String input = myObj.nextLine();
            String[] command = input.split(" ");

            switch (command[0]){
                case "clients":
                        if(command.length > 1 && command[1].equals("-all")){
                            commands.allClientsCounts();
                        }else{
                            for(Client client : commands.getAllClients()){
                                System.out.println("id: "+client.getId()+"; "+client.getAddress());
                            }
                        }

                    break;
                case "client":
                        if(command.length > 3 && command[2].equals("-tax")){
                            commands.setTax( Integer.parseInt(command[1].replace("-", "")), Integer.parseInt(command[3].replace("-", "")) );
                        }else if(command.length > 2 && command[2].equals("-delete")) {
                            commands.deleteClient(Integer.parseInt(command[1].replace("-", "")));
                        }else if(command.length > 2 && command[2].equals("-edit")){
                            commands.editClient(Integer.parseInt(command[1].replace("-", "")));
                        }else if(command.length > 1 && command[1].equals("-all")){
                            commands.allClientsCounts();
                        }else if(command.length > 1){
                            System.out.print(commands.getClient(Integer.parseInt(command[1].replace("-", ""))).prettyPrint());
                        }
                    break;
                case "create":
                        if(command.length > 1){
                            commands.createClient(command[1].replace("-", ""));
                        }else {
                            System.out.println("\u001B[31mPlease specify a type\u001B[0m");
                        }
                    break;
                case "employees":
                        commands.getAllEmployees();
                    break;
                case "employee":
                        if(command.length > 2 && command[2].equals("-edit")){
                            commands.editEmployee(Integer.parseInt(command[1].replace("-", "")));
                        }else if(command.length > 2 && command[2].equals("-delete")) {
                            commands.deleteEmployee(Integer.parseInt(command[1].replace("-", "")));
                        }else if(command.length > 1){
                            commands.getEmployee(Integer.parseInt(command[1].replace("-", "")));
                        }
                    break;
                case "apartments":
                        if( command.length > 1 && command[1].equals("-all") ){
                            commands.getApartmentsCount();
                        }else if(command.length > 1 && command[1].equals("-all-people")){
                            commands.getPeopleCount();;
                        }else if(command.length > 1){
                            commands.getApartments(Integer.parseInt(command[1].replace("-", "")));
                        }
                    break;
                case "apartment":
                        if(command.length > 2 && command[2].equals("-create")){
                            commands.createApartment( Integer.parseInt(command[1].replace("-", "")) );
                        }else if(command.length > 2 && command[2].equals("-pay")){
                            commands.makePayment( Integer.parseInt(command[1].replace("-", "")) );
                        }else if(command.length > 2 && command[2].equals("-edit")){
                            commands.editApartment( Integer.parseInt(command[1].replace("-", "")) );
                        }else if (command.length > 1){
                            commands.getApartment( Integer.parseInt(command[1].replace("-", "")) );
                        }
                    break;
                case "assigned":
                        if(command.length > 1) {
                            commands.getAssigned(Integer.parseInt(command[1].replace("-", "")));
                        }
                    break;
                case "assign":
                        if(command.length > 2){
                            commands.assign(Integer.parseInt(command[1].replace("-", "")), Integer.parseInt(command[2].replace("-", "")));
                        }
                    break;
                case "total":
                        if(command.length > 2){
                            if(command[1].equals("-owed")){
                                    commands.totalOwed(command[2]);
                            }else if(command[1].equals("-collected")){
                                commands.totalCollected(command[2]);
                            }
                        }else if(command.length > 1){
                            if(command[1].equals("-owed")){
                                commands.totalOwed();
                            }else if(command[1].equals("-collected")){
                                commands.totalCollected();
                            }
                        }
                    break;
                case "menu":
                        System.out.print(menu);
                    break;
                case "exit":
                        System.out.print("Exiting program. Bye!");
                        System.exit(0);
                    break;
                default:
                    System.out.println("\u001B[31mUnrecognized command!\u001B[0m");
                    break;
            }

        }
    }
}
