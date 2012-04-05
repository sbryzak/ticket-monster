package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.URL;

/**
 * A reference to a media object such as images, sound bites, video recordings, that can
 * be used in the application.
 *
 * @author Marius Bogoevici
 */
@Entity
@JsonIgnoreProperties("content")
public class MediaItem implements Serializable {

    private static final long serialVersionUID = -3190368407410663590L;

    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private MediaType mediaType;
    
    @URL
    private String url;
    
    public MediaItem() {}

    public Long getId() {
        return id;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
    
    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "[" + mediaType.getDescription() + "] " + url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaItem mediaItem = (MediaItem) o;

        if (id != null ? !id.equals(mediaItem.id) : mediaItem.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
