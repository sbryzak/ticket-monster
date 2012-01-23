package org.jboss.jdf.example.ticketmonster.rest;

/**
 * @author Marius Bogoevici
 */
public class DeleteRequest {
    String cancellationCode;

    public String getCancellationCode() {
        return cancellationCode;
    }

    public void setCancellationCode(String cancellationCode) {
        this.cancellationCode = cancellationCode;
    }
}
