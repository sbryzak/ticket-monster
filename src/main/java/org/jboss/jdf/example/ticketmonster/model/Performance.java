package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Marius Bogoevici
 */
@Entity
@JsonIgnoreProperties("show")
public class Performance implements Serializable {

    private static final long serialVersionUID = -108405033615497885L;

    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    @ManyToOne
    private Show show;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setShow(Show show) {
        this.show = show;
    }


    public Show getShow() {
        return show;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
