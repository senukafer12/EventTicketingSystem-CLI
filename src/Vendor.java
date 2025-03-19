public class Vendor implements Runnable {
    private TicketPool ticketPool;
    private int vendorId;
    private int releaseInterval;

    public Vendor(TicketPool ticketPool, int vendorId, int releaseInterval) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.releaseInterval = releaseInterval;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Create a new ticket with a unique ID
                Ticket ticket = ticketPool.createTicket(vendorId);
                ticketPool.addTicket(ticket);
                Thread.sleep(releaseInterval * 1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Vendor " + vendorId + " interrupted.");
        }
    }
}