package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"performance_id", "section_id"}))
public class SectionAllocation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Version
    private long version;

    @ManyToOne
    private Performance performance;

    @ManyToOne
    private Section section;

    @ElementCollection
    Map<Row, RowAllocation> rowAllocations = new HashMap<Row, RowAllocation>();

    private SectionAllocation() {
    }

    public SectionAllocation(Performance performance, Section section) {
        this.performance = performance;
        this.section = section;
        for (Row row : section.getSectionRows()) {
            final RowAllocation rowAllocation = new RowAllocation(row.getCapacity());
            this.rowAllocations.put(row, rowAllocation);
        }
    }


    public Performance getPerformance() {
        return performance;
    }

    public Section getSection() {
        return section;
    }

    public Map<Row, RowAllocation> getRowAllocations() {
        return rowAllocations;
    }
}
