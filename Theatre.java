import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
public class Theatre { //Creating the class Theatre
    //Declaring Variables
    public static double price_row1;
    public static double price_row2;
    public static double price_row3;
    public static double price = 0;
    static Scanner input = new Scanner(System.in); //Creating Scanner
    static int[][] seats= new int[3][]; //Creating Array
    private static ArrayList<Ticket> tickets = new ArrayList<>(); //Creating ArrayList

    static {
        seats[0] = new int[12]; //Declaring array range for each rows
        seats[1] = new int[16];
        seats[2] = new int[20];
    }
    private static void buy_ticket() { //Creating the method buy_ticket
        //Declaring Variables
        int row,seat;
        String name,email,surname;
        while (true) {
            try { //Exception Handling for row number
                System.out.print("Enter row number (1-3): ");
                row = input.nextInt() - 1;
            } catch (Exception e) {
                System.out.println("Enter a valid number!");
                input.nextLine();
                continue;
            }
            if (row < 0 || row >= seats.length) { //Validation for row number
                System.out.println("Invalid row number.");
                continue;
            }
            try { //Exception Handling for seat number
                System.out.print("Enter seat number (1-" + seats[row].length + "): ");
                seat = input.nextInt() - 1;
            } catch (Exception e) {
                System.out.println("Enter a valid number!");
                input.nextLine();
                continue;
            }
            if (seat < 0 || seat >= seats[row].length) { //Validation for seat number
                System.out.println("Invalid seat number.");
                continue;
            }

            if (seats[row][seat] == 1) { //Validation for sold seats
                System.out.println("Seat already sold.");
                return;
            }
            //Getting user details
            input.nextLine();
            System.out.print("Enter your name: ");
            name = input.next();
            System.out.print("Enter your surname: ");
            surname = input.next();
            email = email_validation(); //Calling the method of email validation
            Person person = new Person(name, surname, email); //Object person

            input.nextLine();
            //Setting prices for each row
            if (row +1 == 1) {
                price = price_row1;
            }
            if (row +1 == 2) {
                price = price_row2;
            }
            if (row +1 == 3) {
                price = price_row3;
            }
            Ticket ticket = new Ticket(row + 1, seat + 1, price, person);
            tickets.add(ticket);

            seats[row][seat] = 1;
            System.out.print("Ticket purchased: Row " + (row + 1) + ", Seat " + (seat + 1) + "\n");
            break;
        }
        System.out.print("\nTicket details: \nName: " + name + "\n" + "Surname: " + surname + "\n" + "Email: " + email + "\n" + "Price: £" + price + "\n");
    }
    private static void print_seating_area() {
        System.out.println("     *********** ");
        System.out.println("     *  STAGE  * ");
        System.out.println("     *********** ");

        for (int i = 0; i < 3; i++) {
            if (i==0) {
                System.out.print("    ");
            }
            if (i==1) {
                System.out.print("  ");
            }
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 1) {
                    System.out.print("X");
                } else {
                    System.out.print("O");
                }
                if (j == ((seats[i].length / 2) - 1)) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println("______________________");
    }

