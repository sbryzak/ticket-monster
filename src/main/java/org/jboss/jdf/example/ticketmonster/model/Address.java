package org.jboss.jdf.example.ticketmonster.model;

import javax.persistence.Embeddable;

/**
 * @author Marius Bogoevici
 */
@Embeddable
public class Address {
    
    public String street;
    
    public String city;
    
    public String country;
}
