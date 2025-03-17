import java.util.InputMismatchException;
import java.util.Scanner;

public class SystemConfig {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrieveRate;
    private int maxTicketCapacity;

    public SystemConfig() {
        // default constructor
    }

    public SystemConfig(int totalTickets, int ticketReleaseRate, int customerRetrieveRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrieveRate = customerRetrieveRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    // Getters and setters


    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrieveRate() {
        return customerRetrieveRate;
    }

    public void setCustomerRetrieveRate(int customerRetrieveRate) {
        this.customerRetrieveRate = customerRetrieveRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    @Override
    public String toString() {
        return "SystemConfig{" +
                "totalTickets=" + totalTickets +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrieveRate=" + customerRetrieveRate +
                ", maxTicketCapacity=" + maxTicketCapacity +
                '}';
    }

    public void configureSystem(Scanner scanner) {
        System.out.println("===== System Configuration =====");

        this.totalTickets = getValidatedInput(scanner, "Enter total number of Tickets: ", value -> value > 0,
                "Total number of tickets must be a positive value.");

        this.ticketReleaseRate = getValidatedInput(scanner, "Enter ticket release rate per second: ", value -> value > 0,
                "Release rate can't be a negative number.");

        this.customerRetrieveRate = getValidatedInput(scanner, "Enter ticket retrieve rate per second: ", value -> value > 0,
                "Retrieve rate can't be a negative number.");

        this.maxTicketCapacity = getValidatedInput(scanner, "Enter maximum ticket capacity for pool: ",
                value -> value > 0 && value <= totalTickets,
                "Max ticket capacity for pool must be less than or equal to total tickets.");
    }

    // method to validate the inputs
    private int getValidatedInput(Scanner scanner, String prompt, java.util.function.IntPredicate condition, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                if (condition.test(value)) {
                    return value;  // Valid input, return the value
                } else {
                    System.out.println(errorMessage);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();  // Clear the buffer
            }
            System.out.println();
        }
    }
}
