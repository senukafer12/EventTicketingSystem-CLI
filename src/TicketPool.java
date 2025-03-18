import java.util.ArrayList;

public class TicketPool {
    private final ArrayList<Ticket> tickets;
    private int maxCapacity;
    private static int ticketIdCounter = 1;  // Static counter for unique ticket ID
    private int totalTickets; // total number of tickets available
    private int purchasedTickets = 0; // counter to track purchased tickets
    private int ticketRelease = 0;

    public TicketPool(ArrayList<Ticket> tickets, int maxCapacity, int totalTickets) {
        this.tickets = tickets == null ? new ArrayList<>() : tickets;
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
    }

    // Get the next unique ticket ID
    private synchronized int getNextTicketId() {
        return ticketIdCounter++;
    }

    public synchronized boolean addTicket(Ticket ticket) {
        if (ticketRelease < totalTickets) {
            tickets.add(ticket);
            ticketRelease++;
            System.out.println("Vendor added ticket: " + ticket);
            return true;
        } else {
            System.out.println("Ticket pool is full. Cannot add more tickets.");
            return false;
        }
    }

    public synchronized Ticket purchaseTicket() {
        if (!tickets.isEmpty()) {
            Ticket ticket = tickets.remove(0);
            System.out.println("Customer purchased ticket: " + ticket);
            return ticket;
        } else {
            System.out.println("No tickets available.");
            return null;
        }
    }

    // Create a new ticket with a unique ticket ID
    public Ticket createTicket(int vendorId) {
        return new Ticket(vendorId, getNextTicketId());
    }

    // Increment the purchased ticket count
    public synchronized void incrementPurchasedTicketCount() {
        purchasedTickets++;
    }

    public synchronized int getPurchasedTicketCount() {
        return purchasedTickets;
    }

    public boolean isAllTicketsPurchased() {
        return purchasedTickets >= totalTickets;
    }
}