    private static void cancel_ticket() {
        int row, seat;
        while (true) {
            try {
                System.out.print("Enter row number: ");
                row = input.nextInt();
            } catch (InputMismatchException nfe) {
                System.out.println("Enter a valid number");
                input.nextLine();
                continue;
            }
            try {
                System.out.print("Enter seat number: ");
                seat = input.nextInt();
            } catch (InputMismatchException nfe) {
                System.out.println("Enter a valid number");
                input.nextLine();
                continue;
            }

            if (row < 1 || row > seats.length || seat < 1 || seat > seats[row - 1].length) { //Validation for row and seat numbers
                System.out.println("Invalid row or seat number.");
                return;
            }
            if (seats[row - 1][seat - 1] == 0) { //Validation for seat availability
                System.out.println("Seat is already available.");
                return;
            }
            for (Ticket ticket : tickets) { //Cancellation of tickets
                if (ticket.getRow() == row && ticket.getSeat() == seat) {
                    tickets.remove(ticket);
                    seats[row - 1][seat - 1] = 0;
                    System.out.println("Ticket cancelled successfully!");
                    break;
                }
            }
            break;
        }
    }
    private static void show_available() {
        System.out.println("Available seats: ");
        for (int i = 0; i < 3; i++) {
            System.out.print("Seats available in row " + (i+1) + ": ");
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 0) {
                    System.out.print(" "+(j+1));
                    if(j<seats[i].length - 1) {
                        System.out.print(",");
                    }
                    else {
                        System.out.print(".");
                    }
                }
            }
            System.out.println();
        }
    }
    private static void save() {
        String filename = "Seat Details"; //Saving in file name called Seat Details
        try {
            FileWriter writer = new FileWriter(filename);
            for (int i = 0; i < seats.length; i++) {
                for (int j = 0; j < seats[i].length; j++) {
                    writer.write(seats[i][j] + " ");
                }
                writer.write("\n");
            }
            writer.close();
            System.out.println("Seats saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage()); //Error Handling for file
        }
    }
    private static void load() { //Loading the data saved in file
        try {
            File my_file = new File("Seat Details");
            Scanner myReader = new Scanner(my_file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private static void show_tickets_info(ArrayList<Ticket> sorted_tickets) {
        double total_price = 0;
        System.out.println("Ticket information:");
        for (Ticket ticket : tickets) {
            ticket.print();
            total_price += ticket.getPrice(); //Adding ticket prices
            System.out.println();
        }
        System.out.println("Total price of all tickets: £" + total_price);
    }
    private static ArrayList<Ticket> sort_tickets(ArrayList<Ticket> tickets) {
        ArrayList<Ticket> sortedTickets = new ArrayList<>(tickets);

        int n = sortedTickets.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sortedTickets.get(j).getPrice() > sortedTickets.get(j+1).getPrice()) {
                    Ticket temp = sortedTickets.get(j);
                    sortedTickets.set(j, sortedTickets.get(j+1));
                    sortedTickets.set(j+1, temp);
                }
            }
        }

        System.out.println("Sorted Tickets Information:");
        double totalPrice = 0.0;
        for (Ticket t : sortedTickets) {
            System.out.println(t);
            totalPrice += t.getPrice();
        }
        System.out.println("Total Price: £" + totalPrice);

        return sortedTickets;
    }
    private static String email_validation(){
        Scanner input = new Scanner(System.in);
        while (true){
            System.out.print("Enter your email: ");
            String email = input.next();
            boolean emailValidity = email.indexOf('@') >=1 && email.indexOf('.') >=1 && email.indexOf('@')<email.length()-1 && email.indexOf('.')<email.length()-1; //Email validation
            if (emailValidity)  {
                return email;
            }else{
                System.out.println("Invalid email!");
            }
        }
    }
    public static void main(String[] args) {
        System.out.print("\nWelcome to the New Theatre\n");
        boolean priceSet = false;
        while (!priceSet) { //Getting prices from staff to set prices for each row
            try {
                System.out.println("\nEnter Price Values (FOR STAFF USE!)");
                System.out.println("----------------------------------------------");
                do {
                    System.out.print("Set the ticket price for row 1: ");
                    price_row1 = input.nextInt();
                    if (price_row1 <= 0) {
                        System.out.println("Invalid price!\n");
                    }
                }
                while (price_row1 <= 0);{
                }
                do {
                    System.out.print("Set the ticket price for row 2: ");
                    ;
                    price_row2 = input.nextInt();
                    if (price_row2 <= 0) {
                        System.out.println("Invalid price!\n");
                    }
                }
                while (price_row2 <= 0);{
                }
                do {
                    System.out.print("Set the ticket price for row 3: ");
                    price_row3 = input.nextInt();
                    if (price_row3 <= 0) {
                        System.out.println("Invalid price!\n");
                    }
                }
                while (price_row3 <= 0);{
                }

                if(price_row1==price_row2 || price_row1==price_row3 || price_row3==price_row2 ){
                    System.out.println("\nYou have to set 3 different prices for each row. TRY AGAIN!");
                    continue;
                }
                priceSet = true;

            } catch (InputMismatchException nfe) {
                System.out.println("Integer required! Please enter the ticket prices again!");
                input.nextLine();
                System.out.println();
            }
            System.out.println("\nPricing system:\n Row 1: £"+price_row1+"\n Row 2: £"+price_row2+"\n Row 3: £"+ price_row3);
        }

        while (true) {
            int option = 10;
            System.out.println(""" 
                    \n----------------------------------------------
                    1) Buy a ticket
                    2) Print seating area
                    3) Cancel ticket
                    4) List available seats
                    5) Save to file
                    6) Load from file
                    7) Print ticket information and total price
                    8) Sort tickets by price
                        0) Quit
                    ----------------------------------------------"""); // Printing the menu
            try { //Validation for option
                System.out.print("Please enter an option: ");
                option = input.nextInt();
            } catch (InputMismatchException nfe) {
                System.out.print("Integer Required!\n");
                input.nextLine();
            }

            switch (option) { //Switch Case for each options
                case 0:
                    System.out.println("Thank you for using the Theatre application.");
                    return;
                case 1:
                    buy_ticket();
                    break;
                case 2:
                    print_seating_area();
                    break;
                case 3:
                    cancel_ticket();
                    break;
                case 4:
                    show_available();
                    break;
                case 5:
                    save();
                    break;
                case 6:
                    load();
                    break;
                case 7:
                    ArrayList<Ticket> sortedTickets = null;
                    show_tickets_info(sortedTickets);
                    break;
                case 8:
                    tickets = sort_tickets(tickets);
                    break;
                default:
                    System.out.println("Invalid option selected. Please try again.");
            }
        }
    }
}