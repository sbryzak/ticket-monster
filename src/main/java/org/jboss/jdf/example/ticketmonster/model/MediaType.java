package org.jboss.jdf.example.ticketmonster.model;


public enum MediaType {
    
    IMAGE("Image", true);
    
    private MediaType(String description, boolean cacheable) {
        this.description = description;
        this.cacheable = cacheable;
    }

    private final String description;
    private final boolean cacheable;
    
    public String getDescription() {
        return description;
    }
    
    public boolean isCacheable() {
        return cacheable;
    }
    
}
