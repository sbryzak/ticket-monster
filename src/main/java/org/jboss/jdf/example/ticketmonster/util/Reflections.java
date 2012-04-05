package org.jboss.jdf.example.ticketmonster.util;

import java.net.URL;

public class Reflections {
    
    private Reflections() {}
    
    public static URL getResource(String name) {
        if (Thread.currentThread().getContextClassLoader() != null) {
            return Thread.currentThread().getContextClassLoader().getResource(name);
        } else {
            return Reflections.class.getClassLoader().getResource(name);
        }
    }

}
