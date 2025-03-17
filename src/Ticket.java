public class Ticket {
    private int vendorId;
    private int ticketId;

    public Ticket(int vendorId, int ticketId) {
        this.vendorId = vendorId;
        this.ticketId = ticketId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "vendorId=" + vendorId +
                ", ticketId=" + ticketId +
                '}';
    }
}
