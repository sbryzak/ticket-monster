package org.jboss.jdf.example.ticketmonster.rest;

import org.jboss.jdf.example.ticketmonster.model.EventCategory;

/**
 * @author Marius Bogoevici
 */
public class CategoryService extends BaseEntityService<EventCategory> {

    public CategoryService() {
        super(EventCategory.class);
    }
}
