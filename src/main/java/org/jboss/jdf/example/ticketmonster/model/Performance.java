package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Marius Bogoevici
 */
@Entity
public class Performance implements Serializable {

    private static final long serialVersionUID = -108405033615497885L;
    
    private Long id;

    private Date date;

    @Id @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
