import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static volatile boolean simulationRunning = false; // Volatile flag to control simulation state

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SystemConfig config = new SystemConfig();

        System.out.println("\n***** Welcome to the Real-Time Event Ticketing Management System *****\n");

        // Configure system
        config.configureSystem(scanner);

        System.out.println();

        int numofVendors = inputValidate("Enter the number of Vendors: ");
        int numofCustomers = inputValidate("Enter the number of Customers: ");

        // Create ticket pool
        TicketPool ticketPool = new TicketPool(new ArrayList<>(), config.getMaxTicketCapacity(), config.getTotalTickets());
    }

    private static int inputValidate(String text) {
        Scanner scanner = new Scanner(System.in);
        int value = 0;
        while (true) {
            System.out.print(text);
            try {
                value = scanner.nextInt();
                if (value > 0) {
                    break;
                } else {
                    System.out.println("Invalid input. Enter a number greater than 0.");
                    System.out.println();
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter a valid number.");
                scanner.nextLine(); // clearing the buffer
                System.out.println();
            }
        }
        return value;
    }
}