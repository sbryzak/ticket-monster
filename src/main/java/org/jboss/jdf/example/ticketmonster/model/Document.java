package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

/**
 * Used to store rich text entries used for venue descriptions, event descriptions,
 * front page announcements, etc. A document may have multiple revisions, only
 * one of which is the 'active' revision.  This allows document changes to
 * go through an approval process before being made live.
 *
 * @author Shane Bryzak
 */
@Entity
public class Document implements Serializable {

    private static final long serialVersionUID = -3190368407410663590L;


    private Long id;
    private Date created;
    private String createdBy;
    private Date modified;
    private String modifiedBy;
    private String content;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Lob
    @Column(length = 1000000)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
