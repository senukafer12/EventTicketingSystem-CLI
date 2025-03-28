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

        // Shared counter for tickets purchased
        final Object lock = new Object(); // Lock object to synchronize access to purchasedTickets
        Thread[] vendorThreads = new Thread[numofVendors];
        Thread[] customerThreads = new Thread[numofCustomers];

        // Start a thread to handle user input while the simulation is running
        Thread inputThread = new Thread(() -> {
            while (true) {
                System.out.println("\nEnter your choice:");
                System.out.println("1. Start Simulation");
                System.out.println("2. End Ticketing (Exit)");
                int userChoice = inputValidate("Please select an option: ");

                switch (userChoice) {
                    case 1:
                        // Start simulation
                        if (!simulationRunning) {
                            System.out.println("Starting simulation...");
                            startSimulation(vendorThreads, customerThreads, numofVendors, numofCustomers, ticketPool, config, lock);
                            simulationRunning = true;
                        } else {
                            System.out.println("Simulation is already running.");
                        }
                        break;

                    case 2:
                        // End ticketing and exit
                        System.out.println("Ending ticketing. Exiting the system.");
                        stopSimulation(vendorThreads, customerThreads);  // Gracefully stop all threads
                        System.exit(0);  // Exit the application with exit code 0
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        });

        // Start the input thread
        inputThread.start();
    }

    // Start the simulation process
    private static void startSimulation(Thread[] vendorThreads, Thread[] customerThreads, int numofVendors, int numofCustomers,
                                        TicketPool ticketPool, SystemConfig config, Object lock) {
        // Number of tickets to be purchased
        int totalTickets = config.getTotalTickets();
        int purchasedTickets = 0;

        // Start vendor threads
        for (int i = 0; i < numofVendors; i++) {
            vendorThreads[i] = new Thread(new Vendor(ticketPool, i, config.getTicketReleaseRate()));
            vendorThreads[i].start();
            System.out.println("Vendor " + i + " started.");
        }

        // Start customer threads
        for (int i = 0; i < numofCustomers; i++) {
            customerThreads[i] = new Thread(new Customer(ticketPool, i, config.getCustomerRetrieveRate(), lock));
            customerThreads[i].start();
            System.out.println("Customer " + i + " started.");
        }

        // Wait until all tickets are purchased
        while (true) {
            synchronized (lock) {
                if (purchasedTickets == totalTickets) {
                    break;  // All tickets have been purchased, exit the loop
                }
            }
            try {
                Thread.sleep(100);  // Sleep to reduce CPU usage while checking
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Interrupt all threads after the run time
        for (Thread vendor : vendorThreads) {
            vendor.interrupt();
        }
        for (Thread customer : customerThreads) {
            customer.interrupt();
        }

        // Ensure all threads are finished before exiting
        try {
            for (Thread vendor : vendorThreads) {
                vendor.join();
            }
            for (Thread customer : customerThreads) {
                customer.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All tickets purchased. Simulation stopped.");
    }

    private static void stopSimulation(Thread[] vendorThreads, Thread[] customerThreads) {
        // Interrupt vendor and customer threads
        for (Thread vendor : vendorThreads) {
            vendor.interrupt();
        }
        for (Thread customer : customerThreads) {
            customer.interrupt();
        }

        // Ensure all threads are finished before exiting
        try {
            for (Thread vendor : vendorThreads) {
                vendor.join();
            }
            for (Thread customer : customerThreads) {
                customer.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Simulation stopped successfully.");
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