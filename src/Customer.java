import java.util.Objects;

public class Customer implements Runnable {
    private TicketPool ticketPool;
    private int customerId;
    private int purchaseInterval;
    private final Object lock; // Lock object to synchronize access to purchasedTickets

    public Customer(TicketPool ticketPool, int customerId, int purchaseInterval, Object lock) {
        this.ticketPool = ticketPool;
        this.customerId = customerId;
        this.purchaseInterval = purchaseInterval;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Ticket ticket = ticketPool.purchaseTicket();
                if (ticket != null) {
                    synchronized (lock) {
                        ticketPool.incrementPurchasedTicketCount();
                        System.out.println("Customer " + customerId + " purchased ticket: " + ticket);
                    }
                }

                // Simulate purchase interval
                Thread.sleep(purchaseInterval * 1000); // Attempt purchase at the rate
            }
        } catch (InterruptedException e) {
            System.out.println("Customer " + customerId + " interrupted.");
        }
    }
}