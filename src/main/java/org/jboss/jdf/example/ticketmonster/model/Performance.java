package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Marius Bogoevici
 */
@Entity
@JsonIgnoreProperties("show")
public class Performance implements Serializable {

    private static final long serialVersionUID = -108405033615497885L;

    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long id;

    @Temporal(TIMESTAMP)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Performance that = (Performance) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (show != null ? !show.equals(that.show) : that.show != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (show != null ? show.hashCode() : 0);
        return result;
    }
}
